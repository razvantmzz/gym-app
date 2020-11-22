package com.razvantmz.onemove.core.responseHandlers

import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.helpers.showErrorSnackbar
import com.razvantmz.onemove.core.helpers.showMessageSnackbar
import com.razvantmz.onemove.ui.base.BaseViewModel

class ResponseHandler(view:View, viewModel:BaseViewModel, viewLifecycleOwner: LifecycleOwner) : IResponseHandler {

    val coordinatorLayout: CoordinatorLayout? = view.findViewById(R.id.coordinatorLayout)
    val context:Context = view.context

    init {
        viewModel.onSuccessCode.observe(viewLifecycleOwner, Observer {
            showMessage(it)
        })

        viewModel.onErrorCode.observe(viewLifecycleOwner, Observer {
            showErrorMessage(it)
        })
    }

    override fun showErrorMessage(responseCode: ResponseCode) {
        coordinatorLayout?:return
        val key = responseMap[responseCode] ?:return
        val message = context.getString(key)
        showErrorSnackbar(message)
    }

    override fun showMessage(responseCode: ResponseCode) {
        coordinatorLayout?:return
        val key = responseMap[responseCode] ?:return
        val message = context.getString(key)
        showMessageSnackbar(message)
    }

    override fun showValidationMessage(responseCode: ResponseCode, validationField: ValidationField, param:String?)
    {
        coordinatorLayout?:return
        val key = responseMap[responseCode] ?:return
        val fieldKey = validationFieldMap[validationField]?:return

        val fieldName = context.getString(fieldKey)

        val message = if(param == null)
                        context.getString(key, fieldName)
                      else
                        context.getString(key, fieldName, param)

        showErrorSnackbar(message)
    }

    private val responseMap = hashMapOf<ResponseCode, Int>().apply {
        this[ResponseCode.ProblemAdded] = R.string.success_add_problem
        this[ResponseCode.PhotoMissing] = R.string.error_no_photo
        this[ResponseCode.InvalidDate] = R.string.error_invalid_date
        this[ResponseCode.IndexNotSet] = R.string.error_index_not_set
        this[ResponseCode.InvalidName] = R.string.error_invalid_name
        this[ResponseCode.RouteNotFound] = R.string.email
        this[ResponseCode.UserNotFound] = R.string.error_user_not_found
        this[ResponseCode.InvalidInputs] = R.string.error_invalid_inputs
        this[ResponseCode.InvalidCredentials] = R.string.error_invalid_credentials
        this[ResponseCode.AnErrorOccurred] = R.string.error_an_error_occurred
        this[ResponseCode.EmailAlreadyTaken] = R.string.error_an_error_occurred

        this[ResponseCode.AddedToFavorite] = R.string.success_added_to_favorite
        this[ResponseCode.RemovedFromFavorite]= R.string.success_removed_from_favorite
        this[ResponseCode.SignInSuccessful]= R.string.success_sign_in
        this[ResponseCode.EditProfileSuccessful]= R.string.success_edit_profile
        this[ResponseCode.FeedbackSubmittedSuccessful]= R.string.success_submit_feedback
        this[ResponseCode.ProblemWasEdited]= R.string.success_edit_problem

    }

    private val validationFieldMap = hashMapOf<ValidationField, Int>().apply {
        this[ValidationField.Name] = R.string.name
    }
}

fun IResponseHandler.validateMinLength(text:String, validationField:ValidationField, min:Int, showError:Boolean = true) : Boolean
{
    if(text.length < min)
    {
        if(showError)
        {
            this.showValidationMessage(ResponseCode.MinLengthError, validationField, min.toString())
        }
        return false
    }
    return true
}
