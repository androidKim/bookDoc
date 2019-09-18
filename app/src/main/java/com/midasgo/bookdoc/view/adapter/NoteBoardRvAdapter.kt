package com.midasgo.bookdoc.view.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.structure.note
import com.midasgo.bookdoc.view.activity.BoardDetailActivity


class NoteBoardRvAdapter(val context: Context, val activity: Activity, val requestManager:RequestManager, var list: ArrayList<note>) : RecyclerView.Adapter<NoteBoardRvAdapter.ViewHolder>()
{
    /*********************** System Function ***********************/
    //-----------------------------------------------------------
    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.row_note, parent, false)
        val holder:ViewHolder = ViewHolder(view)
        return holder
    }

    //-----------------------------------------------------------
    //
    override fun getItemCount(): Int
    {
        return list.size
    }
    //-----------------------------------------------------------
    //
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        var pInfo: note = list[position]

        holder.tvBookName.text = pInfo.book_name
        holder.tvNoteTitle.text = pInfo.title
        holder.tvRegDate.text = pInfo.reg_date

        //thumnail..
        requestManager!!
            .load(pInfo.image)
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
            var pIntent:Intent = Intent(context!!, BoardDetailActivity::class.java)
            pIntent.putExtra(Constant.INTENT_DATA_BOARD_ID, pInfo.id)
            context.startActivity(pIntent)
        }
    }

    /*********************** User Function ***********************/
    //-----------------------------------------------------------
    //
    fun addItem(pInfo: note)
    {
        if(pInfo == null)
            return

        this.list.add(pInfo)
    }
    //-----------------------------------------------------------
    //
    fun addList(pArray:ArrayList<note>)
    {
        if(pArray == null)
            return


        this.list.addAll(pArray)
    }
    //-----------------------------------------------------------
    //
    fun clearData(){
        this.list.clear()
        notifyDataSetChanged()
    }
    /*********************** viewholder ***********************/
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lyBase:RelativeLayout
        var ivThumbnail:ImageView
        var tvBookName: TextView
        var tvNoteTitle: TextView
        var tvRegDate: TextView

        init {
            lyBase = itemView?.findViewById<RelativeLayout>(R.id.lyBase)
            ivThumbnail = itemView?.findViewById<ImageView>(R.id.ivThumbnail)
            tvBookName = itemView?.findViewById<TextView>(R.id.tvBookName)
            tvNoteTitle = itemView?.findViewById<TextView>(R.id.tvNoteTitle)
            tvRegDate = itemView?.findViewById<TextView>(R.id.tvRegDate)
        }
    }
}