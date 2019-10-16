package com.uchoa.twitter.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.uchoa.twitter.R
import kotlinx.android.synthetic.main.activity_splash.*

class TwitterSplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startAnimations()
    }

    private fun startAnimations() {

        val anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                Handler().postDelayed({ startTwitterAppActivity() }, 1000)
            }
        })

        start_image_logo.startAnimation(anim)
    }

    private fun startTwitterAppActivity() {
        val intent = Intent(this, TwitterAppActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}