package br.com.seucaio.testeicasei.ui

import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import br.com.seucaio.testeicasei.data.datasource.VideosDataSourceFactory
import br.com.seucaio.testeicasei.data.remote.YouTubeApiService
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var itemSearchVideoList: Observable<PagedList<ItemSearch>>

    private val compositeDisposable = CompositeDisposable()

    private val sourceFactory: VideosDataSourceFactory

    init {
        sourceFactory = VideosDataSourceFactory(compositeDisposable, YouTubeApiService.create())

        val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setInitialLoadSizeHint(3)
            .setPrefetchDistance(2)
            .setEnablePlaceholders(false)
            .build()


        itemSearchVideoList = RxPagedListBuilder(sourceFactory, config)
            .setFetchScheduler(Schedulers.io())
            .buildObservable()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
