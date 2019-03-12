package br.com.seucaio.testeicasei.data.remote.model.video

import com.google.gson.annotations.SerializedName

data class ItemVideo(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("snippet")
    val snippet: Snippet,
    @SerializedName("statistics")
    val statistics: Statistics
)