package br.com.seucaio.testeicasei.ui.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.seucaio.testeicasei.Constants
import br.com.seucaio.testeicasei.R
import br.com.seucaio.testeicasei.data.remote.YouTubeApiService
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import br.com.seucaio.testeicasei.ui.detail.VideoDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video_list.*
import org.jetbrains.anko.startActivity

class VideoListActivity : AppCompatActivity() {

    val TAG = VideoListActivity::class.java.simpleName

    val service by lazy {
        YouTubeApiService.create()
    }
    var disposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        progressBar_main.visibility = View.VISIBLE
        getVideosBySearch()
    }

    private fun setRecycler(videoList: List<ItemSearch>) {
        rv_list_video.setHasFixedSize(true)
        rv_list_video.layoutManager = LinearLayoutManager(this)
        rv_list_video.adapter = VideoListAdapter(videoList) {
                video -> startActivity<VideoDetailActivity>("video" to video.id.videoId)
        }
        progressBar_main.visibility = View.GONE
    }

    private fun getVideosBySearch() {
        disposable = service.getSearchVideo(
            Constants.PART_SEARCH,
            Constants.Q,
            Constants.KEY
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { search ->

                    //                    setTest(search)
//                    Log.i("MOVIES", "Total = ${search.totalResults}")
                    Log.i(TAG, "Search -> $search")
                    Log.i(TAG, "Search -> ${search.pageInfo}")
                    Log.i(TAG, "Search -> ${search.items}")
                    Log.i(TAG, "Search -> ${search.regionCode}")

                    setRecycler(search.items)


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
