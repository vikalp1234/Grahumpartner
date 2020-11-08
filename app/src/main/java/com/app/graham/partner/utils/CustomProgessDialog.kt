package com.app.graham.partner.utils

import android.R
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper

class CustomProgessDialog(context: Context?) {
    var progressDialog: ProgressDialog
    fun hide_progress() {
        progressDialog.dismiss()
    }

    init {
        progressDialog = ProgressDialog(ContextThemeWrapper(context, R.style.Theme_Holo_Light_Dialog))
        progressDialog.setMessage("Loading...") // Setting Message
        progressDialog.setTitle("Please wait...") // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner
        progressDialog.show() // Display Progress Dialog
        progressDialog.setCancelable(false)
    }
}