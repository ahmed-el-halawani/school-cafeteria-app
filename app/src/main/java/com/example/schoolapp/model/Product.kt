package com.example.schoolapp.model


class Product {
    lateinit var productBarcode:String
    lateinit var productName:String
    lateinit var productImage:String
    var productPrice:Double = 0.0
    lateinit var productType:String
    var productCartonsQuantity:Int = 0
    var productSingleQuantity:Int = 0
    var productCartonsCapacity:Int = 0

    constructor()
    constructor(
        productBarcode:String,
        productName: String,
        productImage: String,
        productPrice: Double=0.0,
        productType: String="other",
        productCartonsQuantity: Int=0,
        productSingleQuantity: Int=0,
        productCartonsCapacity: Int=0
    ) {
        this.productBarcode = productBarcode
        this.productName = productName
        this.productImage = productImage
        this.productPrice = productPrice
        this.productType = productType
        this.productCartonsQuantity = productCartonsQuantity
        this.productSingleQuantity = productSingleQuantity
        this.productCartonsCapacity = productCartonsCapacity
    }

}

