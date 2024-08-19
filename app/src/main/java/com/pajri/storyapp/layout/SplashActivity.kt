package com.pajri.storyapp.layout

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.pajri.storyapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashTime: Long = 4000

        playAnimation()
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTime)
    }

    private fun playAnimation() {
        val splash = ObjectAnimator.ofFloat(findViewById(R.id.splash), View.ALPHA, 1f).setDuration(500)
        val splash2 = ObjectAnimator.ofFloat(findViewById(R.id.splash), View.TRANSLATION_X, -60f, 60f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        AnimatorSet().apply {
            playSequentially(splash, splash2)
            start()
        }
    }
}