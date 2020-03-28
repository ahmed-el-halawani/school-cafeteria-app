package com.example.schoolapp.ui.uiCards


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolapp.model.Product

import com.example.schoolapp.R
import com.example.schoolapp.control.utils.DBEvent
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.control.adaptor.ProductListAdaptor
import com.example.schoolapp.control.utils.Utils
import kotlinx.android.synthetic.main.fragment_store.*

class StoreActivity : AppCompatActivity() {
    lateinit var productList:ArrayList<Product>
    lateinit var dbEvent: DBEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_store)

        btn_back.setOnClickListener {
            onBackPressed()
        }



    }


    override fun onStart() {
        super.onStart()
        if (Utils.isNotConnect(this)) {
            Toast.makeText(this,"no internet connection", Toast.LENGTH_LONG).show()
        }
        dbEvent = DBEvent(this){p0->
                productList = ArrayList()
                val productsDataSnapshot = p0.child(Keys.product).children
                for(i in productsDataSnapshot){
                    val product = i.getValue(Product::class.java)!!
                    productList.add(0,product)
                }

                lv_product.adapter = ProductListAdaptor(
                    this,
                    productList
                )
            }.setEvent()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (!Utils.isNotConnect(this)) {
            dbEvent.removeEvent()
        }
    }






}
