package com.example.photomap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photomap.R
import com.example.photomap.model.TestMapMark
import com.example.photomap.util.AppDateUtils
import kotlinx.android.synthetic.main.timeline_item.view.*
import kotlin.collections.ArrayList

class TimelineRecAdapter(private val clickableRecycler: ClickableRecycler) :
    RecyclerView.Adapter<TimelineRecAdapter.TimelineViewHolder>() {

    private var markList: List<TestMapMark> = ArrayList()

    fun setList(list: List<TestMapMark>) {
        this.markList = list
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
                .load(markList[position].photoUrl)
                .circleCrop()
                .into(imageViewMapPhoto)

            textViewDescription.text = markList[position].description
            textViewDate.text = AppDateUtils.formatDate(markList[position].date, AppDateUtils.detailsDatePattern)
            textViewCategory.text = markList[position].category

            this.setOnClickListener {
                clickableRecycler.onItemClick()
            }
        }
    }

    override fun getItemCount(): Int {
        return markList.size
    }
}