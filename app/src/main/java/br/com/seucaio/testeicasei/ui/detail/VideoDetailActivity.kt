package br.com.seucaio.testeicasei.ui.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.seucaio.testeicasei.Constants
import br.com.seucaio.testeicasei.R
import br.com.seucaio.testeicasei.data.remote.YouTubeApiService
import br.com.seucaio.testeicasei.data.remote.model.video.ResponseVideo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video_detail.*
import java.text.NumberFormat
import java.util.*

class VideoDetailActivity : AppCompatActivity() {

    val TAG = VideoDetailActivity::class.java.simpleName

    val service by lazy {
        YouTubeApiService.create()
    }
    var disposable: Disposable? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        val idVideo = intent.getStringExtra("video")

        progressBar_main.visibility = View.VISIBLE

        supportActionBar?.title = "Items ID: $idVideo"

        getVideoById(idVideo)
    }

    private fun getVideoById(id: String) {
        disposable = service.getVideoById(
            id, Constants.PART_VIDEO, Constants.KEY
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        result ->
// showVideo(result.items[0])

                    Log.i(TAG, "Video -> ${result.items[0].statistics}")
                    Log.i(TAG, "Video -> ${result.items[0].statistics.viewCount}")
                    Log.i(TAG, "Video -> ${result.items[0].statistics.likeCount}")
                    Log.i(TAG, "Video -> ${result.items[0].statistics.dislikeCount}")
//                    Log.i(TAG, "Video -> $result")

                    showVideo(result)
//                    Log.i(TAG, "${result.items[0].snippet.channelTitle}")

                },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                }
            )
    }

    private fun showVideo(result: ResponseVideo) =
    with(result.items[0]) {

        val countView = this.statistics.viewCount.toDouble()

        val ft = NumberFormat.getInstance(Locale.US)

        txt_name_channel.text = this.snippet.channelTitle
        txt_description_video.text = this.snippet.description
        txt_count_like.text = ft.format(this.statistics.likeCount.toDouble())
        txt_count_dislike.text = ft.format(this.statistics.dislikeCount.toDouble())
        txt_count_view.text = ft.format(this.statistics.viewCount.toDouble())
        progressBar_main.visibility = View.GONE

    }
//        Glide
//            .with(this@MovieActivity)
//            .load("$URL_IMAGE${result?.posterPath}")
//            .into(img_poster)



    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
