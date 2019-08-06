package com.midasgo.bookdoc.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.core.model.temp


class TempRvAdapter(val context: Context, val requestManager:RequestManager, var tempList: ArrayList<temp>) : RecyclerView.Adapter<TempRvAdapter.ViewHolder>()
{
    /*********************** System Function ***********************/
    //-----------------------------------------------------------
    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.row_temp, parent, false)
        val holder:ViewHolder = ViewHolder(view)
        return holder
    }

    //-----------------------------------------------------------
    //
    override fun getItemCount(): Int
    {
        return tempList.size
    }
    //-----------------------------------------------------------
    //
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        var pInfo: temp = tempList[position]

        /*
        holder.lyBase!!.tag = pInfo
        holder.lyBase!!.setOnClickListener {
            //item click
            var pIntent = Intent(context!!, ActTempDetail::class.java)
            context!!.startActivity(pIntent)
        }
        */
    }

    /*********************** User Function ***********************/
    //-----------------------------------------------------------
    //
    fun addItem(pInfo:temp)
    {
        if(pInfo == null)
            return

        this.tempList.add(pInfo)
    }
    //-----------------------------------------------------------
    //
    fun addList(pArray:ArrayList<temp>)
    {
        if(pArray == null)
            return


        this.tempList.addAll(pArray)
    }
    /*********************** viewholder ***********************/
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTemp: TextView
        init {
            tvTemp = itemView?.findViewById<TextView>(R.id.tvTemp)
        }
    }

    /*********************** interface ***********************/
    interface ifCallback
    {

    }
}