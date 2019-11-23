package com.gamboa.petapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import java.lang.IllegalStateException

class PetDetails : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)

        val petName_display = findViewById(com.gamboa.petapp.R.id.petName_display) as TextView
        val petPhoto_display = findViewById(R.id.petPhoto_display) as ImageView

        val navBarTitle = intent.getStringExtra("petName")
        supportActionBar?.title = navBarTitle

       petName_display.text = intent.getStringExtra("petName")
    }

}