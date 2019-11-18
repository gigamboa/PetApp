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
            txt_item_text = itemView.findViewById(R.id.txt_item_text) as TextView
            txt_child_item_text = itemView.findViewById(R.id.txt_child_item_text) as TextView

            button = itemView.findViewById(R.id.button) as RelativeLayout

            expandable_layout = itemView.findViewById(R.id.expandable_layout) as ExpandableLinearLayout
        }

        else {

            txt_item_text = itemView.findViewById(R.id.txt_item_text) as TextView
        }

        itemView.setOnClickListener{
            view -> iItemClickListener.onClick(view, adapterPosition)
        }
    }


}