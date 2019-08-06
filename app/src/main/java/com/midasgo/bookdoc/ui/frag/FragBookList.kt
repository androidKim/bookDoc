package com.midasgo.bookdoc.ui.frag

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
import com.midasgo.bookdoc.core.model.book
import com.midasgo.bookdoc.core.room.db_viewmodel.BookDbViewModel
import com.midasgo.bookdoc.core.room.entity.BookEntity
import com.midasgo.bookdoc.databinding.FragBookListBinding
import com.midasgo.bookdoc.ui.adapter.BookListRvAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.frag_book_list.*

class FragBookList : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    /********************* member *********************/
    private val disposable = CompositeDisposable()
    private lateinit var binding:FragBookListBinding
    private var mContext: Context? = null
    private var mApp: MyApp? = null
    private var mRequestManager: RequestManager? = null
    private var mAdapter:BookListRvAdapter? = null
    private lateinit var mBookList:ArrayList<book>
    /**
     * Initialize newInstance for passing paameters
     */
    companion object {
        fun newInstance(): FragBookList {
            var pFragment = FragBookList()
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
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_book_list, container, false);
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

        initValue()
        initLayout()
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
        mBookList = ArrayList<book>()
        mAdapter = BookListRvAdapter(mContext!!, activity!!, mRequestManager!!, mBookList!!)
        recyclerView!!.adapter = mAdapter
        var nSpanCnt = 1
        val pLayoutManager = GridLayoutManager(mContext, nSpanCnt)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = pLayoutManager

        setBookDataFromRoom()

        binding.swipeRefresh.setOnRefreshListener(this)
    }
    //---------------------------------------------------------
    //
    fun setBookDataFromRoom(){
        //get room data
        var dbViewModel: BookDbViewModel = ViewModelProviders.of(this).get(BookDbViewModel::class.java)
        dbViewModel?.bookList?.observe(this, object : Observer<List<BookEntity>> {
            override fun onChanged(list: List<BookEntity>?) {
                if(list != null)
                {
                    val length:Int = list.size - 1
                    for(i in 0..length)
                    {
                        var pInfo: BookEntity = list.get(i)
                        mAdapter?.addEntity(pInfo)
                    }
                    mAdapter?.notifyDataSetChanged()
                }
                else
                {

                }
            }
        })
    }
    //---------------------------------------------------------
    //
    fun setRefresh(){
        binding.swipeRefresh.isRefreshing = false

        if(mAdapter != null)
        {
            mAdapter!!.clearData()
            setBookDataFromRoom()
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