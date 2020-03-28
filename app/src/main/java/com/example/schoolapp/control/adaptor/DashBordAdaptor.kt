package com.example.schoolapp.control.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.schoolapp.R
import com.example.schoolapp.model.DashbordCardDetails
import kotlinx.android.synthetic.main.dashbord_cards.view.*

class DashBordAdaptor(val context:Context,val list:ArrayList<DashbordCardDetails>):BaseAdapter(){
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.dashbord_cards,null)
        view.iv_cardImage.setImageResource(list[position].image)
        view.tv_cardTitle.text = list[position].title
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