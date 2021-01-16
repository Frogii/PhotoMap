package com.example.photomap.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.photomap.R
import com.example.photomap.model.MapMark
import com.example.photomap.util.Constants.ITEM_FROM_RECYCLER
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

        val mapMark = this.arguments?.getSerializable(ITEM_FROM_RECYCLER) as MapMark
        Log.d("myLog", mapMark.toString())

        imageViewDetailsPhoto.setOnClickListener {
            findNavController().navigate(
                R.id.action_detailsFragment_to_photoFragment,
                Bundle().also {
                    it.putSerializable(ITEM_FROM_RECYCLER, mapMark)
                })
        }

        Glide
            .with(this)
            .load(mapMark.url)
            .into(imageViewDetailsPhoto)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.let { supportActionBar ->
            supportActionBar.setDisplayHomeAsUpEnabled(true)
            supportActionBar.title = ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done_details -> findNavController().navigateUp()
            android.R.id.home -> activity?.finish()
        }
        return super.onOptionsItemSelected(item)
    }
}