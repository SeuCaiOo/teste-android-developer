package br.com.seucaio.testeicasei

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.seucaio.testeicasei.data.remote.YouTubeApiService
import br.com.seucaio.testeicasei.ui.detail.VideoDetailActivity
import br.com.seucaio.testeicasei.ui.list.VideoListActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName

    var subscription: Disposable? = null


    val service by lazy { YouTubeApiService.create() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_test_request_search.setOnClickListener { testRequestSearch() }
        btn_test_request_video.setOnClickListener { testRequestVideo() }
    }

    private fun testRequestVideo() {
        subscription = service.getVideoById(
            Constants.Q_ID,
            Constants.PART_VIDEO,
            Constants.KEY
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    startActivity<VideoDetailActivity>("video" to result.items[0].id)


                    Log.i(TAG, "Video -> ${result.items[0].statistics}")
                    Log.i(TAG, "Video -> ${result.items[0].statistics.viewCount}")
                    Log.i(TAG, "Video -> ${result.items[0].statistics.likeCount}")
                    Log.i(TAG, "Video -> ${result.items[0].statistics.dislikeCount}")
                    Log.i(TAG, "Video -> $result")

                             },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                }
            )
    }

    private fun testRequestSearch() {
        subscription =  service.getSearchVideo(
            Constants.PART_SEARCH,Constants.Q, null, Constants.KEY
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
//                    initReycler(result)
//
                    startActivity<VideoListActivity>()

//                    Log.v("VIDEOS", "$result")
//                    Log.v("VIDEOS", "${result.items}")
//                    Log.v("VIDEOS", "${result.items[1]}")
//                    Log.v("VIDEOS", "${result.items[1].id}")
//                    Log.v("VIDEOS", "${result.items[1].snippet}")
//                    Log.v("VIDEOS", "${result.items[1].snippet.title}")
//                    Log.v("VIDEOS", "${result.items[1].snippet.description}")
//                    Log.v("VIDEOS", "${result.items[1].snippet.channelTitle}")
//                    Log.v("VIDEOS", "${result.pageInfo}")

                },
                { error -> Log.e("ERROR", error.message) })
    }


    override fun onPause() {
        super.onPause()
        subscription?.dispose()
    }
}
