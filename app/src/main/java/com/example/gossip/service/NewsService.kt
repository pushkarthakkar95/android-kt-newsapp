package com.example.gossip.service

import com.example.gossip.Constants
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor

interface NewsService {
    companion object{
        fun create() : NewsService{
            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .client(OkHttpClient().newBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NewsService::class.java)
        }
    }
    @GET("top-headlines")
    fun getHeadlines(@Query("q") q: String,
                     @Query("apiKey") apiKey: String = Constants.CONSUMER_API_KEY) :
            Call<Model.Result>
}