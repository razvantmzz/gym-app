package com.razvantmz.onemove.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import coil.transform.CircleCropTransformation
import com.razvantmz.onemove.R
import com.razvantmz.onemove.databinding.FragmentProfileBinding
import com.razvantmz.onemove.extensions.forceLoad
import com.razvantmz.onemove.extensions.px
import com.razvantmz.onemove.ui.base.BaseFragment
import com.razvantmz.onemove.ui.feedback.FeedbackActivity
import com.razvantmz.onemove.ui.profileEdit.ProfileEditActivity
import com.razvantmz.onemove.ui.signIn.SignInActivity
import java.text.MessageFormat

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    private val editProfileRequest = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        addObservers()

        val shape = GradientDrawable()
        shape.cornerRadius = 50.px.toFloat()
        shape.setStroke(6.px, Color.WHITE)
        binding.profileImageContainer.background = shape

        setToolbar()
        setLogout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_CANCELED)
        {
            return
        }
        viewModel.initUserData()
    }

    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.title_profile)
        binding.toolbar.btnToolbarRight.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_edit))
        binding.toolbar.btnToolbarRight.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            startActivityForResult(intent, editProfileRequest)
        }
    }

    fun setLogout()
    {
        binding.logoutBtn.setOnClickListener {
            val intent = Intent(context, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun addObservers()
    {
        binding.feedbackContainerRl.setOnClickListener {
            val intent = Intent(context, FeedbackActivity::class.java)
            startActivity(intent)
        }

        viewModel.firstName.observe (viewLifecycleOwner, Observer { value ->
            binding.firstNameValueTv.text = value
        })

        viewModel.lastName.observe (viewLifecycleOwner, Observer { value ->
            binding.lastNameValueTv.text = value
        })

        viewModel.email.observe (viewLifecycleOwner, Observer { value ->
            binding.emailValueTv.text = value
        })

        viewModel.category.observe (viewLifecycleOwner, Observer { value ->
            val categoryFmt: String = context!!.resources.getText(R.string.category).toString()
            binding.categoryValueTv.text = MessageFormat.format(categoryFmt, value)
        })

        viewModel.gender.observe (viewLifecycleOwner, Observer { value ->
            val genderFmt: String = context!!.resources.getText(R.string.gender_format).toString()
            binding.genderValueTv.text = MessageFormat.format(genderFmt, value)
        })

        viewModel.profileImageUrl.observe(viewLifecycleOwner, Observer { url->
            binding.profileImage.forceLoad(url) {
                transformations(CircleCropTransformation())
            }
        })

        viewModel.coverPhotoUrl.observe(viewLifecycleOwner, Observer { url->
            binding.coverImage.load(url)
        })
    }
}
