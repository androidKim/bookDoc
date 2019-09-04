package com.midasgo.bookdoc.preference
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceCtrl
{
    /************************* Defeine *************************/
    //앱 설정정보
    private val SP_IS_FIRST_RUN:String = "SP_IS_FIRST_RUN"//최초실행여부
    private val SP_IS_LOGIN:String = "SP_IS_LOGIN"
    private val SP_SNS_KEY:String = "SP_SNS_KEY"
    private val SP_SNS_TYPE:String = "SP_SNS_TYPE"

    private lateinit var preference: SharedPreferences

    public constructor()
    {

    }

    public fun init(pContext:Context)
    {
        preference = pContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }
    //---------------------------------------------------------
    //
    public fun clearData()
    {
        val editor = preference.edit()
        editor.clear()
        editor.commit()
    }
    //---------------------------------------------------------
    //
    private fun setStrSaveData(key: String, value: String)
    {
        val editor = preference.edit()
        editor.putString(key, value)
        editor.commit()
    }

    private fun setIntSaveData(key: String, value: Int)
    {
        val editor = preference.edit()
        editor.putInt(key, value)
        editor.commit()
    }
    //---------------------------------------------------------
    //
    private fun getStrLoadData(key:String) : String
    {
        var strResult:String = ""
        strResult = preference.getString(key, "")
        return strResult
    }

    private fun getIntLoadData(key:String) : Int
    {
        return preference.getInt(key, -1)
    }
    //---------------------------------------------------------
    //
    private fun setBoolSaveData(key: String, value: Boolean)
    {
        val editor = preference.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }
    //---------------------------------------------------------
    //
    private fun getBoolLoadData(key:String) : Boolean
    {
        var bResult:Boolean
        bResult = preference.getBoolean(key, false)
        return bResult
    }
    /************************* User Function *************************/
    //---------------------------------------------------------
    //최초실행여부
    fun setSpIsFirstRun(bValue:Boolean)
    {
        setBoolSaveData(SP_IS_FIRST_RUN, bValue)
    }
    fun getSpIsFirstRun():Boolean
    {
        var bResult:Boolean = false
        bResult = getBoolLoadData(SP_IS_FIRST_RUN)
        return bResult
    }
    //---------------------------------------------------------
    //로그인여부
    fun setSpIsLogin(bValue:Boolean)
    {
        setBoolSaveData(SP_IS_LOGIN, bValue)
    }
    fun getSpIsLogin():Boolean
    {
        var bResult:Boolean=false
        bResult = getBoolLoadData(SP_IS_LOGIN)
        return bResult
    }
}