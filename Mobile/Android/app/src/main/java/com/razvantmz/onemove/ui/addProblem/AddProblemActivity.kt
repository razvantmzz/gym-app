package com.razvantmz.onemove.ui.addProblem

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.razvantmz.onemove.R
import com.razvantmz.onemove.ui.base.BaseActivity
import com.razvantmz.onemove.ui.addPhoto.AddPhotoActivity
import com.razvantmz.onemove.ui.addPhoto.IMAGE_BITMAP
import com.razvantmz.onemove.ui.addPhoto.PREVIEW_IMAGE_BITMAP
import com.razvantmz.onemove.adapters.*
import com.razvantmz.onemove.core.extensions.showDateDialog
import com.razvantmz.onemove.core.managers.SliderLayoutManager
import com.razvantmz.onemove.core.managers.StorageManager
import com.razvantmz.onemove.core.utils.toDisplayFormat
import com.razvantmz.onemove.core.helpers.showErrorSnackbar
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.databinding.ActivityAddProblemBinding
import com.razvantmz.onemove.models.RouteModel
import com.razvantmz.onemove.zzzmockdata.SampleData
import java.util.*


class AddProblemActivity() : BaseActivity<ActivityAddProblemBinding, AddProblemViewModel>() {
    companion object{
        const val EDIT_ROUTE:String = "editRoute"
    }

    private val launchTakePhotoActivity = 1

    private lateinit var colorPickerAdapter: ColorListAdapter
    private lateinit var gradePickerAdapter: GradeListAdapter
    private lateinit var typeSpinnerAdapter: RouteTypeSpinnerAdapter
    private lateinit var setterSpinnerAdapter: SetterSpinnerAdapter
    private lateinit var locationSpinnerAdapter: LocationSpinnerAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddProblemBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(AddProblemViewModel::class.java)
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        val routeToEdit = bundle?.getParcelable<RouteModel>(EDIT_ROUTE)

        viewModel.onSuccessCode.observe(this, androidx.lifecycle.Observer {
            responseHandler.showMessage(it)
            if(it == ResponseCode.ProblemAdded)
            {
                Handler().postDelayed( { finish() },3000 )
            }
        })

        binding.nameEt.doAfterTextChanged {
            viewModel.name.setValue(it.toString())
        }

        viewModel.name.observe(this, androidx.lifecycle.Observer {
            val cursorPosition = binding.nameEt.selectionStart
            binding.nameEt.setText(it)
            binding.nameEt.setSelection(cursorPosition)
        })

        viewModel.index.observe(this, androidx.lifecycle.Observer {
            val cursorPosition = binding.indexEt.selectionStart
            binding.indexEt.setText(it.toString())
            binding.indexEt.setSelection(cursorPosition)

        })

        binding.indexEt.doAfterTextChanged {
            try {
                viewModel.index.setValue(it.toString().toInt())
            }
            catch (ex:NumberFormatException)
            {
                viewModel.index.setValue(-1)
            }
        }

        viewModel.addRouteData.observe(this, androidx.lifecycle.Observer {data->
            if (data == null)
            {
                showErrorSnackbar(getString(R.string.error_server))
            }
        })

        viewModel.setterList.observe(this, androidx.lifecycle.Observer {
            setterSpinnerAdapter.itemSource = it
            setterSpinnerAdapter.notifyDataSetChanged()
        })

        viewModel.holdsColorList.observe(this, androidx.lifecycle.Observer {
            colorPickerAdapter.itemSource = it
            colorPickerAdapter.notifyDataSetChanged()
        })

        viewModel.gradeList.observe(this, androidx.lifecycle.Observer {
            gradePickerAdapter.itemSource = it
            gradePickerAdapter.notifyDataSetChanged()
            if(routeToEdit != null)
            {
                val index =it.indexOfFirst { grade -> grade.grade == routeToEdit.grade }
                index?:return@Observer
                val childPos = binding.holdsColorRecycler.getChildAt(index)
                val layoutPos = binding.holdsColorRecycler.getChildLayoutPosition(childPos)
                binding.gradeRecycler.smoothScrollToPosition(index)
            }
        })

        viewModel.locationList.observe(this, androidx.lifecycle.Observer {
            locationSpinnerAdapter.setItemSource(it)
        })

        viewModel.routeType.observe(this, androidx.lifecycle.Observer {
                locationSpinnerAdapter.setType(it.value)
        })

        viewModel.getFullImage().observe(this, androidx.lifecycle.Observer {
            binding.addProblemImage.setImageBitmap(it)
        })

        viewModel.setRouteModel(routeToEdit)

        binding.addProblemImage.setOnClickListener{
            val intent = Intent(this, AddPhotoActivity::class.java)
            startActivityForResult(intent, launchTakePhotoActivity)
        }

        binding.dateValueTv.setOnClickListener{
            onShowDateDialog()
        }

        binding.saveBtn.setOnClickListener { onSave() }

        viewModel.getDate().observe(this, androidx.lifecycle.Observer {date->
            binding.dateValueTv.text = date.toDisplayFormat()
        })

        setToolbar()
        setUpColorSlider()
        setGradeSlider()
        setUpSpinners()
        initialiseData(routeToEdit)
    }

    fun initialiseData(routeModel: RouteModel?)
    {
        routeModel?:return

        viewModel.setterList.observe(this, androidx.lifecycle.Observer {
            val index = it.indexOfFirst { setter -> setter.id == routeModel.setter.id }
            index?:return@Observer
            binding.setterSpinner.setSelection(index)
        })

        viewModel.locationList.observe(this, androidx.lifecycle.Observer {
            val index =it.indexOfFirst { location -> location.name == routeModel.location }
            index?:return@Observer
            binding.locationSpinner.setSelection(index)
        })

        viewModel.routeType.observe(this, androidx.lifecycle.Observer {
            binding.typeSpinner.setSelection(it.value)
        })

        viewModel.holdsColorList.observe(this, androidx.lifecycle.Observer {
            val index =it.indexOfFirst { color -> color == routeModel.holdsColor }
            index?:return@Observer
            binding.holdsColorRecycler.smoothScrollToPosition(index)
        })

        binding.addProblemImage.load(routeModel.imageUrl)
        binding.toolbar.toolbarTitle.text = getString(R.string.title_edit_problem)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == launchTakePhotoActivity)
        {
            if(resultCode == Activity.RESULT_OK && data != null)
            {
                viewModel.setFullImage(StorageManager.loadImageFromStorage(this, IMAGE_BITMAP))
                viewModel.setPreviewImage(StorageManager.loadImageFromStorage(this, PREVIEW_IMAGE_BITMAP))
            }
        }
    }

    private fun onSave()
    {
        viewModel.saveData(this)
    }

    private fun onShowDateDialog()
    {
        val dateSetListener =  DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val actualMonth = monthOfYear + 1
            viewModel.date.postValue(Date(year - 1900, actualMonth, dayOfMonth))
        }
        showDateDialog()
        {
            onDateSetListener = dateSetListener
        }
    }

    private fun setUpSpinners()
    {
        typeSpinnerAdapter = RouteTypeSpinnerAdapter()
        binding.typeSpinner.adapter = typeSpinnerAdapter
        binding.typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.routeType.value = typeSpinnerAdapter.getItem(position)
            }
        }

        setterSpinnerAdapter = SetterSpinnerAdapter(emptyList())
        binding.setterSpinner.adapter = setterSpinnerAdapter
        binding.setterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) { }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
            viewModel.setter.value = setterSpinnerAdapter.itemSource[position]
        }
    }

        locationSpinnerAdapter = LocationSpinnerAdapter()
        binding.locationSpinner.adapter = locationSpinnerAdapter
        binding.locationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                viewModel.location.value = locationSpinnerAdapter.getItemAtPosition(position)
            }
        }
    }

    private fun setUpColorSlider()
    {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val padding = width/2
        binding.holdsColorRecycler.setPadding(padding, 0, padding, 0)
        colorPickerAdapter = ColorListAdapter(emptyList()).apply {
            callback = object : SliderLayoutManager.Callback {
                override fun onItemClicked(view: View) {
                    binding.holdsColorRecycler.smoothScrollToPosition(binding.holdsColorRecycler.getChildLayoutPosition(view))
                }
            }
        }
        binding.holdsColorRecycler.adapter = colorPickerAdapter
        binding.holdsColorRecycler.layoutManager = SliderLayoutManager(this).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    if(colorPickerAdapter.itemSource.isEmpty())
                    {
                        return;
                    }
                    viewModel.color.value = colorPickerAdapter.itemSource[layoutPosition]
                }
            }
        }
    }

    private fun setGradeSlider()
    {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        val padding = width/2
        binding.gradeRecycler.setPadding(padding, 0, padding, 0)
        gradePickerAdapter = GradeListAdapter(emptyList()).apply {
            callback = object : SliderLayoutManager.Callback {
                override fun onItemClicked(view: View) {
                    val layoutpost = binding.gradeRecycler.getChildLayoutPosition(view)
                    binding.gradeRecycler.smoothScrollToPosition(layoutpost)
                }
            }
        }

        binding.gradeRecycler.adapter = gradePickerAdapter
        binding.gradeRecycler.layoutManager = SliderLayoutManager(this).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    if(gradePickerAdapter.itemSource.isEmpty())
                    {
                        return
                    }
                    viewModel.grade.value = gradePickerAdapter.itemSource[layoutPosition]
                }
            }
        }
    }

    private fun setToolbar()
    {
        binding.toolbar.toolbarTitle.text = getString(R.string.title_add_problem)
        binding.toolbar.toolbar.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.om_white))
        binding.toolbar.btnBack.setOnClickListener { onBackPressed() }
    }
}


