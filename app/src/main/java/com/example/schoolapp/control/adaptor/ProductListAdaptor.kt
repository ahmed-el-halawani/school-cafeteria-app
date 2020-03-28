package com.example.schoolapp.control.adaptor

import android.content.Context
import android.icu.text.Transliterator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.schoolapp.model.Product
import com.example.schoolapp.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.child_tile.view.*
import kotlin.coroutines.coroutineContext

class ProductListAdaptor(val c:Context,val list:ArrayList<Product>):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val product = list[position]
        val view = LayoutInflater.from(c).inflate(R.layout.child_tile,null)

        view.tv_productName.text = product.productName
        view.tv_productPrice.text = product.productPrice.toString()
        view.tv_productType.text = product.productType
        FirebaseStorage.getInstance().reference.child(product.productImage).downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(view.iv_productImage)
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}