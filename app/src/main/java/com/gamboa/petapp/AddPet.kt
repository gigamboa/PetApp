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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_pet.*
import java.util.*

class AddPet : AppCompatActivity() {

    private var filePath: Uri? = null


    internal var storage: FirebaseStorage? = null
    internal var storageReference: StorageReference? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        var petName_input = EditText(this)
        var image = ImageView(this)

        petName_input = findViewById(R.id.petName_input)

        var petName = intent.getStringExtra("petName")
        var petAge = intent.getStringExtra("petAge")
        var petType = intent.getStringExtra("petType")
        var petStatus = intent.getStringExtra("petStatus")
//        var petId = intent.getStringExtra("petId")

        var intent = this.intent
        if (intent != null) {
            var caller = intent.getStringExtra("CallerId")

            if (caller.equals("editPet")) {
                petName_input.setText(petName)
                petAge_input.setText(petAge)
                petStatus_input.setText(petStatus)
                petType_input.setText(petType)

            }
        }


        val imageButton = findViewById<View>(R.id.petImage_input)
        imageButton.setOnClickListener { view ->

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }

        val saveBtn = findViewById<View>(R.id.savePet_btn)
        saveBtn.setOnClickListener { view ->

            uploadImage()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

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

                ref.downloadUrl.addOnSuccessListener {

                    saveToDatabase(it.toString())
                }
            }

            .addOnFailureListener {

            }
    }

    private fun saveToDatabase(petImageUrl: String) {

        val db = FirebaseDatabase.getInstance().getReference("pet_item")

        val petItem = Pet.create()
        petItem.petName = petName_input.text.toString()
        petItem.status = petStatus_input.text.toString()
        petItem.petAge = petAge_input.text.toString()
        petItem.petType = petType_input.text.toString()
        petItem.petImage = petImageUrl

        val newItem = db.push().key!!

        petItem.petId = newItem

        if (intent != null) {
            var caller = intent.getStringExtra("CallerId")

            if (caller.equals("addPet")) {

                db.child(newItem).setValue(petItem).addOnCompleteListener {
                    Toast.makeText(applicationContext, "Animal adicionado", Toast.LENGTH_SHORT)
                        .show()
                }

            }
            if (caller.equals("editPet")) {

                val petItemCurrent = intent.getStringExtra("petId")

                db.child(petItemCurrent).setValue(petItem)
            }
        }

    }


}


