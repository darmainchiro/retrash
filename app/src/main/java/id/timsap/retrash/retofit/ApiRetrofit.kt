package id.timsap.retrash.retofit


import id.timsap.retrash.ModelType
import id.timsap.retrash.model.Travel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiRetrofit {
 @GET("retrash/{id}")
 suspend fun getData(@Path("id") id: String?): Call<ModelType>

 @GET("retrash/category/{id}")
 fun getCategory(@Path("id") id: String?):Call<List<Travel>>


}