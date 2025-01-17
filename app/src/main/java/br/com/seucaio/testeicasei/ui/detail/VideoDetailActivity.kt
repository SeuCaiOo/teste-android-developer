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
import com.google.android.youtube.player.*
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
        setContentView(br.com.seucaio.testeicasei.R.layout.activity_video_detail)

        val idVideo = intent.getStringExtra("id")
        val titleVideo = intent.getStringExtra("title")

        supportActionBar?.title = titleVideo
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressBar_main.visibility = View.VISIBLE

        getVideoById(idVideo)
        initThumb(idVideo)

        val frag =
            supportFragmentManager.findFragmentById(R.id.view_video) as YouTubePlayerSupportFragment?

        btn_play.setOnClickListener { playVideo(idVideo, frag) }
        view_video_thumb.setOnClickListener { playVideo(idVideo, frag) }

    }

    private fun initThumb(id: String) {


        view_video_thumb.initialize(
            Constants.KEY,
            object : YouTubeThumbnailView.OnInitializedListener{
                override fun onInitializationSuccess(
                    youTubeThumbnailView: YouTubeThumbnailView?,
                    youTubeThumbnailLoader: YouTubeThumbnailLoader?
                ) {
                    youTubeThumbnailLoader?.setVideo(id)
                    youTubeThumbnailView?.setImageBitmap(null)

                    youTubeThumbnailLoader?.setOnThumbnailLoadedListener(
                        object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                            override fun onThumbnailLoaded(
                                youTubeThumbnailView: YouTubeThumbnailView?,
                                s: String?
                            ) {
                                youTubeThumbnailView?.visibility = View.VISIBLE
                                btn_play.setImageResource(R.drawable.ic_play_circle_filled_24dp)
                                youTubeThumbnailLoader.release()
                            }

                            override fun onThumbnailError(
                                youTubeThumbnailView: YouTubeThumbnailView?,
                                youTubeThumbnailLoader: YouTubeThumbnailLoader.ErrorReason?
                            ) { }
                        }
                    )
                }

                override fun onInitializationFailure(
                    youTubeThumbnailView: YouTubeThumbnailView?,
                    youTubeInitializationResult: YouTubeInitializationResult?
                ) { }
            })

    }

    private fun playVideo(id: String, frag: YouTubePlayerSupportFragment?) {

        frag!!.initialize(
            Constants.KEY,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?,
                    youtubePlayer: YouTubePlayer?,
                    b: Boolean
                ) {
                    view_video_thumb.visibility = View.INVISIBLE
                    btn_play.visibility = View.INVISIBLE
                    youtubePlayer?.cueVideo(id)
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider?,
                    youTubeInitializationResult: YouTubeInitializationResult?
                ) { }

            })

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

        val ft = NumberFormat.getInstance(Locale.US)

        txt_name_channel.text = this.snippet.channelTitle
        txt_description_video.text = this.snippet.description
        txt_count_like.text = ft.format(this.statistics.likeCount.toDouble())
        txt_count_dislike.text = ft.format(this.statistics.dislikeCount.toDouble())
        txt_count_view.text = ft.format(this.statistics.viewCount.toDouble())

        progressBar_main.visibility = View.GONE

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
