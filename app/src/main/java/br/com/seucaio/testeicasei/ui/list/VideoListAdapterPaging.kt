package br.com.seucaio.testeicasei.ui.list

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.seucaio.testeicasei.Constants
import br.com.seucaio.testeicasei.R
import br.com.seucaio.testeicasei.data.remote.model.search.ItemSearch
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
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
        with(holder) {
            bind(video!!)
            video.let {
                itemView.setOnClickListener { listener(video) }
                itemView.btn_detail.setOnClickListener { listener(video) }
            }
        }
  }

    class VideoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(video: ItemSearch) =
                with(itemView) {
                    txt_title_video.text = video.snippet.title
                    txt_name_channel.text = video.snippet.channelTitle
                    txt_description_video.text = video.snippet.description
                    view_video_thumb.initialize(Constants.KEY,
                        object : YouTubeThumbnailView.OnInitializedListener{
                            override fun onInitializationSuccess(
                                youTubeThumbnailView: YouTubeThumbnailView?,
                                youTubeThumbnailLoader: YouTubeThumbnailLoader?
                            ) {
                                youTubeThumbnailLoader?.setVideo(video.id.videoId)
                                youTubeThumbnailView?.setImageBitmap(null)

                                youTubeThumbnailLoader?.setOnThumbnailLoadedListener(
                                    object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                                        override fun onThumbnailLoaded(
                                            youTubeThumbnailView: YouTubeThumbnailView?,
                                            s: String?
                                        ) {
                                            youTubeThumbnailView?.visibility = View.VISIBLE
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