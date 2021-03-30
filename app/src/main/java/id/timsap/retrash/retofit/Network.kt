package id.timsap.retrash.retofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network {

    fun getInterceptor() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return  okHttpClient
    }
    fun getRetrofitCamera() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://136.243.47.204:11181/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofitData() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://136.243.47.204:11180/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getServiceCamera() = getRetrofitCamera().create(ApiRetrofit::class.java)
    fun getServiceData() = getRetrofitData().create(ApiRetrofit::class.java)

}

