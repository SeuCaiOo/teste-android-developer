package br.com.seucaio.testeicasei.ui

import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import br.com.seucaio.testeicasei.ui.detail.VideoDetailActivity
import br.com.seucaio.testeicasei.ui.list.VideoListActivity
import br.com.seucaio.testeicasei.ui.list.VideoListAdapterPaging
import kotlinx.android.synthetic.main.fragment_list.view.*
import org.jetbrains.anko.support.v4.startActivity


class ListFragment : Fragment() {

    val TAG = ListFragment::class.java.simpleName

    lateinit var videoListAdapter: VideoListAdapterPaging

    lateinit var recylcer: RecyclerView

    companion object {
        fun newInstance() = ListFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(br.com.seucaio.testeicasei.R.layout.fragment_list, container, false)

        val activity = activity as VideoListActivity?
        recylcer = rootView.rv_list_video

        val myList = activity!!.getList()

        setRecycler(myList)


        return rootView
    }



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
