package com.example.schoolapp.control.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

    class DBEvent(private val context: Context,val DataChangeEvent: (p0: DataSnapshot) -> Unit = {}) {
        companion object{
            private var event: EventObject =EventObject({})
        }


        init {
            event =
                EventObject(DataChangeEvent)
        }

        fun setEvent(dataChangeEvent:(p0:DataSnapshot)->Unit = DataChangeEvent): DBEvent {
            event =
                EventObject(dataChangeEvent)
            Fb.db.addValueEventListener(event)
            return this
        }

        fun setSingleEvent(dataChangeEvent:(p0:DataSnapshot)->Unit = DataChangeEvent): DBEvent {
            event =
                EventObject(dataChangeEvent)
            Fb.db.addListenerForSingleValueEvent(event)
            return this
        }


        fun removeEvent(): DBEvent {
            Fb.db.removeEventListener(event)
            return this
        }

        private class EventObject(val dataChangeEvent:(p0: DataSnapshot)->Unit) :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                dataChangeEvent(p0)
            }
        }

    }



