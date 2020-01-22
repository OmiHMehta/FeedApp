package com.telstra.feedapp.networkadapter.retrofit

import android.content.Context
import com.google.gson.GsonBuilder
import com.telstra.feedapp.networkadapter.api.request.ApiInterface
import com.telstra.feedapp.networkadapter.apiconstants.ApiConstants
import com.telstra.feedapp.utility.NetworkProvider
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class RetrofitClient(val context: Context) {

    private val TAG: String = RetrofitClient::class.java.simpleName

    private var apiInterface: ApiInterface? = null
    private var httpClient: OkHttpClient? = null

    private val HEADER_CACHE_CONTROL = "Cache-Control"

    companion object {
        private var context: Context? = null
        fun getInstance(context: Context) {
            if (context == null)
                this.context = context
        }
    }

    val apiClient: ApiInterface
        get() {
            if (httpClient == null) initOkHttp()

            if (apiInterface == null)
                apiInterface = Retrofit.Builder().baseUrl(ApiConstants.BASE_URL)
                    .client(httpClient!!)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(ApiInterface::class.java)

            return apiInterface!!
        }

    private fun initOkHttp() {
        httpClient = OkHttpClient().newBuilder()
            .cache(getCacheDir())
            .addInterceptor(httpLoggingInterceptor())
            .addNetworkInterceptor(networkInterceptor())
            .addInterceptor(offlineInterceptor())
            .build()
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                println("TAG --- $TAG --> log: http log: $it")
            })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    // TODO : Custom cache directory for app
    private fun getCacheDir(): Cache {
        val cacheDirectory = File(context.cacheDir, "offline_caching")
        val cacheSize: Long = 5 * 1024 * 1024
        return Cache(cacheDirectory, cacheSize)
    }

    // TODO : Use "networkInterceptor" ONLY when network is available
    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            println("TAG --- $TAG --> network interceptor : called.")
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(5, TimeUnit.SECONDS)
                .build()
            response.newBuilder()
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    // TODO : Use "offlineInterceptor" when network is available/not
    private fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            println("TAG --- $TAG --> offline interceptor : called.")
            var request = chain.request()

            // TODO : Prevent caching when network is on. For that we use the "networkInterceptor"
            if (!NetworkProvider.isConnected(context)) {

                // TODO : Cache data for 2 days
                val cacheControl = CacheControl.Builder()
                    .maxStale(2, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }
}