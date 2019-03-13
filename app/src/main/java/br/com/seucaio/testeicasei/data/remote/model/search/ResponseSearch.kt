package br.com.seucaio.testeicasei.data.remote.model.search

import com.google.gson.annotations.SerializedName

data class ResponseSearch(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val items: List<ItemSearch>,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("prevPageToken")
    val prevPageToken: String?,
    @SerializedName("nextPageToken")
    val nextPageToken: String,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo,
    @SerializedName("regionCode")
    val regionCode: String
)