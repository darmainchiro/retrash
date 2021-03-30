package id.timsap.retrash.ui.detailactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.Picasso
import id.timsap.retrash.ModelType
import id.timsap.retrash.R
import id.timsap.retrash.model.Travel
import id.timsap.retrash.retofit.Network
import id.timsap.retrash.ui.mainactivity.MainAdapter
import id.timsap.retrash.ui.mapsactivity.MapsActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    var travel: Travel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        travel = intent.getParcelableExtra<Travel>(MainAdapter.TAG)

        txtDetailNama.text = travel?.name_solusi
        txtDetailDescription.text = travel?.description
        idImgBack.setOnClickListener {
            finish()
        }
        Picasso.get().load(travel?.gambar)
            .into(ID_Detail_Img)
        imageView2.setOnClickListener {
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }
    }


    fun getDataDetail() {
        GlobalScope.async {
            Network().getServiceData().getData("1").enqueue(object : Callback<ModelType> {
                override fun onResponse(
                    call: Call<ModelType>,
                    response: Response<ModelType>
                ) {
                    Log.d("Data2", response.body().toString())
                    Log.d("Data2", "Berhasil")
                }

                override fun onFailure(call: Call<ModelType>, t: Throwable) {
                    Log.d("Data2", t.localizedMessage)
                }
            })
        }

    }

}