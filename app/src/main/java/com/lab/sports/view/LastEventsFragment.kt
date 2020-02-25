package com.lab.sports.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab.sports.R
import com.lab.sports.data.adapter.LastEventAdapter
import kotlinx.android.synthetic.main.fragment_last_events.*
import kotlinx.android.synthetic.main.fragment_teams.*

/**
 * A simple [Fragment] subclass.
 */
class LastEventsFragment : Fragment() {

    private lateinit var teamID: String
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: LastEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLinearLayoutManager = LinearLayoutManager(context)
        mAdapter = LastEventAdapter()
        teamID = arguments?.get(TeamsFragment.TEAM_ID) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_last_events, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rc_event.layoutManager = mLinearLayoutManager
        rc_event.itemAnimator = DefaultItemAnimator()
        rc_event.setHasFixedSize(true)
        rc_event.adapter = mAdapter

        (activity as MainActivity)?.let { activity ->
            activity.getViewModel().eventsLiveData.observe(this, Observer {
                activity.dismissLoader()
                if (it.items != null) {
                    no_data.visibility = View.GONE
                    rc_event.visibility = View.VISIBLE
                    mAdapter.updateAdapter(it.items)
                } else {
                    rc_event.visibility = View.GONE
                    no_data.visibility = View.VISIBLE
                }
            })
        }

        callLastEvents()
    }

    fun callLastEvents() {
        (activity as MainActivity)?.let {
            it.showLoader()
            it.getViewModel().getEventByTeamId(teamID)
        }
    }
}
