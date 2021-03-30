package id.timsap.retrash.model

class ModelPengepul(val id_pengepul : String,val nama_pengepul:String,val nomorHp:String,var latlang: LatLang) {
    constructor():this("","","",LatLang())
}

class LatLang(val latitude: Double,val longitude: Double){
    constructor():this(0.0,0.0)
}