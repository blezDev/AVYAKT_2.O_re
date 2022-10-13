package com.gietu.avyakt2o.presentation.Forms

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog


import com.gietu.avyakt2o.R
import com.gietu.avyakt2o.data.Entries
import com.gietu.avyakt2o.data.EntriesStatus
import com.gietu.avyakt2o.presentation.HostActivity
import com.gietu.avyakt2o.utils.Constants.EVENT_NAME
import com.gietu.avyakt2o.utils.Constants.EVENT_TYPE
import com.gietu.avyakt2o.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoloForm : AppCompatActivity() {

    private  lateinit var  memberName: EditText
    private  lateinit var  memberRoll: EditText
    private  lateinit var  memberEmail: EditText
    private  lateinit var  memberPhone: EditText
    private  lateinit var  btnVerifySolo: Button
    private lateinit var formViewModel :FormViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var instructionBrn_SOLO : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_form)
        memberName = findViewById(R.id.memberName)
        memberRoll = findViewById(R.id.memberRoll)
        memberPhone = findViewById(R.id.memberPhone)
        memberEmail = findViewById(R.id.memberEmail)
        btnVerifySolo = findViewById(R.id.btnVerifySolo)
        instructionBrn_SOLO =findViewById(R.id.instructionBrn_SOLO)
        tokenManager = TokenManager(applicationContext)
        formViewModel = ViewModelProvider(this)[FormViewModel::class.java]

        val EventType = intent.getStringExtra(EVENT_TYPE).toString()
        val EventName = intent.getStringExtra(EVENT_NAME).toString()
        getAlert()

        instructionBrn_SOLO.setOnClickListener {
            getAlert()
        }

        btnVerifySolo.setOnClickListener {

            if(memberEmail.text.isNotEmpty() && memberName.text.isNotEmpty() && memberRoll.text.isNotEmpty() && memberPhone.text.isNotEmpty())
            {
                Log.e("TAG","Enter the solo form + $EventType")
                val name = listOf(memberName.text.toString())
                val email = listOf(memberEmail.text.toString())
                val rollno = listOf(memberRoll.text.toString())
                val teamName = null
                val phone = listOf(memberPhone.text.toString())
                val type = "SOLO"
                val token = tokenManager.getToken()
                val entry = Entries(token!!,name, email, rollno, teamName, EventName, phone, type)
               Log.e("TAG",entry.toString() + entry.name.toString())
                register(entry,EventType)
            }
            else
            {
                SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Empty Fields")
                    .setContentText("All Fields must be filled")
                    .show()
            }

        }

    }

  private fun getAlert()
  {
      SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
          .setTitleText("INSTRUCTIONS")
          .setContentText(getString(R.string.instruction_SOLO_EVENT))
          .show()
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
          /*  "Robotics EVENT. (Robo soccer, Taskmaster, AquaRobo, Line follower). (SARS)" -> roboticsEventRoute(entry)*/
            "Idea Presentation Event" ->ideapresentationEventRoute(entry)
            "Workshop" -> workshopEventRoute(entry)
            "Guest talks" -> guestTalksEventRoute(entry)
            "Innovative Idea Poster Presentation" -> innovativeIdeaPosterPresentationEventRoute(entry)
            "Quiz" -> quizEventRoute(entry)
         /*   "Gaming" -> gamingEventRoute(entry)*/
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
            "Drama (Based on Short story 10 mins)" -> dramaEventRoute(entry)
           /* "tshirts" -> *//*tshirtsEventRoute(entry)*/
            "stalls" ->stallEventRoute(entry)

        }
    }
    private fun goToHome(){
        val intent = Intent(this@SoloForm,HostActivity::class.java)
        startActivity(intent)
    }
    private fun androidRoute(entry: Entries)
    {
        Log.e("TAG","Entered android Route")
        formViewModel.retService.postAndroidDevelopmentEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .setConfirmClickListener { goToHome() }
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun hackforgietEventRoute(entry: Entries)
    {
        tokenManager.getEmail()
        tokenManager.getToken()
        formViewModel.retService.hackforgietEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun codesoccerEventRoute(entry: Entries)
    {
        tokenManager.getEmail()
        tokenManager.getToken()
        formViewModel.retService.codesoccerEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun blindcodingEventRoute(entry: Entries)
    {

        formViewModel.retService.blindcodingEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun codedebuggingEventRoute(entry: Entries)
    {

        formViewModel.retService.codedebuggingEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun webpuzzleEventRoute(entry: Entries)
    {

        formViewModel.retService.webpuzzleEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun postermakingEventRoute(entry: Entries)
    {

        formViewModel.retService.postermakingEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
   /* private fun roboticsEventRoute(entry: GameEntries)
    {

        formViewModel.retService.roboticsEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }*/
    private fun ideapresentationEventRoute(entry: Entries)
    {
        formViewModel.retService.ideapresentationEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun workshopEventRoute(entry: Entries)
    {

        formViewModel.retService.workshopEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun guestTalksEventRoute(entry: Entries)
    {

        formViewModel.retService.guestTalksEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun innovativeIdeaPosterPresentationEventRoute(entry: Entries)
    {

        formViewModel.retService.innovativeIdeaPosterPresentationEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun quizEventRoute(entry: Entries)
    {

        formViewModel.retService.quizEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
   /* private fun gamingEventRoute(entry: Entries)
    {

        formViewModel.retService.gamingEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }*/
    private fun groupDiscussionEventRoute(entry: Entries)
    {

        formViewModel.retService.groupDiscussionEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun craftMakingEventRoute(entry: Entries)
    {

        formViewModel.retService.craftMakingEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun treasureHuntEventRoute(entry: Entries)
    {

        formViewModel.retService.treasureHuntEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun rangoliEventRoute(entry: Entries)
    {

        formViewModel.retService.rangoliEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun musicalChairEventRoute(entry: Entries)
    {

        formViewModel.retService.musicalChairEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun eurekaEventRoute(entry: Entries)
    {

        formViewModel.retService.eurekaEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun gkQuizEventRoute(entry: Entries)
    {

        formViewModel.retService.gkQuizEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun cseGotTallentEventRoute(entry: Entries)
    {

        formViewModel.retService.cseGotTallentEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun onTheSpotPaintingEventRoute(entry: Entries)
    {

        formViewModel.retService.onTheSpotPaintingEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun cartooningEventRoute(entry: Entries)
    {

        formViewModel.retService.cartooningEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun songEventRoute(entry: Entries)
    {

        formViewModel.retService.songEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun danceEventRoute(entry: Entries)
    {

        formViewModel.retService.danceEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun fashionShowEventRoute(entry: Entries)
    {

        formViewModel.retService.fashionShowEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun mimicryEventRoute(entry: Entries)
    {

        formViewModel.retService.mimicryEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }
  /*  private fun tshirtsEventRoute(entry: Entries)
    {

        formViewModel.retService.tshirtsEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
*/
    private fun dramaEventRoute(entry: Entries)
    {

        formViewModel.retService.dramaEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }

        })
    }



    private fun stallEventRoute(entry: Entries)
    {

        formViewModel.retService.stallEvent(entries = entry).enqueue(object :Callback<EntriesStatus>
        {
            override fun onResponse(call: Call<EntriesStatus>, response: Response<EntriesStatus>) {

                when(response.code())
                {
                    200-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }
                    400-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("!!😰!!")
                            .setContentText("Team name already exist!.")
                            .show()
                    }
                    500-> {
                        SweetAlertDialog(this@SoloForm,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("!!ERROR!!")
                            .setContentText(response.body()?.message.toString())
                            .show()
                    }

                }

            }
            override fun onFailure(call: Call<EntriesStatus>, t: Throwable) {
                Log.e("TAG",t.toString())
                Toast.makeText(this@SoloForm,"Something went wrong.Try again",Toast.LENGTH_SHORT).show()
            }
        })
    }






}