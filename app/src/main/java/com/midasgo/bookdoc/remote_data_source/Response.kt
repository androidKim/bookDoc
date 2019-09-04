package com.midasgo.bookdoc.remote_data_source

import com.google.gson.annotations.SerializedName
import com.midasgo.bookdoc.structure.documents
import com.midasgo.bookdoc.structure.meta
import com.midasgo.bookdoc.structure.note


data class res_base (@SerializedName("code") val code: Int,
                     @SerializedName("message") val message: String)

/*
kakao 책검색 결과
 */
data class res_kakao_book (@SerializedName("documents") val documents: ArrayList<documents>,
                           @SerializedName("meta") val meta: meta)

//note list
data class res_note_list (@SerializedName("list") val list: List<note>)
