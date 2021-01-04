package com.example.photomap.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photomap.R
import com.example.photomap.adapter.ClickableRecycler
import com.example.photomap.adapter.TimelineRecAdapter
import com.example.photomap.ui.DetailsActivity
import com.example.photomap.util.TestData
import kotlinx.android.synthetic.main.fragment_time_line.*
import java.util.*


class TimeLineFragment : Fragment(), ClickableRecycler {

    lateinit var timelineAdapter: TimelineRecAdapter

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
        timelineAdapter.setList(TestData.data)

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

    override fun onItemClick() {
        startActivity(Intent(this.context, DetailsActivity::class.java))
    }
}