package id.timsap.retrash.retofit


import id.timsap.retrash.model.Travel
import id.timsap.retrash.ModelType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiRetrofit {
 @GET("retrash/{id}" )
 suspend fun getData(@Path("id") id: String?): Call<ModelType>

 @GET("retrash/category/{id}")
 fun getCategory(@Path("id") id: String?):Call<List<Travel>>
}