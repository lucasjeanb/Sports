package com.lab.sports.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lab.sports.R
import com.lab.sports.data.model.TeamModel
import com.lab.sports.view.FavoriteFragment.Companion.TYPE_FAVORITE
import com.lab.sports.view.MainActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.team_item_row.view.*


class TeamsAdapter(
    private val context: Context,
    private val listener: OnTeamSelected,
    private val favoriteListener: OnTeamFavorite, private val fragmentType: String
) :
    RecyclerView.Adapter<TeamsAdapter.PostViewHolder>() {
    private var teamsList: ArrayList<TeamModel.Item> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.team_item_row, parent, false)
        return PostViewHolder(view)
    }

    fun updateAdapter(items: List<TeamModel.Item>) {
        teamsList.clear()
        teamsList.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return teamsList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = teamsList[position]

        item.teamName?.let { holder.teamName.text = it }
        item.teamBadge?.let {
            Picasso.with(context)
                .load(it)
                .into(holder.teamLogo)
        }

        holder.constraintTeam?.setOnClickListener {
            item.teamId?.let { it1 -> listener.onSelected(it1) }
        }

        if (fragmentType == TYPE_FAVORITE) {
            holder.favorite?.setImageDrawable(context.resources.getDrawable(R.drawable.ic_added_to_favorites))
        } else {
            holder.favorite?.setImageDrawable(context.resources.getDrawable(R.drawable.ic_add_to_favorites))
        }

        holder.favorite?.setOnClickListener {
            item?.let { it1 -> favoriteListener.onFavorite(it1, holder.favorite) }

        }

    }

    inner class PostViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val constraintTeam: ConstraintLayout = mView.constraint_team
        val teamName: TextView = mView.tv_team_name
        val teamLogo: CircleImageView = mView.img_team_logo
        val favorite: ImageView = mView.img_favorite

    }

    interface OnTeamSelected {
        fun onSelected(id: String)
    }

    interface OnTeamFavorite {
        fun onFavorite(team: TeamModel.Item, view: ImageView)
    }
}