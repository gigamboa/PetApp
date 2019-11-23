package com.gamboa.petapp

class Pet {

    companion object Factory {
        fun create(): Pet = Pet()
    }

    var petId: String? = null
    var petName: String? = null
    var petImage: String? = null
    var petType: String? = null
    var petAge: String? = null
    var status: String? = null

}