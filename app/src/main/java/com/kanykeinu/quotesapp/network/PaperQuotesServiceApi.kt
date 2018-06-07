package com.kanykeinu.quotesapp.network

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by KanykeiNu on 23.05.2018.
 */
interface PaperQuotesServiceApi {

//    @GET("quoteModels")
//    fun getQuote(@Header("Authorization") apiKey : String, @Query("tags") tag: String, @Query("limit") count : Int) : Observable<Response>

    companion object {
        val BASE_URL : String = "https://api.paperquotes.com/apiv1/"
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val builder = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)

        fun create(): PaperQuotesServiceApi {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .build()

            return retrofit.create(PaperQuotesServiceApi::class.java);
        }
    }
}