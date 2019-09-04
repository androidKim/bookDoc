package com.midasgo.bookdoc.view.adapter


import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.structure.book
import com.midasgo.bookdoc.model.entity.WishEntity


class WishListRvAdapter(val context: Context, val activity: Activity, val requestManager:RequestManager, var bookList: ArrayList<book>, var pCallbakc:ifCallback) : RecyclerView.Adapter<WishListRvAdapter.ViewHolder>()
{
    /*********************** System Function ***********************/
    //-----------------------------------------------------------
    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.row_wish, parent, false)
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

        holder.lyBase!!.tag = pInfo
        holder.lyBase!!.setOnClickListener {
            //item click
        }

        //item delete
        holder.btnDelete.setOnClickListener {
            pCallbakc.deleteItem(pInfo)//db delete

            bookList.removeAt(position)
            notifyDataSetChanged()
        }
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
    fun addEntity(entity: WishEntity){
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
        var btnDelete:ImageButton
        init {
            lyBase = itemView?.findViewById<RelativeLayout>(R.id.lyBase)
            ivThumbnail = itemView?.findViewById<ImageView>(R.id.ivThumbnail)
            tvTitle = itemView?.findViewById<TextView>(R.id.tvTitle)
            btnDelete = itemView?.findViewById<ImageButton>(R.id.btnDelete)

        }
    }

    /*********************** interface ***********************/
    interface ifCallback
    {
        fun deleteItem(item: book)
    }
}