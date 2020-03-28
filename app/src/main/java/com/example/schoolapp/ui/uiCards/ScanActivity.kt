package com.example.schoolapp.ui.uiCards

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolapp.model.Product
import com.example.schoolapp.R
import com.example.schoolapp.control.utils.Fb
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.control.scanners.scanner
import com.example.schoolapp.control.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.add_remove.view.*
import kotlinx.android.synthetic.main.add_remove.view.et_addCartons
import kotlinx.android.synthetic.main.fragment_scan.*
import java.util.UUID
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("SetTextI18n","InflateParams")
class ScanActivity : AppCompatActivity() {
    private lateinit var uri: Uri
    private var isPermissions = false
    lateinit var image:String
    private lateinit var typeList:ArrayList<String>
    private lateinit var cartonCapacity:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_scan)
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onStart() {
        super.onStart()
        cartonCapacity= "0"
        typeList = arrayListOf("juice","chips","chocolate","other")
        et_barcode.isFocusable = false

        btn_scan.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
            if (isPermissions){

                val intent = Intent(this, scanner::class.java)
                intent.putExtra("state","barCode")
                startActivityForResult(intent, 2)
            }
        }

        tv_editImage.setOnClickListener {
            getImage()
        }

        val adaptor = ArrayAdapter(this,android.R.layout.simple_list_item_1,typeList)
        et_productType.setAdapter(adaptor)


        btn_add_quantity.setOnClickListener {
            addQuantityDialog()
        }

        btn_remove_quantity.setOnClickListener {
            removeQuantityDialog()
        }

        btn_done.setOnClickListener {
            scan_progress_bar.visibility = View.VISIBLE
            doneButton()
        }
    }

    private fun doneButton(){
        var barcode = et_barcode.text.toString()
        val name = tv_productName.text.toString()
        var price = et_productPrice.text.toString()
        val type = et_productType.text.toString()
        val cartons = tv_addCartons.text.toString().toInt()
        val single = tv_addSingle.text.toString().toInt()



        if(price.isEmpty()){
            price = "0.0"
        }

        if(name.isEmpty()){
            tv_productName.error = "cant be empty"
        }else{
            if (image.isEmpty()) {
                image = "productImage/product.jpg"
            }
            if (barcode.isEmpty()){
                barcode = UUID.randomUUID().toString()
            }
            scan_progress_bar.visibility = View.VISIBLE
            val ref = Fb.storage.child(image)
            ref.putFile(uri).addOnSuccessListener {

                Fb.db.child(Keys.product).child(barcode)
                    .setValue(
                        Product(
                            barcode,
                            name,
                            image,
                            price.toDouble(),
                            type,
                            cartons,
                            single,
                            cartonCapacity.toInt()
                        )
                    )
                Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()
                reset()
                scan_progress_bar.visibility = View.GONE
            }.addOnCanceledListener {
                if (Utils.isNotConnect(this)) {
                    Toast.makeText(this,"no internet connection",Toast.LENGTH_LONG).show()

                }
            }

        }
    }

    private fun getImage(){
        val getPhoto = Intent()
        getPhoto.type = "image/*"
        getPhoto.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(getPhoto,3)
    }

    private fun reset(){
        et_barcode.setText("")
        tv_productName.setText("")
        tv_addCartons.text = "0"
        tv_addSingle.text = "0"
        et_productType.setText("")
        et_productPrice.setText("")
        iv_productImageRegester.setBackgroundColor(Color.GRAY)
    }

    data class DC(val dView:View,val alertDialog:AlertDialog)
    private fun dialog():DC{
        val dView = layoutInflater.inflate(R.layout.add_remove,null)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setView(dView)
        val dialog = alertDialog.create()
        dialog.show()
        return DC(dView,dialog)
    }

    private fun addQuantityDialog(){
        val dialog  = dialog()
        val dView = dialog.dView
        dView.btn_addOrRemove.text = "add"
        dView.btn_addOrRemove.setOnClickListener {
            var cartons = dView.et_addCartons.text.toString()
            var single = dView.et_addSingle.text.toString()

            if (cartons.isEmpty()){
                cartons = "0"
            }

            if (single.isEmpty()){
                single = "0"
            }
            tv_addCartons.text = (cartons.toInt() + tv_addCartons.text.toString().toInt()).toString()
            tv_addSingle.text = (single.toInt() + tv_addSingle.text.toString().toInt()).toString()
            cartonCapacity = dView.et_cartonCapacity.text.toString()
            dialog.alertDialog.dismiss()
        }
    }

    private fun removeQuantityDialog() {
        val dialog = dialog()
        val dView = dialog.dView
        dView.btn_addOrRemove.text = "remove"
        dView.btn_addOrRemove.setOnClickListener {
            var cartons = dView.et_addCartons.text.toString()
            var single = dView.et_addSingle.text.toString()

            if (cartons.isEmpty()) {
                cartons = "0"
            }

            if (single.isEmpty()) {
                single = "0"
            }

            tv_addCartons.text =
                (cartons.toInt() - tv_addCartons.text.toString().toInt()).toString()
            tv_addSingle.text =
                (single.toInt() - tv_addSingle.text.toString().toInt()).toString()
            cartonCapacity = dView.et_cartonCapacity.text.toString()
            dialog.alertDialog.dismiss()

            dView.btn_cancel.setOnClickListener {
                dialog.alertDialog.dismiss()
            }
        }
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

        when{
            (requestCode == 3 && resultCode == RESULT_OK && data !=null)->{
                uri = data.data!!
                Picasso.get().load(uri).into(iv_productImageRegester)
                image = "productImage/"+ UUID.randomUUID().toString()
            }
            (requestCode == 2 && resultCode == RESULT_OK && data != null)->{
                val d = data.getStringExtra("result")!!
                et_barcode.setText(d)
            }
            else->{
                Toast.makeText(this, "canceled", Toast.LENGTH_LONG).show()

            }
        }

    }

}
