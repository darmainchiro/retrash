package id.timsap.retrash.ui.mainactivity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.timsap.retrash.ui.detailactivity.DetailActivity
import id.timsap.retrash.model.Travel
import id.timsap.retrash.R
import kotlinx.android.synthetic.main.item.view.*

class MainAdapter(context: Context, val dataList: List<Travel>?) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {
companion object{
val TAG ="Data"
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val addItem = layoutInflater.inflate(R.layout.item, parent, false)
        return ViewHolder(
            addItem
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.txtNamaSolusi.text = dataList?.get(position)?.name_solusi.toString()
        holder.itemView.txtDescription.text = dataList?.get(position)?.description.toString()
        val datas = dataList?.get(position)
        val img = holder.itemView.ID_ImgAdapter
        Picasso.get().load(datas?.gambar).into(img)
        holder.dataInit(datas!!)

    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var data: Travel? = null

        init {
            view.setOnClickListener {
                val i = Intent(view.context, DetailActivity::class.java)
                i.putExtra(TAG, data)
                view.context.startActivity(i)

            }


        }

        fun dataInit(dataIntent: Travel) {
            data = dataIntent
        }
    }
}