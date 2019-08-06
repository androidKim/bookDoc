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
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jakewharton.rxbinding3.widget.textChanges
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.core.api.MyApi
import com.midasgo.bookdoc.core.model.documents
import com.midasgo.bookdoc.core.viewmodel.SearchViewModel
import com.midasgo.bookdoc.databinding.FragSearchBinding
import com.midasgo.bookdoc.ui.adapter.SearchListRvAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class FragSearch : Fragment() {
    /********************* member *********************/
    private val disposable = CompositeDisposable()
    private lateinit var binding:FragSearchBinding
    private var mContext: Context? = null
    private var mApp: MyApp? = null
    private var mRequestManager: RequestManager? = null
    private var mAdapter:SearchListRvAdapter?=null
    private var mCurrentWord:String=""
    private var mPage:Int = 1
    private var mSearchViewModel:SearchViewModel?=null
    private var mBookList:ArrayList<documents>?=ArrayList()
    /**
     * Initialize newInstance for passing paameters
     */
    companion object {
        fun newInstance(): FragSearch {
            var pFragment = FragSearch()
            var args = Bundle()
            pFragment.arguments = args
            return pFragment
        }
    }

    /********************* system function *********************/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val rootView: View = inflater!!.inflate(R.layout.frag_search, container, false)
        //return rootView
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_search, container, false)
        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        binding.model = mSearchViewModel
        // Observe the model
        mSearchViewModel!!.array.observe(this, Observer {
            mAdapter!!.setList(it!!)
        })

        return binding.root
    }

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

    //------------------------------------------------------
    //
    override fun onDestroy() {
        super.onDestroy()
        // Activity 가 종료되면 사용중인 통신을 중단합니다.
        disposable.clear()
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
        mAdapter = SearchListRvAdapter(mContext!!, activity!!, mRequestManager!!, mBookList!!)
        binding.recyclerView!!.adapter = mAdapter
        var nSpanCnt = 1
        val pLayoutManager = GridLayoutManager(mContext, nSpanCnt)
        binding.recyclerView!!.setHasFixedSize(true)
        binding.recyclerView!!.layoutManager = pLayoutManager

        setRxBinding()
    }

    //---------------------------------------------------------
    //
    fun setRxBinding(){
        binding.editSearch
                .textChanges()
                .debounce(1000, TimeUnit.MILLISECONDS)//1초 딜레이
                .subscribe {
                    var word:String = it.toString()

                    //검색api수행
                    if(!word.equals(""))
                        searchKakaoBookProc(word)
                }
    }

    /********************* kakao api *********************/
    //------------------------------------------------------
    //
    fun searchKakaoBookProc(word:String){
        if(mCurrentWord.equals(word))
            mPage++
        else
            mPage = 1

        disposable.add(mApp!!.mWebCtrl!!.provideMyApi(MyApi.KAKAO_HOST).search_book(
            "KakaoAK 295eaa0eb7147ee768a7f8c664b4dc8c",
            word,"accuracy", mPage, 50, "title")
            // REST API 를 통해 받은 응답에서 Result 값을 추출합니다.

            // 이 이후에 수행되는 코드는 모두 메인 스레드에서 실행합니다.
            .observeOn(AndroidSchedulers.mainThread())

            // 구독할 때 수행할 작업을 구현합니다.
            .doOnSubscribe {

            }

            // 스트림이 종료될 때 수행할 작업을 구현합니다.
            .doOnTerminate {
                mCurrentWord = word
            }

            // 옵서버블을 구독합니다.
            .subscribe({ it ->
                // response.body 를 받아 처리합니다.
                // 작업 중 오류가 발생하면 이 블록은 호출되지 않습니다.
                if(it.code() == 200)
                {
                    if(it.body() != null)
                    {
                        var tempArr:ArrayList<documents> = ArrayList()
                        val length:Int = it.body()!!.documents.size - 1
                        for(i in 0..length){
                            val item:documents = it.body()!!.documents.get(i)
                            tempArr!!.add(item)
                        }
                        mSearchViewModel!!.changeList(tempArr!!)
                    }
                }
            }) {
                // 에러블록
                // 네트워크 오류나 데이터 처리 오류 등
                // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
                mApp!!.showMessageDialog(mContext!!, "${it.message}", mContext!!.resources.getString(R.string.ok))
            })
    }
}