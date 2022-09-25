package com.example.mygrocerylist.Model

class Grocery {
    var name: String? = null
    var quantity: String? = null
    var dateItemAdded: String? = null
    var id = 0

    constructor() {}
    constructor(name: String?, quantity: String?, dateItemAdded: String?, id: Int) {
        this.name = name
        this.quantity = quantity
        this.dateItemAdded = dateItemAdded
        this.id = id
    }
}