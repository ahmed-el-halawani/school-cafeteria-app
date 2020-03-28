package com.example.schoolapp.model
class Meals{

    lateinit var mealId:String
    var mealDate:String= "11/12/1997"
    lateinit var mealDay:String
    lateinit var mealHour:String
    var mealPrice: Double = 0.0
    lateinit var mealState: String
    lateinit var mealProducts:Map<String,Product>

    constructor()
    constructor(
        mealId: String,
        mealDay:String,
        mealHour:String,
        mealPrice: Double=0.0,
        mealDate:String = "",
        mealState: String = "in progress",
        mealProducts: Map<String, Product> = mapOf()
    ) {
        this.mealDate = mealDate
        this.mealId = mealId
        this.mealDay = mealDay
        this.mealHour = mealHour
        this.mealPrice = mealPrice
        this.mealState = mealState
        this.mealProducts = mealProducts
    }


}