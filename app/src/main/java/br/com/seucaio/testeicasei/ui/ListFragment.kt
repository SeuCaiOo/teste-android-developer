package br.com.seucaio.testeicasei.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.seucaio.testeicasei.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class ListFragment : Fragment() {

    val TAG = ListFragment::class.java.simpleName

    var disposable: Disposable? = null

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        // TODO: Use the ViewModel

        subscribeToList()

    }

    private fun subscribeToList() {
        disposable = viewModel.itemSearchVideoList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->

                    if(list.size == 0) {
//                        setNotFoundVideo()
                    } else {
//                        setRecycler()
//                        videoListAdapter.submitList(list)
                    }


                    list.map { item ->

                        Log.i(TAG, "ID Item -> ${item.id.videoId}")

//                        list.filter { item.id.videoId != null }
                    }

//                    getJustVideos(list)



                },
                { error ->
//                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, error.message)
                    Log.e(TAG, error.message, error)
                }
            )
    }

}
