package com.razvantmz.onemove.ui.textEditor

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.github.irshulx.Editor
import com.github.irshulx.EditorListener
import com.github.irshulx.models.EditorTextStyle
import com.razvantmz.onemove.databinding.ActivityTextEditorBinding
import com.razvantmz.onemove.databinding.ToolbarTextEditorBinding
import com.razvantmz.onemove.extensions.hideAnimated
import com.razvantmz.onemove.extensions.showAnimated
import com.razvantmz.onemove.ui.base.BaseActivity
import com.razvantmz.onemove.ui.base.CoreApplication
import top.defaults.colorpicker.ColorPickerPopup
import java.util.*


class TextEditorActivity : BaseActivity<ActivityTextEditorBinding, TextEditorViewModel>() {

    companion object
    {
        const val TEXT_INPUT = "textInput"
        const val TOOLBAR_TITLE = "toolbarTitle"
        const val REQUEST_CODE = "textInput"
    }

    private lateinit var editor:Editor
    private lateinit var eToolbar:ToolbarTextEditorBinding
    private var requestCode:Int = Activity.RESULT_CANCELED

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTextEditorBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(TextEditorViewModel::class.java)
        super.onCreate(savedInstanceState)
        editor = binding.editor
        eToolbar = binding.editorToolbar

        setUpEditor()
        setUpToolbar()

        val text = CoreApplication.Instance.intentData
        if(!text.isNullOrEmpty())
        {
            val deserialised = editor.getContentDeserialized(text)
            editor.render(deserialised)
        }
        requestCode = intent.extras?.getInt(REQUEST_CODE, Activity.RESULT_CANCELED)!!

    }

    override fun onResume() {
        super.onResume()

        binding.editor.requestFocus()
        binding.editor.editorListener = object : EditorListener {
            override fun onUpload(image: Bitmap?, uuid: String?) {
            }

            override fun onRenderMacro(
                name: String?,
                props: MutableMap<String, Any>?,
                index: Int
            ): View {
                return View(baseContext)
            }

            override fun onTextChanged(editText: EditText?, text: Editable?) {
                if(text.isNullOrEmpty())
                {
                    binding.placeholder.visibility = View.VISIBLE
                }
                else
                {
                    binding.placeholder.visibility = View.GONE
                }
            }

        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit editor")
            .setMessage("Are you sure you want to exit the editor?")
            .setPositiveButton("Yes") { dialog, which ->
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun setUpToolbar()
    {
        val title = intent.extras?.getString(TOOLBAR_TITLE)
        binding.toolbar.toolbarTitle.text = title

        binding.toolbar.saveBtn.setOnClickListener {
            val contentText = editor.contentAsSerialized
            editor.contentAsHTML
            val intent = Intent()
            intent.putExtra(TEXT_INPUT, contentText)
            setResult(requestCode, intent)
            finish()

        }
        binding.toolbar.cancelBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpEditor()
    {
        eToolbar.actionH1.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.H1)
        }
        eToolbar.actionH2.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.H2)
        }
        eToolbar.actionH3.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.H3)
        }
        eToolbar.actionBold.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.BOLD)
        }
        eToolbar.actionItalic.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.ITALIC)
        }
        eToolbar.actionIndent.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.INDENT)
        }
        eToolbar.actionOutdent.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.OUTDENT)
        }
        eToolbar.actionBlockquote.setOnClickListener {
            editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE)
        }
        eToolbar.actionBulleted.setOnClickListener {
            editor.insertList(false)
        }
        eToolbar.actionUnorderedNumbered.setOnClickListener {
            editor.insertList(true)
        }
        eToolbar.actionHr.setOnClickListener {
            editor.insertDivider()
        }
        eToolbar.actionColor.setOnClickListener {
            ColorPickerPopup.Builder(baseContext)
                .initialColor(Color.RED)
                .enableAlpha(true)
                .okTitle("Choose")
                .cancelTitle(getString(R.string.cancel))
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(findViewById(android.R.id.content),object: ColorPickerPopup.ColorPickerObserver()
                {
                    override fun onColorPicked(color: Int) {
                        Toast.makeText(baseContext, "picked" + colorHex(color), Toast.LENGTH_LONG).show();
                        editor.updateTextColor(colorHex(color));
                    }
                })
        }
//        eToolbar.actionInsertImage.setOnClickListener {
////            editor.insertDivider()
//        }
        eToolbar.actionInsertLink.setOnClickListener {
            editor.insertLink()
        }
        eToolbar.actionErase.setOnClickListener {
            editor.clearAllContents()
        }

    }

    private fun colorHex(color: Int): String? {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return java.lang.String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b)
    }

    fun setGhost(button: Button) {
        val radius = 4
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.setStroke(4, Color.WHITE)
        background.cornerRadius = radius.toFloat()
        button.setBackgroundDrawable(background)
    }

}
