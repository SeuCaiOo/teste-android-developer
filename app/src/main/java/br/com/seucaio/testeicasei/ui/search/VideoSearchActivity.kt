package br.com.seucaio.testeicasei.ui.search

import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import br.com.seucaio.testeicasei.Constants
import br.com.seucaio.testeicasei.R
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_video_search.*


class VideoSearchActivity : AppCompatActivity() {
    val TAG = VideoSearchActivity::class.java.simpleName

    private val KEY_SEARCH = ""

    private val viewModel: VideoListViewModel by lazy {
        ViewModelProviders.of(this).get(VideoListViewModel::class.java)
    }

    var disposable: Disposable? = null
    var qSearch: CharSequence? = null


    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null



    //    lateinit var linearLayoutManager: LinearLayoutManager
//    private val lastVisibleItemPosition: Int
//        get() = linearLayoutManager.findLastVisibleItemPosition()

    //    private val adapter: VideoListAdapterPaging by lazy {
//        VideoListAdapterPaging {
//            startActivity<VideoDetailActivity>()
//        }
//    }

    //
//    lateinit var firstPageNext: String
//    lateinit var morePageNext: String
    //    val service by lazy {
//        YouTubeApiService.create()
//    }
    //    lateinit var itemsListSearchVideo: Observable<List<ItemSearch>>
//    lateinit var itemsNext: Observable<ItemSearch>
//    lateinit var itemCombine: Observable<ItemSearch>
//    lateinit var response: Observable<ResponseSearch>
//
//    var videoList = emptyList<ItemSearch>().toMutableList()
//    lateinit var videoListAdapter: VideoListAdapterPaging


    lateinit var pagedList: PagedList<ItemSearch>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_search)

        Log.d(TAG,"---> onCreate")

        swipeRefresh.setOnRefreshListener{ reloadVideos() }

        progressBar_main.visibility = View.VISIBLE


        val q = intent.getCharSequenceExtra("q")

        if (q != null) {
            qSearch = q
        } else {
            qSearch = Constants.Q
        }


        setQuerySearch(qSearch)
        subscribeToList()


//        testSearchQuery(qSearch.toString())


    }

    private fun reloadVideos() {
        swipeRefresh.isRefreshing = true
        subscribeToList()
    }

    fun subscribeToList() {
        disposable = viewModel.itemSearchVideoList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    pagedList = list
                    displayView(list.size)
                    swipeRefresh.isRefreshing = false
                    progressBar_main.visibility = View.GONE
                    list.map { item ->
                        Log.i(TAG, "ID Item -> ${item.id.videoId}")
                    }
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                    Log.e(TAG, error.message, error)
                }
            )
    }

    private fun displayView(size: Int) {
        val fragManager = supportFragmentManager

        val fragmentList = VideoListFragment()
        val fragmentNotFound = VideoNotFoundFragment()

        if (size != 0) {
            fragManager.beginTransaction().replace(R.id.frame_search, fragmentList).commit()
        } else {
            fragManager.beginTransaction().replace(R.id.frame_search, fragmentNotFound).commit()

        }


    }

    fun getList() = pagedList

    fun setQuerySearch(q: CharSequence?) {

        Constants.Q = q.toString()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"---> onStart")
    }




/*    private fun setNotFoundVideo() {
        progressBar_main.visibility = View.GONE
        rv_list_video.visibility = View.INVISIBLE
        setContentView(R.layout.view_video_not_found)

        txt_video_not_found.visibility = View.VISIBLE
    }*/

/*
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
*/

/*    private fun initReycler(results: List<ItemSearch>) {

        linearLayoutManager = LinearLayoutManager(this)
        rv_list_video.setHasFixedSize(false)
        rv_list_video.layoutManager = linearLayoutManager

        rv_list_video.adapter = VideoListAdapter(results) { video ->
            startActivity<VideoDetailActivity>("id" to video.id.videoId)
        }

        progressBar_main.visibility = View.GONE
    }*/

/*
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
*/


    override fun onSaveInstanceState(outState: Bundle?) {

        outState?.putCharSequence(KEY_SEARCH, qSearch)

        super.onSaveInstanceState(outState)

        Log.d(TAG,"---> onSaveInstanceState")

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


            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    Log.i(TAG, "onQueryTextChange -> $newText")
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.i(TAG, "onQueryTextSubmit -> $query")
                    progressBar_main.visibility = View.VISIBLE
                    setQuery(query, false)
                    setQuerySearch(query)
                    subscribeToList()
                    return false
                }

            }
            setOnQueryTextListener(queryTextListener)
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
        Log.d(TAG,"---> onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        Log.d(TAG,"---> onDestroy")
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG,"---> onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"---> onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"---> onResume")
    }

}
