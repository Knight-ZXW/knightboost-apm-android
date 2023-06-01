package com.knightboost.apm.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            val intent: Intent = Intent()
            intent.setClass(this,MainActivity::class.java)
            startActivity(intent)
        }, 500)
    }
}