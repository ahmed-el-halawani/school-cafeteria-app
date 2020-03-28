package com.example.schoolapp.ui.uiCards


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolapp.model.Product
import com.example.schoolapp.R
import com.example.schoolapp.control.utils.DBEvent
import com.example.schoolapp.control.utils.Fb
import com.example.schoolapp.control.adaptor.MealAdaptor
import com.example.schoolapp.model.Meals
import com.example.schoolapp.control.scanners.scanner
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.control.utils.Utils
import kotlinx.android.synthetic.main.fragment_meal_scanner.*
import kotlin.collections.ArrayList


class MealScannerActivity : AppCompatActivity() {
    private var isPermissions = false
    private var scanCount = 0
    lateinit var mealList:ArrayList<Pair<String, Product>>
    private lateinit var mealData:String
    private lateinit var dbEvent: DBEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_meal_scanner)
        mealData = ""
        scanCount = 0

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        dbEvent = DBEvent(this,{})

        requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
        btn_scanMeal.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
            if (isPermissions){
                val intent = Intent(this, scanner::class.java)
                intent.putExtra("state","qrCode")
                startActivityForResult(intent, 2)
            }
        }
    }

    private fun showMeal(mealData:String){
        dbEvent = DBEvent(this){p0->
            val mealPasswordPath = p0.child(Keys.toStudentMeal).child(mealData)
            mealList = ArrayList()
            if (mealPasswordPath.exists()){
                val studentId = mealPasswordPath.value as String
                val mealProduct = p0.child("student").child(studentId).child("meals")
                    .child(mealData).child("mealProducts")
                if (mealProduct.exists()){
                    val mealPath = p0.child("student").child(studentId).child("meals")
                        .child(mealData)

                    val meal = mealPath.getValue(Meals::class.java)!!
                    for (i in meal.mealProducts){
                        mealList.add(Pair(i.key,i.value))
                    }
                    lv_mealList.adapter = MealAdaptor(
                        this,
                        mealList
                    )
                }
            }else{
                mealList.clear()
                lv_mealList.adapter = MealAdaptor(
                    this,
                    mealList
                )
                Toast.makeText(this,"this qr not valid or already scanned",Toast.LENGTH_LONG).show()
                scanCount = 0
            }

        }.setEvent()
    }

    private fun removeMeal(mealId:String){
        val ks= Keys
        DBEvent(this){ p0->
            val mealPass = p0.child(ks.toStudentMeal).child(mealId)
            mealList.clear()
            if (mealPass.exists()){
                val studentId = mealPass.value as String
                val mealPath = p0.child(ks.student).child(studentId).child(ks.meals)
                    .child(mealId)

                if (mealPath.exists()){

                    val removedMeal = mealPath.getValue(Meals::class.java)
                    Fb.db.child(ks.scannedMealHistory).child(Utils.cd()).setValue(removedMeal)

                    Fb.db.child(ks.student).child(studentId).child(ks.meals)
                        .child(mealId).child(ks.mealState).setValue("Done")

                    Fb.db.child(ks.student).child(studentId).child(ks.currentMeal)
                        .setValue("")

                    Fb.db.child(ks.toStudentMeal).child(mealId).removeValue()


                    lv_mealList.adapter = MealAdaptor(
                        this@MealScannerActivity,
                        mealList
                    )
                    Toast.makeText(this@MealScannerActivity,"Done",Toast.LENGTH_LONG).show()
                }
            }
        }.setSingleEvent()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissions = true
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            mealData = data.getStringExtra("result")!!
            scanCount++
            showMeal(mealData)
            if(scanCount==2){
                dbEvent.removeEvent()
                removeMeal(mealData)
                scanCount=0
            }
        } else {
            Toast.makeText(this, "canceled", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dbEvent.removeEvent()
    }


}
