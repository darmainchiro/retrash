package id.timsap.retrash.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import id.timsap.retrash.R
import id.timsap.retrash.model.Travel
import id.timsap.retrash.retofit.Network
import id.timsap.retrash.ui.main.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val REQUEST_IMAGE_CAPTURE = 101

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.async {
            setUpCamera() }

        //mengambil data rekomendasi
        GlobalScope.async {
            getDataRekomendasi()
        }

    }

    fun getDataRekomendasi() {
        Network().getService().getCategory("1").enqueue(object : Callback<List<Travel>> {
            override fun onResponse(call: Call<List<Travel>>, response: Response<List<Travel>>) {
                Log.d("Data", response.body().toString())
                Log.d("Data", "Berhasil")
                recyclerViewMain.layoutManager = LinearLayoutManager(MainActivity())
                recyclerViewMain.adapter = MainAdapter(this@MainActivity, response.body())
            }

            override fun onFailure(call: Call<List<Travel>>, t: Throwable) {
                Log.d("Data", t.localizedMessage)
            }
        })
    }

    private fun setUpCamera() {
        BottomSheetBehavior.from(design_bottom_sheet).apply {
            peekHeight = 300
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        imgMainActivity.visibility = View.GONE
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_IMAGE_CAPTURE
            )
        } else {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            var pic: Bitmap? = data?.getParcelableExtra("data")
            imgMainActivity.visibility = View.VISIBLE
            imgMainActivity.setImageBitmap(pic)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}