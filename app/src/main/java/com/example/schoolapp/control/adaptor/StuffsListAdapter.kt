package com.example.schoolapp.control.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.schoolapp.R
import com.example.schoolapp.control.Ud
import com.example.schoolapp.control.utils.Fb
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.model.Stuffs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.addworker_dialog.view.*
import kotlinx.android.synthetic.main.stuff_cards.view.*

class StuffsListAdapter(val context: Context,private val list:ArrayList<Stuffs>,val adapterM:String = "showStuffs"):BaseAdapter() {
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
            view.iv_stuffImage.visibility = View.GONE
            view.tv_profile_first_letter.visibility = View.VISIBLE
            view.cv_profileColor.setBackgroundResource(stuff.color)
            view.tv_profile_first_letter.text = stuff.userName[0].toString()
        }

        if(adapterM == "search"){
            view.btn_more.visibility = View.GONE
            Toast.makeText(context,"here", Toast.LENGTH_LONG).show()
            view.setOnClickListener {
                val dView = LayoutInflater.from(context).inflate(R.layout.addworker_dialog,null)
                val dialog = AlertDialog.Builder(context)
                dialog.setView(dView)
                val alert = dialog.create()
                alert.show()

                when (Ud.userRole){
                    Keys.owner->{
                        dView.admin.visibility = View.VISIBLE
                        dView.cashier.visibility = View.GONE
                        dView.store.visibility = View.GONE
                    }
                    Keys.admin->{
                        dView.admin.visibility = View.GONE
                        dView.cashier.visibility = View.VISIBLE
                        dView.store.visibility = View.VISIBLE
                    }
                }

                dView.btn_addWorker.setOnClickListener {
                    val roleText = when(dView.rg_roleList.checkedRadioButtonId) {
                        R.id.admin -> Keys.admin
                        R.id.cashier -> Keys.cashier
                        R.id.store -> Keys.store
                        else -> ""
                    }
                    val userPath = Fb.db.child(Keys.SchoolWorker).child(stuff.id    )
                    userPath.child(Keys.school).setValue(Ud.userSchool)
                    userPath.child(Keys.rule).setValue(roleText)
                    alert.dismiss()
                }
                dView.btn_cancel2.setOnClickListener {
                    alert.dismiss()
                }
            }
        }else{

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