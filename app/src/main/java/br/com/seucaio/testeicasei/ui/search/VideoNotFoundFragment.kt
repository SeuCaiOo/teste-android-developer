package br.com.seucaio.testeicasei.ui.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.seucaio.testeicasei.R


class VideoNotFoundFragment : Fragment() {

    val TAG = VideoNotFoundFragment::class.java.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView  = inflater.inflate(R.layout.fragment_video_not_found, container, false)
        return rootView
    }


}
