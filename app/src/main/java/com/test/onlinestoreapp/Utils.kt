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

    fun SetData(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences("onlinestore", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun GetData(context: Context, key: String, defaultValue: String = ""): String {
        val sharedPref = context.getSharedPreferences("onlinestore", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }
}
