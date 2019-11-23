package com.gamboa.petapp

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.aakira.expandablelayout.ExpandableLinearLayout
import org.w3c.dom.Text


class ItemViewHolder(itemView: View, isExpandable:Boolean) : RecyclerView.ViewHolder(itemView) {

    var txt_petName: TextView
    var txt_petStatus: TextView
    var row_petImage: ImageView

    lateinit var iItemClickListener: ItemClickListener

    fun setItemClickListener(iItemClickListener: ItemClickListener) {

        this.iItemClickListener = iItemClickListener

    }

    init {

        txt_petName = itemView.findViewById(R.id.txt_petName) as TextView
        txt_petStatus = itemView.findViewById(R.id.txt_petStatus) as TextView
        row_petImage = itemView.findViewById(R.id.row_petImage) as ImageView


        itemView.setOnClickListener{
            view -> iItemClickListener.onClick(view, adapterPosition)
        }
    }


}