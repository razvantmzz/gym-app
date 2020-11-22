package com.razvantmz.onemove.ui.addEvent

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.github.irshulx.Editor
import com.razvantmz.onemove.R
import com.razvantmz.onemove.adapters.addPrice.PriceEntryListAdapter
import com.razvantmz.onemove.adapters.addSchedule.ScheduleListAdapter
import com.razvantmz.onemove.constants.RepositoryConstants
import com.razvantmz.onemove.core.extensions.setHoursMin
import com.razvantmz.onemove.core.extensions.showDateDialog
import com.razvantmz.onemove.core.extensions.showTimePickerDialog
import com.razvantmz.onemove.core.helpers.HtmlTagHandler
import com.razvantmz.onemove.core.managers.ImageManager.copyStream
import com.razvantmz.onemove.core.managers.ImageManager.createImageFile
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.core.utils.toHourMinFormat
import com.razvantmz.onemove.core.utils.toScheduleFormat
import com.razvantmz.onemove.databinding.ActivityAddEventBinding
import com.razvantmz.onemove.extensions.hideAnimated
import com.razvantmz.onemove.extensions.showAnimated
import com.razvantmz.onemove.ui.base.BaseActivity
import com.razvantmz.onemove.ui.base.CoreApplication
import com.razvantmz.onemove.ui.textEditor.TextEditorActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class AddEventActivity : BaseActivity<ActivityAddEventBinding, AddEventViewModel>() {

    companion object
    {
        const val RULES_RESULT = 6
        const val DESCRIPTION_RESULT = 7
    }
    private lateinit var scheduleListAdapter:ScheduleListAdapter
    private lateinit var priceListAdapter:PriceEntryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(AddEventViewModel::class.java)
        super.onCreate(savedInstanceState)

        scheduleListAdapter = ScheduleListAdapter(viewModel.schedule.value!!)
        priceListAdapter = PriceEntryListAdapter(this, viewModel.feesList.value!!)
        setTitleSection();
        setUpScheduleRecycler()
        setUpPriceRecycler()
        setTimeSection()
        setFormSection()
        setDescriptionSection()
        setRulesSection()
        setCoverPhotoSection()
        setToolbar()
        setValidation()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        priceListAdapter.context = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_CANCELED)
        {
            return
        }

        if(resultCode == RULES_RESULT)
        {
            val text = data?.extras?.getString(TextEditorActivity.TEXT_INPUT)
            if(!text.isNullOrEmpty())
            {
                viewModel.rules.value = text

            }
            return
        }


        if(resultCode == DESCRIPTION_RESULT)
        {
            val text = data?.extras?.getString(TextEditorActivity.TEXT_INPUT)
            if(!text.isNullOrEmpty())
            {
                viewModel.description.value = text
            }
            return
        }

        try {
            // Creating file
            var photoFile: File? = null
            try {
                photoFile = createImageFile(RepositoryConstants.UserProfileImageName)
                if(photoFile == null)
                {
                    viewModel.onErrorCode.setValue(ResponseCode.AnErrorOccurred)
                    return
                }
                viewModel.setCoverPhoto(photoFile)
            } catch (ex: IOException) {
                Log.d("Debug", "Error occurred while creating the file")
            }

            val inputStream: InputStream? = contentResolver.openInputStream(data?.data!!)
            val fileOutputStream = FileOutputStream(photoFile)
            // Copying
            copyStream(inputStream!!, fileOutputStream)
            fileOutputStream.close()
            inputStream.close()
        } catch (e: Exception) {
            Log.d("Debug", "onActivityResult: $e")
        }
    }

    fun setValidation()
    {
        viewModel.addValidationFields(
            binding.eventTitleEt,
            binding.formLinkET
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpScheduleRecycler()
    {
        binding.scrollView.setOnTouchListener { v, event ->
            binding.coordinatorLayout.requestFocusFromTouch()
            return@setOnTouchListener false
        }

        binding.addScheduleIv.setOnClickListener{
            scheduleListAdapter.addNextEntry()
        }

        binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        binding.scheduleRecyclerView.adapter = scheduleListAdapter
    }

    private fun setUpPriceRecycler()
    {
        binding.priceRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        binding.priceRecyclerView.adapter = priceListAdapter
    }

    private fun setCoverPhotoSection()
    {
        binding.uploadPhotoTv.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, 1)
        }

        binding.deleteCoverImageIv.setOnClickListener {
            binding.coverImageContainer.hideAnimated(true)
            binding.deleteCoverImageIv.hideAnimated(true)
            binding.uploadPhotoTv.showAnimated()
            viewModel.setCoverPhoto(null)
        }

        viewModel.getCoverPhoto().observe(this, Observer { url->
            if(viewModel.getCoverPhoto().value == null)
            {
//                binding.coverImageContainer.forceLoad(url) {
//                    transformations(CircleCropTransformation())
//                }
            }
            else
            {
                binding.coverImageContainer.showAnimated()
                binding.deleteCoverImageIv.showAnimated()
                binding.uploadPhotoTv.hideAnimated()
                binding.coverImageContainer.load(viewModel.getCoverPhoto().value) {
                    transformations(RoundedCornersTransformation(5f, 5f, 5f, 5f))
                }
            }
        })
    }

    private fun setTimeSection()
    {
        viewModel.startDate.observe(this, androidx.lifecycle.Observer {
            binding.timeStartDateTv.text = it.time.toScheduleFormat()
            binding.timeStartHourTv.text = it.time.toHourMinFormat()
        })

        viewModel.endDate.observe(this, androidx.lifecycle.Observer {
            binding.timeEndDateTv.text = it.time.toScheduleFormat()
            binding.timeEndHourTv.text = it.time.toHourMinFormat()
        })

        viewModel.isAllDay.observe(this, androidx.lifecycle.Observer {isChecked->
            if(isChecked)
            {
                binding.timeStartHourTv.hideAnimated()
                binding.timeEndHourTv.hideAnimated()
            }
            else
            {
                binding.timeStartHourTv.showAnimated()
                binding.timeEndHourTv.showAnimated()
            }
        })

        binding.timeStartDateTv.setOnClickListener {
            showDateDialog()
            {
                onDateSetListener =
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val cal = viewModel.startDate.value
                        cal?.set(year, month, dayOfMonth)
                        viewModel.startDate.value = cal
                    }
            }
        }

        binding.timeEndDateTv.setOnClickListener {
            showDateDialog()
            {
                onDateSetListener =
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val cal =  viewModel.endDate.value
                        cal?.set(year, month, dayOfMonth)
                        viewModel.endDate.value = cal
                    }
            }
        }

        binding.timeStartHourTv.setOnClickListener {
            showTimePickerDialog()
            {
                onDateSetListener =
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        val cal = viewModel.startDate.value
                        viewModel.startDate.value = cal?.setHoursMin(hourOfDay, minute)
                    }
            }
        }

        binding.timeEndHourTv.setOnClickListener {
            showTimePickerDialog()
            {
                onDateSetListener =
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        val cal = viewModel.endDate.value
                        viewModel.endDate.value = cal?.setHoursMin(hourOfDay, minute)
                    }
            }
        }

        binding.btnAllDay.setOnClickListener {
            viewModel.isAllDay.value = binding.btnAllDay.isChecked
        }
    }

    private fun setFormSection()
    {
        viewModel.formLink.observe(this, androidx.lifecycle.Observer {
            binding.formLinkET.setText(it)
            binding.formLinkET.setSelection(it.length)
        })

        binding.formLinkET.doAfterTextChanged {
            viewModel.formLink.value = it.toString()
        }
    }

    private  fun setDescriptionSection()
    {
        viewModel.description.observe(this, androidx.lifecycle.Observer {
            val editor = Editor(this, null)
            val mSerializedHtml = editor.getContentAsHTML(it)
            binding.descriptionHtmlRendered.text = Html.fromHtml(mSerializedHtml, null, HtmlTagHandler())
        })

        binding.descriptionContainer.setOnClickListener {
            val intent = Intent(this, TextEditorActivity::class.java)
            CoreApplication.Instance.intentData = viewModel.rules.value
            intent.putExtra(TextEditorActivity.REQUEST_CODE, DESCRIPTION_RESULT)
            intent.putExtra(TextEditorActivity.TOOLBAR_TITLE, getString(R.string.description))
            startActivityForResult(intent, DESCRIPTION_RESULT)
        }
    }

    private fun setRulesSection()
    {
        viewModel.rules.observe(this, androidx.lifecycle.Observer {
            val editor = Editor(this, null)
            val mSerializedHtml = editor.getContentAsHTML(it)
            binding.rulesHtmlRendered.text = Html.fromHtml(mSerializedHtml, null, HtmlTagHandler())
        })

        binding.rulesContainer.setOnClickListener {
            val intent = Intent(this, TextEditorActivity::class.java)
            CoreApplication.Instance.intentData = viewModel.rules.value
            intent.putExtra(TextEditorActivity.REQUEST_CODE, RULES_RESULT)
            intent.putExtra(TextEditorActivity.TOOLBAR_TITLE, getString(R.string.rules))
            startActivityForResult(intent, RULES_RESULT)
        }
    }

    private fun setTitleSection()
    {
        viewModel.eventTitle.observe(this, androidx.lifecycle.Observer {
            binding.eventTitleEt.setText(it)
            binding.eventTitleEt.setSelection(it.length)
        })

        binding.eventTitleEt.doAfterTextChanged {
            viewModel.eventTitle.value = it.toString()
        }
    }

    private fun setToolbar()
    {
        binding.toolbar.toolbarTitle.setText(R.string.title_add_event)
        binding.toolbar.saveBtn.setOnClickListener {
            viewModel.addEventAsync()
        }

        binding.toolbar.cancelBtn.setOnClickListener {
            finish()
        }
    }
}
