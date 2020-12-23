package com.example.photomap.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.photomap.R
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewDetailsPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_detailsFragment_to_photoFragment)
        }

        Glide
            .with(this)
            .load(
                "https://sun9-17.userapi.com/impg/VtM3gvmnGVF082-nxJOKLDR24BE9aDoGkz7pUQ/ixnI_gW6-1Q.jpg?" +
                        "size=1080x1350&quality=96&proxy=1&sign=c4f4c0b5c60b7b3df255495a8cb058ee&type=album"
            )
            .into(imageViewDetailsPhoto)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done_details -> findNavController().navigateUp()
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }


}