package br.com.seucaio.testeicasei.data.remote.model.video

import com.google.gson.annotations.SerializedName

data class Statistics(
    @SerializedName("commentCount")
    val commentCount: String,
    @SerializedName("dislikeCount")
    val dislikeCount: String,
    @SerializedName("favoriteCount")
    val favoriteCount: String,
    @SerializedName("likeCount")
    val likeCount: String,
    @SerializedName("viewCount")
    val viewCount: String
)