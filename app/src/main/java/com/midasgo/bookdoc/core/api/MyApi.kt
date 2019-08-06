package com.midasgo.bookdoc.core.api

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MyApi {
    companion object {
        //server info..
        val SERVER_HOST: String = "https://"
        val KAKAO_HOST:String = "https://dapi.kakao.com"
    }

    //
    /*
     test api
     */
    @GET("/test")
    fun test(@Header("temp") temp: req_test): Observable<Response<res_base>>


    /*
    kakao 책 검색 rest api

    query	검색을 원하는 질의어	O	String
    sort	결과 문서 정렬 방식	X (accuracy)	accuracy (정확도순) or latest (최신순)
    page	결과 페이지 번호	X(기본 1)	1-100 사이 Integer
    size	한 페이지에 보여질 문서의 개수	X(기본 10)	1-50 사이 Integer
    target	검색 필드 제한	X	title (제목에서 검색) or isbn (ISBN에서 검색) or publisher (출판사에서 검색) or person(인명에서 검색)
     */
    @GET("/v3/search/book")
    fun search_book(@Header("Authorization") Authorization:String,
                    @Query("query") query:String,
                    @Query("sort") sort:String,
                    @Query("page") page:Int,
                    @Query("size") size:Int,
                    @Query("target") target:String
                    ): Observable<Response<res_kakao_book>>
}