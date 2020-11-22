package com.razvantmz.onemove.ui.registerConfirmation

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.razvantmz.onemove.databinding.ActivityRegisterConfirmationBinding
import com.razvantmz.onemove.ui.signIn.SignInActivity
import com.razvantmz.onemove.ui.base.BaseActivity

class RegisterConfirmationActivity : BaseActivity<ActivityRegisterConfirmationBinding, RegisterConfirmationViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterConfirmationBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(RegisterConfirmationViewModel::class.java)
        super.onCreate(savedInstanceState)

        binding.logInButton.setOnClickListener {
            onSignInClick()
        }
    }

    override fun onBackPressed() {
        return
    }

    private fun onSignInClick()
    {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}
