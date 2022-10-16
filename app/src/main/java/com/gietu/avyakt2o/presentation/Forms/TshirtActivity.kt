package com.gietu.avyakt2o.presentation.Forms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.gietu.avyakt2o.R
import com.gietu.avyakt2o.data.EntriesStatus
import com.gietu.avyakt2o.data.TshirtsEntries
import com.gietu.avyakt2o.databinding.ActivityTshirtBinding
import com.gietu.avyakt2o.presentation.HostActivity
import com.gietu.avyakt2o.utils.Constants.EVENT_NAME
import com.gietu.avyakt2o.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TshirtActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTshirtBinding
    private lateinit var formViewModel : FormViewModel
    private lateinit var tokenManager: TokenManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_tshirt)
        formViewModel = ViewModelProvider(this)[FormViewModel::class.java]
        var options = arrayOf("S","M","L","XL","XXL","XXXL")
        val EventName = intent.getStringExtra(EVENT_NAME)
        binding.opGameType1.adapter =  ArrayAdapter(this,android.R.layout.simple_list_item_1,options)
        tokenManager = TokenManager(applicationContext)
        val token = tokenManager.getToken()!!
        SweetAlertDialog(this@TshirtActivity,SweetAlertDialog.WARNING_TYPE)
            .setTitleText("!!NOTICE!!")
            .setContentText("AFTER REGISTRATION\n" + "PAY FOR TSHIRT AT\n" + "Location- CSE ground floor\n" + "Time:5PM-7PM")
            .show()

        binding.btnVerifySoloGameSolo1.setOnClickListener {
            val name = binding.memberNameGameSolo1.text.toString()
            val rollno = binding.memberRollGameSolo1.text.toString()
            val email = binding.memberEmailGameSolo1.text.toString()
            val phone = binding.memberPhoneGameSolo1.text.toString()
            if(name.isBlank() || rollno.isEmpty() || email.isEmpty() || phone.isEmpty())
            {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Empty Fields")
                    .setContentText("All Fields must be filled")
                    .show()
            }else
            {
                val entry = TshirtsEntries(
                    token = token,
                    name = listOf(name),
                    email = listOf(email),
                    rollno = listOf(rollno),
                    phone = listOf(phone),
                    teamName = null,
                    size = binding.opGameType1.selectedItem.toString(),
                    eventName = EventName!!,
                    type = "SOLO"
                )
                formViewModel.retService.tshirtsEvent(entry).enqueue(object :Callback<EntriesStatus>{
                    override fun onResponse(
                        call: Call<EntriesStatus>,
                        response: Response<EntriesStatus>
                    ) {
                        when(response.code())
                        {
                            200->{
                                SweetAlertDialog(this@TshirtActivity,SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("DONE😎")
                                    .setContentText("Successfully registered. Don't forget to pay for tshirt ASAF")
                                    .setConfirmClickListener { val intent = Intent(this@TshirtActivity,HostActivity::class.java)
                                     startActivity(intent)
                                    finish()}
                                    .show()
                            }
                            else ->{
                                SweetAlertDialog(this@TshirtActivity,SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("WARNING")
                                    .setContentText("Something went wrong. Pls try later!!")
                                    .show()
                            }

                        }
                    }

                    override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                        Toast.makeText(this@TshirtActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                })





            }
        }
    }
}