package com.midasgo.bookdoc.view.custom.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.databinding.DlgMessageBinding


/*
커스텀 메세지 다이얼로그 뷰
 */
class MessageDialog(pContext: Context) : Dialog(pContext, R.style.msg_dialog_style), View.OnClickListener {

    private lateinit var mBinding: DlgMessageBinding
    private var title:String = ""
    private var msg:String = ""
    private var ok:String = ""
    private var mContext:Context = pContext
    private var listener:ifCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dlg_message, null, false)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(false)
        //window.setBackgroundDrawable(ColorDrawable())
        //window.setDimAmount(0.0f)

        mBinding.massage = this.msg
        mBinding.ok = this.ok
        mBinding.lyOk.setOnClickListener(this)
    }

    class Builder(context: Context){
        private val dialog = MessageDialog(context)
        fun setMassage(text:String): Builder {
            dialog.msg = text
            return this
        }
        fun setOkButton(text:String): Builder {
            dialog.ok = text
            return this
        }
        fun show(): MessageDialog {
            dialog.show()
            return dialog
        }
    }

    override fun onClick(v: View?) {
        if(v == mBinding.lyOk)
        {
           listener?.confirm()
            dismiss()
        }
    }

    fun setListener(listener:ifCallback){

            this.listener = listener

        setOnKeyListener(object : DialogInterface.OnKeyListener {

            override fun onKey(
                arg0: DialogInterface, keyCode: Int,
                event: KeyEvent
            ): Boolean {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    listener?.confirm()
                    dismiss()
                }
                return true
            }
        })
    }

    /************** interface **************/
    interface ifCallback {

        fun confirm()
    }
}