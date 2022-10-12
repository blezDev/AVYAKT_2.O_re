package com.example.avyakt2o.presentation.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
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


class Login : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private  lateinit var tvSignup : TextView
    private  lateinit var tvForgot : TextView
    private  lateinit var etEmail : EditText
    private  lateinit var etPassword : EditText
    private  lateinit var btnLogin : ElasticButton
    private lateinit var logProgressBar : ProgressBar
    private lateinit var loginViewModel: LoginViewModel
    private val emailPattern = "[a-zA-Z0-9._-]+@giet+.+edu+"

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

        if(!isOnline(this))
        {
            SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
            .setTitleText("WARNING")
            .setContentText("!! PLEASE CONNECT TO INTERNET !!")
            .show()
        }



        logProgressBar.visibility = View.INVISIBLE

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()


            if(etEmail.text.isNotEmpty() && validateEmail(email) && etPassword.text.toString().isNotEmpty())
            { logProgressBar.visibility = View.VISIBLE

                loginViewModel.retService.login(com.example.avyakt2o.data.Login(email,etPassword.text.toString())).enqueue(
                    object :Callback<LoginStatus>
                    {
                        override fun onResponse(
                            call: Call<LoginStatus>,
                            response: Response<LoginStatus>
                        ) {
                            Log.e("TAG",response.body()?.message.toString())
                            logProgressBar.visibility = View.INVISIBLE
                            when (response.code()) {
                                404 -> {
                                    Toast.makeText(this@Login,"User does not exist. \n Please create a account",Toast.LENGTH_LONG).show()
                                    val intent = Intent(this@Login,SignUp::class.java)
                                    startActivity(intent)
                                }
                                400 -> {
                                    tokenManager = TokenManager(applicationContext)
                                    Toast.makeText(this@Login,response.body()?.message.toString(),Toast.LENGTH_LONG).show()
                                    tokenManager.saveEmail(etEmail.text.toString().trim())
                                    goToOtp()
                                }
                                401 ->
                                {
                                    SweetAlertDialog(this@Login,SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("!!OPPS!!")
                                        .setContentText("INVALID CREDENTIAL")
                                        .show()
                                }
                                200 -> {
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
    //
    fun validateEmail(email:String) : Boolean {
        if (email.matches(emailPattern.toRegex())) {
            return true
        } else {
           return false
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