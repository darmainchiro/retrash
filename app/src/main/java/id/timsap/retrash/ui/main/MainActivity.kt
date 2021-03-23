package id.timsap.retrash.ui.main

import android.R.attr
import android.R.attr.bitmap
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import id.timsap.retrash.R
import id.timsap.retrash.model.Travel
import id.timsap.retrash.retofit.Network
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


val REQUEST_IMAGE_CAPTURE = 101
private const val REQUEST_CODE = 42
const val FILE_NAME = "photo.jpg"
lateinit var photoFile: File
var selectedPhotoUri: Uri? = null

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgMainActivity.setImageResource(R.drawable.ic_launcher_foreground)
        BottomSheetBehavior.from(design_bottom_sheet).apply {
            peekHeight = 300
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        GlobalScope.async {
            setUpCamera()
        }

        //mengambil data rekomendasi


    }

    fun getDataRekomendasi() {
        simmer_view.startShimmer()

        Network().getService().getCategory("1").enqueue(object : Callback<List<Travel>> {

            override fun onResponse(call: Call<List<Travel>>, response: Response<List<Travel>>) {
                Log.d("Data", response.body().toString())
                Log.d("Data", "Berhasil")
                recyclerViewMain.layoutManager = LinearLayoutManager(MainActivity())
                recyclerViewMain.adapter = MainAdapter(this@MainActivity, response.body())
                simmer_view.stopShimmer()
                simmer_view.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Travel>>, t: Throwable) {
                Log.d("Data", t.localizedMessage)
            }
        })

    }

    fun setUpCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotFile(FILE_NAME)

//    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
        val fileProvider =
            FileProvider.getUriForFile(this, "edu.stanford,rkpandey.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {

            startActivityForResult(takePictureIntent, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhotFile(fileName: String): File {
        val stroageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", stroageDir)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri: Uri
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val bitmap: Bitmap
            selectedPhotoUri = data?.data
            Log.d("BitMap", selectedPhotoUri.toString())

            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

            imgMainActivity.visibility = View.VISIBLE
            Log.d("BitMap", takenImage.toString())
            imgMainActivity.setImageBitmap(takenImage)
            getDataRekomendasi()
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