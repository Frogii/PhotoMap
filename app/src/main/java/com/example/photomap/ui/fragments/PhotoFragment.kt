package com.example.photomap.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.photomap.R
import com.example.photomap.model.MapMark
import com.example.photomap.util.Constants.EMPTY_ACTION_BAR_TITLE
import com.example.photomap.util.Constants.ITEM_FROM_RECYCLER
import kotlinx.android.synthetic.main.fragment_photo.*


class PhotoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideTextView()
        val mapMark = this.arguments?.getSerializable(ITEM_FROM_RECYCLER) as MapMark
        textViewFullPhotoDescription.text = mapMark.description
        textViewFullPhotoDate.text = mapMark.date
        Glide
            .with(this)
            .load(mapMark.url)
            .into(imageViewFullPhoto)
    }

    private fun hideTextView() {
        imageViewFullPhoto.setOnClickListener {
            if (textViewFullPhotoDescription.visibility == View.VISIBLE) {
                textViewFullPhotoDescription.visibility = View.INVISIBLE
                textViewFullPhotoDate.visibility = View.INVISIBLE
            } else {
                textViewFullPhotoDescription.visibility = View.VISIBLE
                textViewFullPhotoDate.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) findNavController().navigateUp()
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = EMPTY_ACTION_BAR_TITLE
    }
}