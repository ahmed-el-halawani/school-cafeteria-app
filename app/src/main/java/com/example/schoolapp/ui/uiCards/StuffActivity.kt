package com.example.schoolapp.ui.uiCards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.schoolapp.R
import com.example.schoolapp.control.utils.DBEvent
import com.example.schoolapp.control.utils.Fb
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.control.adaptor.StuffsListAdapter
import com.example.schoolapp.control.utils.Utils
import com.example.schoolapp.model.Stuffs
import kotlinx.android.synthetic.main.stuffe_activity.*

class StuffActivity : AppCompatActivity() {
    lateinit var dbEvent: DBEvent
    lateinit var stuffList:ArrayList<Stuffs>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stuffe_activity)
        stuffList = ArrayList()

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onStart() {
        super.onStart()
        dbEvent = DBEvent(this){
            val stuffPath = it.child(Keys.SchoolWorker)
            if (stuffPath.exists()){
                stuffList.clear()
                for (i in stuffPath.children){
                    if ((i.child(Keys.rule).value as String).toUpperCase() != Keys.owner && i.key != Fb.auth.uid){
                        val stuffObject = i.getValue(Stuffs::class.java)!!
                        stuffList.add(stuffObject)
                    }
                }
                lv_stuffsList.adapter = StuffsListAdapter(this,stuffList)
            }
        }.setEvent()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (!Utils.isNotConnect(this)) {
            dbEvent.removeEvent()
        }

    }
}
