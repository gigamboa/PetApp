package com.gamboa.petapp

class Pet {

    companion object Factory {
        fun create(): Pet = Pet()
    }

    var petId: String? = null
    var petName: String? = null
    var petType: String? = null
    var status: Boolean? = false
    var isExpandable:Boolean = false
}