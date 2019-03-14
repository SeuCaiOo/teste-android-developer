package br.com.seucaio.testeicasei.data.remote.model.error

import com.google.gson.annotations.SerializedName

data class ResponseError(
    @SerializedName("code")
    val code: Int,
    @SerializedName("errors")
    val errors: List<Error>,
    @SerializedName("message")
    val message: String
)