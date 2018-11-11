package com.test.testapplication.model.store.sp

import android.content.Context

object ServiceSharedPreferences {

    private val PARAM_SERVICE_SP_FILE_NAME = "ServiceSharedPreferencesFileName"
    private val PARAM_SERVICE_SP_IS_RUNNING = "ServiceSharedPreferencesIsRunning"

    /**
     * Set service is running or not.
     *
     * @param context The context is used.
     * @param isRunning Is service running.
     */
    fun setIsServiceRunning(context: Context, isRunning: Boolean) {
        val sharedPreferences = context
                .getSharedPreferences(PARAM_SERVICE_SP_FILE_NAME, Context.MODE_PRIVATE)
        val ed = sharedPreferences.edit()
        ed.putBoolean(PARAM_SERVICE_SP_IS_RUNNING, isRunning)
        ed.apply()
    }

    /**
     * Get service is running or not.
     *
     * @param context The context is used.
     * @return true if service is running, otherwise false.
     */
    fun getIsServiceRunning(context: Context): Boolean {
        val sharedPreferences = context
                .getSharedPreferences(PARAM_SERVICE_SP_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(PARAM_SERVICE_SP_IS_RUNNING, false)
    }
}