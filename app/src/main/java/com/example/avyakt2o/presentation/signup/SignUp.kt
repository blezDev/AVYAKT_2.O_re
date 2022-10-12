package com.example.avyakt2o.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.avyakt2o.R
import com.example.avyakt2o.data.Register
import com.example.avyakt2o.data.RegisterStatus
import com.example.avyakt2o.presentation.common.LoginViewModel
import com.example.avyakt2o.presentation.login.Login
import com.example.avyakt2o.presentation.otp.OtpActivity
import com.example.avyakt2o.utils.Constants.RE_TAG
import com.example.avyakt2o.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {


    private lateinit var etName : EditText
    private lateinit var etRollnumber : EditText
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var etLogin : TextView
    private lateinit var etPhone : EditText
    private lateinit var etYear : Spinner
    private lateinit var btnSignup : Button
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var regProgressBar : ProgressBar
    private val emailPattern = "[a-zA-Z0-9._-]+@giet+.+edu+"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        supportActionBar?.hide()

        etName = findViewById(R.id.etName)
        etRollnumber = findViewById(R.id.etRollnumber)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etLogin = findViewById(R.id.etLogin)
        btnSignup = findViewById(R.id.btnSignup)
        etPhone = findViewById(R.id.etPhone)
        etYear = findViewById(R.id.yearop) as Spinner
        regProgressBar = findViewById(R.id.regProgressBar)
        regProgressBar.visibility = View.INVISIBLE



        etLogin.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }

        btnSignup.setOnClickListener {
            Log.e("TAG",etYear.selectedItem.toString())

           if(etEmail.text.isNotEmpty() && validateEmail(etEmail.text.toString()) &&etName.text.isNotEmpty()&&etRollnumber.text.isNotEmpty()&&etPassword.text.isNotEmpty()&&etPhone.text.isNotEmpty()){
               regProgressBar.visibility = View.VISIBLE
               loginViewModel.retService.registerUser( Register(
                   email = etEmail.text.toString().trim(),
                   password = etPassword.text.toString(),
                   name = etName.text.toString(),
                   roll = etRollnumber .text.toString().trim(),
                   number = etPhone.text.toString().trim(),
                   year = etYear.selectedItem.toString()
               )
               ).enqueue(object :Callback<RegisterStatus>{
                   override fun onResponse(
                       call: Call<RegisterStatus>,
                       response: Response<RegisterStatus>
                   ) {
                       regProgressBar.visibility = View.INVISIBLE
                       when(response.code()) {
                           200 -> {
                               tokenManager = TokenManager(applicationContext)
                               tokenManager.saveEmail(etEmail.text.toString().trim())
                               gotoOTP()
                           }
                           else -> {
                               SweetAlertDialog(this@SignUp, SweetAlertDialog.ERROR_TYPE)
                                   .setTitleText("OOPS!!")
                                   .setContentText(response.body()?.message)
                                   .show()
                           }
                       }
                   }

                   override fun onFailure(call: Call<RegisterStatus>, t: Throwable) {
                       Log.e(RE_TAG,t.message.toString())
                       Toast.makeText(this@SignUp,"Something Went Wrong!!! Try Later!!",Toast.LENGTH_SHORT).show()

                   }
               })

           }
            else
           {SweetAlertDialog(this@SignUp,SweetAlertDialog.WARNING_TYPE)
               .setTitleText("!!HEY!!")
               .setContentText("All fields must be Filled!")
               .show()
           }
        }

        var options = arrayOf("1st Year","2nd Year","3rd Year","4th Year")

         etYear.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        etYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }


    private fun gotoOTP()
    {
            val intent = Intent(this,OtpActivity::class.java)
            startActivity(intent)
            finish()

    }

    private fun validEmail(Email:EditText): String? {
        val simpleInput: String = Email.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(simpleInput).matches()) {
            return "Invalid Email Address"
        }
        return null
    }
    fun validateEmail(email:String) : Boolean {
        if (email.matches(emailPattern.toRegex())) {
            return true
        } else {
            return false
        }
    }
    }
/*
private fun validEmail(Email:EditText): String? {
    val simpleInput: String = Email.text.toString()

    if (!Patterns.EMAIL_ADDRESS.matcher(simpleInput).matches()) {
        return "Invalid Email Address"
    }
    return null
}
 */
