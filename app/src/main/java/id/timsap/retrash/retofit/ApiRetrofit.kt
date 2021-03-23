package id.timsap.retrash.retofit


import id.timsap.retrash.ModelType
import id.timsap.retrash.model.Prediction
import id.timsap.retrash.model.Travel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface ApiRetrofit {
 @GET("retrash/{id}")
 suspend fun getData(@Path("id") id: String?): Call<ModelType>

 @GET("retrash/category/{id}")
 fun getCategory(@Path("id") id: String?):Call<List<Travel>>

 @FormUrlEncoded
 @POST("api/image-classifier-b64")
 fun postFoto(
  @FieldMap params: HashMap<String, String>
 ): Call<Prediction>

}