package com.example.schoolapp.model

class Parents{

    lateinit var id:String
    lateinit var email:String
    lateinit var password:String
    lateinit var name:String
    var cash:Double = 0.0
    lateinit var image:String
    lateinit var rule:String
    lateinit var children:Map<String,String>


    constructor()
    constructor(
        id:String,
        email: String,
        password: String,
        name: String,
        cash: Double,
        image: String,
        rule:String,
        children: Map<String,String> = mapOf()
        ) {
        this.id = id
        this.email = email
        this.password = password
        this.name = name
        this.cash = cash
        this.image = image
        this.rule = rule
        this.children = children


    }
}