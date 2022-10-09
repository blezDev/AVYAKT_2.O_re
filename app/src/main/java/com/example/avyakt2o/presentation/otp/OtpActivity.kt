package com.example.avyakt2o.presentation.otp

import android.content.Intent
import android.hardware.input.InputManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.avyakt2o.R
import com.example.avyakt2o.data.OTP
import com.example.avyakt2o.data.OtpStatus
import com.example.avyakt2o.data.ResetOTP
import com.example.avyakt2o.data.VerifyOTP
import com.example.avyakt2o.databinding.ActivityOtpBinding
import com.example.avyakt2o.presentation.common.LoginViewModel
import com.example.avyakt2o.presentation.signup.SignUp
import com.example.avyakt2o.utils.Constants.CONTEXT
import com.example.avyakt2o.utils.Constants.EMAIL
import com.example.avyakt2o.utils.Constants.PASSWORD
import com.example.avyakt2o.utils.Constants.RE_TAG
import com.example.avyakt2o.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtpActivity : AppCompatActivity() {
    private lateinit var tokenManager: TokenManager
    private lateinit var binding: ActivityOtpBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var frameout : LinearLayout


    private lateinit var otp1 :EditText
    private lateinit var otp2 :EditText
    private lateinit var otp3 :EditText
    private lateinit var otp4 :EditText
    private lateinit var otp5 :EditText
    private lateinit var otp6 :EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = DataBindingUtil.setContentView(this,R.layout.activity_otp)
        tokenManager= TokenManager(applicationContext)
        binding.otpProgess.visibility = View.INVISIBLE

        frameout = findViewById(R.id.frameout)

        otp1 = findViewById(R.id.otp1)
        otp2 = findViewById(R.id.otp2)
        otp3 = findViewById(R.id.otp3)
        otp4 = findViewById(R.id.otp4)
        otp5 = findViewById(R.id.otp5)
        otp6 = findViewById(R.id.otp6)


        setListner()

        when(intent.getStringExtra(CONTEXT)){
            "FORGET"->{
                binding.otpProgess.visibility = View.VISIBLE
                val F_email = intent.getStringExtra(EMAIL)
                val F_password = intent.getStringExtra(PASSWORD)
                if(F_email.toString().isNotEmpty() && F_password.toString().isNotEmpty())
                {
                    loginViewModel.retService.getOTP(OTP(F_email.toString())).enqueue(
                        object : Callback<OtpStatus>{
                            override fun onResponse(
                                call: Call<OtpStatus>,
                                response: Response<OtpStatus>
                            ) {
                               binding.otpProgess.visibility = View.INVISIBLE
                               SweetAlertDialog(this@OtpActivity,SweetAlertDialog.NORMAL_TYPE)
                                   .setTitleText("NOTICE")
                                   .setTitleText(response.body()?.message.toString())
                                   .show()
                            }

                            override fun onFailure(call: Call<OtpStatus>, t: Throwable) {
                                Toast.makeText(this@OtpActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                    binding.btnVerify.setOnClickListener {
                        val otp = getOTP()

                        if(otp != 0)
                        {
                            Log.e("TAG","RESET PASSWORD")
                            binding.otpProgess.visibility = View.VISIBLE
                            loginViewModel.retService.restPassword(ResetOTP(email = F_email.toString(), password = F_password.toString(), opt = otp)).enqueue(
                                object : Callback<OtpStatus>{
                                    override fun onResponse(
                                        call: Call<OtpStatus>,
                                        response: Response<OtpStatus>
                                    ) {
                                        binding.otpProgess.visibility = View.INVISIBLE
                                        Log.e("TAG",response.code().toString() + response.body()?.message)
                                        when(response.code())
                                        {


                                            200-> { SweetAlertDialog(this@OtpActivity,SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("😆")
                                                .setContentText(response.body()?.message.toString())
                                                .setConfirmClickListener { goToLogin() }
                                                .show()

                                            }
                                            401 ->{
                                                SweetAlertDialog(this@OtpActivity,SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("!!OOPS!!")
                                                .setContentText("INVAILD OTP")

                                                .show()

                                            }
                                            400 ->{
                                                SweetAlertDialog(this@OtpActivity,SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("!!OOPS!!")
                                                    .setContentText(response.body()?.message.toString())
                                                    .setConfirmClickListener { goToSignUp() }
                                                    .show()
                                            }

                                        }



                                    }

                                    override fun onFailure(call: Call<OtpStatus>, t: Throwable) {
                                        Toast.makeText(this@OtpActivity," Retry",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }

                }



            }
            else ->
            {
                val email = tokenManager.getEmail()
                if (email != null){
                    loginViewModel.retService.getOTP(OTP(email)).enqueue(
                        object : Callback<OtpStatus>{
                            override fun onResponse(
                                call: Call<OtpStatus>,
                                response: Response<OtpStatus>
                            ) {
                                SweetAlertDialog(this@OtpActivity,SweetAlertDialog.NORMAL_TYPE)
                                    .setTitleText("NOTICE")
                                    .setTitleText(response.body()?.message.toString())
                                    .show()
                            }

                            override fun onFailure(call: Call<OtpStatus>, t: Throwable) {
                                Toast.makeText(this@OtpActivity,"Something went wrong",Toast.LENGTH_SHORT).show()

                            }
                        }
                    )
                }
                else{
                    Toast.makeText(this,"EMAIL is missing",Toast.LENGTH_SHORT).show()
                }



                binding.btnVerify.setOnClickListener {
                    val otp = getOTP()
                    if (email.toString().isEmpty()  && otp == 0)
                    {
                        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("😖")
                            .setContentText("ENTER OTP PLS")
                            .show()
                    }
                    else {
                        Log.e("TAG","REGISTER PASSWORD")
                        loginViewModel.retService.verifyOTP(VerifyOTP(email = email!!, otp)).enqueue(
                            object : Callback<OtpStatus> {
                                override fun onResponse(
                                    call: Call<OtpStatus>,
                                    response: Response<OtpStatus>
                                ) {
                                    when (response.code()) {
                                        200 -> {
                                            SweetAlertDialog(
                                                this@OtpActivity,
                                                SweetAlertDialog.SUCCESS_TYPE
                                            )
                                                .setTitleText("😆")
                                                .setContentText(response.body()?.message.toString())
                                                .setConfirmClickListener { goToLogin() }
                                                .show()

                                        }
                                         401-> {
                                            SweetAlertDialog(
                                                this@OtpActivity,
                                                SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("OOPS!!")
                                                .setContentText("INVAILD OTP")
                                                .show()

                                        }
                                        else ->{
                                            SweetAlertDialog(
                                                this@OtpActivity,
                                                SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("OOPS!!")
                                                .setContentText(response.body()?.message.toString())
                                                .show()
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<OtpStatus>, t: Throwable) {
                                    Log.e(RE_TAG, t.printStackTrace().toString())
                                }
                            }
                        )
                    }
                }
            }
        }



    }
    private fun goToLogin(){
        val intent = Intent(this, com.example.avyakt2o.presentation.login.Login::class.java)
        startActivity(intent)
        finish()
    }
    private fun getOTP(): Int {
        return if(binding.otp1.text.isNotEmpty()){
            val opt = "${binding.otp1.text}${binding.otp2.text}${binding.otp3.text}${binding.otp4.text}${binding.otp5.text}${binding.otp6.text}"
            opt.toInt()
        } else {
            Toast.makeText(this,"All field must be filled",Toast.LENGTH_SHORT).show()
            0
        }

    }
    private fun goToSignUp()
    {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        finish()
    }

    private fun setTextChange(fromEditText:EditText, targetEditText: EditText? = null, done:(() -> Unit)? = null)
    {
        fromEditText.addTextChangedListener {
            it?.let {
                string ->
                if(string.isNotEmpty()){
                    targetEditText?.let{  editText ->

                        editText.isEnabled = true
                        editText.requestFocus()
                    }?: run{
                        done ?.let{ done ->
                            done()
                        }
                    }
                    fromEditText.clearFocus()
                    fromEditText.isEnabled = false
                }
            }
        }
    }

    private fun setListner(){
        frameout.setOnClickListener {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(frameout.windowToken,0)
        }

        setTextChange(fromEditText = otp1, targetEditText = otp2)
        setTextChange(fromEditText = otp2, targetEditText = otp3)
        setTextChange(fromEditText = otp3, targetEditText = otp4)
        setTextChange(fromEditText = otp4, targetEditText = otp5)
        setTextChange(fromEditText = otp5, targetEditText = otp6)
        setTextChange(fromEditText = otp6, done={
            getOTP()
        })

    }
}