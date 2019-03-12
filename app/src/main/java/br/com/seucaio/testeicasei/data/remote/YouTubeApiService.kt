package br.com.seucaio.testeicasei.data.remote

import br.com.seucaio.testeicasei.Constants
import br.com.seucaio.testeicasei.data.remote.model.search.ResponseSearch
import br.com.seucaio.testeicasei.data.remote.model.video.ResponseVideo
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface YouTubeApiService {

//    @GET("/search")
//    fun getSearchVideo(
//        @Query("") part: List<String>,
//        @Query("") q: String,
//        @Query("") key: String
//    ): Observable<ResponseList>


    @GET("search")
    fun getSearchVideo(
        @Query("part") part: String,
        @Query("q") q: String,
        @Query("key") key: String
    ): Observable<ResponseSearch>

//    @GET("videos")
//    fun getVideoById(
//        @Query("id") id: String,
//        @Query("part") part: List<String>,
//        @Query("key") key: String
//    ): Observable<ResponseVideo>

 @GET("videos")
    fun getVideoById(
        @Query("id") id: String,
        @Query("part") part: String,
        @Query("key") key: String
    ): Observable<ResponseVideo>


    companion object {
         fun create() : YouTubeApiService {

             val retrofit = Retrofit.Builder()
                 .addConverterFactory(GsonConverterFactory.create())
                 .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                 .baseUrl(Constants.URL)
                 .build()

             return  retrofit.create(YouTubeApiService::class.java)
         }
    }
}
