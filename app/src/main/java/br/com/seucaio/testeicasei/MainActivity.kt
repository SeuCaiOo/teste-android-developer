package br.com.seucaio.testeicasei

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SearchView
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


        setupSearch()
    }

    private fun setupSearch() {

        searchView.queryHint = getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                startActivity<VideoListActivity>("termo" to query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })
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

                    startActivity<VideoDetailActivity>("id" to result.items[0].id,
                        "title" to result.items[0].snippet.title)


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
//          val termo = editText.text.toString()
//        startActivity<VideoListActivity>("termo " to termo)


        val query = searchView.query
        startActivity<VideoListActivity>("termo" to query)

    }


    override fun onPause() {
        super.onPause()
        subscription?.dispose()
    }
}
