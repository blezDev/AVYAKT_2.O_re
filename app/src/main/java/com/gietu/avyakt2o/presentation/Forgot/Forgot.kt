package com.gietu.avyakt2o.presentation.Forgot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.gietu.avyakt2o.R
import com.gietu.avyakt2o.databinding.ActivityForgotBinding
import com.gietu.avyakt2o.presentation.otp.OtpActivity
import com.gietu.avyakt2o.utils.Constants.CONTEXT
import com.gietu.avyakt2o.utils.Constants.EMAIL
import com.gietu.avyakt2o.utils.Constants.PASSWORD

class Forgot : AppCompatActivity() {

   private lateinit var binding : ActivityForgotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot)

        supportActionBar?.hide()


        binding.NewPasswordBtn.setOnClickListener {
            if(binding.etEmail.text.toString().isNotEmpty() && binding.etNewPassword.text.toString().isNotEmpty())
            {
                val intent = Intent(this,OtpActivity::class.java)
                intent.putExtra(EMAIL,binding.etEmail.text.toString())
                intent.putExtra(PASSWORD,binding.etNewPassword.text.toString())
                intent.putExtra(CONTEXT,"FORGET")
                startActivity(intent)
            }
            else
            {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Empty Fields")
                    .setContentText("All Fields must be filled")
                    .show()
            }
        }


    }
}