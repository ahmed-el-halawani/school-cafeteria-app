package com.example.schoolapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.example.schoolapp.control.adaptor.DashBordAdaptor
import com.example.schoolapp.control.utils.DBEvent
import com.example.schoolapp.control.utils.Fb
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.control.utils.Utils
import com.example.schoolapp.model.DashbordCardDetails
import com.example.schoolapp.ui.ProfileActivity
import com.example.schoolapp.ui.uiCards.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dashbord.*

@SuppressLint("DefaultLocale")
class MainActivity : AppCompatActivity() {
    private lateinit var  dbEvent: DBEvent
    private var endApp = 0
    private var currentPage = 0
    private lateinit var dashboardCards:ArrayList<DashbordCardDetails>
    private lateinit var roleBordCards:ArrayList<DashbordCardDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashbord)
        currentPage = 0
    }

    override fun onStart() {
        super.onStart()
        dashboardCards = arrayListOf(
            DashbordCardDetails(R.drawable.profile, Keys.profile),
            DashbordCardDetails(R.drawable.stuffs, Keys.staffs),
            DashbordCardDetails(R.drawable.store, Keys.store),
            DashbordCardDetails(R.drawable.meal_scanner, Keys.meal_scanner),
            DashbordCardDetails(R.drawable.add_products, Keys.add_products),
            DashbordCardDetails(R.drawable.history, Keys.history)
        )

        iv_workerProfile.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }

        dbEvent = DBEvent(this){ p0->
            if (Utils.isNotConnect(this)) {
                Toast.makeText(this,"no internet connection",Toast.LENGTH_LONG).show()
            }else {

                val rulePath = p0.child(Keys.SchoolWorker).child(Fb.auth.uid!!).child(
                    Keys.rule
                )
                val imagePath = p0.child(Keys.SchoolWorker).child(
                    Fb.auth.uid!!
                ).child(Keys.image)

                if (rulePath.exists()) {
                    roleSelect(rulePath.value as String)
                }

                if (imagePath.exists()) {
                    val image = imagePath.value as String
                    if (image.isNotEmpty()) {
                        Fb.storage.child(image).downloadUrl.addOnSuccessListener {
                            Picasso.get().load(it).into(iv_workerProfile)
                        }
                    }
                }
            }

        }.setEvent()
    }

    private fun roleSelect(role:String){
        roleBordCards = ArrayList()

        when (role.toUpperCase()) {
            Keys.owner->{
                roleBordCards = dashboardCards
            }

            Keys.admin -> {
                roleBordCards = arrayListOf(
                    dashboardCards[0],
                    dashboardCards[1],
                    dashboardCards[5],
                    dashboardCards[2]
                    )

            }
//
            Keys.cashier -> {
                roleBordCards = arrayListOf(
                    dashboardCards[0],
                    dashboardCards[3],
                    dashboardCards[5]

                )
            }
//
            Keys.store -> {
                roleBordCards = arrayListOf(
                    dashboardCards[0],
                    dashboardCards[4]
                )
            }
//
            else -> {
                roleBordCards = arrayListOf(
                    dashboardCards[0]
                )
            }
        }
        cardList(roleBordCards)
    }

    private fun cardList(RoleBordCards:ArrayList<DashbordCardDetails>){
        waitingView.visibility = View.GONE
        gv_dashBordCardsView.adapter = DashBordAdaptor(this,RoleBordCards)
        gv_dashBordCardsView.setOnItemClickListener { _, _, position, _ ->

            when(roleBordCards[position].title){
                Keys.meal_scanner->{
                    startActivity(Intent(this,MealScannerActivity::class.java))
                }
                Keys.add_products->{
                    startActivity(Intent(this,ScanActivity::class.java))
                }
                Keys.profile->{
                    startActivity(Intent(this,ProfileActivity::class.java))
                }
                Keys.store->{
                    startActivity(Intent(this,StoreActivity::class.java))
                }
                Keys.staffs->{
                    startActivity(Intent(this,StuffActivity::class.java))
                }
                Keys.history->{
                    startActivity(Intent(this,MealScannedHistoryActivity::class.java))
                }
            }
        }
    }

    override fun onBackPressed() {
        endApp += 1
        if(endApp == 1){
            Toast.makeText(this,"please click two time to end", Toast.LENGTH_SHORT).show()
            val mHandler = Handler()
            val mRunnable = Runnable{
                endApp = 0
            }
            mHandler.postDelayed(mRunnable,5000)
        }else{
            dbEvent.removeEvent()
            Fb.auth.signOut()
            finishAffinity()
        }

    }

    override fun onStop() {
        super.onStop()
        if (!Utils.isNotConnect(this)) {
            dbEvent.removeEvent()
        }
    }

}
