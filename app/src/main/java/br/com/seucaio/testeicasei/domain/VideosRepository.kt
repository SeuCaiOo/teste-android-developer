package br.com.seucaio.testeicasei.domain

import android.arch.paging.DataSource
import com.google.api.services.youtube.model.Video
import io.reactivex.Single

interface VideosRepository {

    fun videoById(id: Long): Single<Video>

    fun searchVideo(): DataSource.Factory<Int, Video>
}