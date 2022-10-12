package com.example.avyakt2o.presentation.Forms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.avyakt2o.R
import com.example.avyakt2o.data.EntriesStatus
import com.example.avyakt2o.data.TshirtsEntries
import com.example.avyakt2o.databinding.ActivityTshirtBinding
import com.example.avyakt2o.utils.Constants.EVENT_NAME
import com.example.avyakt2o.utils.TokenManager
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
        var options = arrayOf("S","M","L","XL","XXL","XXXL")
        val EventName = intent.getStringExtra(EVENT_NAME)
        binding.opGameType1.adapter =  ArrayAdapter(this,android.R.layout.simple_list_item_1,options)
        tokenManager = TokenManager(applicationContext)
        val token = tokenManager.getToken()!!

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
                                SweetAlertDialog(this@TshirtActivity,SweetAlertDialog.NORMAL_TYPE)
                                    .setTitleText("INSTRUCTIONS")

                                    .setContentText(getString(R.string.instruction_SOLO_EVENT))
                                    .show()
                            }

                        }
                    }

                    override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                        Toast.makeText(this@TshirtActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                })

                Toast.makeText(this, entry.toString(), Toast.LENGTH_SHORT).show()



            }
        }
    }
}