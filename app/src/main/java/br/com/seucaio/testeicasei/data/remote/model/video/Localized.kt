package br.com.seucaio.testeicasei.data.remote.model.video

import com.google.gson.annotations.SerializedName

data class Localized(
    @SerializedName("description")
    val description: String,
    @SerializedName("title")
    val title: String
)