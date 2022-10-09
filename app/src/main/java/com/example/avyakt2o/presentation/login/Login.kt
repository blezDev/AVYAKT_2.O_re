package com.example.avyakt2o.presentation.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.avyakt2o.R
import com.example.avyakt2o.data.LoginStatus
import com.example.avyakt2o.presentation.Forgot.Forgot
import com.example.avyakt2o.presentation.HostActivity
import com.example.avyakt2o.presentation.common.LoginViewModel
import com.example.avyakt2o.presentation.otp.OtpActivity
import com.example.avyakt2o.presentation.signup.SignUp
import com.example.avyakt2o.utils.TokenManager
import com.skydoves.elasticviews.ElasticButton

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timerTask

class Login : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private  lateinit var tvSignup : TextView
    private  lateinit var tvForgot : TextView
    private  lateinit var etEmail : EditText
    private  lateinit var etPassword : EditText
    private  lateinit var btnLogin : ElasticButton
    private lateinit var logProgressBar : ProgressBar
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        tvSignup = findViewById(R.id.tvSignup)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvForgot = findViewById(R.id.tvForgot)
        logProgressBar = findViewById(R.id.logProgressBar)
        val imageView = findViewById<ImageView>(R.id.imageView2)
        if(!isOnline(this))
        {
            SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
            .setTitleText("WARNING")
            .setContentText("!! PLEASE CONNECT TO INTERNET !!")
            .show()
        }

        logProgressBar.visibility = View.INVISIBLE

        btnLogin.setOnClickListener {

            if(etEmail.text.isNotEmpty() && etPassword.text.toString().isNotEmpty())
            { logProgressBar.visibility = View.VISIBLE
                loginViewModel.retService.login(com.example.avyakt2o.data.Login(etEmail.text.toString().trim(),etPassword.text.toString())).enqueue(
                    object :Callback<LoginStatus>
                    {
                        override fun onResponse(
                            call: Call<LoginStatus>,
                            response: Response<LoginStatus>
                        ) {
                            logProgressBar.visibility = View.INVISIBLE
                            when (response.body()?.message) {
                                "User Doesn't exist." -> {
                                    Toast.makeText(this@Login,response.body()?.message.toString(),Toast.LENGTH_LONG).show()
                                    val intent = Intent(this@Login,SignUp::class.java)
                                    startActivity(intent)
                                }
                                "User Account is not active." -> {
                                    tokenManager = TokenManager(applicationContext)
                                    tokenManager.saveEmail(etEmail.text.toString().trim())
                                    goToOtp()
                                }
                                null ->
                                {
                                    SweetAlertDialog(this@Login,SweetAlertDialog.WARNING_TYPE)

                                        .setTitleText("!!OPPS!!")
                                        .setContentText("Wrong Credentials")
                                        .show()
                                }
                                else -> {
                                    tokenManager = TokenManager(applicationContext)
                                    tokenManager.saveToken(response.body()?.token!!)
                                    tokenManager.saveEmail(etEmail.text.toString())
                                    Toast.makeText(this@Login,response.body()?.message.toString(),Toast.LENGTH_LONG).show()
                                    goToHome()
                                    Log.e("TAG",response.body()?.message.toString())
                                }
                            }
                        }



                        override fun onFailure(call: Call<LoginStatus>, t: Throwable) {
                            Toast.makeText(this@Login,"Something went wrong",Toast.LENGTH_SHORT).show()
                        }
                    }
                )

            }
            else{

                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Empty Fields")
                    .setContentText("All Fields must be filled")
                    .show()
            }

            /*gotoSignup()*/
        }

        tvSignup.setOnClickListener {
            gotoSignup()

        }
        tvForgot.setOnClickListener {
            goToForget()
        }

    }
    private fun goToForget()
    {
        val intent = Intent(this, Forgot::class.java)
        startActivity(intent)

    }
    private fun goToHome(){
        val intent = Intent(this, HostActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun goToOtp()
    {
        val intent = Intent(this, OtpActivity::class.java)

        startActivity(intent)
    }

    private fun gotoSignup() {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        finish()
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}