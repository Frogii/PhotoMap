package com.example.photomap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.photomap.R
import com.example.photomap.util.Constants.DEFAULT_CATEGORY
import com.example.photomap.util.Constants.FRIENDS_CATEGORY
import com.example.photomap.util.Constants.NATURE_CATEGORY
import com.example.photomap.util.MarkCategory
import kotlinx.android.synthetic.main.category_item.view.*

class CategoriesRecAdapter : RecyclerView.Adapter<CategoriesRecAdapter.CategoriesViewHolder>() {

    private val categoriesList = listOf(
        MarkCategory(FRIENDS_CATEGORY, R.color.pink_check_box),
        MarkCategory(NATURE_CATEGORY, R.color.green_check_box),
        MarkCategory(DEFAULT_CATEGORY, R.color.blue_check_box)
    )

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        holder.itemView.apply {
            val color = ContextCompat.getColorStateList(context, categoriesList[position].color)
            checkBoxCategories.text = categoriesList[position].name
            CompoundButtonCompat.setButtonTintList(checkBoxCategories, color)
            checkBoxCategories.setTextColor(color)
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }
}