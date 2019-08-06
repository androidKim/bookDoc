package com.midasgo.bookdoc.core.model

import android.os.Parcel
import android.os.Parcelable

/*
book data 클래스
 */
data class book (var seq: Long,
                 var title: String,
                 var desc: String,
                 var read_page: String,
                 var total_page: String,
                 var reg_date: String,
                 var img_url:String):Parcelable
{
    constructor():this(0,"","","","","", ""){

    }

    //parcel..
    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<book> {
            override fun createFromParcel(parcel: Parcel) = book(parcel)
            override fun newArray(size: Int) = arrayOfNulls<book>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        seq = parcel.readLong(),
        title = parcel.readString(),
        desc = parcel.readString(),
        read_page = parcel.readString(),
        total_page = parcel.readString(),
        reg_date = parcel.readString(),
        img_url = parcel.readString()
    )

    //Parcelable
    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeLong(seq)
        parcel?.writeString(title)
        parcel?.writeString(desc)
        parcel?.writeString(read_page)
        parcel?.writeString(total_page)
        parcel?.writeString(reg_date)
        parcel?.writeString(img_url)
    }

    override fun describeContents(): Int {
        return 0
    }
}
