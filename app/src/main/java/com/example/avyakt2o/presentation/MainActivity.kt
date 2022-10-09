package com.example.avyakt2o.presentation

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.avyakt2o.R
import com.example.avyakt2o.presentation.login.Login
import com.example.avyakt2o.utils.TokenManager
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    private lateinit var tokenManager: TokenManager

    lateinit var tvText: TextView
    lateinit var tvText2: TextView
    lateinit var tvText3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportActionBar?.hide()

        tvText = findViewById(R.id.tvText)
        tvText2 = findViewById(R.id.tvText2)
        tvText3 = findViewById(R.id.tvText3)

        ObjectAnimator.ofFloat(tvText, "translationX", 100f).apply {
            duration = 1000
            start()
        }

        ObjectAnimator.ofFloat(tvText2, "translationX", 200f).apply {
            duration = 2000
            start()
        }

        ObjectAnimator.ofFloat(tvText3, "translationX", 300f).apply {
            duration = 3000
            start()
        }

        runActivity()
    }


    fun runActivity()
    {
        if(!isDestroyed)
        {   tokenManager = TokenManager(applicationContext)

            val intentLogin = Intent(this, Login::class.java)
            val intentHome = Intent(this, HostActivity::class.java)
            val tmtask = timerTask {
                if(!isDestroyed)
                {
                    if(tokenManager.getToken() !=null){
                        startActivity(intentHome)
                    }else
                        startActivity(intentLogin)
                    finish()
                }
            }
            val timer = Timer()
            timer.schedule(tmtask,4000)
        }

    }
}