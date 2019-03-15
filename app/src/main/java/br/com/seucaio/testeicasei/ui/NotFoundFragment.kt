package br.com.seucaio.testeicasei.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import br.com.seucaio.testeicasei.R
import kotlinx.android.synthetic.main.fragment_not_found.view.*


class NotFoundFragment : Fragment() {

    val TAG = NotFoundFragment::class.java.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView  = inflater.inflate(R.layout.fragment_not_found, container, false)
        return rootView
    }


}
