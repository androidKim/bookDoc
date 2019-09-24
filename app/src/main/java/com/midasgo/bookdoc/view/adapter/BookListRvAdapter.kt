package com.midasgo.bookdoc.view.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.jakewharton.rxbinding3.view.clicks
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.com.midasgo.bookdoc.util.Util
import com.midasgo.bookdoc.model.entity.BookEntity
import com.midasgo.bookdoc.structure.book
import com.midasgo.bookdoc.view.activity.ActBookDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class BookListRvAdapter(val context: Context, val activity: Activity, val requestManager:RequestManager, var bookList: ArrayList<book>) : RecyclerView.Adapter<BookListRvAdapter.ViewHolder>()
{
    /*********************** System Function ***********************/
    //-----------------------------------------------------------
    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.row_book, parent, false)
        val holder:ViewHolder = ViewHolder(view)
        return holder
    }

    //-----------------------------------------------------------
    //
    override fun getItemCount(): Int
    {
        return bookList.size
    }
    //-----------------------------------------------------------
    //
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        var pInfo: book = bookList[position]

        holder.tvTitle.text = pInfo.title
        holder.tvPage.text = String.format("%s / %s", pInfo.read_page, pInfo.total_page)
        holder.tvDate.text = Util.getDateFromTimestamp(pInfo.reg_date.toLong(), "yyyy-MM-dd")

        //thumnail..
        requestManager!!
            .load(pInfo.img_url)
            .listener(object : RequestListener<Drawable>
            {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean
                {
                    return false
                }
                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean
                {
                    return false
                }
            })
            .into(holder.ivThumbnail)

        //진행률
        holder.progressBar.max = Integer.parseInt(pInfo.total_page)
        holder.progressBar.progress = Integer.parseInt(pInfo.read_page)


        holder.lyBase!!.tag = pInfo
        holder.lyBase!!.clicks()//rxBinding
                .debounce(200, TimeUnit.MILLISECONDS)//중복클릭 방지
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //item click
                    var pIntent:Intent = Intent(context!!, ActBookDetail::class.java)
                    pIntent.putExtra(Constant.INTENT_DATA_BOOK_INFO, pInfo)//parcelable
                    startActivityForResult(activity, pIntent, 0, null)
                })
    }

    /*********************** User Function ***********************/
    //-----------------------------------------------------------
    //
    fun addItem(pInfo: book)
    {
        if(pInfo == null)
            return

        this.bookList.add(pInfo)
    }
    //-----------------------------------------------------------
    //
    fun addEntity(entity: BookEntity){
        if(entity == null)
            return

        var pInfo: book = book()
        pInfo.id = entity.id
        pInfo.title = entity.title
        pInfo.desc = entity.desc
        pInfo.read_page = entity.read_page
        pInfo.total_page = entity.total_page
        pInfo.reg_date = entity.reg_date
        pInfo.img_url = entity.img_url
        this.addItem(pInfo)
    }
    //-----------------------------------------------------------
    //
    fun addList(pArray:ArrayList<book>)
    {
        if(pArray == null)
            return


        this.bookList.addAll(pArray)
    }
    //-----------------------------------------------------------
    //
    fun clearData(){
        this.bookList.clear()
        notifyDataSetChanged()
    }
    /*********************** viewholder ***********************/
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lyBase:RelativeLayout
        var ivThumbnail:ImageView
        var tvTitle: TextView
        var tvPage: TextView
        var tvDate: TextView
        var progressBar:ProgressBar
        init {
            lyBase = itemView?.findViewById<RelativeLayout>(R.id.lyBase)
            ivThumbnail = itemView?.findViewById<ImageView>(R.id.ivThumbnail)
            tvTitle = itemView?.findViewById<TextView>(R.id.tvTitle)
            tvPage = itemView?.findViewById<TextView>(R.id.tvPage)
            tvDate = itemView?.findViewById<TextView>(R.id.tvDate)
            progressBar = itemView?.findViewById<ProgressBar>(R.id.progressBar)
        }
    }

    /*********************** interface ***********************/
    interface ifCallback
    {

    }
}