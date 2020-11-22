package com.razvantmz.onemove.ui.feedback

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.databinding.ActivityFeedbackBinding
import com.razvantmz.onemove.ui.base.BaseActivity

class FeedbackActivity : BaseActivity<ActivityFeedbackBinding, FeedbackViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(FeedbackViewModel::class.java)
        super.onCreate(savedInstanceState)

        setBindings()
        setToolbar()
    }

    fun setBindings()
    {
        binding.emailTv.text = getString(R.string.feedback_email, viewModel.getEmail().value)

        binding.feedbackEt.hint = getString(R.string.feedback_message_placeholder)
        binding.feedbackEt.doAfterTextChanged {
            viewModel.setFeedBackText(it.toString())
        }

        viewModel.onSuccessCode.observe(this, Observer {
            responseHandler.showMessage(it)
            if(it == ResponseCode.FeedbackSubmittedSuccessful)
            {
                Handler().postDelayed({
                    finish()
                }, 3000)
            }
        })
    }

    fun setToolbar()
    {
        binding.toolbar.toolbarTitle.text = getString(R.string.title_feedback)
        binding.toolbar.cancelBtn.setOnClickListener {
            finish()
        }

        binding.toolbar.saveBtn.setOnClickListener {
                viewModel.submitFeedback()
        }
    }
}
