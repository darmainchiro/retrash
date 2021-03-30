package id.timsap.retrash.ui.dashboardactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import id.timsap.retrash.R
import id.timsap.retrash.model.LatLang
import id.timsap.retrash.model.ModelPengepul
import id.timsap.retrash.ui.mainactivity.MainActivity
import id.timsap.retrash.ui.mapsactivity.MapsActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()
        btnToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

//            val model = ModelPengepul("2","Jajang","081111111", LatLang(12.11,11.3))
//            FirebaseDatabase.getInstance().reference.child("RETRASH").child("DataPengepul").push().setValue(model)
        }
        txtJual.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}