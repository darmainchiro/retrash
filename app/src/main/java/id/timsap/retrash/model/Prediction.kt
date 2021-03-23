package id.timsap.retrash.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Prediction(

	@field:SerializedName("prediction")
	val prediction: String? = null
) : Parcelable
