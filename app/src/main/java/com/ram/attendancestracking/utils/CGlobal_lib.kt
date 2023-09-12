package com.ram.attendancestracking.utils

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.ram.attendancestracking.R
import com.ram.attendancestracking.utils.Constants.SHARED_PREFERENCE_PRESISTENT

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CGlobal_lib(context: Context?) {

    private lateinit var context: Context
    private lateinit var mEditorVersionPersistent: SharedPreferences.Editor
    private lateinit var mPrefsVersionPersistent: SharedPreferences

    init {
        try {
            if (context != null) {
                this.context = context
            };
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var instant: CGlobal_lib? = null
        private var authority: String? = null

        @Synchronized
        fun getInstance(context: Context?): CGlobal_lib? {
            if (instant == null) {
                instant = CGlobal_lib(context)
            }
            return instant
        }

    }

    fun getPersistentPreference(context: Context?): SharedPreferences {/*   if (mPrefsVersionPersistent == null)
           {
               mPrefsVersionPersistent = context.applicationContext
                   .getSharedPreferences(
                       CGlobal_Constant.SHARED_PREFERENCE_PRESISTENT,
                       Context.MODE_PRIVATE)
           }*/

        mPrefsVersionPersistent = context?.applicationContext!!.getSharedPreferences(
            SHARED_PREFERENCE_PRESISTENT, Context.MODE_PRIVATE
        )

        return mPrefsVersionPersistent
    }

    fun getPersistentPreferenceEditor(context: Context?): SharedPreferences.Editor {/*  if (mEditorVersionPersistent == null)
          {
              mEditorVersionPersistent = getPersistentPreference(context).edit()
          }*/
        mEditorVersionPersistent = getPersistentPreference(context).edit()
        return mEditorVersionPersistent
    }

    fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        return formatter.format(date)
    }

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("HH:mm")
        val date = Date()
        return formatter.format(date)
    }

    fun toDate(it: String): Date {
        val dateFormat = "dd/MM/yyyy HH:mm:ss"
        val timeZone: TimeZone = TimeZone.getTimeZone("UTC")

        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(it)
    }

    fun formatTo(
        date: Date, dateFormat: String
    ): String {
        val timeZone: TimeZone = TimeZone.getDefault()
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(date)
    }

    fun toCustomDate(it: String): String {
        val originalDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val targetDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = originalDateFormat.parse(it)
        return targetDateFormat.format(date)
    }

}