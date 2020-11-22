package com.razvantmz.onemove.ui.addPhoto

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import coil.api.clear
import coil.api.load
import coil.transform.CircleCropTransformation
import com.camerakit.CameraKitView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.managers.StorageManager
import com.razvantmz.onemove.extensions.px
import com.razvantmz.onemove.core.helpers.showErrorSnackbar
import com.razvantmz.onemove.databinding.ActivityAddPhotoBinding

const val IMAGE_BITMAP = "fullImageEncoded"
const val PREVIEW_IMAGE_BITMAP = "previewImage"

class AddPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotoBinding

    private lateinit var bitmap: Bitmap
    private var workingBitmap: Bitmap? = null
    private var previewBitmap: Bitmap? = null
    lateinit var canvas: Canvas
    lateinit var paint: Paint


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()

        binding.ivAddPhoto.setOnTouchListener { _, m -> onImageTapped(m) }
        binding.btnTakePicture.setOnClickListener{ onTakePicture() }
    }

    override fun onStart() {
        super.onStart()
        binding.camera.onStart();
    }

    override fun onResume() {
        super.onResume()
        binding.camera.onResume();

    }

    override fun onPause() {
        super.onPause()
        binding.camera.onPause();
    }

    override fun onStop() {
        super.onStop()
        binding.camera.onStop();
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onImageTapped(m: MotionEvent): Boolean {
        val circleRadius = 60.px
        if (bitmap.width > m.rawX + 120.px && bitmap.height > m.rawY + 120.px && m.rawX - circleRadius >= 0 && m.rawY - circleRadius >= 0) {
            workingBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            canvas = Canvas(workingBitmap!!)
            paint = Paint()
            paint.color = Color.RED
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 3.px * 1.0f

            canvas.drawCircle(m.rawX * 1.0f, m.rawY * 1.0f, circleRadius * 1.0f, paint)
//            canvas.drawPoint(m.rawX, m.rawY, paint)
            val centerX = m.rawX.toInt() - circleRadius
            val centerY = m.rawY.toInt() - circleRadius

            previewBitmap = Bitmap.createBitmap( bitmap, centerX, centerY,
                circleRadius * 2,
                circleRadius * 2
            )
            binding.ivAddPhoto.setImageBitmap(workingBitmap)
            binding.previewImg.load(previewBitmap)
            {
                transformations(CircleCropTransformation())
            }
        }
        return true
    }

    private fun onTakePicture() {
        binding.camera.captureImage(object : CameraKitView.ImageCallback {
            override fun onImage(p0: CameraKitView?, photo: ByteArray?) {
                if (photo == null) {
                    return
                }
                bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.size)
                binding.ivAddPhoto.setImageBitmap(bitmap)
                binding.camera.visibility = View.GONE
                binding.btnTakePicture.visibility = View.GONE
                binding.vAddPhotoCameraContainer.visibility = View.GONE
                binding.camera.onPause()
            }
        })
    }

    private fun onSaveButton() {
        if(previewBitmap == null)
        {
            showErrorSnackbar(getString(R.string.error_no_preview_photo))
            return
        }
        StorageManager.saveToInternalStorage(this, workingBitmap!!,
            IMAGE_BITMAP
        )
        StorageManager.saveToInternalStorage(this, previewBitmap!!,
            PREVIEW_IMAGE_BITMAP
        )

        setResult(RESULT_OK, intent);
        finish()
    }


    private fun onRetryPhoto() {
        binding.camera.visibility = View.VISIBLE
        binding.btnTakePicture.visibility = View.VISIBLE
        binding.vAddPhotoCameraContainer.visibility = View.VISIBLE
        binding.previewImg.clear()
        previewBitmap = null
        binding.camera.onResume()
    }

    private fun setToolbar()
    {
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.title_add_photo)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.om_white))

        val backBtn = findViewById<ImageButton>(R.id.btn_back)
        backBtn.setOnClickListener { onBackPressed() }

        val retryBtn = findViewById<ImageButton>(R.id.btn_toolbar_retry)
        retryBtn.setOnClickListener { onRetryPhoto() }

        val saveBtn = findViewById<ImageButton>(R.id.btn_toolbar_save)
        saveBtn.setOnClickListener { onSaveButton() }
    }
}
