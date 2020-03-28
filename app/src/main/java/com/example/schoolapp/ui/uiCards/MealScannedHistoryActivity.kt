package com.example.schoolapp.ui.uiCards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.controler.productsAndMeals.MealHistoryAdapter
import com.example.schoolapp.R
import com.example.schoolapp.control.utils.DBEvent
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.model.Meals
import kotlinx.android.synthetic.main.meal_scanned_history_activity.*

class MealScannedHistoryActivity : AppCompatActivity() {
    lateinit var dbEvent: DBEvent
    lateinit var historyList:ArrayList<Meals>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_scanned_history_activity)
        historyList = ArrayList()

        btn_back.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onStart() {
        super.onStart()
        dbEvent = DBEvent(this){ p0 ->
            val history = p0.child(Keys.scannedMealHistory)
            if(history.exists()){
                historyList.clear()
                for (i in history.children){
                    val meal = i.getValue(Meals::class.java)!!
                    historyList.add(meal)
                }
                lv_mealHistoryListView.adapter = MealHistoryAdapter(this,historyList)
            }
        }.setEvent()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        dbEvent.removeEvent()
    }
}
