package com.razvantmz.onemove.dialogs

import android.app.DatePickerDialog
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.razvantmz.onemove.R
import com.razvantmz.onemove.adapters.AttemptsSpinnerAdapter
import com.razvantmz.onemove.adapters.GradeListAdapter
import com.razvantmz.onemove.core.extensions.showDateDialog
import com.razvantmz.onemove.core.managers.SliderLayoutManager
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.core.utils.toDisplayFormat
import com.razvantmz.onemove.databinding.DialogSentRouteBinding
import com.razvantmz.onemove.extensions.hideAnimated
import com.razvantmz.onemove.extensions.showAnimated
import com.razvantmz.onemove.models.Attempt
import com.razvantmz.onemove.models.Grade
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.zzzmockdata.SampleData
import java.util.*


class SentRouteDialog(val routeName: String, val routeGrade:String, val onSaveClickListener:OnSaveClickListener): DialogFragment(), ApiCallback<StringApiResponse> {

    interface OnSaveClickListener
    {
        fun onSave(date:Date?, attempt: Attempt?, grade: Grade?, apiCallback: ApiCallback<StringApiResponse>)
    }

    private lateinit var gradePickerAdapter: GradeListAdapter
    private lateinit var attemptsSpinnerAdapter: AttemptsSpinnerAdapter
    private lateinit var binding: DialogSentRouteBinding

    private var date:Date = Calendar.getInstance().time
    private var attempt:Attempt?=null
    private var grade:Grade?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DialogSentRouteBinding.inflate(inflater, container, false)

        setButtonsAction()
        setDateDialog()
        setAttemptsSpinner()

        binding.title.text = getString(R.string.sentDialogTitle, routeName, routeGrade)
        binding.dateTv.text = date.toDisplayFormat()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGradeSlider(view)

    }

    override fun onSuccess(responseCode: ResponseCode, data:StringApiResponse)
    {
        binding.dialogContainer.visibility = View.INVISIBLE
        binding.successContainer.visibility = View.VISIBLE
        (binding.succedImage.drawable as Animatable).start()
        binding.progressBar.hideAnimated()
        Handler().postDelayed({
            dismiss()
        }, 1000)
    }

    override fun onFailure(responseCode: ResponseCode)
    {
        binding.dialogContainer.showAnimated()
        binding.progressBar.hideAnimated()
    }

    //region private methods

    fun setButtonsAction()
    {
        binding.cancelButton.setOnClickListener {
            this.dismiss()
        }

        binding.saveButton.setOnClickListener {
            binding.dialogContainer.hideAnimated()
            binding.progressBar.showAnimated()
            onSaveClickListener.onSave(date, attempt, grade, this)
        }
    }

    private fun setDateDialog()
    {
        val dateSetListener =  DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val actualMonth = monthOfYear + 1
            date = Date(year - 1900, actualMonth, dayOfMonth)
            binding.dateTv.text = date?.toDisplayFormat()
        }
        binding.dateTv.setOnClickListener {
            context?.showDateDialog()
            {
                onDateSetListener = dateSetListener
            }
        }
    }

    private fun setGradeSlider(view: View)
    {
        view.measure(
        View.MeasureSpec.UNSPECIFIED,
        View.MeasureSpec.UNSPECIFIED
    )
        var width =  view.measuredWidth//displayMetrics.widthPixels
        val padding = width/2
        binding.gradeRecyclerView.setPadding(padding, 0, padding, 0)
        gradePickerAdapter = GradeListAdapter(SampleData.getGrades()).apply {
            callback = object : SliderLayoutManager.Callback {
                override fun onItemClicked(view: View) {
                    binding.gradeRecyclerView.smoothScrollToPosition(binding.gradeRecyclerView.getChildLayoutPosition(view))
                }
            }
        }

        binding.gradeRecyclerView.adapter = gradePickerAdapter
        binding.gradeRecyclerView.layoutManager = SliderLayoutManager(context!!).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    grade = gradePickerAdapter.itemSource[layoutPosition]
                }
            }
        }
    }

    private fun setAttemptsSpinner()
    {
        attemptsSpinnerAdapter = AttemptsSpinnerAdapter(context!!,  SampleData.getAttemptList())
        binding.numberOfTriesSpinner.adapter = attemptsSpinnerAdapter
        binding.numberOfTriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
              attempt = attemptsSpinnerAdapter.itemSource[position]
            }
        }
    }

}