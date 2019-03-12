package br.com.seucaio.testeicasei.data.remote.model.search

import com.google.gson.annotations.SerializedName

data class ItemSearch(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: Id,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("snippet")
    val snippet: Snippet
)