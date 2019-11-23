package com.gamboa.petapp

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.aakira.expandablelayout.ExpandableLinearLayout


class ItemViewHolder(itemView: View, isExpandable:Boolean) : RecyclerView.ViewHolder(itemView) {

    lateinit var txt_item_text: TextView
    lateinit var txt_child_item_text: TextView
    lateinit var button:RelativeLayout
    lateinit var expandable_layout: ExpandableLinearLayout

    lateinit var iItemClickListener: ItemClickListener

    fun setItemClickListener(iItemClickListener: ItemClickListener) {

        this.iItemClickListener = iItemClickListener

    }

    init {
        if(isExpandable){
            txt_item_text = itemView.findViewById(R.id.txt_petName) as TextView

        }

        else {

            txt_item_text = itemView.findViewById(R.id.txt_petName) as TextView
        }

        itemView.setOnClickListener{
            view -> iItemClickListener.onClick(view, adapterPosition)
        }
    }


}