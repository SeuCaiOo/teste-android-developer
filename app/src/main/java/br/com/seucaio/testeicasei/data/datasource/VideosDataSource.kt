package br.com.seucaio.testeicasei.data.datasource

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import br.com.seucaio.testeicasei.Constants
import br.com.seucaio.testeicasei.data.remote.YouTubeApiService
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import io.reactivex.disposables.CompositeDisposable

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

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, ItemSearch>
    ) {
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
            youTubeApi.getSearchVideo(
                Constants.PART_SEARCH, Constants.Q, pageToken, Constants.TYPE_VIDEO, Constants.KEY
            )
                .subscribe(
                    { response ->

                        Log.i(TAG, "onNext Search: Loading page $pageToken")
                        initialCallback?.onResult(
                            response.items, null,
                            response.nextPageToken
                        )
                        callback?.onResult(response.items, response.nextPageToken)

                    },
                    { error ->
                        Log.e(TAG, error.message, error)
                        Log.e(TAG, "Erro loading page: $pageToken", error)
                    },
                    {}
                )

        )
    }

//    private fun getJustVideos(listSearch: List<ItemSearch>?
//                              ) =
//            Observable.fromIterable(listSearch)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .filter { item -> item.id.videoId != null }
////            .subscribe(
//                { search ->
//
////                    Log.i(TAG, "onNext Search: Loading page $pageToken")
////                    initialCallback?.onResult(, null,
////                        response.nextPageToken)
////                    callback?.onResult(response.items, response.nextPageToken)
////
//                    Log.i(TAG, "Search With IdVideo-> $search")
//                    Log.i(TAG, "Search With IdVideo -> ${search.id.videoId}")
////                        setRecycler(search)
//
//                },
//                { error ->
//                    Log.e(TAG, error.message)
//                }
//            )
//    }

//    private fun getNextPage(prevPage: String) =
//        youTubeApi.getSearchVideo(Constants.PART_SEARCH, Constants.Q, prevPage, Constants.KEY)
//            .map { result -> result.nextPageToken }
//            .toString()
//
//
//    private fun getPrevPage(prevPage: String) =
//        youTubeApi.getSearchVideo(Constants.PART_SEARCH, Constants.Q, prevPage, Constants.KEY)
//            .map { result -> result.prevPageToken }
//            .toString()
//
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