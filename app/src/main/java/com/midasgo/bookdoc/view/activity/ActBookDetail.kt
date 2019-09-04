package com.midasgo.bookdoc.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.jakewharton.rxbinding3.view.clicks
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.com.midasgo.bookdoc.util.Util
import com.midasgo.bookdoc.model.entity.BookEntity
import com.midasgo.bookdoc.model.entity.NoteEntity
import com.midasgo.bookdoc.structure.book
import com.midasgo.bookdoc.view.custom.dialog.MessageDialogYn
import com.midasgo.bookdoc.viewmodel.BookViewModel
import com.midasgo.bookdoc.viewmodel.NoteViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.act_book_detail.*
import java.util.concurrent.TimeUnit


/*
액티비티 기본파일
 */
class ActBookDetail : AppCompatActivity(), MessageDialogYn.ifCallback {
    /********************* delete dialog callback *********************/
    override fun onClickOK() {
        //delete ok
        var viewModel: BookViewModel? = ViewModelProviders.of(this).get(BookViewModel::class.java)
        viewModel?.delete(BookEntity.getEntity(mBookInfo))
        setResult(Constant.FOR_RESULT_DELETE)
        finish()
    }

    override fun onClickCancel() {
        //delete cancel
    }
    /********************* define *********************/

    /********************* member *********************/
    //private lateinit var binding: ActBookDetailBinding
    var mApp: MyApp? = null
    var mContext: Context? = null
    var mRequestManager: RequestManager? = null
    var mBookInfo: book = book()

    /********************* controller *********************/

    /********************* system function *********************/
    /****************** system function ******************/
    //---------------------------------------------------
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = DataBindingUtil.setContentView(this, R.layout.act_book_detail)
        setContentView(R.layout.act_book_detail)
       
        mContext = this
        mApp = application as MyApp
        if(mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        mRequestManager = Glide.with(mContext!!)

        initValue()
        recvIntentData()
        initLayout()
    }
    //------------------------------------------------------
    //
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Constant.FOR_RESULT_NOTE_CHANGE)
        {
            checkNoteData()
        }
    }
    /****************** toolbar menu ******************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home->{//메뉴버튼

            }
            R.id.menu_modify->{//수정
                var pIntent:Intent = Intent(mContext!!, ActModifyBook::class.java)
                pIntent.putExtra(Constant.INTENT_DATA_BOOK_INFO, mBookInfo)//parcelable
                startActivity(pIntent)

            }
            R.id.menu_delete->{//삭제
                mApp!!.showMessageDialogYn(mContext!!,
                    this,
                    mContext!!.resources.getString(R.string.dialog_delete_msg),
                    mContext!!.resources.getString(R.string.ok),
                    mContext!!.resources.getString(R.string.cancel))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /****************** user function ******************/
    //---------------------------------------------------
    //
    fun initValue(){

    }
    //---------------------------------------------------
    //
    fun recvIntentData(){
        mBookInfo = intent.getParcelableExtra(Constant.INTENT_DATA_BOOK_INFO)
    }
    //---------------------------------------------------
    //
    fun initLayout(){
        setSupportActionBar(toolbar)

        //
        if(mBookInfo != null)
        {
            tvTitle.text = mBookInfo.title
            tvDesc.text = mBookInfo.desc
            tvReadPage.text = mBookInfo.read_page
            tvTotalPage.text = mBookInfo.total_page
            tvDate.text = Util.getDateFromTimestamp(mBookInfo.reg_date.toLong(), "yyyy-MM-dd")


            //thumnail..
            mRequestManager!!
                .load(mBookInfo.img_url)
                .listener(object : RequestListener<Drawable>
                {
                    override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean
                    {
                        return false
                    }
                    override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean
                    {
                        return false
                    }
                })
                .into(ivThumbnail)

            checkNoteData()
        }

        setEventListener()
    }

    fun checkNoteData(){
        //해당책의 노트가 작성된것이 있는지 확인
        Thread(Runnable{
            val viewModel: NoteViewModel? = ViewModelProviders.of(this).get(NoteViewModel::class.java)
            val item: NoteEntity = viewModel!!.selectItem(mBookInfo.id)
            runOnUiThread {
                if(item.id > 0)//item exist
                {
                    btnWriteNote.visibility = View.GONE
                    btnViewNote.visibility = View.VISIBLE
                }
                else//not exist
                {
                    btnWriteNote.visibility = View.VISIBLE
                    btnViewNote.visibility = View.GONE
                }
            }
        }).start()
    }
    //---------------------------------------------------
    //
    fun setEventListener(){
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //rxbinding
        //노트작성
        btnWriteNote.clicks()
                .debounce(200, TimeUnit.MILLISECONDS)//중복클릭 방지
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    var pIntent = Intent(this!!, ActNoteWrite::class.java)
                    pIntent.putExtra(Constant.INTENT_DATA_BOOK_INFO, mBookInfo)//parcelable
                    startActivityForResult(pIntent, 0)
                })

        //노트보기
        btnViewNote.clicks()
            .debounce(200, TimeUnit.MILLISECONDS)//중복클릭 방지
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var pIntent = Intent(this!!, NoteDetailActivity::class.java)
                pIntent.putExtra(Constant.INTENT_DATA_BOOK_INFO, mBookInfo)
                startActivityForResult(pIntent, 0)
            })
    }

}
