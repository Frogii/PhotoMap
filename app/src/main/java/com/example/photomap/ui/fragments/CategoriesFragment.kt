package com.example.photomap.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.photomap.R
import com.example.photomap.adapter.CategoriesRecAdapter
import com.example.photomap.adapter.CategoryClickListener
import com.example.photomap.ui.MainActivity
import com.example.photomap.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoriesFragment : Fragment(), CategoryClickListener {

    lateinit var categoriesRecAdapter: CategoriesRecAdapter
    private lateinit var mainViewModel: MainViewModel
    private var categoryList = mutableListOf<String>()
    private var checkBoxStateMap = mutableMapOf<String, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()

        mainViewModel = (activity as MainActivity).mainViewModel
        mainViewModel.categoryLiveDataList.observe(viewLifecycleOwner, {
            categoryList = it
        })
        mainViewModel.checkBoxLiveDataStateMap.observe(viewLifecycleOwner, {
            checkBoxStateMap = it
            categoriesRecAdapter.setCheckBoxStateMap(checkBoxStateMap)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_categories_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.action_done_categories -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecycler() {
        categoriesRecAdapter = CategoriesRecAdapter(this)
        recyclerViewCategories.adapter = categoriesRecAdapter
    }

    override fun onCategoryClick(category: String) {
        //remove category from liveData
        if (categoryList.contains(category)) {
            categoryList.remove(category)
            checkBoxStateMap[category] = false
            mainViewModel.checkBoxLiveDataStateMap.postValue(checkBoxStateMap)
            mainViewModel.categoryLiveDataList.postValue(categoryList)
        } else {
            //add category to liveData
            categoryList.add(category)
            checkBoxStateMap[category] = true
            mainViewModel.checkBoxLiveDataStateMap.postValue(checkBoxStateMap)
            mainViewModel.categoryLiveDataList.postValue(categoryList)
        }
    }
}
