package com.midasgo.bookdoc.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.databinding.ActBoardDetailBinding
import com.midasgo.bookdoc.viewmodel.BoardDetailViewModel


/*
노트 게시판 상세
viewmodel,  recyclerview header...
 */
class BoardDetailActivity : AppCompatActivity(){
    /********************* member *********************/
    private lateinit var binding: ActBoardDetailBinding
    var mApp: MyApp? = null
    var mContext: Context? = null
    var mRequestManager: RequestManager? = null
    var mBoardId:Int=0
    lateinit var viewModel:BoardDetailViewModel
    /****************** system function ******************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.act_note_write)
        binding = DataBindingUtil.setContentView(this, R.layout.act_board_detail)
        viewModel = ViewModelProviders.of(this).get(BoardDetailViewModel::class.java)
        binding.viewmodel = this.viewModel

        mContext = this
        mApp = application as MyApp
        if (mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        mRequestManager = Glide.with(mContext!!)

        recvIntentData()
        initLayout()
    }
    /****************** user function ******************/
    fun recvIntentData() {
        if(intent != null)
        {
            mBoardId = intent.getIntExtra(Constant.INTENT_DATA_BOARD_ID, 0)
        }
    }

    fun initLayout() {
        setSupportActionBar(binding.toolbar)

        //toolbar back button event
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        settingBoardDetailView()
    }

    fun settingBoardDetailView(){
        if(mBoardId > 0)
        {
            //load data
            viewModel.loadNoteItem(mBoardId)
            viewModel.loadReplyCount(mBoardId)

            //note info
            viewModel.selectNoteItem().observe(this, Observer {
                if(it != null)
                {
                    if(it.title != null)
                        binding.toolbar.title = it.title

                    if(it.content != null)
                        binding.tvContent.text = it.content
                }
            })

            //reply count
            viewModel.selectReplyCount().observe(this, Observer { count ->
                binding.btnReplyList.text = String.format("댓글(%s)", count)
            })

            //댓글리스트 이동
            viewModel.getGoReplyActivityStatus().observe(this, Observer {
                if(it)
                {
                    var pIntent = Intent(this, ReplyListActivity::class.java)
                    pIntent.putExtra(Constant.INTENT_DATA_BOARD_ID, mBoardId)
                    startActivity(pIntent)
                }
                else
                {

                }
            })
        }
        else
        {
            Toast.makeText(this, mContext!!.resources.getString(R.string.toast_board_id_incorrect), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
