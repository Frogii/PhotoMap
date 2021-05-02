package com.example.photomap.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photomap.R
import com.example.photomap.adapter.ClickableRecyclerItem
import com.example.photomap.adapter.TimelineRecAdapter
import com.example.photomap.model.MapMark
import com.example.photomap.ui.DetailsActivity
import com.example.photomap.ui.MainActivity
import com.example.photomap.ui.MainViewModel
import com.example.photomap.util.AppConnectionUtils
import com.example.photomap.util.AppPermissionUtils
import com.example.photomap.util.Constants.MAP_MARK_ITEM
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_time_line.*
import kotlinx.android.synthetic.main.timeline_item.*
import java.util.*


class TimeLineFragment : Fragment(), ClickableRecyclerItem {

    lateinit var timelineAdapter: TimelineRecAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        mainViewModel = (activity as MainActivity).mainViewModel
        mainViewModel.dataList.observe(viewLifecycleOwner) {
            timelineAdapter.setList(it)
        }

        if (AppConnectionUtils.isNetworkEnable(activity as MainActivity)) {
            Log.d("myLog", "from NET")
            mainViewModel.getAllMarksFromFirebase()
        } else {
            Log.d("myLog", "from DB")
            if (AppPermissionUtils.checkReadStoragePermission(activity as MainActivity))
                mainViewModel.getMarksFromLocalDB()
        }

    }

    private fun setupRecycler() {
        timelineAdapter = TimelineRecAdapter(this)
        recyclerViewTimeline.apply {
            adapter = timelineAdapter
            layoutManager = LinearLayoutManager(this@TimeLineFragment.context)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_timeline_fragment, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        val actionSearchView = searchMenuItem.actionView as SearchView
        actionSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryString: String?): Boolean {
                queryString?.let {
                    mainViewModel.searchMapMarks(queryString)
                }
                return false
            }

            override fun onQueryTextChange(queryString: String?): Boolean {
                queryString?.let {
                    mainViewModel.searchMapMarks(queryString)
                }
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_categories -> findNavController().navigate(
                R.id.action_timeLineFragment_to_categoriesFragment
            )
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(item: MapMark, view: ImageView) {
        val options = activity?.let { ActivityOptionsCompat.makeSceneTransitionAnimation(it, view, "photo" ) }
        startActivity(Intent(this.context, DetailsActivity::class.java).also {
            it.putExtra(MAP_MARK_ITEM, item)
        }, options?.toBundle())
    }

    override fun onDeleteClick(mark: MapMark) {
        mainViewModel.deleteMapMark(mark)
    }
}