package com.midasgo.bookdoc.view.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.databinding.ActNoteDetailBinding
import com.midasgo.bookdoc.model.entity.NoteEntity
import com.midasgo.bookdoc.remote_data_source.MyApi
import com.midasgo.bookdoc.structure.book
import com.midasgo.bookdoc.structure.note
import com.midasgo.bookdoc.view.custom.dialog.MessageDialogYn
import com.midasgo.bookdoc.viewmodel.NoteViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


/*
노트 보기
 */
class NoteDetailActivity : AppCompatActivity(), MessageDialogYn.ifCallback {
    /*
    MessageDialogYn callback..
     */
    override fun onClickOK() {
        deleteNoteProc()
    }

    override fun onClickCancel() {

    }
    /********************* member *********************/
    private lateinit var binding: ActNoteDetailBinding
    private val disposable = CompositeDisposable()
    var mApp: MyApp? = null
    var mContext: Context? = null
    var mRequestManager: RequestManager? = null
    lateinit var bookInfo:book
    /****************** system function ******************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.act_note_write)
        binding = DataBindingUtil.setContentView(this, R.layout.act_note_detail)

        mContext = this
        mApp = application as MyApp
        if (mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        mRequestManager = Glide.with(mContext!!)

        recvIntentData()
        initLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Activity 가 종료되면 사용중인 통신을 중단합니다.
        disposable.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Constant.FOR_RESULT_NOTE_CHANGE)
        {
            selectNoteData()
        }
    }

    /****************** toolbar menu ******************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_detail_menu, menu)
        var menuItem1: MenuItem = menu!!.findItem(R.id.menu_delete).setVisible(true)
        var menuItem2: MenuItem = menu!!.findItem(R.id.menu_modify).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {//메뉴버튼

            }
            R.id.menu_modify -> {//수정
                var pIntent: Intent = Intent(mContext!!, ActNoteWrite::class.java)
                pIntent.putExtra(Constant.INTENT_DATA_BOOK_INFO, bookInfo)
                pIntent.putExtra(Constant.INTENT_DATA_NOTE_TEXT, binding.tvNoteContents.text.toString())
                pIntent.putExtra(Constant.INTENT_DATA_NOTE_TITLE, binding.tvTitle.text.toString())
                startActivityForResult(pIntent, 0)

            }
            R.id.menu_delete -> {//삭제
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
    fun recvIntentData() {
        bookInfo = intent.getParcelableExtra(Constant.INTENT_DATA_BOOK_INFO)
    }

    fun initLayout() {
        setSupportActionBar(binding.toolbar)

        //toolbar back button event
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        selectNoteData()

        binding.btnUpload.setOnClickListener {

            val dialogBuilder:AlertDialog.Builder = AlertDialog.Builder(this)
            dialogBuilder.setTitle(mContext!!.resources.getString(R.string.dialog_title))
            dialogBuilder.setMessage(mContext!!.resources.getString(R.string.dialog_upload_note_msg))
                    .setCancelable(false)
                    .setPositiveButton(mContext!!.resources.getString(R.string.ok)){DialogInterface, i ->
                        //upload note
                        var book_name:String = bookInfo.title
                        var title:String = binding.tvTitle.text.toString()
                        var content: String = binding.tvNoteContents.text.toString()
                        var image:String = bookInfo.img_url
                        val param:note = note(0, book_name, title, content, 0, image, "")
                        uploadNoteProc(param)
                    }
                    .setNegativeButton(mContext!!.resources.getString(R.string.cancel)){DialogInterface, i ->

                    }
            dialogBuilder.show()
        }
    }

    fun selectNoteData() {
        val viewModel: NoteViewModel? = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        object :AsyncTask<Void,Void,Boolean>(){
            override fun doInBackground(vararg param: Void?): Boolean? {
                val item: NoteEntity = viewModel!!.selectItem(bookInfo.id)
                if (item.id > 0) {
                    runOnUiThread {
                        binding.tvTitle.text = item!!.title
                        binding.tvNoteContents.text = item!!.contents
                    }
                }
                return item.is_upload
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)

                if(result == true)
                {
                    binding.btnUpload.text = mContext!!.resources.getString(R.string.already_upload_note)
                    binding.btnUpload.isEnabled = false
                }

            }
        }.execute()
    }

    fun deleteNoteProc() {
        val viewModel: NoteViewModel? = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        object : AsyncTask<Void, Void, Void>() {
            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun doInBackground(vararg param: Void?): Void? {

                val noteEntity: NoteEntity = viewModel!!.selectItem(bookInfo.id)
                if (noteEntity.id > 0) {
                    viewModel!!.delete(noteEntity)
                }
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(mContext, mContext!!.resources.getText(R.string.toast_delete_note_complte_msg), Toast.LENGTH_LONG).show()
                setResult(Constant.FOR_RESULT_NOTE_CHANGE)
                finish()
            }
        }.execute()
    }

    fun uploadNoteProc(param: note){
        disposable.add(mApp!!.mWebCtrl!!.provideMyApi(MyApi.SERVER_HOST).setNoteItem(param)
                // REST API 를 통해 받은 응답에서 Result 값을 추출합니다.

                // 이 이후에 수행되는 코드는 모두 메인 스레드에서 실행합니다.
                .observeOn(AndroidSchedulers.mainThread())
                // 구독할 때 수행할 작업을 구현합니다.
                .doOnSubscribe {
                    binding.progressBar.visibility = View.VISIBLE
                }
                // 스트림이 종료될 때 수행할 작업을 구현합니다.
                .doOnTerminate {
                    binding.progressBar.visibility = View.GONE
                }

                // 옵서버블을 구독합니다.
                .subscribe({ it ->
                    // response.body 를 받아 처리합니다.
                    // 작업 중 오류가 발생하면 이 블록은 호출되지 않습니다.
                    if(it.code() == 200)
                    {
                        if(it.body() != null)
                        {
                            updateNoteStatus()//sqlite update

                            val dialogBuilder:AlertDialog.Builder = AlertDialog.Builder(this)
                            dialogBuilder.setTitle(mContext!!.resources.getString(R.string.dialog_title))
                            dialogBuilder.setMessage(mContext!!.resources.getString(R.string.upload_complte_msg))
                                .setCancelable(false)
                                .setPositiveButton(mContext!!.resources.getString(R.string.ok)){DialogInterface, i ->

                                }
                            dialogBuilder.show()
                        }
                    }
                }) {
                    // 에러블록
                    // 네트워크 오류나 데이터 처리 오류 등
                    // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
                    mApp!!.showMessageDialog(mContext!!, "${it.message}", mContext!!.resources.getString(R.string.ok))
                })
    }


    //---------------------------------------------------
    //
    fun updateNoteStatus(){
        var viewModel: NoteViewModel? = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        object :AsyncTask<Void, Void, Boolean>()
        {
            override fun onPreExecute() {
                super.onPreExecute()

            }

            override fun doInBackground(vararg p0: Void?): Boolean? {

                val item:NoteEntity = viewModel!!.selectItem(bookInfo.id)
                if(item.id > 0)
                    viewModel!!.update(NoteEntity(item.id, item.title, item.contents, item.book_name, item.book_key, true))//update

                return true
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                if(result == true)
                {
                    binding.btnUpload.text = mContext!!.resources.getString(R.string.already_upload_note)
                    binding.btnUpload.isEnabled = false
                }

            }

        }.execute()
    }
}
