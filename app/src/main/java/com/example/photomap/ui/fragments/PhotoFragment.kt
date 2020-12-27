package com.example.photomap.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.photomap.R
import kotlinx.android.synthetic.main.fragment_photo.*


class PhotoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = ""
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
        Glide
            .with(this)
            .load(
                "https://sun9-46.userapi.com/impg/PeODZgO8Cl6f3z25_x_X-v7a4Xzak7p1DJ-qkg/oKzBDoVYtfU.jpg?" +
                        "size=2400x1600&quality=96&proxy=1&sign=f73a6c72416abc6fa6d5185b55bd537c&type=album"
            )
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
}