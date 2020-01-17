package com.telstra.feedapp.networkadapter.retrofit

import android.content.Context
import com.google.gson.GsonBuilder
import com.telstra.feedapp.networkadapter.api.request.ApiInterface
import com.telstra.feedapp.networkadapter.apiconstants.ApiConstants
import com.telstra.feedapp.utility.NetworkProvider
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(private val context: Context) {

    private var apiInterface: ApiInterface? = null
    private var httpClient: OkHttpClient? = null

    val apiClient: ApiInterface
        get() {
            if (httpClient == null) initOkHttp()

            if (apiInterface == null) {
                apiInterface = Retrofit.Builder().baseUrl(ApiConstants.BASE_URL)
                    .client(httpClient!!)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(ApiInterface::class.java)
            }

            return apiInterface!!
        }

    private fun initOkHttp() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val cacheSize: Long = 10 * 1024 * 1024
        val cache = Cache(context.cacheDir, cacheSize)

        val httpBuilder = OkHttpClient().newBuilder()
            .cache(cache)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor).addInterceptor { chain ->
                val originalResponse: Response = chain.proceed(chain.request())
                val maxAge: Int
                if (NetworkProvider.isConnected(context)) {
                    maxAge = 60 // NOTE : Read cache for 1 minute
                    originalResponse.newBuilder().header("Cache-Control", "public, max-age=$maxAge")
                        .build()
                } else {
                    maxAge = 3600 * 24 * 28 // NOTE : Read cache for 4 weeks
                    originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-age=$maxAge")
                        .build()
                }
            }

        httpClient = httpBuilder.build()
    }

    companion object {
        private const val REQUEST_TIMEOUT = 30L
    }

}