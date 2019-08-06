package com.midasgo.bookdoc.ui.act

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.com.midasgo.bookdoc.util.Util
import com.midasgo.bookdoc.core.model.book
import com.midasgo.bookdoc.core.room.db_viewmodel.BookDbViewModel
import com.midasgo.bookdoc.core.room.entity.BookEntity
import com.midasgo.bookdoc.core.viewmodel.BookViewModel
import com.midasgo.bookdoc.databinding.ActAddBookBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.act_book_detail.*
import java.util.*
import java.util.concurrent.TimeUnit


/*
책 추가화면
 */
class ActAddBook : AppCompatActivity() {
    /****************** member ******************/
    private lateinit var binding: ActAddBookBinding
    private var mContext:Context ?= null
    private var mApp: MyApp?= null
    private lateinit var mBook: book
    /****************** system function ******************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.act_add_book)

        // 뷰 모델 초기화 및 관찰자 연결
        var bookViewModel = ViewModelProviders.of(this).get(BookViewModel::class.java)
        // 데이터 바인딩에 뷰 모델 연결
        binding.bookViewModel = bookViewModel
        // 데이터 바인딩에 LifecycleOwner 연결하여 liveData 를 DataBinding 과 사용 할 수 있게 함
        binding.lifecycleOwner = this

        mContext = this
        mApp = application as MyApp
        if(mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        initValue()
        recvIntentData()
        initLayout()
    }
    /****************** user funtion ******************/
    //-------------------------------------------
    //
    private fun initValue(){
        mBook = book()
    }
    //-------------------------------------------
    //
    private fun recvIntentData(){
        if(intent.hasExtra(Constant.INTENT_DATA_BOOK_INFO))
            mBook = intent.getParcelableExtra(Constant.INTENT_DATA_BOOK_INFO)//api검색후 전달된 객체
    }
    //-------------------------------------------
    //
    private fun initLayout() {
        if(mBook.title != null)
            binding.bookViewModel!!.changeTitle(mBook.title)//viewmodel livadata 변경하여 뷰갱신

        if(mBook.desc != null)
            binding.bookViewModel!!.changeDesc(mBook.desc)

        setEventListener()
        setRxBinding()
    }
    //---------------------------------------------------
    //
    fun setEventListener(){
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    //-------------------------------------------
    //
    private fun setRxBinding(){
        //RxBinding UIEvent, 뷰에서 변경시 즉시 모델 동기화
        binding.editTitle
            .textChanges()
            .subscribe {
                mBook.title = it.toString()
            }

        binding.editDesc
            .textChanges()
            .subscribe(){
                mBook.desc = it.toString()
            }

        binding.editReadPage
            .textChanges()
            .subscribe() {
                mBook.read_page = it.toString()
            }

        binding.editTotalPage
            .textChanges()
            .subscribe() {
                mBook.total_page =it.toString()
            }

        //완료이벤트
        binding.btnComplete.clicks()
            .debounce(500, TimeUnit.MILLISECONDS)//중복클릭 방지
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(checkValidation()){
                    var dbViewModel:BookDbViewModel = ViewModelProviders.of(this).get(BookDbViewModel::class.java)
                    dbViewModel.insert(BookEntity.getEntity(mBook))
                    mApp!!.goMain(mContext!!)
                }
                else
                {

                }
            })
    }
    //-------------------------------------------
    //책 데이터 입력 값 유효성검사
    private fun checkValidation():Boolean{
        var bResult:Boolean = false
        if(mBook.title.equals(""))
        {
            Toast.makeText(mContext!!, mContext!!.resources.getString(R.string.input_title_msg), Toast.LENGTH_SHORT).show()
        }
        else if(mBook.desc.equals("")){
            Toast.makeText(mContext!!, mContext!!.resources.getString(R.string.input_desc_msg), Toast.LENGTH_SHORT).show()
        }
        else if(mBook.read_page.equals(""))
        {
            Toast.makeText(mContext!!, mContext!!.resources.getString(R.string.input_read_page_msg), Toast.LENGTH_SHORT).show()
        }
        else if(mBook.total_page.equals(""))
        {
            Toast.makeText(mContext!!, mContext!!.resources.getString(R.string.input_total_page_msg), Toast.LENGTH_SHORT).show()
        }
        else{
            bResult = true
        }

        var date: Date = Util.getDateFromDatePicker(binding.datePicker)
        mBook.reg_date = String.format("%s",date.time)
        return bResult
    }
}
