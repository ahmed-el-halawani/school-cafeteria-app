package com.example.schoolapp.model

class Stuffs{
    lateinit var email:String
    lateinit var password:String
    lateinit var userName:String
    lateinit var image:String
    lateinit var parentList:ArrayList<Parents>
    lateinit var rule:String

    constructor()

    constructor(email: String,password:String, userName: String,image:String, parentList: ArrayList<Parents> = arrayListOf(),rule:String = "user") {
        this.password = password
        this.email = email
        this.userName = userName
        this.parentList = parentList
        this.rule = rule
        this.image = image
    }




}