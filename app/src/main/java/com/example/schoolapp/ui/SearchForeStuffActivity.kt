package com.example.schoolapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.schoolapp.R
import com.example.schoolapp.control.Ud
import com.example.schoolapp.control.adaptor.StuffsListAdapter
import com.example.schoolapp.control.utils.CustomSearchBar
import com.example.schoolapp.control.utils.DBEvent
import com.example.schoolapp.control.utils.Fb
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.model.Stuffs
import kotlinx.android.synthetic.main.addworker_dialog.*
import kotlinx.android.synthetic.main.addworker_dialog.view.*
import kotlinx.android.synthetic.main.addworker_dialog.view.rg_roleList
import kotlinx.android.synthetic.main.custom_search_bar.*
import kotlinx.android.synthetic.main.search_for_stuffs.*
import kotlinx.android.synthetic.main.search_for_stuffs.btn_back

class SearchForeStuffActivity : AppCompatActivity() {
    private lateinit var dbEvent: DBEvent
    private lateinit var workerList:ArrayList<Stuffs>
    private lateinit var searchBar:CustomSearchBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_for_stuffs)
        workerList =  ArrayList()
        searchBar = CustomSearchBar(this)

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        dbEvent = DBEvent(this) { p0 ->
            workerList.clear()
            for (i in p0.child(Keys.SchoolWorker).children) {
                val worker = i.getValue(Stuffs::class.java)!!

                if (worker.school.isEmpty() && worker.rule != Keys.admin && worker.rule != Keys.owner && i.key != Fb.auth.uid) {
                    workerList.add(worker)
                }
            }
            lv_stuffsList.adapter = StuffsListAdapter(this, workerList, "search")

        }.setEvent()

        searchBar.onChangeText {
            search(it.toString())
        }
    }

    private fun search(searchKey: String = ""){
        dbEvent = DBEvent(this){p0->
            workerList.clear()
            for (i in p0.child(Keys.SchoolWorker).children){
                val worker = i.getValue(Stuffs::class.java)!!
                if(worker.school.isEmpty() && worker.rule != Keys.admin && worker.rule != Keys.owner && i.key != Fb.auth.uid){
                    if (worker.email == searchKey || worker.userName.contains(searchKey) || searchKey.isEmpty()){
                        workerList.add(worker)
                    }
                }
            }
            lv_stuffsList.adapter = StuffsListAdapter(this,workerList,"search")
        }.setEvent()
    }

    override fun onBackPressed() {
        if (searchBar.isNotBack()){
            searchBar.onBackButton()
        }else{
            dbEvent.removeEvent()
            super.onBackPressed()
        }
    }
}
