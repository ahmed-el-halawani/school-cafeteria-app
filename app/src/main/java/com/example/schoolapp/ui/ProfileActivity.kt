package com.example.schoolapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.schoolapp.R
import com.example.schoolapp.control.utils.DBEvent
import com.example.schoolapp.control.utils.Fb
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.control.utils.Utils
import com.example.schoolapp.model.Stuffs
import com.example.schoolapp.ui.signInUp.SignInActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_activity.*

class ProfileActivity : AppCompatActivity() {
    lateinit var userId:String
    lateinit var dbEvent: DBEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        userId = Fb.auth.uid!!

        btn_logOt.setOnClickListener {
            logOut()
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        dbEvent = DBEvent(this){p0->
            val userPath = p0.child(Keys.SchoolWorker).child(userId)
            if (userPath.exists()){
                val userData = userPath.getValue(Stuffs::class.java)!!
                tv_email.text = userData.email
                tv_profileUserName.text = userData.userName
                tv_role.text = userData.rule
                if((userPath.child("image").value as String).isNotEmpty()){
                    Fb.storage.child(userData.image).downloadUrl.addOnCompleteListener {
                        if (it.isSuccessful){
                            val pic = Picasso.get().load(it.result)
                            pic.into(iv_profile)
                            pic.into(iv_profileBg)
                        }
                    }
                }
            }
        }.setEvent()


    }

    private fun logOut(){
        Fb.auth.signOut()
        dbEvent.removeEvent()
        startActivity(Intent(this,SignInActivity::class.java))
        finishAffinity()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!Utils.isNotConnect(this)) {
            dbEvent.removeEvent()
        }
    }



}
