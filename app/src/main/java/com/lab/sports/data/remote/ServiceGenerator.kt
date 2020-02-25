package com.github.repos.data.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lab.sports.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ServiceGenerator {

    private val BASE_URL = "https://www.thesportsdb.com/api/"
    private var httpClient = OkHttpClient.Builder()
    var builder = Retrofit.Builder()

    // Final Calling Method
    fun <S> createService(serviceClass: Class<S>): S {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(logging)
        }

        val gson = GsonBuilder()
            .setLenient()
            .create()

        builder.baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

//            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
        var interceptor: Interceptor

        interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Charset", "UTF-8")
                .build()
            chain.proceed(request)
        }


        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor)

            builder.client(httpClient.build())
        }
        val retrofit = builder.build()
        return retrofit.create(serviceClass)
    }
}