package br.com.seucaio.testeicasei.ui.list

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
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video_list.*
import org.jetbrains.anko.startActivity

class VideoListActivity : AppCompatActivity() {
    val TAG = VideoListActivity::class.java.simpleName

    lateinit var linearLayoutManager: LinearLayoutManager
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()


    val service by lazy {
        YouTubeApiService.create()
    }
    var disposable: Disposable? = null


    lateinit var firstPageNext: String
    lateinit var morePageNext: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        progressBar_main.visibility = View.VISIBLE
//        getVideosBySearch()
        getFirstPage()
    }

    private fun setRecycler(videoList: List<ItemSearch>) {
        linearLayoutManager = LinearLayoutManager(this)
        rv_list_video.setHasFixedSize(false)
        rv_list_video.layoutManager = linearLayoutManager

        rv_list_video.adapter = VideoListAdapter(videoList) { video ->
            startActivity<VideoDetailActivity>("video" to video.id.videoId)
        }
        rv_list_video.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = rv_list_video.layoutManager!!.itemCount

                if (totalItemCount == lastVisibleItemPosition + 1)
                    getNextPage()
            }
        })

        progressBar_main.visibility = View.GONE
    }


    private fun getNextPageObservable(pageToken: String): Observable<ResponseSearch> {
        return service.getSearchVideo(Constants.PART_SEARCH, Constants.Q, pageToken, Constants.KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    private fun getFirstPageObservable(): Observable<ResponseSearch> {
        return service.getSearchVideo(Constants.PART_SEARCH, Constants.Q, null, Constants.KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    private fun getFirstPage() {
        disposable = getFirstPageObservable()
            .subscribe(
                { searchFirst ->

                    getJustVideos(searchFirst.items)
                    Log.i(TAG, "Search First -> Prev ${searchFirst.prevPageToken}")
                    Log.i(TAG, "Search First -> Next ${searchFirst.nextPageToken}")

                    morePageNext = searchFirst.nextPageToken
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                }
            )
    }

    private fun getNextPage() {
        disposable = getNextPageObservable(morePageNext).subscribe(
            { searchNext ->

                getJustVideos(searchNext.items)
                Log.i(TAG, "Search Next -> Prev ${searchNext.prevPageToken}")
                Log.i(TAG, "Search Next -> Next ${searchNext.nextPageToken}")

                morePageNext = searchNext.nextPageToken
            },
            { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, error.message)
            }
        )
    }

//        disposable = service.getSearchVideo(Constants.PART_SEARCH,Constants.Q,pageToken,Constants.KEY)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ search ->
//                    Log.i(TAG, "Search -> $search")
//                    Log.i(TAG, "Search -> ${search.prevPageToken}")
//                    Log.i(TAG, "Search -> ${search.nextPageToken}")
//                    Log.i(TAG, "Search -> ${search.pageInfo}")
//                    Log.i(TAG, "Search -> ${search.items}")
//                    Log.i(TAG, "Search -> ${search.regionCode}")
//                    setRecycler(search.items)
//                },{ error ->
//                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
//                    Log.e(TAG, error.message)
//                })
//}

    private fun getVideosBySearch() {


//            disposable = service.getSearchVideo(Constants.PART_SEARCH,Constants.Q,
//                null, Constants.KEY)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { search ->
//                        //                        Log.i(TAG, "Search Items -> ${search.items}")
////                        Log.i(TAG, "Search -> ${search.regionCode}")
//                        Log.i(TAG, "Search -> Prev ${search.prevPageToken}")
//                        Log.i(TAG, "Search -> Next ${search.nextPageToken}")
////                        Log.i(TAG, "Search -> ${search}")
//
//                        if (search.prevPageToken != null) {
//                            val nextPage = search.nextPageToken
//                            getNextPageObservable(nextPage)
//                        }
//
//
//                        getJustVideos(search.items)
//
//                    },
//                    { error ->
//                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
//                        Log.e(TAG, error.message)
//                    }
//                )
//            else {
//            disposable = service.getSearchVideo(
//                Constants.PART_SEARCH,
//                Constants.Q,
//                pageToken,
//                Constants.KEY
//            )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { search ->
//
//                        Log.i(TAG, "More -> Prev ${search.prevPageToken}")
//                        Log.i(TAG, "Morw -> Next ${search.nextPageToken}")
//
//                        getJustVideos(search.items)
//                    },
//                    { error ->
//                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
//                        Log.e(TAG, error.message)
//                    }
//                )
//        }


    }

    private fun getJustVideos(listSearch: List<ItemSearch>?) {
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


    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

}
