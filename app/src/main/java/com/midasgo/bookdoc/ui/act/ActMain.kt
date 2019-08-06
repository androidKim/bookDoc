package com.midasgo.bookdoc.ui.act

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.databinding.ActMainBinding
import com.midasgo.bookdoc.ui.frag.FragBookList
import com.midasgo.bookdoc.ui.frag.FragSearch
import com.midasgo.bookdoc.ui.frag.FragSetting
import io.reactivex.Observable

/*
메인화면(기록, 검색, 설정)
 */
class ActMain : AppCompatActivity() {
    /****************** member ******************/
    private lateinit var binding:ActMainBinding
    private var mContext:Context ?= null
    private var mApp: MyApp?= null
    /****************** system function ******************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.act_main)
        mContext = this
        mApp = application as MyApp
        if(mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        initLayout()
    }

    override fun onStart() {
        super.onStart()

        val btnFloatingObservable = floatingBtnClickObservable()//floating action button click
        btnFloatingObservable
            .subscribe { query ->
                var pIntent: Intent = Intent(mContext, ActAddBook::class.java)
                startActivity(pIntent)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode)
        {
            Constant.FOR_RESULT_DELETE ->{
                val fragment = FragBookList()
                addFragment(fragment)
            }
        }
    }
    /****************** user funtion ******************/
    private fun initLayout(){
        val fragment = FragBookList()
        addFragment(fragment)
    }

    /****************** bottom navition ******************/
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_list -> {
                val fragment = FragBookList()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                val fragment = FragSearch()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_setting -> {
                val fragment = FragSetting()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    //-------------------------------------
    //change fragment
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.fragmentContainer, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    /********************* Obsevable Ui Event *********************/
    //---------------------------------------------------
    //FloatingAction Button click
    private fun floatingBtnClickObservable(): Observable<String> {
        return Observable.create { emitter ->
            binding.btnFloating.setOnClickListener {
                emitter.onNext("")
            }

            emitter.setCancellable {
                binding.btnFloating.setOnClickListener(null)
            }
        }
    }
}
