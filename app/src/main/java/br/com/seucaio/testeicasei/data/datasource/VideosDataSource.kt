package br.com.seucaio.testeicasei.data.datasource

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import android.widget.Toast
import br.com.seucaio.testeicasei.Constants
import br.com.seucaio.testeicasei.data.remote.YouTubeApiService
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import br.com.seucaio.testeicasei.data.remote.model.search.ResponseSearch
import br.com.seucaio.testeicasei.ui.list.VideoListActivity
import com.google.api.services.youtube.model.Video
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class VideosDataSource(
    private val youTubeApi: YouTubeApiService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<String, ItemSearch>() {

    val TAG = VideosDataSource::class.java.simpleName

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, ItemSearch>
    ) {
        createObservable(null, callback, null)
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, ItemSearch>) {
        val page = params.key
//        val nextPage =  getNextPage(page)
        createObservable(page, null, callback)
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, ItemSearch>) {
        val page = params.key
//        val prevPage =  getPrevPage(page)
        createObservable(page, null, callback)
    }

    private fun createObservable(
        pageToken: String?,
        initialCallback: LoadInitialCallback<String, ItemSearch>?,
        callback: LoadCallback<String, ItemSearch>?
    ) {
        compositeDisposable.add(
            youTubeApi.getSearchVideo(Constants.PART_SEARCH, Constants.Q, pageToken, Constants.KEY)
                .subscribe(
                    {response ->
                        Log.i(TAG, "onNext Search: Loading page $pageToken")
                        initialCallback?.onResult(response.items, null,
                            response.nextPageToken)
                        callback?.onResult(response.items, response.nextPageToken)

                    },
                    {error ->
                        Log.e(TAG, error.message, error)
                        Log.e(TAG,"Erro loading page: $pageToken", error)
                    },
                    {}
                )

        )
    }

    private fun getNextPage(prevPage: String) =
        youTubeApi.getSearchVideo(Constants.PART_SEARCH, Constants.Q, prevPage, Constants.KEY)
            .map { result -> result.nextPageToken }
            .toString()


    private fun getPrevPage(prevPage: String) =
        youTubeApi.getSearchVideo(Constants.PART_SEARCH, Constants.Q, prevPage, Constants.KEY)
            .map { result -> result.prevPageToken }
            .toString()

//
//
//    private fun getFirstPageObservable(): Observable<ResponseSearch> =
//            youTubeApi
//                .getSearchVideo(Constants.PART_SEARCH, Constants.Q, null, Constants.KEY)
//                .take(5)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//
//
//
//    private fun getNextPageObservable(pageToken: String): Observable<ResponseSearch> =
//            youTubeApi.getSearchVideo(Constants.PART_SEARCH, Constants.Q, pageToken, Constants.KEY)
//                .debounce(5, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())



}