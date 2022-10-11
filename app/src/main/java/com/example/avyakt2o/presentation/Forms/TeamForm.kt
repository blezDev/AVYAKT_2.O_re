package com.example.avyakt2o.presentation.Forms

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.avyakt2o.Adapter.TeamFormAdapter
import com.example.avyakt2o.R
import com.example.avyakt2o.data.Entries
import com.example.avyakt2o.data.EntriesStatus
import com.example.avyakt2o.databinding.ActivityTeamFormBinding
import com.example.avyakt2o.presentation.HostActivity
import com.example.avyakt2o.utils.Constants
import com.example.avyakt2o.utils.Constants.MAXSIZE
import com.example.avyakt2o.utils.Constants.MINSIZE
import com.example.avyakt2o.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamForm : AppCompatActivity() {
  private lateinit var  EventType :String
    private lateinit var EventName :String
    private lateinit var FormRecycle: RecyclerView
    private lateinit var FormReg : ArrayList<com.example.avyakt2o.data.TeamForm>
    private lateinit var regFormAdapter : TeamFormAdapter
    private lateinit var addCard : ImageView
    val list = listOf("Song","Dance","Mono Acting/Mimicry","Drama (Based on Short story 10 mins)")
    private lateinit var formViewModel: FormViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var instructionBrn : Button
    private lateinit var binding : ActivityTeamFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_team_form)

        val list = listOf("Song","Dance","Mono Acting/Mimicry","Drama (Based on Short story 10 mins)")

        val MIN_SIZE = intent.getIntExtra(MINSIZE,1) - 1
        val MAX_SIZE = intent.getIntExtra(MAXSIZE,5) - 1
         formViewModel = ViewModelProvider(this)[FormViewModel::class.java]
         EventType = intent.getStringExtra(Constants.EVENT_TYPE).toString()
         EventName = intent.getStringExtra(Constants.EVENT_NAME).toString()

        val nameList = ArrayList<String>()
        val emailList = ArrayList<String>()
        val rollnoList = ArrayList<String>()
        val phoneList = ArrayList<String>()

        var count = 0

        addCard = findViewById(R.id.AddForm)

        instructionBrn = findViewById(R.id.instructionBrn_TEAM)
        FormRecycle = findViewById(R.id.TeamRecycle)
        FormRecycle.layoutManager = LinearLayoutManager(this)
        tokenManager = TokenManager(applicationContext)
        val token = tokenManager.getToken()!!
        FormReg = ArrayList()

        if( EventType in list)
            getAlertSolo()
        else
            getAlertTeam()

        instructionBrn.setOnClickListener {
            if( EventType in list)
                getAlertSolo()
            else
                getAlertTeam()
        }
        regFormAdapter = TeamFormAdapter(FormReg,this@TeamForm)
        FormRecycle.adapter = regFormAdapter
        regFormAdapter.onItemClick =
        {
                val name = it.name
                val rollno = it.roll.trim()
                val email = it.mail
                val phone  = it.phone.trim()
                nameList.add(name)
                rollnoList.add(rollno)
                emailList.add(email)
                phoneList.add(phone)
            count+=1

        }


     binding.SubmitFormBtn.setOnClickListener {
            if(binding.Name1.text.isNullOrBlank() && binding.email1.text.isNullOrBlank() && binding.phone1.text.isNullOrBlank() && binding.roll1.text.isNullOrBlank() ) {
                SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("FILL OUT DETAILS")
                    .show()
            }else
            {
                nameList.add(binding.Name1.text.toString())
                rollnoList.add(binding.roll1.text.toString())
                emailList.add(binding.email1.text.toString())
                phoneList.add(binding.phone1.text.toString())
                val teamName = binding.teamName1.text.toString()
                val nameListSub = nameList.distinct()
                if(EventType !in list && (teamName.isEmpty() || teamName.isBlank()))
                {
                    SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("WARNING!!")
                        .setContentText("THIS IS A GROUP EVENT.\n Team name is necessary along with team members details.")
                        .show()

                }else if(EventType !in list && nameListSub.size < (MIN_SIZE + 1 + 1))
                {
                    SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("WARNING!!")
                        .setContentText("THIS IS A GROUP EVENT of ${MIN_SIZE + 1 + 1} minimum participation.")
                        .show()
                }

                else{
                    val entry = getEntry(token,nameList.distinct(),rollnoList.distinct(),emailList.distinct(),phoneList.distinct(),teamName)
                    Log.e("TAG",nameList.size.toString())
                    Log.e("TAG",entry.toString())
                }


            }

        }


        addCard.setOnClickListener {
            if(FormReg.size > MAX_SIZE)
            {
                SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("INSTRUCTION")
                    .setContentText("Max Participation Limit is ${MAX_SIZE + 1}")
                    .show()
            }
            else
            {
                if(binding.teamName1.text.isEmpty() || binding.teamName1.text.isBlank( )){
                    SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("WARNING")
                        .setContentText("YOU LEFT OUT TEAM NAME IF YOU ARE ADDING MORE MEMBERS")
                        .show()
                }
                else
              addNewItem(0,com.example.avyakt2o.data.TeamForm("Name","Email","Official Mail","Phone Number"))
            }
        }

    }

    private fun getEntry(
        token: String,
        nameList: List<String>,
        rollnoList: List<String>,
        emailList: List<String>,
        phoneList: List<String>,
        teamName: String
    ): Entries {
        if(nameList.size == 1 && (teamName.isBlank() || teamName.isEmpty())){
            return Entries(token = token,nameList.reversed(),emailList.reversed(),rollnoList.reversed(),null, eventName = EventName,phoneList.reversed(),"SOLO")
        }
        else {
            return Entries(token = token,nameList.reversed(),emailList.reversed(),rollnoList.reversed(),teamName, eventName = EventName,phoneList.reversed(),"GROUP")
        }
    }

  private  fun getAlertSolo()
    {
        SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("INSTRUCTIONS")
            .setContentText(getString(R.string.instruction_SOLO))
            .show()
    }
  private  fun getAlertTeam()
    {
        SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("INSTRUCTIONS")
            .setContentText(getString(R.string.instruction_TEAM))
            .show()
    }

    fun addNewItem(index : Int ?=0  , teamForm : com.example.avyakt2o.data.TeamForm ){
        FormReg.add(0,teamForm)
            regFormAdapter.notifyDataSetChanged()
    }

    private fun register(entry: Entries, eventType: String) {
        when(eventType) {

            "android" -> androidRoute(entry)
            "GDSC(Hack for Giet)" -> hackforgietEventRoute(entry)
            "Code Soccer (DSC Club)" -> codesoccerEventRoute(entry)
            "Blind Coding. (DSC Club)" -> blindcodingEventRoute(entry)
            "Code Debugging. (Cyber Security)" -> codedebuggingEventRoute(entry)
            "Web puzzle. (Web eye)" -> webpuzzleEventRoute(entry)
            "Poster Making Competition" ->postermakingEventRoute(entry)
            "Robotics EVENT. (Robo soccer, Taskmaster, AquaRobo, Line follower). (SARS)" -> roboticsEventRoute(entry)
            "Idea Presentation Event" ->ideapresentationEventRoute(entry)
            "Workshop" -> workshopEventRoute(entry)
            "Guest talks" -> guestTalksEventRoute(entry)
            "Innovative Idea Poster Presentation" -> innovativeIdeaPosterPresentationEventRoute(entry)
            "Quiz" -> quizEventRoute(entry)
            "Gaming" -> gamingEventRoute(entry)
            "Group Discussion" -> groupDiscussionEventRoute(entry)
            "Craft Making. (Based on all recyclable items)" -> craftMakingEventRoute(entry)
            "Treasure Hunt" ->treasureHuntEventRoute(entry)
            "Rangoli. (individual)" -> rangoliEventRoute(entry)
            "Musical Chair" -> musicalChairEventRoute(entry)
            "Eureka (PDCS CLUB)" -> eurekaEventRoute(entry)
            "G.K Quiz. (Organising team)" -> gkQuizEventRoute(entry)
            "CSE-GOT Tallent" -> cseGotTallentEventRoute(entry)
            "On the spot painting" -> onTheSpotPaintingEventRoute(entry)
            "CARTOONING" -> cartooningEventRoute(entry)
            "Song" -> songEventRoute(entry)
            "Dance" -> danceEventRoute(entry)
            "Fashion Show" -> fashionShowEventRoute(entry)
            "Mono Acting/Mimicry" -> mimicryEventRoute(entry)
            "Drama (Based on Short story 10 mins)" -> DramaEventRoute(entry)
            "tshirts" -> tshirtsEventRoute(entry)
            "stalls" -> stallEventRoute(entry)

        }
    }
    private fun reload()
    {
       this.recreate()

    }


    private fun goToHome(){
        val intent = Intent(this@TeamForm, HostActivity::class.java)
        startActivity(intent)
    }
    private fun androidRoute(entry: Entries)
    {
        Log.e("TAG","Entered android Route")
        formViewModel.retService.postAndroidDevelopmentEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .setConfirmClickListener { goToHome() }
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun hackforgietEventRoute(entry: Entries)
    {
        formViewModel.retService.hackforgietEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun codesoccerEventRoute(entry: Entries)
    {

        formViewModel.retService.codesoccerEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun blindcodingEventRoute(entry: Entries)
    {

        formViewModel.retService.blindcodingEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun codedebuggingEventRoute(entry: Entries)
    {

        formViewModel.retService.codedebuggingEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun webpuzzleEventRoute(entry: Entries)
    {

        formViewModel.retService.webpuzzleEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun postermakingEventRoute(entry: Entries)
    {

        formViewModel.retService.postermakingEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun roboticsEventRoute(entry: Entries)
    {

        formViewModel.retService.roboticsEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun ideapresentationEventRoute(entry: Entries)
    {
        formViewModel.retService.ideapresentationEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun workshopEventRoute(entry: Entries)
    {

        formViewModel.retService.workshopEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun guestTalksEventRoute(entry: Entries)
    {

        formViewModel.retService.guestTalksEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun innovativeIdeaPosterPresentationEventRoute(entry: Entries)
    {

        formViewModel.retService.innovativeIdeaPosterPresentationEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun quizEventRoute(entry: Entries)
    {

        formViewModel.retService.quizEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun gamingEventRoute(entry: Entries)
    {

        formViewModel.retService.gamingEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun groupDiscussionEventRoute(entry: Entries)
    {

        formViewModel.retService.groupDiscussionEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun craftMakingEventRoute(entry: Entries)
    {

        formViewModel.retService.craftMakingEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun treasureHuntEventRoute(entry: Entries)
    {

        formViewModel.retService.treasureHuntEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun rangoliEventRoute(entry: Entries)
    {

        formViewModel.retService.rangoliEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun musicalChairEventRoute(entry: Entries)
    {

        formViewModel.retService.musicalChairEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun eurekaEventRoute(entry: Entries)
    {

        formViewModel.retService.eurekaEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun gkQuizEventRoute(entry: Entries)
    {

        formViewModel.retService.gkQuizEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun cseGotTallentEventRoute(entry: Entries)
    {

        formViewModel.retService.cseGotTallentEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun onTheSpotPaintingEventRoute(entry: Entries)
    {

        formViewModel.retService.onTheSpotPaintingEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun cartooningEventRoute(entry: Entries)
    {

        formViewModel.retService.cartooningEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun songEventRoute(entry: Entries)
    {

        formViewModel.retService.songEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun danceEventRoute(entry: Entries)
    {

        formViewModel.retService.danceEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun fashionShowEventRoute(entry: Entries)
    {

        formViewModel.retService.fashionShowEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun mimicryEventRoute(entry: Entries)
    {

        formViewModel.retService.mimicryEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun tshirtsEventRoute(entry: Entries)
    {

        formViewModel.retService.tshirtsEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun DramaEventRoute(entry: Entries)
    {

        formViewModel.retService.dramaEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }



    private fun stallEventRoute(entry: Entries)
    {

        formViewModel.retService.stallEvent(entries = entry).enqueue(object :
            Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@TeamForm, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@TeamForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }




    }
