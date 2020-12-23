package com.example.photomap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photomap.R
import com.example.photomap.model.MapMark
import kotlinx.android.synthetic.main.timeline_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimelineRecAdapter(val clickableRecycler: ClickableRecycler) :
    RecyclerView.Adapter<TimelineRecAdapter.TimelineViewHolder>() {

    private var markList: List<MapMark> = ArrayList()

    fun setList(list: List<MapMark>) {
        this.markList = list
    }

    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        return TimelineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.timeline_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val mapPhotoItem = markList[position]
        val formatter = SimpleDateFormat("yyyy.mm.dd", Locale.getDefault())
        val dayTime = formatter.format(mapPhotoItem.date)

        holder.itemView.apply {
            Glide
                .with(this)
                .load(mapPhotoItem.photoUrl)
                .circleCrop()
                .into(imageViewMapPhoto)

            textViewDescription.text = mapPhotoItem.description
            textViewDate.text = dayTime
            textViewCategory.text = mapPhotoItem.category

            this.setOnClickListener {
                clickableRecycler.onItemClick()
            }
        }
    }

    override fun getItemCount(): Int {
        return markList.size
    }
}