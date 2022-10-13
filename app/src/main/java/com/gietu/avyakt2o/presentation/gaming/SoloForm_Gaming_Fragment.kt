package com.gietu.avyakt2o.presentation.gaming

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.gietu.avyakt2o.R
import com.gietu.avyakt2o.data.EntriesStatus
import com.gietu.avyakt2o.data.GameEntries
import com.gietu.avyakt2o.databinding.FragmentSoloFormGamingBinding
import com.gietu.avyakt2o.presentation.Forms.FormViewModel
import com.gietu.avyakt2o.presentation.HostActivity
import com.gietu.avyakt2o.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SoloForm_Gaming_Fragment : Fragment() {
    private lateinit var binding : FragmentSoloFormGamingBinding
    private lateinit var formViewModel : FormViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_solo_form__gaming_,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       val  args = arguments?.getString("key")
        val eventName = when(args)
        {
            "FIFA" -> "FIFA 19"
            "NFS" -> "NFS"
            else -> "game event"
        }
        getAlertTeam()
        tokenManager = TokenManager(requireContext())
        formViewModel = ViewModelProvider(this)[FormViewModel::class.java]
        binding.instructionBrnGameSolo.setOnClickListener {
            getAlertTeam()
        }

        binding.btnVerifySoloGameSolo.setOnClickListener {
            val token = tokenManager.getToken()!!
            val name = binding.memberNameGameSolo.text.toString()
            val rollno = binding.memberRollGameSolo.text.toString()
            val email = binding.memberEmailGameSolo.text.toString()
            val phone = binding.memberPhoneGameSolo.text.toString()
            if(name.isBlank() || rollno.isEmpty() || email.isEmpty() || phone.isEmpty())
            {
                SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Empty Fields")
                    .setContentText("All Fields must be filled")
                    .show()
            }
            else
            {

                formViewModel.retService.gamingEvent(GameEntries(
                    token = token,
                    name = listOf(name),
                    email = listOf(email),
                    rollno = listOf(rollno),
                    phone = listOf(phone),
                    teamName = null,
                    category = args.toString(),
                    eventName = eventName,
                    type = "SOLO"

                )).enqueue(object : Callback<EntriesStatus>{
                    override fun onResponse(
                        call: Call<EntriesStatus>,
                        response: Response<EntriesStatus>
                    ) {
                       when(response.code())
                       {
                           200->{
                               SweetAlertDialog(requireContext(),SweetAlertDialog.SUCCESS_TYPE)
                                   .setTitleText("🥳")
                                   .setContentText("Registration successfull!! \n Don't forget to pay for the event ASAF")
                                   .setConfirmClickListener {
                                       val intent = Intent(requireContext(), HostActivity::class.java)
                                       requireContext().startActivity(intent)
                                       activity?.finish()
                                   }
                                   .show()
                               getAlertTeam()
                           }
                           500 ->
                           {
                               SweetAlertDialog(requireContext(),SweetAlertDialog.SUCCESS_TYPE)
                                   .setTitleText("😥")
                                   .setContentText("some error occured")
                                   .show()
                           }
                       }
                    }

                    override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                        Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
                    }

                })
            }

        }
    }
    private  fun getAlertTeam()
    {
        SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
            .setTitleText("!!NOTICE!!")
            .setContentText("Games:\n" + "Solo- RS 20\n" + "Group- RS 100\n" + "Location- CSE ground floor\n" + "Time:5PM-7PM")
            .show()
    }
}