package com.example.photomap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photomap.R
import com.example.photomap.model.MapMark
import com.example.photomap.util.AppDateUtils
import kotlinx.android.synthetic.main.timeline_item.view.*

class TimelineRecAdapter(private val clickableRecyclerItem: ClickableRecyclerItem) :
    RecyclerView.Adapter<TimelineRecAdapter.TimelineViewHolder>() {

    private var markList: List<MapMark> = ArrayList()

    fun setList(list: List<MapMark>) {
        this.markList = list
        notifyDataSetChanged()
    }

    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        return TimelineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.timeline_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {

        holder.itemView.apply {
            Glide
                .with(this)
                .load(markList[position].url)
                .circleCrop()
                .into(imageViewMapPhoto)

            textViewDescription.text = markList[position].description
            textViewDate.text = AppDateUtils.changeLongToShortPattern(markList[position].date)
            textViewCategory.text = markList[position].category

            this.setOnClickListener {
                clickableRecyclerItem.onItemClick(markList[position], holder.itemView.imageViewMapPhoto)
            }

            imageViewDelete.setOnClickListener {
                clickableRecyclerItem.onDeleteClick(markList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return markList.size
    }
}