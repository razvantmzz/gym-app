package com.razvantmz.onemove.core.managers

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class StorageManager {
   companion object
   {
       fun saveToInternalStorage(context: Context, bitmapImage: Bitmap, name:String ): String? {
           val cw = ContextWrapper(context)
           val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
           val myPath = File(directory, "$name.jpg")
           try {
               val fos = FileOutputStream(myPath)
               bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
               fos.close()
           } catch (e: Exception) {
               e.printStackTrace()
           }
           return directory.absolutePath
       }


       fun loadImageFromStorage(context:Context, name:String): Bitmap? {
           try {
               val cw = ContextWrapper(context)
               val path1 = cw.getDir("imageDir", Context.MODE_PRIVATE)
               val f = File(path1, "$name.jpg")
               return BitmapFactory.decodeStream(FileInputStream(f))
           } catch (e: FileNotFoundException) {
               e.printStackTrace()
           }
           return null
       }
   }
}