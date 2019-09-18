package com.midasgo.bookdoc.remote_data_source

import com.google.gson.annotations.SerializedName
import com.midasgo.bookdoc.structure.documents
import com.midasgo.bookdoc.structure.meta
import com.midasgo.bookdoc.structure.note
import com.midasgo.bookdoc.structure.reply


data class res_base (@SerializedName("code") val code: Int,
                     @SerializedName("message") val message: String)

/*
kakao 책검색 결과
 */
data class res_kakao_book (@SerializedName("documents") val documents: ArrayList<documents>,
                           @SerializedName("meta") val meta: meta)

//note list
data class res_note_list (@SerializedName("list") val list: List<note>)

//note item
data class res_note_item(@SerializedName("note") val note: note)

//reply list
data class res_reply_list (@SerializedName("list") val list: List<reply>)
//reply count
data class res_reply_count(@SerializedName("count") val count:Int)