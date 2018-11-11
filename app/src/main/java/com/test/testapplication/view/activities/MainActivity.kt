package com.test.testapplication.view.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.test.testapplication.BuildConfig
import com.test.testapplication.R
import com.test.testapplication.model.services.TimerForegroundService
import com.test.testapplication.model.store.sp.ServiceSharedPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buildVersionName.text = BuildConfig.BUILD_VARIANT_NAME

        //Start service if it hasn't been started before
        if (!ServiceSharedPreferences.getIsServiceRunning(this))
            startService(Intent(applicationContext, TimerForegroundService::class.java))
    }
}
