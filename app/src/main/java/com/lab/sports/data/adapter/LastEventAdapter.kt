package com.lab.sports.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lab.sports.R
import com.lab.sports.data.model.EventModel
import kotlinx.android.synthetic.main.event_item_row.view.*


class LastEventAdapter:
    RecyclerView.Adapter<LastEventAdapter.PostViewHolder>() {
    private var eventsList: ArrayList<EventModel.Item> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.event_item_row, parent, false)
        return PostViewHolder(view)
    }

    fun updateAdapter(items: List<EventModel.Item>) {
        eventsList.clear()
        eventsList.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = eventsList[position]

        item.homeTeam?.let { holder.teamOneName.text = it }
        item.awayTeam?.let { holder.teamTwoName.text = it }
        holder.score.text = item.homeScore + "  :  " + item.awayScore
        item.time?.let { holder.time.text = it }
        item.date?.let { holder.date.text = it }
    }

    inner class PostViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val teamOneName: TextView = mView.tv_team1_name
        val teamTwoName: TextView = mView.tv_team2_name
        val score: TextView = mView.tv_score
        val time: TextView = mView.tv_time
        val date: TextView = mView.tv_date
    }

    interface OnTeamSelected {
        fun onSelected(id: String)
    }
}