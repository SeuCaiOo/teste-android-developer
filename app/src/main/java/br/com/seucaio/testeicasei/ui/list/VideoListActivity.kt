package br.com.seucaio.testeicasei.ui.list

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.seucaio.testeicasei.Constants
import br.com.seucaio.testeicasei.R
import br.com.seucaio.testeicasei.data.remote.YouTubeApiService
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import br.com.seucaio.testeicasei.data.remote.model.search.ResponseSearch
import br.com.seucaio.testeicasei.ui.detail.VideoDetailActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video_list.*
import org.jetbrains.anko.startActivity
import java.util.concurrent.TimeUnit

class VideoListActivity : AppCompatActivity() {
    val TAG = VideoListActivity::class.java.simpleName


    private val viewModel: VideoListViewModel by lazy {
        ViewModelProviders.of(this).get(VideoListViewModel::class.java)
    }

    private val adapter: VideoListAdapterPaging by lazy {
        VideoListAdapterPaging() {
//            startActivity<VideoDetailActivity>("video" to video.id.videoId)
        }
    }



    lateinit var linearLayoutManager: LinearLayoutManager
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()


    val service by lazy {
        YouTubeApiService.create()
    }
    var disposable: Disposable? = null


    lateinit var firstPageNext: String
    lateinit var morePageNext: String


    //    lateinit var itemsListSearchVideo: Observable<List<ItemSearch>>
    lateinit var itemsNext: Observable<ItemSearch>
    lateinit var itemCombine: Observable<ItemSearch>
    lateinit var response: Observable<ResponseSearch>

    var videoList = emptyList<ItemSearch>().toMutableList()
    var videoListAdapter: VideoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

//        progressBar_main.visibility = View.VISIBLE
//        getVideosBySearch()
//        getFirstPage()

        val llm = LinearLayoutManager(this)
        rv_list_video.layoutManager = llm
        rv_list_video.adapter = adapter
        subscribeToList()
    }

    private fun subscribeToList() {
        val disposable = viewModel.itemSearchVideoList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    adapter.submitList(list)
//                    if (recyclerState != null) {
//                        recyclerCharacters.layoutManager?.onRestoreInstanceState(recyclerState)
//                        recyclerState = null
//                    }
                },
                {error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                    Log.e(TAG, error.message, error)
                }
            )
    }

/*    private fun setAdapter(): VideoListAdapter? {
        videoListAdapter = VideoListAdapter(videoList!!) { video ->
            startActivity<VideoDetailActivity>("video" to video.id.videoId)
        }
        return videoListAdapter
    }*/

/*    private fun setRecycler() {
        linearLayoutManager = LinearLayoutManager(this)
        rv_list_video.setHasFixedSize(false)
        rv_list_video.layoutManager = linearLayoutManager

*//*
        rv_list_video.adapter = VideoListAdapter(videoList) { video ->
            startActivity<VideoDetailActivity>("video" to video.id.videoId)
        }
*//*

        rv_list_video.adapter = setAdapter()

        rv_list_video.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = rv_list_video.layoutManager!!.itemCount

                if (totalItemCount == lastVisibleItemPosition + 1) {
                    Log.i(TAG, "Last item recycler -> getMore()")
                    getNextPage()
                }

            }
        })

//        progressBar_main.visibility = View.GONE
    }*/


    private fun getNextPageObservable(pageToken: String): Observable<ResponseSearch> {
        val nextResponse = response.mergeWith(
            service.getSearchVideo(Constants.PART_SEARCH, Constants.Q, pageToken, Constants.KEY)
                .debounce(5, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        )


        return nextResponse

    }

    private fun getFirstPageObservable(): Observable<ResponseSearch> {
        response =
            service.getSearchVideo(Constants.PART_SEARCH, Constants.Q, null, Constants.KEY)
                .take(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        return response
    }


    private fun getFirstPage() {
        disposable = getFirstPageObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { searchFirst ->

                    //                    getJustVideos(searchFirst.items)
                    Log.i(TAG, "onNext Search First -> Prev ${searchFirst.prevPageToken}")
                    Log.i(TAG, "onNext Search First -> Next ${searchFirst.nextPageToken}")

                    itemCombine = searchFirst.items.toObservable()

                    Log.i(TAG, "onNext Items First -> First $itemCombine")
//                    Log.i(TAG, "Items First -> First $itemFirstListSearchVideo")
//                    Log.i(TAG, "Items Next -> $mergeItems")



                    morePageNext = searchFirst.nextPageToken
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                },

                {
                    mapJustVideos(itemCombine)
                    Log.i(TAG, "onCompleted Items First Combine -> $itemCombine")
                    Log.i(TAG, "onCompleted -> First videos COMPLETED")

                }
            )


    }

    private fun getNextPage() {

        val nextPage = morePageNext

        disposable = getNextPageObservable(nextPage)
            .take(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(5, TimeUnit.MILLISECONDS)
            .subscribe(
                { searchNext ->



                    itemsNext = searchNext.items.toObservable()
                    morePageNext = searchNext.nextPageToken


                    Log.i(TAG, "onNext -> Init more videos !")

                    Log.i(TAG, "onNext Search Next -> Prev ${searchNext.prevPageToken}")
                    Log.i(TAG, "onNext Search Next -> Next ${searchNext.nextPageToken}")
                    Log.i(TAG, "onNext Items Next -> Prev ${searchNext.items.toObservable()}")
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                },
                {

//                    itemCombine =
//                        itemsNext
                     val testConcat =   mergeItemList(itemsNext)

                    mapJustVideos(testConcat)
                    Log.i(TAG, "onCompleted Items Combine -> $itemCombine")
                    Log.i(TAG, "onCompleted Items Combine -> $testConcat")
                    Log.i(TAG, "onCompleted -> More videos COMPLETED")
                }
            )
    }


//    private fun mergeItemList(itemNextListSearch: Observable<ItemSearch>): Observable<ItemSearch> {
//        return itemFirstListSearchVideo
//            .subscribeOn(Schedulers.io())
//            .mergeWith(itemNextListSearch)
//    }

    private fun mergeItemList(itemNextListSearch: Observable<ItemSearch>) =

        itemCombine
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .concatWith(itemNextListSearch)

//        itemNextListSearch
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .startWith(itemCombine)


/*        disposable = service.getSearchVideo(Constants.PART_SEARCH,Constants.Q,pageToken,Constants.KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ search ->
                    Log.i(TAG, "Search -> $search")
                    Log.i(TAG, "Search -> ${search.prevPageToken}")
                    Log.i(TAG, "Search -> ${search.nextPageToken}")
                    Log.i(TAG, "Search -> ${search.pageInfo}")
                    Log.i(TAG, "Search -> ${search.items}")
                    Log.i(TAG, "Search -> ${search.regionCode}")
                    setRecycler(search.items)
                },{ error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                })
}*/

/*    private fun getVideosBySearch() {


            disposable = service.getSearchVideo(Constants.PART_SEARCH,Constants.Q,
                null, Constants.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { search ->
                        Log.i(TAG, "Search Items -> ${search.items}")
                        Log.i(TAG, "Search -> ${search.regionCode}")
                        Log.i(TAG, "Search -> Prev ${search.prevPageToken}")
                        Log.i(TAG, "Search -> Next ${search.nextPageToken}")
                        Log.i(TAG, "Search -> ${search}")

                        if (search.prevPageToken != null) {
                            val nextPage = search.nextPageToken
                            getNextPageObservable(nextPage)
                        }


                        getJustVideos(search.items)

                    },
                    { error ->
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, error.message)
                    }
                )
            else {
            disposable = service.getSearchVideo(
                Constants.PART_SEARCH,
                Constants.Q,
                pageToken,
                Constants.KEY
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { search ->

                        Log.i(TAG, "More -> Prev ${search.prevPageToken}")
                        Log.i(TAG, "Morw -> Next ${search.nextPageToken}")

                        getJustVideos(search.items)
                    },
                    { error ->
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, error.message)
                    }
                )
        }

    }*/

/*    private fun updateList(justVideo: ItemSearch) {

        val list = videoList
        Toast.makeText(this, "list -> ${list.size}", Toast.LENGTH_SHORT).show()

        if (videoListAdapter == null) {
            list.add(justVideo)
            setRecycler()
        } else {
            list.add(justVideo)
            videoListAdapter?.notifyDataSetChanged()
        }

    }*/


    private fun mapJustVideos(itemsSearch: Observable<ItemSearch>) =
        itemsSearch
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter { item -> item.id.videoId != null }
            .subscribe(
                {
                        justVideo ->
//                    updateList(justVideo)


//                    Log.i(TAG, "Items -> $justVideo")
                    Log.i(TAG, "Items -> ${justVideo.id.videoId}")

                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                }, {
                    Log.i(TAG, "onCompleted -> DONE LIST")

                }
            )


/*
    private fun getJustVideos(listSearch: List<ItemSearch>?){
        val items =
            Observable.fromIterable(listSearch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { item -> item.id.videoId != null }
                .toList()
                .subscribe(
                    { search ->
                        //                      Log.i(TAG, "Search With IdVideo-> $search")
//                      Log.i(TAG, "Search With IdVideo -> ${search.id.videoId}")
                        setRecycler(search)
                    },
                    { error ->
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, error.message)
                    }
                )
    }
*/


    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

}
