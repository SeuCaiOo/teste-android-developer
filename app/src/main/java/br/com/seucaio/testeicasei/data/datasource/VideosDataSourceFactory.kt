package br.com.seucaio.testeicasei.data.datasource

import android.arch.paging.DataSource
import br.com.seucaio.testeicasei.data.remote.YouTubeApiService
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import io.reactivex.disposables.CompositeDisposable

class VideosDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                              private val youTubeApi: YouTubeApiService)
    : DataSource.Factory<String, ItemSearch>() {

    override fun create(): DataSource<String, ItemSearch> {
            return VideosDataSource(youTubeApi, compositeDisposable)
    }


}