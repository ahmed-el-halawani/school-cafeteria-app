package com.example.schoolapp.control.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.schoolapp.R
import com.example.schoolapp.model.Stuffs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.stuff_cards.view.*

class StuffsListAdapter(val context: Context,private val list:ArrayList<Stuffs>):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.stuff_cards,null)
        val stuff = list[position]

        view.tv_name.text = stuff.userName
        view.tv_rule.text = stuff.rule
        if (stuff.image.isNotEmpty()){
            view.tv_profile_first_letter.visibility = View.GONE
            view.iv_stuffImage.visibility = View.VISIBLE
            Picasso.get().load(stuff.image).into(view.iv_stuffImage)
        }else{
            val colorList = arrayListOf(
                R.color.c1,
                R.color.c2,
                R.color.c3,
                R.color.c4,
                R.color.c5,
                R.color.c6
            )

            view.iv_stuffImage.visibility = View.GONE
            view.tv_profile_first_letter.visibility = View.VISIBLE
            view.cv_profileColor.setBackgroundResource(colorList.random())

            view.tv_profile_first_letter.text = stuff.userName[0].toString()
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