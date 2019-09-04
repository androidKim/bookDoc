package com.midasgo.bookdoc.structure

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
note
 */
data class note (@SerializedName("id") var id: Int,
                 @SerializedName("book_name") var book_name:String,
                 @SerializedName("title") var title:String,
                 @SerializedName("content") var content: String,
                 @Expose(deserialize = false, serialize = false) var book_id:Int,
                 @SerializedName("image") var image:String,
                 @SerializedName("reg_date") var reg_date:String)
{

}
