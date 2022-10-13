package com.gietu.avyakt2o.presentation.gaming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.gietu.avyakt2o.Adapter.TeamFormAdapter
import com.gietu.avyakt2o.R
import com.gietu.avyakt2o.data.EntriesStatus
import com.gietu.avyakt2o.data.GameEntries
import com.gietu.avyakt2o.databinding.FragmentTeamFormGamingBinding
import com.gietu.avyakt2o.presentation.Forms.FormViewModel
import com.gietu.avyakt2o.presentation.HostActivity
import com.gietu.avyakt2o.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TeamForm_GamingFragment : Fragment() {
    private lateinit var binding:FragmentTeamFormGamingBinding

    private lateinit var FormReg : ArrayList<com.gietu.avyakt2o.data.TeamForm>
    private lateinit var regFormAdapter : TeamFormAdapter
    private lateinit var formViewModel: FormViewModel
    private lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_team_form__gaming,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAlertTeam()
        formViewModel = ViewModelProvider(this)[FormViewModel::class.java]
        val sarList = arrayOf("Robo Soccer","Taskmaster","AquaRobo","Line Follower")
        val gameList = arrayOf("FIFA 19","NFS","Valorant","COD","Modern Warfare","PUBG")
        var MAX_SIZE = 0
        var MIN_SIZE = 0
        var eventName = ""
        val nameList = ArrayList<String>()
        val emailList = ArrayList<String>()
        val rollnoList = ArrayList<String>()
        val phoneList = ArrayList<String>()
        var count = 0
        val  args = arguments?.getString("key")
        when(args)
        {
            "VALORANT"->{
                MAX_SIZE = 5
                eventName = "Valorant"
            }
            "COD"->{
                MAX_SIZE = 5
                eventName = "COD MOBILE"
            }
            "MODERN_WARFARE"->{
                MAX_SIZE = 4
                eventName = "Modern Warfare"
            }
            "PUBG"->{
                MAX_SIZE = 4
                eventName = "BGMI"
            }
            "ROBO_SOCCER"->{
                MAX_SIZE = 4
                MIN_SIZE = 2
                eventName = "Robo Soccer"
            }
            "TASKMASTER"->{
                MAX_SIZE = 4
                MIN_SIZE = 2
                eventName = "Taskmaster"
            }
            "AQUAROBO"->{
                MAX_SIZE = 4
                MIN_SIZE = 2
                eventName = "AquaRobo"
            }
            "LINE_FOLLOWER"->{
                MAX_SIZE = 4
                MIN_SIZE = 2
                eventName = "Line Follower"
            }
        }
        when(eventName)
        {
            in gameList ->{
                    SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("!!NOTICE!!")
                        .setContentText("Games:\n" + "Solo- RS 20\n" + "Group- RS 100\n" + "Location- CSE ground floor\n" + "Time:5PM-7PM")
                        .show()
            }
            in sarList ->{
                SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("!!NOTICE!!")
                    .setContentText("Robotics:\n" + "RS 200\n" + "Location- CSE ground floor\n" + "Time:5PM-7PM")
                    .show()

            }
        }




        binding.FormRecycle1.layoutManager = LinearLayoutManager(requireContext())
        tokenManager = TokenManager(requireContext())
        val token = tokenManager.getToken()!!
        FormReg = ArrayList()

        regFormAdapter = TeamFormAdapter(FormReg,requireContext())
        binding.FormRecycle1.adapter = regFormAdapter
        regFormAdapter.onItemClick =
            {
                val name = it.name
                val rollno = it.roll.trim()
                val email = it.mail
                val phone = it.phone.trim()
                nameList.add(name)
                rollnoList.add(rollno)
                emailList.add(email)
                phoneList.add(phone)
                count += 1

            }
        binding.SubmitFormBtn1.setOnClickListener {
            if(binding.Name2.text.isNullOrEmpty() || binding.email2.text.isNullOrEmpty() || binding.phone2.text.isNullOrEmpty() || binding.roll2.text.isNullOrEmpty() || binding.teamName2.text.isNullOrEmpty()) {
                SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("FILL OUT DETAILS")
                    .show()
            }else
            {
                nameList.add(binding.Name2.text.toString())
                rollnoList.add(binding.roll2.text.toString())
                emailList.add(binding.email2.text.toString())
                phoneList.add(binding.phone2.text.toString())
                val teamName = binding.teamName2.text.toString()
                val nameListSub = nameList.distinct()
                if( (teamName.isEmpty() || teamName.isBlank()))
                {
                    SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("WARNING!!")
                        .setContentText("THIS IS A GROUP EVENT.\n Team name is necessary along with team members details.")
                        .show()

                }else if( eventName in gameList && nameListSub.size < (MAX_SIZE))
                {
                    SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("WARNING!!")
                        .setContentText("THIS IS A GROUP EVENT of ${MAX_SIZE} participation.")
                        .show()
                }
                else if( eventName in sarList &&  nameListSub.size < (MIN_SIZE))
                {
                    SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("WARNING!!")
                        .setContentText("THIS IS A GROUP EVENT of ${MIN_SIZE} of minimum participation.")
                        .show()
                }

                else{
                    when(args){
                        "VALORANT"->{
                            val entry = GameEntries(token =token,name = nameList.distinct().reversed(), rollno = rollnoList.distinct().reversed(),email = emailList.distinct().reversed(),
                                phone = phoneList.distinct().reversed(), teamName =  teamName, category = args!!, eventName = eventName, type =  "GROUP")

                           Log.e("TAG",entry.toString())

                            register(entry)
                        }
                        "COD"->{
                            val entry = GameEntries(token =token,name = nameList.distinct().reversed(), rollno = rollnoList.distinct().reversed(),email = emailList.distinct().reversed(),
                                phone = phoneList.distinct().reversed(), teamName =  teamName, category = args!!, eventName = eventName, type =  "GROUP")
                            register(entry)
                        }
                        "MODERN_WARFARE"->{
                            val entry = GameEntries(token =token,name = nameList.distinct().reversed(), rollno = rollnoList.distinct().reversed(),email = emailList.distinct().reversed(),
                                phone = phoneList.distinct().reversed(), teamName =  teamName, category = args!!, eventName = eventName, type =  "GROUP")
                            register(entry)
                        }
                        "PUBG"->{
                            val entry = GameEntries(token =token,name = nameList.distinct().reversed(), rollno = rollnoList.distinct().reversed(),email = emailList.distinct().reversed(),
                                phone = phoneList.distinct().reversed(), teamName =  teamName, category = args!!, eventName = eventName, type =  "GROUP")
                            register(entry)
                        }
                        "ROBO_SOCCER"->{
                            val entry = GameEntries(token =token,name = nameList.distinct().reversed(), rollno = rollnoList.distinct().reversed(),email = emailList.distinct().reversed(),
                                phone = phoneList.distinct().reversed(), teamName =  teamName, category = args!!, eventName = eventName, type =  "GROUP")

                            sarsRegister(entry)
                        }
                        "TASKMASTER"->{
                            val entry = GameEntries(token =token,name = nameList.distinct().reversed(), rollno = rollnoList.distinct().reversed(),email = emailList.distinct().reversed(),
                                phone = phoneList.distinct().reversed(), teamName =  teamName, category = args!!, eventName = eventName, type =  "GROUP")
                            sarsRegister(entry)

                        }
                        "AQUAROBO"->{
                            val entry = GameEntries(token =token,name = nameList.distinct().reversed(), rollno = rollnoList.distinct().reversed(),email = emailList.distinct().reversed(),
                                phone = phoneList.distinct().reversed(), teamName =  teamName, category = args!!, eventName = eventName, type =  "GROUP")
                            sarsRegister(entry)
                        }
                        "LINE_FOLLOWER"->{
                            val entry = GameEntries(token =token,name = nameList.distinct().reversed(), rollno = rollnoList.distinct().reversed(),email = emailList.distinct().reversed(),
                                phone = phoneList.distinct().reversed(), teamName =  teamName, category = args!!, eventName = eventName, type =  "GROUP")
                            sarsRegister(entry)

                        }
                    }




                }
            }
        }
             binding.addCard2.setOnClickListener {
            if(FormReg.size > MAX_SIZE -2)
            {
                SweetAlertDialog(requireContext(), SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("INSTRUCTION")
                    .setContentText("Max Participation Limit is ${MAX_SIZE}")
                    .show()
            }
            else
            {
                if(binding.teamName2.text.isEmpty() || binding.teamName2.text.isBlank()){
                    SweetAlertDialog(requireContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("WARNING")
                        .setContentText("YOU LEFT OUT TEAM NAME IF YOU ARE ADDING MORE MEMBERS")
                        .show()
                }
                else
                    addNewItem(0,com.gietu.avyakt2o.data.TeamForm("Name","Roll No","Official Mail","Phone Number"))
            }
        }




    }
    fun addNewItem(index : Int ?=0  , teamForm : com.gietu.avyakt2o.data.TeamForm ){
        FormReg.add(0,teamForm)
        regFormAdapter.notifyDataSetChanged()
    }
    private  fun getAlertTeam()
    {
        SweetAlertDialog(requireContext(),SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("INSTRUCTIONS")
            .setContentText(getString(R.string.instruction_TEAM))
            .show()
    }
    private fun sarsRegister(entry : GameEntries){
        formViewModel.retService.roboticsEvent(entry).enqueue(object : Callback<EntriesStatus>{
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {
                when(response.code())
                {
                    200->{
                        SweetAlertDialog(requireContext(),SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("🎉")
                            .setContentText("Registration successfull!! \n Don't forget to pay for the event ASAF")
                            .setConfirmClickListener {
                                val intent = Intent(requireContext(),HostActivity::class.java)
                                requireContext().startActivity(intent)
                                activity?.finish()
                            }
                            .show()
                    }
                    400->
                    {
                        SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING")
                            .setContentText("Team name already exist.")
                            .show()
                    }
                    500->{
                        SweetAlertDialog(requireContext(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("WARNING")
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



    private fun register(entry : GameEntries)
    {
        formViewModel.retService.gamingEvent(entry).enqueue(object : Callback<EntriesStatus>{
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {
               when(response.code())
               {
                   200->{
                       SweetAlertDialog(requireContext(),SweetAlertDialog.SUCCESS_TYPE)
                           .setTitleText("🎉")
                           .setContentText("Registration successfull!!\n Don't forget to pay for the event ASAF")
                           .setConfirmClickListener {
                               val intent = Intent(requireContext(),HostActivity::class.java)
                               requireContext().startActivity(intent)
                               activity?.finish()

                           }
                           .show()
                   }
                   400->
                   {
                       SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                           .setTitleText("WARNING")
                           .setContentText("Team name already exist.")
                           .show()
                   }
                   500->{
                       SweetAlertDialog(requireContext(),SweetAlertDialog.ERROR_TYPE)
                           .setTitleText("WARNING")
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