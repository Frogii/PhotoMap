package com.example.photomap.adapter

import com.example.photomap.model.MapMark

interface ClickableRecyclerItem {

    fun onItemClick(item: MapMark)

    fun onDeleteClick(mark: MapMark)
}