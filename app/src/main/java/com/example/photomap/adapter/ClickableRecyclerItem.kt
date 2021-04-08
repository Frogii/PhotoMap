package com.example.photomap.adapter

import android.widget.ImageView
import com.example.photomap.model.MapMark

interface ClickableRecyclerItem {

    fun onItemClick(item: MapMark, view: ImageView)

    fun onDeleteClick(mark: MapMark)
}