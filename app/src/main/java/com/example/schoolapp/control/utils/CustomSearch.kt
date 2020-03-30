package com.example.schoolapp.control.utils

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.search_for_stuffs.*

class CustomSearchBar(activity: Activity) {
    private val editText = activity.search
    private val removeBtn = activity.btn_close

    init {
        removeBtn.setOnClickListener {
            editText.setText("")
        }
    }

    fun isNotBack():Boolean{
        editText.clearFocus()
        return editText.text.isNotEmpty()
    }

    fun onBackButton(){
        editText.setText("")
    }

    fun onChangeText(dataChange:(s:CharSequence)->Unit){
        editText.addTextChangedListener(DataChange(dataChange))
    }

    inner class DataChange(val dataChange:(s:CharSequence)->Unit={}) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.length != 0) {
                removeBtn.visibility = View.VISIBLE
            }else{
                removeBtn.visibility = View.INVISIBLE
            }
            dataChange(s!!)
        }

    }

}
