package com.midasgo.bookdoc.view.adapter


import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.structure.reply


class ReplyListRvAdapter(val context: Context, val activity: Activity, val requestManager:RequestManager, var list: ArrayList<reply>) : RecyclerView.Adapter<ReplyListRvAdapter.ViewHolder>()
{
    /*********************** System Function ***********************/
    //-----------------------------------------------------------
    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.row_reply, parent, false)
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
        var pInfo: reply = list[position]

        holder.tvContent.text = pInfo.content
        holder.tvRegDate.text = pInfo.reg_date

        holder.lyBase!!.tag = pInfo
        holder.lyBase!!.setOnClickListener {
            //item click
        }
    }

    /*********************** User Function ***********************/
    //-----------------------------------------------------------
    //
    fun clearData(){
        this.list.clear()
        notifyDataSetChanged()
    }

    fun refreshData(array:ArrayList<reply>) {
        this.list.clear()
        this.list.addAll(array)
        notifyDataSetChanged()
    }

    /*********************** viewholder ***********************/
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lyBase:LinearLayout
        var tvContent: TextView
        var tvRegDate: TextView

        init {
            lyBase = itemView?.findViewById<LinearLayout>(R.id.lyBase)
            tvContent = itemView?.findViewById<TextView>(R.id.tvContent)
            tvRegDate = itemView?.findViewById<TextView>(R.id.tvRegDate)
        }
    }
}