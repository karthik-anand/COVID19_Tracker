package com.example.covid19tracker.data.model
import com.google.gson.annotations.SerializedName


data class Statewise(
    @SerializedName("active")
    val active: String,
    @SerializedName("confirmed")
    val confirmed: String,
    @SerializedName("deaths")
    val deaths: String,
    @SerializedName("")
    val deltaconfirmed: String,
    val deltadeaths: String,
    val deltarecovered: String,
    val lastupdatedtime: String,
    val migratedother: String,
    val recovered: String,
    val state: String,
    val statecode: String,
    val statenotes: String
)