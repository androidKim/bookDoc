package com.midasgo.bookdoc.structure

import com.google.gson.annotations.SerializedName

/*
reply data class
 */
data class reply (@SerializedName("id") var id: Int,
                  @SerializedName("content") var content: String,
                  @SerializedName("note_id") var note_id:Int,
                  @SerializedName("reg_date") var reg_date:String)

