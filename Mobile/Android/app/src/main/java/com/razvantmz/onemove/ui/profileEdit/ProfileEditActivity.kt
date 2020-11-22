package com.razvantmz.onemove.ui.profileEdit

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import coil.transform.CircleCropTransformation
import com.razvantmz.onemove.R
import com.razvantmz.onemove.constants.RepositoryConstants
import com.razvantmz.onemove.core.managers.ImageManager.copyStream
import com.razvantmz.onemove.core.managers.ImageManager.createImageFile
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.databinding.ActivityProfileEditBinding
import com.razvantmz.onemove.enums.CompetitionCategory
import com.razvantmz.onemove.enums.Gender
import com.razvantmz.onemove.extensions.forceLoad
import com.razvantmz.onemove.extensions.px
import com.razvantmz.onemove.models.SelectItem
import com.razvantmz.onemove.ui.base.BaseActivity
import com.razvantmz.onemove.ui.selectItems.SelectItemsActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.MessageFormat


class ProfileEditActivity : BaseActivity<ActivityProfileEditBinding, ProfileEditViewModel>() {

    private val chooseImageRequest = 1
    private val genderRequest = 2
    private val categoryRequest = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(ProfileEditViewModel::class.java)
        super.onCreate(savedInstanceState)
        addObservers()
        addBindings()

        val shape = GradientDrawable()
        shape.cornerRadius = 50.px.toFloat()
        shape.setStroke(6.px, Color.WHITE)
        binding.profileImageContainer.background = shape

        val addImageShape = GradientDrawable()
        addImageShape.cornerRadius = 8.px.toFloat()
        addImageShape.setStroke(4.px, Color.WHITE)
        addImageShape.color = ColorStateList.valueOf(Color.WHITE)
        binding.addImageIv.background = addImageShape

        binding.profileImageContainer.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, 1)
        }

        setToolbar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_CANCELED)
        {
            return
        }
        val response = data?.getParcelableExtra<SelectItem>(SelectItemsActivity.DATA)
        when(requestCode)
        {
            genderRequest ->
            {
                if(response == null)
                {
                    viewModel.onErrorCode.setValue(ResponseCode.AnErrorOccurred)
                    return
                }
                viewModel.setGender(response.id)
            }
            categoryRequest ->
            {
                if(response == null)
                {
                    viewModel.onErrorCode.setValue(ResponseCode.AnErrorOccurred)
                    return
                }
                viewModel.setCategory(response.id)
            }
            chooseImageRequest ->
            {
                if(data == null)
                {
                    viewModel.onErrorCode.setValue(ResponseCode.AnErrorOccurred)
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
                        viewModel.setProfileImageFile(photoFile)
                    } catch (ex: IOException) {
                        Log.d("Debug", "Error occurred while creating the file")
                    }

                    val inputStream: InputStream? = contentResolver.openInputStream(data.data!!)
                    val fileOutputStream = FileOutputStream(photoFile)
                    // Copying
                    copyStream(inputStream!!, fileOutputStream)
                    fileOutputStream.close()
                    inputStream.close()
                } catch (e: Exception) {
                    Log.d("Debug", "onActivityResult: $e")
                }
            }
        }
    }

    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.title_profile)
        binding.toolbar.saveBtn.setOnClickListener {
            val imm: InputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            viewModel.saveUserProfile(this)
        }

        binding.toolbar.cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun addObservers()
    {
        viewModel.onSuccessCode.observe(this, Observer{
            responseHandler.showMessage(it)
            if(it == ResponseCode.EditProfileSuccessful)
            {
                Handler().postDelayed({

                    setResult(Activity.RESULT_OK)
                    finish()
                }, 2000)
            }
        })

        val genderList = arrayListOf<SelectItem>()
        genderList.add(SelectItem(Gender.Female.value, "Female"))
        genderList.add(SelectItem(Gender.Male.value, "Male"))

        val categoryList = arrayListOf<SelectItem>()
        categoryList.add(SelectItem(CompetitionCategory.ROOKIE.value, "Rookie"))
        categoryList.add(SelectItem(CompetitionCategory.MASTER.value, "Master"))

        binding.categoryValueTv.setOnClickListener {
            val intent = Intent(this, SelectItemsActivity::class.java)
            intent.putParcelableArrayListExtra(SelectItemsActivity.DATA,  categoryList)
            intent.putExtra(SelectItemsActivity.TITLE,  getString(R.string.category_name))
            startActivityForResult(intent, categoryRequest)
        }

        binding.genderValueTv.setOnClickListener {
            val intent = Intent(this, SelectItemsActivity::class.java)
            intent.putParcelableArrayListExtra(SelectItemsActivity.DATA,  genderList)
            intent.putExtra(SelectItemsActivity.TITLE,  getString(R.string.gender))
            startActivityForResult(intent, genderRequest)
        }

        viewModel.firstName.observe (this, Observer { value ->
            binding.firstNameValueEt.setText(value)
            viewModel.firstName.removeObservers(this)
        })

        viewModel.lastName.observe (this, Observer { value ->
            binding.lastNameValueEt.setText(value)
            viewModel.lastName.removeObservers(this)
        })

//        viewModel.email.observe (this, Observer { value ->
//            binding.emailValueEt.setText(value)
//            viewModel.email.removeObservers(this)
//        })

        viewModel.category.observe (this, Observer { value ->
            val categoryFmt: String = getText(R.string.category).toString()
            binding.categoryValueTv.text = MessageFormat.format(categoryFmt, value)
        })

        viewModel.gender.observe (this, Observer { value ->
            val genderFmt: String = getText(R.string.gender_format).toString()
            binding.genderValueTv.text = MessageFormat.format(genderFmt, value)
        })

        viewModel.profileImageUrl.observe(this, Observer { url->
            if(viewModel.profileImageFile.value == null)
            {
                binding.profileImage.forceLoad(url) {
                    transformations(CircleCropTransformation())
                }
            }
            else
            {
                binding.profileImage.load(viewModel.profileImageFile.value) {
                    transformations(CircleCropTransformation())
                }
            }
        })

        viewModel.profileImageFile.observe(this, Observer { bitmap->
            binding.profileImage.load(bitmap) {
                transformations(CircleCropTransformation())
            }
        })

        viewModel.coverPhotoUrl.observe(this, Observer { url->
            binding.coverImage.load(url)
        })
    }

    private fun addBindings()
    {
        binding.firstNameValueEt.doAfterTextChanged {
            viewModel.setFirstName(it.toString())
        }

        binding.lastNameValueEt.doAfterTextChanged {
            viewModel.setLastName(it.toString())
        }

//        binding.emailValueEt.doAfterTextChanged {
//            viewModel.setEmail(it.toString())
//        }
    }
}
