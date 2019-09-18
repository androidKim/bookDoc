package com.midasgo.bookdoc.view.activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.databinding.ActReplyListBinding
import com.midasgo.bookdoc.structure.reply
import com.midasgo.bookdoc.view.adapter.ReplyListRvAdapter
import com.midasgo.bookdoc.viewmodel.ReplyListViewModel
import kotlinx.android.synthetic.main.act_reply_list.*


/*
노트 게시판 상세
viewmodel,  recyclerview header...
 */
class ReplyListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener{
    //swiperefresh layout
    override fun onRefresh() {
        setRefresh()
    }

    private fun setRefresh() {
        binding.swipeRefresh.isRefreshing = false
        getReplyListProc()
    }

    /********************* member *********************/
    private lateinit var binding: ActReplyListBinding
    var mApp: MyApp? = null
    var mContext: Context? = null
    var mRequestManager: RequestManager? = null
    lateinit var mViewModel:ReplyListViewModel
    var mAdapter:ReplyListRvAdapter?=null
    var mBoardId:Int=0
    /****************** system function ******************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.act_note_write)
        binding = DataBindingUtil.setContentView(this, R.layout.act_reply_list)
        mViewModel = ViewModelProviders.of(this).get(ReplyListViewModel::class.java)
        binding.viewmodel = mViewModel

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

        settingRecyclerView()
        binding.swipeRefresh.setOnRefreshListener(this)

        //reply upload
        mViewModel.getIsUpload().observe(this, Observer {
            if(it) {
                if(binding.editContent.text.toString().length > 0)
                {
                    val replyItem:reply = reply(0, binding.editContent.text.toString(),mBoardId,"")
                    mViewModel.uploadReply(replyItem)
                }
                else {
                    Toast.makeText(this, mContext!!.resources.getString(R.string.toast_input_reply_plz), Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, mContext!!.resources.getString(R.string.error_msg), Toast.LENGTH_SHORT).show()
            }
        })

        //success toast
        mViewModel.successShow().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            binding.editContent.setText("")
            binding.editContent.isEnabled = false
            binding.btnWirte.isEnabled = false
            getReplyListProc()//refresh
        })

        //error toast
        mViewModel.errorShow().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        getReplyListProc()
    }

    //recyclerview init
    private fun settingRecyclerView() {
        //recyclerview
        var array:ArrayList<reply> = ArrayList()
        mAdapter = ReplyListRvAdapter(mContext!!, this, mRequestManager!!, array)
        recyclerView!!.adapter = mAdapter
        var nSpanCnt = 1
        val pLayoutManager = GridLayoutManager(mContext, nSpanCnt)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = pLayoutManager
    }

    //getDataFromWeb
    fun getReplyListProc(){
        if(mBoardId > 0)
        {
            mViewModel.loadReplyList(mBoardId)

            mViewModel.selectReplyList().observe(this, Observer {

                var tempArr:ArrayList<reply> = ArrayList()
                val length:Int = it.size - 1
                for(i in 0..length){
                    val item: reply = it.get(i)
                    tempArr!!.add(item)
                }

                if(mAdapter!=null) {
                    mAdapter!!.refreshData(tempArr)
                    mAdapter!!.notifyDataSetChanged()

                    if(mAdapter!!.itemCount > 0) {
                        binding.recyclerView.smoothScrollToPosition(mAdapter!!.itemCount -1)//move last position
                    }

                }
            })
        }
        else
        {
            Toast.makeText(this, mContext!!.resources.getString(R.string.error_msg), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
