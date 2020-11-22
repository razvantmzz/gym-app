package com.razvantmz.onemove.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.razvantmz.onemove.BuildConfig
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.validations.Validation
import com.razvantmz.onemove.constants.ValidationConstants
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.databinding.ActivitySignInBinding
import com.razvantmz.onemove.ui.activities.HomeActivity
import com.razvantmz.onemove.ui.base.BaseActivity
import com.razvantmz.onemove.ui.register.RegisterActivity

class SignInActivity : BaseActivity<ActivitySignInBinding, SignInViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        super.onCreate(savedInstanceState)
        initialiseViews()

        binding.registerButton.setOnClickListener{ onRegisterClick() }
        binding.signInButton.setOnClickListener { onSignInClick() }
    }

    private fun initialiseViews()
    {
        viewModel.onSuccessCode.observe(this, Observer<ResponseCode> {
            if(it == ResponseCode.SignInSuccessful)
            {
                responseHandler.showMessage(it)
                Handler().postDelayed({
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }, 1000)
            }
        })

        binding.emailEt.doAfterTextChanged {
            viewModel.setEmail(it.toString())
        }

        binding.passwordEt.doAfterTextChanged {
            viewModel.setPassword(it.toString())
        }

        viewModel.addValidationFields(
            binding.emailEt,
            binding.passwordEt
        )

        if(BuildConfig.DEBUG)
        {
            binding.emailEt.setText("razvantmz@gmail.com")
            binding.passwordEt.setText("@thebest.21")
//            binding.passwordEt.setText("1234")

        }
    }

    private fun onSignInClick()
    {
        //TODO: add login server logic
        if(!validateInputs())
        {
            return
        }

        viewModel.signIn()
    }

    private fun onRegisterClick()
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private  fun validateInputs() : Boolean
    {
        if(binding.emailEt.text!!.toString() == "razvantmz@gmail.com" && binding.passwordEt.text!!.toString() == "@thebest.21" || binding.passwordEt.text!!.toString() == "1234")
        {
            return true;
        }
        val coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
        if(!Validation.validateEmail(this, coordinatorLayout, binding.emailEt.text.toString()))
        {
            return false
        }

        if(!Validation.validateMinLength(this, coordinatorLayout, getString(R.string.password), binding.passwordEt.text.toString(), ValidationConstants.MIN_PASSWORD_LENGTH))
        {
            return false
        }
        return true
    }


}
