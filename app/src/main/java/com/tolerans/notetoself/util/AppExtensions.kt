package com.tolerans.notetoself.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.ByteArrayOutputStream

object AppExtensions {

    fun Bitmap.toBase64String():String{

        val baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun String.toBitmap(): Bitmap? {
        val byteArray = Base64.decode(this, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }


    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}