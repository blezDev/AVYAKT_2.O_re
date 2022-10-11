package com.example.avyakt2o.presentation.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.avyakt2o.Adapter.ImageAdapter
import com.example.avyakt2o.Adapter.RegistrationReAdapter
import com.example.avyakt2o.R
import com.example.avyakt2o.data.Auth
import com.example.avyakt2o.data.RegisterRecycleView
import com.example.avyakt2o.data.ShowEvent
import com.example.avyakt2o.databinding.FragmentHomeBinding
import com.example.avyakt2o.presentation.Forms.SoloForm
import com.example.avyakt2o.presentation.Forms.TeamForm
import com.example.avyakt2o.utils.Constants.EVENT_NAME
import com.example.avyakt2o.utils.Constants.EVENT_TYPE
import com.example.avyakt2o.utils.Constants.MAXSIZE
import com.example.avyakt2o.utils.Constants.MINSIZE
import com.example.avyakt2o.utils.Constants.TEAM_SIZE
import com.example.avyakt2o.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var regList: ArrayList<RegisterRecycleView>
    private lateinit var regAdapter: RegistrationReAdapter
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ImageAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setUpTransformer()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })
        binding.recyclerViewProgressBar.visibility = View.VISIBLE

        if(!isOnline(requireContext()))
        {
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("WARNING")
                .setContentText("!! PLEASE CONNECT TO INTERNET !!")
                .show()
        }
        recyclerView = view.findViewById(R.id.regList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        /* regList = ArrayList()

        for(i in 1..10)
        {
            regList.add(RegisterRecycleView(R.drawable.freefire,"Nov","03","Valorant","4","Lab1"))
        }*/
        homeFragmentViewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        tokenManager = TokenManager(requireContext())
        val email = tokenManager.getEmail()
        val token = tokenManager.getToken()
        Log.e("FRAGMENT", email.toString() + token.toString())
        homeFragmentViewModel.retService.showEvents(Auth(email = email.toString(), token = token.toString())).enqueue(
                object : Callback<ShowEvent> {
                    override fun onResponse(call: Call<ShowEvent>, response: Response<ShowEvent>) {
                        binding.recyclerViewProgressBar.visibility = View.INVISIBLE
                        Log.e("TAG",response.body()?.eventsData.toString())
                        regAdapter = RegistrationReAdapter(response.body()?.eventsData!!, requireContext())
                        recyclerView.adapter = regAdapter
                        regAdapter.onItemClick = {
                            Log.e("TAG",it.EventType)
                            routeTo(it.EventType,it.EventName)
                        }
                    }

                    override fun onFailure(call: Call<ShowEvent>, t: Throwable) {
                        binding.recyclerViewProgressBar.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_LONG).show()
                    }

                }
        )


    }

    private fun routeTo(EventType : String,EventName : String)
    {
        when(EventType)
        {
            "android" -> formType(1,EventType,EventName,2,2)
            "GDSC(Hack for Giet)" -> formType(1,EventType,EventName,1,5)
            "Code Soccer (DSC Club)" -> formType(1,EventType,EventName,2,5)
            "Blind Coding. (DSC Club)" -> formType(0,EventType,EventName,1,5)
            "Code Debugging. (Cyber Security)" -> formType(1,EventType,EventName,1,5)
            "Web puzzle. (Web eye)" -> formType(1,EventType,EventName,1,5)
            "Poster Making Competition" -> formType(0,EventType,EventName,0,5)
            "Robotics EVENT. (Robo soccer, Taskmaster, AquaRobo, Line follower). (SARS)" -> formType(1,EventType,EventName,3,4)
            "Workshop" -> formType(1,EventType,EventName,1,3)
            "Idea Presentation Event" -> formType(0,EventType,EventName,1,5)
            "Guest talks" -> formType(0,EventType,EventName,1,5)
            "Innovative Idea Poster Presentation" -> formType(0,EventType,EventName,1,5)
            "Quiz" -> formType(0,EventType,EventName,1,5)
            "Gaming" -> formType(1,EventType,EventName,1,5)
            "Group Discussion" -> formType(1,EventType,EventName,1,5)
            "Craft Making. (Based on all recyclable items)" -> formType(0,EventType,EventName,1,5)
            "Treasure Hunt" -> formType(1,EventType,EventName,1,5)
            "Rangoli. (individual)" -> formType(0,EventType,EventName,1,5)
            "Musical Chair" -> formType(0,EventType,EventName,1,5)
            "Eureka (PDCS CLUB)" -> formType(0,EventType,EventName,1,5)
            "G.K Quiz. (Organising team)" -> formType(1,EventType,EventName,4,5)
            "CSE-GOT Tallent" -> formType(0,EventType,EventName,1,5)
            "On the spot painting" -> formType(0,EventType,EventName,1,5)
            "CARTOONING" -> formType(0,EventType,EventName,1,5)
            "Song" -> formType(1,EventType,EventName,1,5)
            "Dance" -> formType(1,EventType,EventName,1,6)
            "Fashion Show" -> formType(0,EventType,EventName,1,5)
            "Mono Acting/Mimicry" -> formType(1,EventType,EventName,1,5)
            "Drama (Based on Short story 10 mins)" -> formType(1,EventType,EventName,1,5)
            "tshirts"->formType(0,EventType,EventName,1,5)
            "stalls"->formType(1,EventType,EventName,1,5)


        }

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

    private fun formType(formType : Int, EventType: String,EventName: String, minSize : Int,maxSize :Int)
    {
        if(formType == 1)
        {
            val intent = Intent(requireContext(),TeamForm::class.java)
            intent.putExtra(EVENT_TYPE,EventType)
            intent.putExtra(EVENT_NAME,EventName)
            intent.putExtra(MINSIZE,minSize)
            intent.putExtra(MAXSIZE,maxSize)
            requireContext().startActivity(intent)
        }
        else{
            val intent = Intent(requireContext(),SoloForm::class.java)
            intent.putExtra(EVENT_TYPE,EventType)
            intent.putExtra(EVENT_NAME,EventName)
            requireContext().startActivity(intent)
        }

    }


    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable , 2000)
    }

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun init() {
        viewPager2 = requireView().findViewById(R.id.viewPagerImageSlider)!!
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        imageList.add(R.drawable.csgo)
        imageList.add(R.drawable.freefire)
        imageList.add(R.drawable.pubg)
        imageList.add(R.drawable.valo)


        adapter = ImageAdapter(imageList, viewPager2)

        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}