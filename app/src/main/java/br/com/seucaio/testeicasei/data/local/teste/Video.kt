package br.com.seucaio.testeicasei.data.local.teste

import br.com.seucaio.testeicasei.data.local.teste.Channel

data class Video(
    val id: String,
    val title: String,
    val description: String,
    val channel: Channel,
    val countLike: Double,
    val countDislike: Double,
    val countView: Double,
    val pathVideo: String
    )