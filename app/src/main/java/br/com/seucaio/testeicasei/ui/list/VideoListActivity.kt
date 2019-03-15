package br.com.seucaio.testeicasei.ui.list

import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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


    private val viewModel: VideoListViewModel by lazy {
        ViewModelProviders.of(this).get(VideoListViewModel::class.java)
    }

//    private val adapter: VideoListAdapterPaging by lazy {
//        VideoListAdapterPaging {
//            startActivity<VideoDetailActivity>()
//        }
//    }


    lateinit var linearLayoutManager: LinearLayoutManager
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()


    val service by lazy {
        YouTubeApiService.create()
    }
    var disposable: Disposable? = null

    lateinit var firstPageNext: String
    lateinit var morePageNext: String

    var qSearch: CharSequence? = null

    //    lateinit var itemsListSearchVideo: Observable<List<ItemSearch>>
    lateinit var itemsNext: Observable<ItemSearch>
    lateinit var itemCombine: Observable<ItemSearch>
    lateinit var response: Observable<ResponseSearch>

    var videoList = emptyList<ItemSearch>().toMutableList()
    lateinit var videoListAdapter: VideoListAdapterPaging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        progressBar_main.visibility = View.VISIBLE

        val bundle = intent.extras
        qSearch = bundle?.getCharSequence("termo")


        setQuery(qSearch)


        subscribeToList()



//        testSearchQuery(qSearch.toString())

        // Verify the action and get the qSearch
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                qSearch = query
//                testSearchQuery(query)
                setQuery(query)
                subscribeToList()
            }
        }


    }

    private fun setNotFoundVideo() {
        setContentView(R.layout.view_video_not_found)
    }

    private fun setQuery(q: CharSequence?) {

        Constants.Q = q.toString()

    }

    private fun testSearchQuery(query: String) {
        disposable = service.getSearchVideo(
            Constants.PART_SEARCH, query, null, Constants.TYPE_VIDEO, Constants.KEY
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
//                    initReycler(result.items)


                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                })
    }

    private fun initReycler(results: List<ItemSearch>) {

        linearLayoutManager = LinearLayoutManager(this)
        rv_list_video.setHasFixedSize(false)
        rv_list_video.layoutManager = linearLayoutManager

        rv_list_video.adapter = VideoListAdapter(results) { video ->
            startActivity<VideoDetailActivity>("id" to video.id.videoId)
        }

        progressBar_main.visibility = View.GONE
    }

    private fun subscribeToList() {
        disposable = viewModel.itemSearchVideoList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->

                    if(list.size == 0) {
                        setNotFoundVideo()
                    } else {
                        setRecycler()
                        videoListAdapter.submitList(list)
                    }


                    list.map { item ->

                        Log.i(TAG, "ID Item -> ${item.id.videoId}")

//                        list.filter { item.id.videoId != null }
                    }

//                    getJustVideos(list)



                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                    Log.e(TAG, error.message, error)
                }
            )
    }


    private fun setRecycler() {

        videoListAdapter = VideoListAdapterPaging { video ->
            startActivity<VideoDetailActivity>(
                "id" to video.id.videoId,
                "title" to video.snippet.title
            )
        }

        val llm = LinearLayoutManager(this)
        rv_list_video.layoutManager = llm
        rv_list_video.adapter = videoListAdapter



        progressBar_main.visibility = View.GONE
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
            queryHint = getString(R.string.search_hint)
            setQuery(qSearch, false)
        }

        return true

    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

}
