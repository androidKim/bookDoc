package com.midasgo.bookdoc.view.activity

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.databinding.ActNoteWriteBinding
import com.midasgo.bookdoc.model.entity.NoteEntity
import com.midasgo.bookdoc.structure.book
import com.midasgo.bookdoc.view.custom.dialog.MessageDialogYn
import com.midasgo.bookdoc.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.act_note_write.*


/*
노트작성
 */
class ActNoteWrite : AppCompatActivity(), MessageDialogYn.ifCallback {
    /*
    MessageDialogYn callback..
     */
    override fun onClickOK() {
        saveNoteProc()
    }

    override fun onClickCancel() {

    }
    /********************* define *********************/

    /********************* member *********************/
    private lateinit var binding: ActNoteWriteBinding
    var mApp: MyApp? = null
    var mContext: Context? = null
    var mRequestManager: RequestManager? = null
    lateinit var bookInfo: book
    var noteText:String?=null
    var noteTitle:String?=null
    /********************* controller *********************/

    /********************* system function *********************/
    /****************** system function ******************/
    //---------------------------------------------------
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.act_note_write)
        binding = DataBindingUtil.setContentView(this, R.layout.act_note_write)

        mContext = this
        mApp = application as MyApp
        if(mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        mRequestManager = Glide.with(mContext!!)

        recvIntentData()
        initLayout()
    }
    //------------------------------------------------------
    //
    override fun onDestroy() {
        super.onDestroy()
    }
    /****************** toolbar menu ******************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_note_menu, menu)
        var menuItem:MenuItem = menu!!.findItem(R.id.menu_save).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home->{//메뉴버튼

            }
            R.id.menu_save->{//저장
                val title:String = binding.editTitle.text.toString()
                val contents:String = binding.editContents.text.toString()
                if(title.length <= 0 || contents.length <=0)
                {
                    Toast.makeText(this, mContext!!.resources.getString(R.string.toast_note_empty), Toast.LENGTH_SHORT).show()
                }
                else
                {
                    var msg:String = mContext!!.resources.getString(R.string.dialog_save_msg)
                    mApp!!.showMessageDialogYn(mContext!!,
                            this,
                            msg,
                            mContext!!.resources.getString(R.string.ok),
                            mContext!!.resources.getString(R.string.cancel))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /****************** user function ******************/
    fun recvIntentData(){
        bookInfo = intent.getParcelableExtra(Constant.INTENT_DATA_BOOK_INFO)
        noteText = intent.getStringExtra(Constant.INTENT_DATA_NOTE_TEXT)
        noteTitle = intent.getStringExtra(Constant.INTENT_DATA_NOTE_TITLE)
    }

    fun initLayout(){
        setSupportActionBar(toolbar)

        //toolbar back button event
        toolbar.setNavigationOnClickListener {
            finish()
        }

        if(noteText != null)
            binding.editContents.setText(noteText)

        if(noteTitle != null)
            binding.editTitle.setText(noteTitle)
    }
    //---------------------------------------------------
    //
    fun saveNoteProc(){
        var viewModel: NoteViewModel? = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        object :AsyncTask<Void, Void, Void>()
        {
            override fun onPreExecute() {
                super.onPreExecute()
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                Thread.sleep(1000)

                val item:NoteEntity = viewModel!!.selectItem(bookInfo.id)
                if(item.id > 0)
                    viewModel!!.update(NoteEntity(item.id, binding.editTitle.text.toString(), binding.editContents.text.toString(),item.book_name, item.book_key, false))//update
                else
                    viewModel!!.insert(NoteEntity(0,binding.editTitle.text.toString(), binding.editContents.text.toString(),item.book_name, item.book_key, false))//insert

                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                binding.progressBar.visibility = View.GONE
                Toast.makeText(mContext, mContext!!.resources.getText(R.string.toast_add_note_complte_msg), Toast.LENGTH_LONG).show()
                setResult(Constant.FOR_RESULT_NOTE_CHANGE)
                finish()
            }

        }.execute()
    }
}
