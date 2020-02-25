package com.lab.sports.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room

import com.lab.sports.R
import com.lab.sports.data.adapter.TeamsAdapter
import com.lab.sports.data.local.AppDataBase
import com.lab.sports.data.model.TeamModel
import com.lab.sports.utils.NetworkUtils
import kotlinx.android.synthetic.main.fragment_teams.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {

    private lateinit var dataBaseManager: AppDataBase
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: TeamsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLinearLayoutManager = LinearLayoutManager(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sw_home.setOnRefreshListener {
//            (activity as MainActivity)?.let {
//                getFavoriteFromDB()
//            }
            sw_home.isRefreshing = false
        }
        rc_home.layoutManager = mLinearLayoutManager
        rc_home.itemAnimator = DefaultItemAnimator()
        rc_home.setHasFixedSize(true)

        mAdapter =
            TeamsAdapter(activity!!.applicationContext, object : TeamsAdapter.OnTeamSelected {
                override fun onSelected(id: String) {
                    val fragment = LastEventsFragment()
                    var bundle = Bundle()
                    bundle.putString(TeamsFragment.TEAM_ID, id)
                    fragment.arguments = bundle

                    (activity as MainActivity).showNavigation()
                    (activity as MainActivity).setFragment(fragment)
                }

            }, object : TeamsAdapter.OnTeamFavorite {
                override fun onFavorite(team: TeamModel.Item, view: ImageView) {
                    if (view.tag != null && view.tag.equals("favorite")) {
                        view.tag = "unfavorite"
                        view.setImageDrawable(resources.getDrawable(R.drawable.ic_add_to_favorites))
                        removeFavorite(team)
                    } else {
                        view.setImageDrawable(resources.getDrawable(R.drawable.ic_added_to_favorites))
                        view.tag = "favorite"
                        addToFavorite(team)
                    }
                }
            }, TYPE_FAVORITE)

        rc_home.adapter = mAdapter
        subscribeObserver()
        getFavoriteFromDB()
    }

    fun removeFavorite(team: TeamModel.Item) {
        (activity as MainActivity)?.let { activity ->
            activity.getViewModel().removeFavoriteTeams(dataBaseManager, team)
        }
    }

    fun addToFavorite(team: TeamModel.Item) {
        (activity as MainActivity)?.let { activity ->
            activity.getViewModel().addFavoriteTeams(dataBaseManager, team)
        }
    }

    /**
     * set observer on the LiveData objects to be notified for any update
     * */
    private fun subscribeObserver() {

        (activity as MainActivity)?.let { activity ->
            activity.getViewModel().postsLiveData.observe(this, Observer {
                activity.dismissLoader()
                if (it.items != null) {
                    tv_no_data.visibility = View.GONE
                    rc_home.visibility = View.VISIBLE
                    mAdapter.updateAdapter(it.items)
                } else {
                    rc_home.visibility = View.GONE
                    tv_no_data.visibility = View.VISIBLE
                }
            })
        }
    }

    fun getFavoriteFromDB() {
        dataBaseManager =
            activity?.let {
                Room.databaseBuilder(it, AppDataBase::class.java, NetworkUtils.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }!!

        (activity as MainActivity)?.let { activity ->
            activity.getViewModel().getFavoriteTeams(dataBaseManager)
        }
    }

    companion object {
        const val TYPE_FAVORITE = "FAVORITE"
        const val TYPE_TEAMS = "TEAMS"
    }


}
