package com.test.testapplication.model.store.sp

import android.content.Context

object ServiceSharedPreferences {

    private val PARAM_SERVICE_SP_FILE_NAME = "ServiceSharedPreferencesFileName"
    private val PARAM_SERVICE_SP_LEFT_TIME = "ServiceSharedPreferencesLeftTime"

    /**
     * Set left time for service.
     *
     * @param context The context is used.
     * @param time The time is left.
     */
    fun setLeftTime(context: Context, time: Long) {
        val sharedPreferences = context
                .getSharedPreferences(PARAM_SERVICE_SP_FILE_NAME, Context.MODE_PRIVATE)
        val ed = sharedPreferences.edit()
        ed.putLong(PARAM_SERVICE_SP_LEFT_TIME, time)
        ed.apply()
    }

    /**
     * Get left time for service.
     *
     * @param context The context is used.
     * @return left time if it was set or -1.
     */
    fun getLeftTime(context: Context): Long {
        val sharedPreferences = context
                .getSharedPreferences(PARAM_SERVICE_SP_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(PARAM_SERVICE_SP_LEFT_TIME, -1)
    }
}