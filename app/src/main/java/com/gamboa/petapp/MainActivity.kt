package com.gamboa.petapp

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter
import com.github.aakira.expandablelayout.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pet_row.*


class MainActivity : AppCompatActivity() {

    internal var pets: MutableList<Pet> = ArrayList()
    internal lateinit var adapter: FirebaseRecyclerAdapter<Pet, ItemViewHolder>

    internal var expandState = SparseBooleanArray()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_data.setHasFixedSize(true)
        recycler_data.layoutManager = LinearLayoutManager(this)



        retrieveData()

        setData()

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            addNewItemDialog()
        }

    }

    private fun setData() {
        val query = FirebaseDatabase.getInstance().reference.child("pet_item")
        val options = FirebaseRecyclerOptions.Builder<Pet>()
            .setQuery(query, Pet::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Pet, ItemViewHolder>(options) {

            override fun getItemViewType(position: Int): Int {
                return if (pets[position].isExpandable)
                    1
                else
                    0
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

                if (viewType == 0) {
                    val itemView = LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.pet_row, parent, false)
                    return ItemViewHolder(itemView, viewType == 1)
                } else {

                    val itemView = LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.pet_row_expanded, parent, false)
                    return ItemViewHolder(itemView, viewType == 1)
                }
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: Pet) {
                when (holder.itemViewType) {
                    0 -> {
                        holder.setIsRecyclable(false)
                        holder.txt_item_text.text = model.petName

                        holder.setItemClickListener(object : ItemClickListener {
                            override fun onClick(view: View, position: Int) {

                                Toast.makeText(
                                    this@MainActivity,
                                    "" + model.petName,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }

                    1 -> {

                        holder.setIsRecyclable(false)
                        holder.txt_item_text.text = model.petName

                        holder.setItemClickListener(object : ItemClickListener {
                            override fun onClick(view: View, position: Int) {

                                Toast.makeText(
                                    this@MainActivity,
                                    "" + model.petName,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                        holder.expandable_layout.setInRecyclerView(true)
                        holder.expandable_layout.isExpanded = expandState.get(position)
                        holder.expandable_layout.setListener(object :
                            ExpandableLayoutListenerAdapter() {
                            override fun onPreOpen() {
                                changeRotate(holder.button, 0f, 180f).start()
                                expandState.put(position, true)
                            }

                            override fun onPreClose() {
                                changeRotate(holder.button, 0f, 180f).start()
                                expandState.put(position, false)
                            }
                        })

                        holder.button.rotation = if (expandState.get(position)) 180f else 0f
                        holder.button.setOnClickListener {
                            holder.expandable_layout.toggle()
                        }

                        holder.txt_child_item_text.text = model.petType
                    }
                }
            }

        }

        expandState.clear()
        for (i in pets.indices)
            expandState.append(i, false)
        recycler_data.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_data.adapter = adapter
    }

    private fun changeRotate(button: RelativeLayout, from: Float, to: Float): ObjectAnimator {

        val animator = ObjectAnimator.ofFloat(button, "rotation", from, to)
        animator.duration = 300
        animator.interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        return animator

    }

    private fun retrieveData() {
        pets.clear()

        val db = FirebaseDatabase.getInstance()
            .reference
            .child("pet_item")

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                Log.d("ERROR", "" + p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (itemSnapshot in p0.children) {

                    val item = itemSnapshot.getValue(Pet::class.java)

                    pets.add(item!!)
                }
            }

        })


    }

    override fun onStart() {
        super.onStart()
        if (adapter != null)
            adapter.startListening()
    }

    override fun onStop() {
        if (adapter != null)
            adapter.stopListening()
        super.onStop()
    }

//    private fun addDataPetList(dataSnapshot: DataSnapshot){
//
//        val items = dataSnapshot.children.iterator()
//
//        if (items.hasNext()) {
//            val petsIndex = items.next()
//            val itemsIterator = petsIndex.children.iterator()
//
//            while (itemsIterator.hasNext()) {
//
//                val currentItem = itemsIterator.next()
//                val petItem = Pet.create()
//
//                val map = currentItem.getValue() as HashMap<String, Any>
//
//                petItem.petId = currentItem.key
//                petItem.petName = map.get("itemText") as String?
//
//                pets!!.add(petItem)
//
//            }
//        }
//
//        adapter.notifyDataSetChanged()
//
//    }

    private fun addNewItemDialog() {


        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)
        alert.setMessage("Add New Item")
        alert.setTitle("Enter To Do Item Text")
        alert.setView(itemEditText)
        alert.setPositiveButton("Submit") { dialog, positiveButton ->

            val db = FirebaseDatabase.getInstance().getReference("pet_item")

            val petItem = Pet.create()
            petItem.petName = itemEditText.text.toString()
            petItem.status = false
            //We first make a push so that a new item is made with a unique ID
            val newItem = db.push().key

            petItem.petId = newItem

            db.child(newItem!!).setValue(petItem).addOnCompleteListener{
                Toast.makeText(applicationContext, "salvou", Toast.LENGTH_SHORT).show()
            }
            //then, we used the reference to set the value on that ID
            dialog.dismiss()

        }
        alert.show()
    }
}


//    private fun addDataPetList(dataSnapshot: DataSnapshot){
//
//        val items = dataSnapshot.children.iterator()
//
//        if (items.hasNext()) {
//            val petsIndex = items.next()
//            val itemsIterator = petsIndex.children.iterator()
//
//            while (itemsIterator.hasNext()) {
//
//                val currentItem = itemsIterator.next()
//                val petItem = Pet.create()
//
//                val map = currentItem.getValue() as HashMap<String, Any>
//
//                petItem.petId = currentItem.key
//                petItem.petName = map.get("itemText") as String?
//
//                pets!!.add(petItem)
//
//            }
//        }
//
//        adapter.notifyDataSetChanged()
//
//    }





//    private fun addNewItemDialog() {
//
//
//        val alert = AlertDialog.Builder(this)
//        val itemEditText = EditText(this)
//        alert.setMessage("Add New Item")
//        alert.setTitle("Enter To Do Item Text")
//        alert.setView(itemEditText)
//        alert.setPositiveButton("Submit") { dialog, positiveButton ->
//            val petItem = Pet.create()
//            petItem.petName = itemEditText.text.toString()
//            petItem.status = false
//            //We first make a push so that a new item is made with a unique ID
//            val newItem = Database.child(Constants.FIREBASE_ITEM).push()
//            petItem.petId = newItem.key
//            //then, we used the reference to set the value on that ID
//            newItem.setValue(petItem)
//            dialog.dismiss()
//            Toast.makeText(this, "Item saved with ID " + petItem.petId, Toast.LENGTH_SHORT).show()   }
//        alert.show()
//    }


