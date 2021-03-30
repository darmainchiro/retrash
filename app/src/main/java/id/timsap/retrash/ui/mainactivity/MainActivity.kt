package id.timsap.retrash.ui.mainactivity


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import id.timsap.retrash.R
import id.timsap.retrash.model.Prediction
import id.timsap.retrash.model.Travel
import id.timsap.retrash.retofit.Network
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

private const val REQUEST_CODE = 42
const val FILE_NAME = "photo.jpg"
lateinit var photoFile: File
private var postPath: String? = null
private var encode_image = "0"



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgMainActivity.setImageResource(R.drawable.ic_launcher_foreground)
        Id_Layout_Bottom.visibility = View.GONE
//        getDataRekomendasi("plastic")
        txtTap.setOnClickListener {
            setUpCamera()
        }
        BottomSheetBehavior.from(design_bottom_sheet).apply {
            peekHeight = 300
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        imgMainActivity.visibility = View.GONE

        setUpCamera()
    }

    fun getDataRekomendasi(category: String) {
        simmer_view.startShimmer()

        Network().getServiceData().getCategory(category).enqueue(object : Callback<List<Travel>> {

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

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            txtHasil.text = "Loading.."
            BottomSheetBehavior.from(design_bottom_sheet).apply {
                peekHeight = 300
                this.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            recyclerViewMain.adapter =null
            simmer_view.startShimmer()
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            imgMainActivity.visibility = View.VISIBLE
            Id_Layout_Bottom.visibility = View.VISIBLE
            imgMainActivity.setImageBitmap(takenImage)
            encode_image = encodeTobase64(takenImage)
            uploadFile()

        } else {
            Log.d("Request", "Fail Request")
        }
    }

    private fun uploadFile() {
        val params = HashMap<String, String>()
        params["file"] = encode_image
        Log.d("ResponseData", "Try To send Data")
        GlobalScope.async {
            Network().getServiceCamera().postFoto(params).enqueue(object :
                Callback<Prediction> {
                override fun onResponse(
                    call: Call<Prediction>,
                    response: Response<Prediction>
                ) {
                    if (response.body() != null) {
                        val serverResponse = response.body()
                        txtHasil.text = serverResponse?.prediction
                        txtHasil.isAllCaps=true
                        Log.d("ResponseData", response.body()!!.prediction.toString())
                        getDataRekomendasi(response.body()!!.prediction!!)
                        Log.d("ResponseData", "Berhasil")
                    } else {
                        Log.d("ResponseData", "Belum Berhasil")
                    }
                }

                override fun onFailure(call: Call<Prediction>, t: Throwable) {
                    Log.d("ResponseDataFailure", t.localizedMessage.toString())

                }
            })
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun encodeTobase64(image: Bitmap): String {
        val imageEncoded: String
        val baos = ByteArrayOutputStream()
        Log.d("imageBitmap", image.width.toString())
        val reduce = reduceBitmapSize(image, 10000)
        Log.d("imageBitmapReduce", reduce!!.width.toString())
        reduce.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        Bitmap.createScaledBitmap(reduce, 250, 600, false)
        val byte_arr = baos.toByteArray()

        // Encode Image to String
        imageEncoded = Base64.encodeToString(byte_arr, 0)
        return imageEncoded
    }

    fun reduceBitmapSize(bitmap: Bitmap, MAX_SIZE: Int): Bitmap? {
        val ratioSquare: Double
        val bitmapHeight: Int
        val bitmapWidth: Int
        bitmapHeight = bitmap.height
        bitmapWidth = bitmap.width
        ratioSquare = (bitmapHeight * bitmapWidth / MAX_SIZE).toDouble()
        if (ratioSquare <= 1) return bitmap
        val ratio = Math.sqrt(ratioSquare)
        Log.d("mylog", "Ratio: $ratio")
        val requiredHeight = Math.round(bitmapHeight / ratio).toInt()
        val requiredWidth = Math.round(bitmapWidth / ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
    }
}

