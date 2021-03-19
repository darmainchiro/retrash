package id.timsap.retrash
import com.google.gson.annotations.SerializedName
import id.timsap.retrash.model.Travel

data class ModelType(
    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("travel")
    val travel: Travel? = null
)


