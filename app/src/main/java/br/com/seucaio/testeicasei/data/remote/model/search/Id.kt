package br.com.seucaio.testeicasei.data.remote.model.search

import com.google.gson.annotations.SerializedName

data class Id(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("videoId")
    val videoId: String
)