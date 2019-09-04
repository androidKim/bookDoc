package com.midasgo.bookdoc.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.databinding.FragNoteBoardBinding
import com.midasgo.bookdoc.structure.note
import com.midasgo.bookdoc.view.adapter.NoteBoardRvAdapter
import com.midasgo.bookdoc.viewmodel.NoteBoardViewModel
import kotlinx.android.synthetic.main.frag_book_list.*

class FragNoteBoard : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    /********************* member *********************/
    private lateinit var binding: FragNoteBoardBinding
    private var mContext: Context? = null
    private var mApp: MyApp? = null
    private var mRequestManager: RequestManager? = null
    private var mAdapter: NoteBoardRvAdapter? = null
    private var mList:ArrayList<note> = ArrayList()
    lateinit var viewModel: NoteBoardViewModel
    /**
     * Initialize newInstance for passing paameters
     */
    companion object {
        fun newInstance(): FragNoteBoard {
            var pFragment = FragNoteBoard()
            var args = Bundle()
            pFragment.arguments = args
            return pFragment
        }
    }
    /********************* system function *********************/
    //---------------------------------------------------------
    //Layout 을 inflate 하는 곳, View 객체를 얻어서 초기화
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val rootView: View = inflater!!.inflate(R.layout.frag_book_list, container, false)
        //return rootView
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_note_board, container, false);
        return binding.root
    }
    //---------------------------------------------------------
    //onCreateView에서 리턴해준 뷰
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = context
        mApp = mContext!!.applicationContext as MyApp
        if (mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        mRequestManager = Glide.with(mContext!!)

        viewModel = ViewModelProviders.of(this).get(NoteBoardViewModel::class.java)
        viewModel.getList().observe(this, Observer {
                var tempArr:ArrayList<note> = ArrayList()
                val length:Int = it.size - 1
                for(i in 0..length){
                    val item: note = it.get(i)
                    tempArr!!.add(item)
                }
                mList.clear()
                mList.addAll(tempArr)

                if(mAdapter!=null)
                    mAdapter!!.notifyDataSetChanged()
        })

        viewModel.errorShow().observe(this, Observer {
            mApp!!.showMessageDialog(mContext!!, it.toString(), mContext!!.resources.getString(R.string.ok))
        })

        initValue()
        initLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    /********************* user function *********************/
    //---------------------------------------------------------
    //
    fun initValue() {

    }
    //---------------------------------------------------------
    //
    fun initLayout() {
        //recycler view setting.
        mAdapter = NoteBoardRvAdapter(mContext!!, activity!!, mRequestManager!!, mList)
        recyclerView!!.adapter = mAdapter
        var nSpanCnt = 1
        val pLayoutManager = GridLayoutManager(mContext, nSpanCnt)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = pLayoutManager

        binding.swipeRefresh.setOnRefreshListener(this)
    }
    //---------------------------------------------------------
    //
    fun setRefresh(){
        binding.swipeRefresh.isRefreshing = false
        if(mAdapter != null)
        {
            mAdapter!!.clearData()
            viewModel.refresh()
        }
    }

    /********************* listener *********************/
    //---------------------------------------------------------
    //swipe refresh listener method
    override fun onRefresh()
    {
        setRefresh()
    }
}