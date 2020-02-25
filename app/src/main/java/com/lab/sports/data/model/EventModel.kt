package com.lab.sports.data.model

import com.google.gson.annotations.SerializedName


class EventModel(
    @SerializedName("results") var items: List<Item>
) {

    open class Item(
        @SerializedName("idEvent")
        var eventId: String? = null,

        @SerializedName("strHomeTeam")
        var homeTeam: String? = null,

        @SerializedName("strAwayTeam")
        var awayTeam: String? = null,

        @SerializedName("intHomeScore")
        var homeScore: String? = null,

        @SerializedName("intAwayScore")
        var awayScore: String? = null,

        @SerializedName("strDate")
        var date: String? = null,

        @SerializedName("strTime")
        var time: String? = null
    )
}
