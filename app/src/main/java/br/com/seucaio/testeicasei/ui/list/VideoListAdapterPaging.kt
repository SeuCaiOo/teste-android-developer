package br.com.seucaio.testeicasei.ui.list

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.seucaio.testeicasei.R
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import kotlinx.android.synthetic.main.adapter_list_video.view.*


class VideoListAdapterPaging(
    private val listener: (ItemSearch) -> Unit
): PagedListAdapter<ItemSearch, VideoListAdapterPaging.VideoListViewHolder>(videoDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_list_video, parent, false)

        return VideoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video!!, listener)
  }


    class VideoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(video: ItemSearch?, listener: (ItemSearch) -> Unit) =
                with(itemView) {
                    txt_title_video.text = video?.snippet?.title
                    txt_name_channel.text = video?.snippet?.channelTitle
                    txt_description_video.text = video?.snippet?.description
                    btn_detail.setOnClickListener { listener(video!!) }
                }
    }


    companion object {

        val videoDiff = object : DiffUtil.ItemCallback<ItemSearch>() {
            override fun areItemsTheSame(old: ItemSearch, new: ItemSearch): Boolean {
                return old.id == new.id
            }

            override fun areContentsTheSame(old: ItemSearch, new: ItemSearch): Boolean {
                return old == new
            }
        }
    }
}