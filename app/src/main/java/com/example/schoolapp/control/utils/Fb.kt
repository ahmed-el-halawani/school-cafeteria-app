package com.example.schoolapp.control.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

object Fb{
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseDatabase.getInstance().reference
    val storage = FirebaseStorage.getInstance().reference







}