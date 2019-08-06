package com.midasgo.bookdoc.core.api

import com.google.gson.annotations.SerializedName
import com.midasgo.bookdoc.core.model.documents
import com.midasgo.bookdoc.core.model.meta


data class res_base (
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String)

/*
kakao 책검색 결과
 */
data class res_kakao_book (
    @SerializedName("documents") val documents: ArrayList<documents>,
    @SerializedName("meta") val meta: meta
)