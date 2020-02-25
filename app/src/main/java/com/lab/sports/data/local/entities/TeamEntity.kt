package com.lab.sports.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_table")
class TeamEntity(
    @ColumnInfo(name = "team_id")
    @PrimaryKey
    var teamID: String,
    @ColumnInfo(name = "team_name")
    var teamName: String,
    @ColumnInfo(name = "team_logo")
    var teamLogo: String
)