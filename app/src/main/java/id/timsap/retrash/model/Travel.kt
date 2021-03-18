package id.timsap.retrash.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Travel (
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("id_type")
    val id_type: Int? = null,

    @field:SerializedName("name_solusi")
    val name_solusi: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null,

    @field:SerializedName("updated_at")
    val updated_at: String? = null,

    @field:SerializedName("category")
    val category: String? = null
):Parcelable
