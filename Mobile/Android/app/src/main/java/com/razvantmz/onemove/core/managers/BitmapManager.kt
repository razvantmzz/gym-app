package com.razvantmz.onemove.core.managers

import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.razvantmz.onemove.ui.base.CoreApplication
import java.io.*


class BitmapManager {
    companion object{

        const val THUMBNAIL_SIZE:Double = 500.0

       fun Bitmap.toFile(contextWrapper: ContextWrapper,  fileName:String, compress:Boolean = true): File
       {
           val f = File(contextWrapper.cacheDir, fileName)
           f.createNewFile()
           val fullImageBytes = ByteArrayOutputStream()
           if(compress)
           {
               this.compress(Bitmap.CompressFormat.PNG, 0, fullImageBytes)
           }
           val byteArray = fullImageBytes.toByteArray()
           val fos = FileOutputStream(f)
           fos.write(byteArray)
           fos.flush()
           fos.close()
           return f
       }

        @Throws(FileNotFoundException::class, IOException::class)
        fun getThumbnail(uri: Uri?): Bitmap? {
            uri?:return null
            var input: InputStream? = CoreApplication.Instance.applicationContext.contentResolver.openInputStream(uri)
            val onlyBoundsOptions = BitmapFactory.Options()
            onlyBoundsOptions.inJustDecodeBounds = true
            onlyBoundsOptions.inDither = true //optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
            input?.close()

            if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
                return null
            }

            val originalSize =
                if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth)
                    onlyBoundsOptions.outHeight
                else onlyBoundsOptions.outWidth

            val ratio:Double =
                if (originalSize > THUMBNAIL_SIZE)
                    originalSize / THUMBNAIL_SIZE
                else 1.0
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio)
            bitmapOptions.inDither = true //optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //
            input = CoreApplication.Instance.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
            input?.close()
            return bitmap
        }

        private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
            val k = Integer.highestOneBit(Math.floor(ratio).toInt())
            return if (k == 0) 1 else k
        }
    }
}