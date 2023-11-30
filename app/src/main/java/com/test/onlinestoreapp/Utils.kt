package com.test.onlinestoreapp

import android.app.ProgressDialog
import android.content.Context

object Utils {

    private var progressDialog: ProgressDialog? = null

    fun showProgressDialog(context: Context) {
        progressDialog?.dismiss()
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage("Please wait...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}
