package com.midasgo.bookdoc.structure

import com.google.gson.annotations.SerializedName

/*
total_count	전체 검색된 문서수	Integer
pageable_count	검색결과로 제공 가능한 문서수	Integer
is_end	현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음	Boolean
 */
data class meta (@SerializedName("total_count") val total_count: Int,
                 @SerializedName("pageable_count") val pageable_count: Int,
                 @SerializedName("is_end") val is_end: Boolean)

