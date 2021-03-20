package id.timsap.retrash.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.Picasso
import id.timsap.retrash.ModelType
import id.timsap.retrash.R
import id.timsap.retrash.model.Travel
import id.timsap.retrash.retofit.Network
import id.timsap.retrash.ui.main.MainActivity
import id.timsap.retrash.ui.main.MainAdapter
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
        getDataDetail()
        txtDetailNama.text = travel?.name_solusi
        txtDetailDescription.text = travel?.description
        idImgBack.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        Picasso.get().load(travel?.gambar)
            .into(ID_Detail_Img)
    }


    fun getDataDetail() {
        GlobalScope.async {
            Network().getService().getData("1").enqueue(object : Callback<ModelType> {
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