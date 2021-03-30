package id.timsap.retrash.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import id.timsap.retrash.R
import id.timsap.retrash.ui.dashboardactivity.Dashboard
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animation =
            AnimationUtils.loadAnimation(this, R.anim.mytransition)
        img_logo.startAnimation(animation)

        val i = Intent(this@SplashActivity, Dashboard::class.java)
        val timer: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    startActivity(i)
                    finish()
                }
            }
        }
        timer.start()

    }
}