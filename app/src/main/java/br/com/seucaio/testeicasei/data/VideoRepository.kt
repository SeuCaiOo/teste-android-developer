package br.com.seucaio.testeicasei.data

import br.com.seucaio.testeicasei.data.remote.YouTubeApiService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class VideoRepository
@Inject constructor
    (private val youTubeApiService: YouTubeApiService) {





//    fun loadVideos(): Observable<ResponseList> {
//        return youTubeApiService.getSearchVideo(
//            Constants.PART_SEARCH,Constants.Q, Constants.KEY
//        )
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {},
//                {}
//            )
//    }
}