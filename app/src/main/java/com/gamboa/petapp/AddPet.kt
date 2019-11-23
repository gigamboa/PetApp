package com.gamboa.petapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_pet.*
import java.util.*

class AddPet : AppCompatActivity() {

    private var filePath: Uri? = null


    internal var storage: FirebaseStorage?=null
    internal var storageReference: StorageReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        var petName_input = EditText(this)
        var image = ImageView(this)

        petName_input = findViewById(R.id.petName_input)


        val imageButton = findViewById<View>(R.id.petImage_input)
        imageButton.setOnClickListener {view ->

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }

        val saveBtn = findViewById<View>(R.id.savePet_btn)
        saveBtn.setOnClickListener{ view ->
            //addNewItemDialog()

            val db = FirebaseDatabase.getInstance().getReference("pet_item")

            val petItem = Pet.create()
            petItem.petName = petName_input.text.toString()
            petItem.status = false
            //We first make a push so that a new item is made with a unique ID
            val newItem = db.push().key!!

            petItem.petId = newItem

            db.child(newItem).setValue(petItem).addOnCompleteListener{
                Toast.makeText(applicationContext, "salvou", Toast.LENGTH_SHORT).show()

            }

            val intent =  Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(this.resources, bitmap)
            petImage_input.setBackground(bitmapDrawable)
        }
    }

    private fun uploadImage() {

        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register", "Upload image: ${it.metadata?.path}")
            }
    }



}

