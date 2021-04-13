package com.example.photomap.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.photomap.R
import com.example.photomap.model.MapMark
import com.example.photomap.ui.DetailsActivity
import com.example.photomap.ui.DetailsViewModel
import com.example.photomap.ui.dialog.ChangeCategoryDialog
import com.example.photomap.util.Constants.CATEGORY_DIALOG_TAG
import com.example.photomap.util.Constants.EMPTY_ACTION_BAR_TITLE
import com.example.photomap.util.Constants.MAP_MARK_ITEM
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment(), ChangeCategoryDialog.ChangeCategoryClickListener {

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var mapMark: MapMark

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

        detailsViewModel = (activity as DetailsActivity).detailsViewModel

        detailsViewModel.getCategoryState().observe(viewLifecycleOwner){
            textViewDetailsCategory.text = it
        }

        detailsViewModel.getDescriptionState().observe(viewLifecycleOwner){
            editTextDetailsDescription.setText(it)
        }
        mapMark = this.arguments?.getSerializable(MAP_MARK_ITEM) as MapMark
        Log.d("myLog", mapMark.toString())

        imageViewDetailsPhoto.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                imageViewDetailsPhoto to PHOTO,
                textViewDetailsDate to DATE,
                editTextDetailsDescription to DESCRIPTION
            )
            findNavController().navigate(
                R.id.action_detailsFragment_to_photoFragment,
                Bundle().also {
                    it.putSerializable(MAP_MARK_ITEM, mapMark)
                }, null, extras
            )
        }

        Glide
            .with(this)
            .load(mapMark.url)
            .into(imageViewDetailsPhoto)

        textViewDetailsCategory.text = mapMark.category
        editTextDetailsDescription.setText(mapMark.description)
        textViewDetailsDate.text = mapMark.date
        textViewDetailsCategory.setOnClickListener {
            ChangeCategoryDialog.getInstance().show(childFragmentManager, CATEGORY_DIALOG_TAG)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.let { supportActionBar ->
            supportActionBar.setDisplayHomeAsUpEnabled(true)
            supportActionBar.title = EMPTY_ACTION_BAR_TITLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done_details -> {
                if (mapMark.description != editTextDetailsDescription.text.toString() ||
                    mapMark.category != textViewDetailsCategory.text
                ) {
                    mapMark.description = editTextDetailsDescription.text.toString()
                    mapMark.category = textViewDetailsCategory.text.toString()
                    detailsViewModel.updateMapMarkDetails(mapMark)
                    Toast.makeText(
                        this.context,
                        getString(R.string.map_mark_changed),
                        Toast.LENGTH_SHORT
                    ).show()
                } else Toast.makeText(
                    this.context,
                    getString(R.string.nothing_to_change),
                    Toast.LENGTH_SHORT
                ).show()
            }
            android.R.id.home -> {
                activity?.supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        detailsViewModel.saveDescriptionState(editTextDetailsDescription.text.toString())
        detailsViewModel.saveCategoryState(textViewDetailsCategory.text.toString())
    }

    override fun changeCategory(category: String) {
        textViewDetailsCategory.text = category
    }

    companion object {
        const val PHOTO = "photo"
        const val DATE = "date"
        const val DESCRIPTION = "description"
    }
}