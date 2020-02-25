package com.lab.sports.data.mapper


import com.lab.sports.data.local.entities.TeamEntity
import com.lab.sports.data.model.TeamModel


class FromCashFavoriteMapper  : Mapper<TeamEntity, TeamModel.Item>() {
    override fun map(value: TeamEntity): TeamModel.Item =
        TeamModel.Item(value.teamID, value.teamName, value.teamLogo, "","")

    override fun reverseMap(value: TeamModel.Item): TeamEntity =
        TeamEntity(value.teamId!!, value.teamName!!, value.teamBadge!!)

}