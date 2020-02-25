package com.lab.sports.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.repos.data.remote.ServiceGenerator
import com.lab.sports.data.local.AppDataBase
import com.lab.sports.data.mapper.FromCashFavoriteMapper
import com.lab.sports.data.model.EventModel
import com.lab.sports.data.model.TeamModel
import com.lab.sports.data.remote.ServiceAPIs
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel : ViewModel() {

    val postsLiveData = MutableLiveData<TeamModel>()
        get() = field

    val eventsLiveData = MutableLiveData<EventModel>()
        get() = field

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)
    private val serviceApi = ServiceGenerator().createService(ServiceAPIs::class.java)

    fun searchForTeamByName(teamName: String) {
        scope.async {
            val teams = serviceApi
                .searchForTeamByName(teamName).await()
            postsLiveData.postValue(teams)
        }
    }

    fun getEventByTeamId(teamID: String) {
        scope.async {
            val events = serviceApi
                .getTeamEvents(teamID).await()
            eventsLiveData.postValue(events)
        }
    }

    fun getFavoriteTeams(dataBaseManager: AppDataBase) {
        scope.async {
            val teams = dataBaseManager.getFavoriteDao().getAllFavorites()
            val teamModel = TeamModel(FromCashFavoriteMapper().map(teams))
            postsLiveData.postValue(teamModel)
        }
    }

    fun addFavoriteTeams(dataBaseManager: AppDataBase, team: TeamModel.Item) {
        scope.async {
            val teamEntity = FromCashFavoriteMapper().reverseMap(team)
            dataBaseManager.getFavoriteDao().addFavoriteTeam(teamEntity)
        }
    }

    fun removeFavoriteTeams(dataBaseManager: AppDataBase, team: TeamModel.Item) {
        scope.async {
            dataBaseManager.getFavoriteDao().unFavorite(team.teamId!!)
            getFavoriteTeams(dataBaseManager)
        }
    }


}