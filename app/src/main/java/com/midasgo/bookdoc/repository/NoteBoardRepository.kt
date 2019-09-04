package com.midasgo.bookdoc.repository

import android.app.Application
import com.midasgo.bookdoc.remote_data_source.ApiProvider
import com.midasgo.bookdoc.structure.note

class NoteBoardRepository(application: Application)
{
    private var mWebCtrl:ApiProvider ?= null
    private var noteList:ArrayList<note>? = ArrayList()

    init {
        mWebCtrl = ApiProvider()
    }
}