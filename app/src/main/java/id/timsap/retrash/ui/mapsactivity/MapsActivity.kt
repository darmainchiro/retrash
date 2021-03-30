package id.timsap.retrash.ui.mapsactivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.timsap.retrash.R
import id.timsap.retrash.model.ModelPengepul
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.GlobalScope
import java.net.URLEncoder


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myMarker: Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        idImgBackMap.setOnClickListener {
            finish()
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera

            FirebaseDatabase.getInstance().reference.child("RETRASH").child("DataPengepul")
                .addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        mMap.clear()
                        Log.d(
                            "firebasedata",
                            snapshot.children.count().toString() + snapshot.value.toString()
                        )
                        snapshot.children.forEach {
                            val dataPengpul =
                                it.getValue(ModelPengepul::class.java) as ModelPengepul
                            val latitude = dataPengpul.latlang.latitude
                            val longitude = dataPengpul.latlang.longitude
                           myMarker = mMap.addMarker(
                                MarkerOptions().position(LatLng(latitude, longitude))
                                    .title(dataPengpul.nama_pengepul).snippet(dataPengpul.nomorHp)
                            )
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
                        }
                        if (ActivityCompat.checkSelfPermission(
                                this@MapsActivity,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                this@MapsActivity,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }
                        mMap.isMyLocationEnabled = true
                        fusedLocationClient =
                            LocationServices.getFusedLocationProviderClient(applicationContext)
                        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        loc.latitude,
                                        loc.longitude
                                    ), 15.0f
                                )
                            )
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("Error Firebase", error.message)
                    }
                })


        mMap.setOnMarkerClickListener {
            val url="https://api.whatsapp.com/send?phone="+it.snippet+"&text=" + URLEncoder.encode("Permisi "+it.title+", Saya Mau Jual barang bekas", "UTF-8")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
           return@setOnMarkerClickListener true

            }
        }
    }

