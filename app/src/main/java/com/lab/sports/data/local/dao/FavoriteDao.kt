package com.lab.sports.data.local.dao

import androidx.room.*
import com.lab.sports.data.local.entities.TeamEntity

@Dao
interface FavoriteDao {

    @Query("select COUNT(*) FROM  team_table")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteTeam(postCash: TeamEntity)

    @Query("delete from team_table where team_id  =:teamId")
    suspend fun unFavorite(teamId: String)

    @Query("Select * from team_table")
    suspend fun getAllFavorites(): List<TeamEntity>

    @Query("select team_id from team_table")
    suspend fun getAllIDs(): List<String>

    @Query("delete from team_table")
    suspend fun deleteAllPosts()
}