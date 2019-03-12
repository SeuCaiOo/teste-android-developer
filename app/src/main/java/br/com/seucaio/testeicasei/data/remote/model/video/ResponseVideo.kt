package br.com.seucaio.testeicasei.data.remote.model.video

import com.google.gson.annotations.SerializedName

data class ResponseVideo(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val items: List<ItemVideo>,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo
)