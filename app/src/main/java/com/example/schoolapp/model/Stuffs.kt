package com.example.schoolapp.model

class Stuffs{
    lateinit var email:String
    lateinit var password:String
    lateinit var userName:String
    lateinit var image:String
    lateinit var id:String
    lateinit var parentList:ArrayList<Parents>
    lateinit var school:String
    lateinit var rule:String
    var color:Int = 0

    constructor()

    constructor(
        email: String,
        password:String,
        userName: String,
        image:String,
        color: Int = 2131099692,
        id:String = "",
        school:String = "",
        parentList: ArrayList<Parents> = arrayListOf(),
        rule:String = "user"
    ) {
        this.password = password
        this.email = email
        this.userName = userName
        this.parentList = parentList
        this.id = id
        this.school = school
        this.rule = rule
        this.image = image
        this.color = color

    }


}