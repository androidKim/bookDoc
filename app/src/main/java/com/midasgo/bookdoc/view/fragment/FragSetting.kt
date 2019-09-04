package com.midasgo.bookdoc.view.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jakewharton.rxbinding3.view.clicks
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.databinding.FragSettingBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


class FragSetting : Fragment() {
    /********************* member *********************/
    private val disposable = CompositeDisposable()
    private lateinit var binding:FragSettingBinding
    private var mContext: Context? = null
    private var mApp: MyApp? = null
    private var mRequestManager: RequestManager? = null
    /**
     * Initialize newInstance for passing paameters
     */
    companion object {
        fun newInstance(): FragSetting {
            var pFragment = FragSetting()
            var args = Bundle()
            pFragment.arguments = args
            return pFragment
        }
    }

    /********************* system function *********************/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val rootView: View = inflater!!.inflate(R.layout.frag_setting, container, false)
        //return rootView

        binding = DataBindingUtil.inflate(inflater, R.layout.frag_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = context!!
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

        binding.tvVersion.text = mApp!!.mVersionCode
        setEventListener()
    }
    //---------------------------------------------------------
    //
    fun setEventListener(){

        //rxbinding
        binding.lyVersion.clicks().debounce(1000, TimeUnit.MILLISECONDS)//중복클릭 방지
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val appPackageName:String = mContext!!.packageName // getPackageName() from Context or Activity object
                    try
                    {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName)))
                    }
                    catch (e: ActivityNotFoundException)
                    {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+appPackageName)))
                    }

                })
    }
}