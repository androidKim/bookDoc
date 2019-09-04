package com.midasgo.bookdoc.structure

import com.google.gson.annotations.SerializedName

/*
kakao book data 클래스

title	도서 제목	String
contents	도서 소개	String
url	도서 상세 URL	String
isbn	국제 표준 도서번호(ISBN10 ISBN13) (ISBN10,ISBN13 중 하나 이상 존재하며, ' '(공백)을 구분자로 출력됩니다)	String
datetime	도서 출판날짜. ISO 8601. [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]	String
authors	도서 저자 리스트	Array of String
publisher	도서 출판사	String
translators	도서 번역자 리스트	Array of String
price	도서 정가	Integer
sale_price	도서 판매가	Integer
thumbnail	도서 표지 썸네일 URL	String
status	도서 판매 상태 정보(정상, 품절, 절판 등), 상황에 따라 변동 가능성이 있으므로 문자열 처리 지양, 단순 노출요소로 활용을 권장합니다.	String
 */
data class documents (@SerializedName("title") val title: String,
                      @SerializedName("contents") val contents: String,
                      @SerializedName("url") val url: String,
                      @SerializedName("isbn") val isbn: String,
                      @SerializedName("datetime") val datetime: String,
                      @SerializedName("authors") val authors: ArrayList<String>,
                      @SerializedName("publisher") val publisher: String,
                      @SerializedName("translators") val translators: ArrayList<String>,
                      @SerializedName("price") val price: Int,
                      @SerializedName("sale_price") val sale_price: Int,
                      @SerializedName("thumbnail") val thumbnail: String,
                      @SerializedName("status") val status: String)
{

}
