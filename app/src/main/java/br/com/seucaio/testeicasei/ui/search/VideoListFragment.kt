package br.com.seucaio.testeicasei.ui.search

import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import br.com.seucaio.testeicasei.ui.detail.VideoDetailActivity
import kotlinx.android.synthetic.main.fragment_video_list.view.*
import org.jetbrains.anko.support.v4.startActivity


class VideoListFragment : Fragment() {

    val TAG = VideoListFragment::class.java.simpleName

    lateinit var videoListAdapter: VideoListAdapterPaging

    lateinit var recylcer: RecyclerView

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    companion object {
        fun newInstance() = VideoListFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        Log.d(TAG,"---> onCreateView")
        val rootView = inflater.inflate(br.com.seucaio.testeicasei.R.layout.fragment_video_list, container, false)

        val activity = activity as VideoSearchActivity?
        recylcer = rootView.rv_list_video

        val myList = activity!!.getList()

        setRecycler(myList)


        return rootView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)

        Log.d(TAG,"---> onCreate")
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
//        inflater?.inflate(br.com.seucaio.testeicasei.R.menu.main_menu, menu)
//
//        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//
//        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
//            // Assumes current activity is the searchable activity
//            setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
//            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
//            queryHint = getString(R.string.search_hint)
//
//            queryTextListener = object : SearchView.OnQueryTextListener {
//                override fun onQueryTextChange(newText: String): Boolean {
//                    Log.i(TAG, "onQueryTextChange -> $newText")
//                    listActivity!!.setQuerySearch(query)
//                    listActivity!!.subscribeToList()
//                    return false
//                }
//
//                override fun onQueryTextSubmit(query: String): Boolean {
//                    Log.i(TAG, "onQueryTextSubmit -> $query")
//                    listActivity!!.setQuerySearch(query)
//                    listActivity!!.subscribeToList()
//                    return false
//                }
//
////                fun getVideo() {
////                    listActivity!!.setQuerySearch(query)
////                    listActivity!!.subscribeToList()
////                }
//
//            }
//            searchView?.setOnQueryTextListener(queryTextListener)
//        }
//
//        super.onCreateOptionsMenu(menu, inflater)
//    }

//    override fun onOptionsItemSelected(item: MenuItem) =
//        when (item.itemId) {
//            R.id.action_search -> { false }
//
//            else -> {
//                searchView?.setOnQueryTextListener(queryTextListener)
//                super.onOptionsItemSelected(item) }
//        }

    fun setRecycler(pagedList: PagedList<ItemSearch>) {

        videoListAdapter = VideoListAdapterPaging { video ->
            startActivity<VideoDetailActivity>(
                "id" to video.id.videoId,
                "title" to video.snippet.title
            )
        }

        val llm = LinearLayoutManager(context)
        recylcer.layoutManager = llm
        recylcer.adapter = videoListAdapter

        videoListAdapter.submitList(pagedList)
    }


}
