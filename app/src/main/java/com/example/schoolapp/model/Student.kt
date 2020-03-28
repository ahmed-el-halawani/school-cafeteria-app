package com.example.schoolapp.model

class Student{

    lateinit var id:String
    lateinit var name:String
    lateinit var img:String
    lateinit var email:String
    lateinit var password:String
    lateinit var parent:String
    lateinit var rule:String
    lateinit var meals:Map<String, Meals>
    var cash:Double = 0.0


    constructor()
    constructor(
        id:String = "ahmed",
        name: String,
        img: String,
        email: String,
        password: String,
        parent: String,
        rule: String,
        meals: Map<String, Meals> = mapOf(),
        cash:Double = 0.0

        ) {
        this.id = id
        this.name = name
        this.img = img
        this.email = email
        this.password = password
        this.parent = parent
        this.rule = rule
        this.meals = meals
        this.cash = cash

    }


}


