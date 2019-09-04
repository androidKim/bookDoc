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
import com.midasgo.bookdoc.databinding.FragWishListBinding
import com.midasgo.bookdoc.structure.book
import com.midasgo.bookdoc.model.entity.WishEntity
import com.midasgo.bookdoc.viewmodel.WishViewModel
import com.midasgo.bookdoc.view.adapter.WishListRvAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.frag_book_list.*

class FragWish : Fragment(), SwipeRefreshLayout.OnRefreshListener, WishListRvAdapter.ifCallback {
    //recyclerview interface callback
    override fun deleteItem(item: book) {
        var viewModel: WishViewModel = ViewModelProviders.of(this).get(WishViewModel::class.java)
        viewModel.delete(WishEntity.getEntity(item))
    }

    /********************* member *********************/
    private val disposable = CompositeDisposable()
    private lateinit var binding: FragWishListBinding
    private var mContext: Context? = null
    private var mApp: MyApp? = null
    private var mRequestManager: RequestManager? = null
    private var mAdapter: WishListRvAdapter? = null
    private lateinit var mBookList:ArrayList<book>
    /**
     * Initialize newInstance for passing paameters
     */
    companion object {
        fun newInstance(): FragWish {
            var pFragment = FragWish()
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
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_wish_list, container, false);
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
        mAdapter = WishListRvAdapter(mContext!!, activity!!, mRequestManager!!, mBookList!!, this)
        recyclerView!!.adapter = mAdapter
        var nSpanCnt = 1
        val pLayoutManager = GridLayoutManager(mContext, nSpanCnt)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = pLayoutManager

        setWishDataFromRoom()

        binding.swipeRefresh.setOnRefreshListener(this)
    }
    //---------------------------------------------------------
    //
    fun setWishDataFromRoom(){
        var isAdd:Boolean = false//onChanged 1회 적용을 위한 변수
        //get room data
        var viewModel: WishViewModel = ViewModelProviders.of(this).get(WishViewModel::class.java)
        viewModel?.wishList?.observe(this, object : Observer<List<WishEntity>> {
            override fun onChanged(list: List<WishEntity>?) {
                if(list != null)
                {
                    if(!isAdd)
                    {
                        isAdd = true
                        val length:Int = list.size - 1
                        for(i in 0..length)
                        {
                            var pInfo: WishEntity = list.get(i)
                            mAdapter?.addEntity(pInfo)
                        }
                        mAdapter?.notifyDataSetChanged()
                    }
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
            setWishDataFromRoom()
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