package com.midasgo.bookdoc.view.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.structure.book
import com.midasgo.bookdoc.structure.documents
import com.midasgo.bookdoc.model.entity.BookEntity
import com.midasgo.bookdoc.view.activity.ActAddBook


class SearchListRvAdapter(val context: Context, val activity: Activity, val requestManager:RequestManager, var bookList: ArrayList<documents>, val pCallback:ifCallback) : RecyclerView.Adapter<SearchListRvAdapter.ViewHolder>()
{
    /*********************** System Function ***********************/
    //-----------------------------------------------------------
    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.row_search_book, parent, false)
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
        var pInfo: documents = bookList[position]

        holder.tvTitle.text = pInfo.title
        holder.tvContents.text = pInfo.contents
        holder.tvAuthors.text = pInfo.authors.toString()

        //thumnail..
        requestManager!!
                .load(pInfo.thumbnail)
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
            //move book add
            var item: book = book()
            item.title = pInfo.title
            item.desc = pInfo.contents
            item.img_url = pInfo.thumbnail

            var pIntent: Intent = Intent(context!!, ActAddBook::class.java)
            pIntent.putExtra(Constant.INTENT_DATA_BOOK_INFO, item)//parcelable
            context!!.startActivity(pIntent)

        }

        //위시리스트 담기
        holder.tvAddWish.setOnClickListener {
            var item: book = book()
            item.title = pInfo.title
            item.desc = pInfo.contents
            item.img_url = pInfo.thumbnail
            pCallback.addWish(item)
        }
    }

    /*********************** User Function ***********************/
    //-----------------------------------------------------------
    //
    fun addItem(pInfo: documents)
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

    }
    //-----------------------------------------------------------
    //
    fun addList(pArray:ArrayList<documents>)
    {
        if(pArray == null)
            return


        this.bookList.addAll(pArray)
    }
    //-----------------------------------------------------------
    //
    fun setList(pArray:ArrayList<documents>){
        this.bookList.clear()
        this.bookList.addAll(pArray)
        notifyDataSetChanged()
    }
    //-----------------------------------------------------------
    //
    fun clearData(){
        this.bookList.clear()
        notifyDataSetChanged()
    }
    /*********************** viewholder ***********************/
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lyBase:ConstraintLayout
        var ivThumbnail:ImageView
        var tvTitle: TextView
        var tvContents: TextView
        var tvAuthors: TextView
        var tvAddWish:TextView
        init {
            lyBase = itemView?.findViewById<ConstraintLayout>(R.id.lyBase)
            ivThumbnail = itemView?.findViewById<ImageView>(R.id.ivThumbnail)
            tvTitle = itemView?.findViewById<TextView>(R.id.tvTitle)
            tvContents = itemView?.findViewById<TextView>(R.id.tvContents)
            tvAuthors = itemView?.findViewById<TextView>(R.id.tvAuthors)
            tvAddWish = itemView?.findViewById<TextView>(R.id.tvAddWish)
        }
    }

    /*********************** interface ***********************/
    interface ifCallback
    {
        fun addWish(item: book)
    }
}