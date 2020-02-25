package com.lab.sports.data.remote

import com.lab.sports.data.model.EventModel
import com.lab.sports.data.model.TeamModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface ServiceAPIs {

    @GET("v1/json/1/searchteams.php?")
    fun searchForTeamByName(@Query("t") team: String): Deferred<TeamModel>

    @GET("v1/json/1/eventslast.php?")
    fun getTeamEvents(@Query("id") teamID: String): Deferred<EventModel>
}
