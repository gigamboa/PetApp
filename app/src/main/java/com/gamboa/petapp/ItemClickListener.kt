package com.gamboa.petapp

import android.view.View
import java.text.FieldPosition

interface ItemClickListener {

    fun onClick(view: View, position: Int)
}