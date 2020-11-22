package com.razvantmz.onemove.ui.register

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProviders
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.databinding.ActivityRegisterBinding
import com.razvantmz.onemove.ui.base.BaseActivity
import com.razvantmz.onemove.ui.registerConfirmation.RegisterConfirmationActivity

class RegisterActivity() : BaseActivity<ActivityRegisterBinding, RegisterViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar.toolbar)

        initViews();
        setUpToolbar()
        setValidations()

        binding.btnRegister.setOnClickListener{
            viewModel.registerUser()
        }
    }

    fun setValidations()
    {
        viewModel.addValidationFields(
            binding.firstName,
            binding.lastName,
            binding.email,
            binding.password,
            binding.confirmPassword
        )
    }


    private fun initViews()
    {
        viewModel.onSuccessCode.observe(this, androidx.lifecycle.Observer {
            if(it == ResponseCode.AccountCreated)
            {
                var intent = Intent(this, RegisterConfirmationActivity::class.java)
                startActivity(intent)
            }
        })

        binding.firstName.doAfterTextChanged { text->
            viewModel.setFirstName(text.toString())
        }

        binding.lastName.doAfterTextChanged { text->
            viewModel.setLastName(text.toString())
        }

        binding.email.doAfterTextChanged { text->
            viewModel.setEmail(text.toString())
        }

        binding.password.doAfterTextChanged { text->
            viewModel.setPassword(text.toString())
        }

        binding.confirmPassword.doAfterTextChanged { text->
            viewModel.setConfirmPassword(text.toString())
        }

        binding.subscribtionNumber.doAfterTextChanged { text->
            viewModel.setSubscribtionNumber(text.toString())
        }
    }

    private fun setUpToolbar()
    {
        binding.toolbar.toolbarTitle.setText(R.string.title_register)
        binding.toolbar.btnBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }
}
