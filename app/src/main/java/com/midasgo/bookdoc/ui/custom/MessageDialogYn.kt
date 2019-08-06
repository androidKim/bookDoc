package com.midasgo.bookdoc.ui.custom.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.databinding.DlgMessageYnBinding

/*
커스텀 메세지 다이얼로그 뷰(확인, 취소)
 */
class MessageDialogYn(pContext: Context, pCallback:ifCallback) : Dialog(pContext, R.style.msg_dialog_style), View.OnClickListener {

    private lateinit var mBinding: DlgMessageYnBinding
    private var title:String = ""
    private var msg:String = ""
    private var ok:String = ""
    private var cancel:String = ""
    private var mContext:Context = pContext
    private var mIfCallback:ifCallback = pCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dlg_message_yn, null, false)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(false)
        //window.setBackgroundDrawable(ColorDrawable())
        //window.setDimAmount(0.0f)

        mBinding.massage = this.msg
        mBinding.ok = this.ok
        mBinding.cancel = this.cancel
        mBinding.lyOk.setOnClickListener(this)
        mBinding.lyCancel.setOnClickListener(this)

        setOnKeyListener(object : DialogInterface.OnKeyListener {

            override fun onKey(
                arg0: DialogInterface, keyCode: Int,
                event: KeyEvent
            ): Boolean {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mIfCallback?.onClickCancel()
                    dismiss()
                }
                return true
            }
        })
    }

    class Builder(context: Context, callback:ifCallback){
        private val dialog = MessageDialogYn(context, callback)
        fun setMassage(text:String): Builder {
            dialog.msg = text
            return this
        }
        fun setOkButton(text:String): Builder {
            dialog.ok = text
            return this
        }
        fun setCancelButton(text:String):Builder{
            dialog.cancel = text
            return this
        }
        fun show(): MessageDialogYn {
            dialog.show()
            return dialog
        }
    }

    override fun onClick(v: View?) {
        if(v == mBinding.lyOk)
        {
            dismiss()
            mIfCallback.onClickOK()
        }
        else if(v == mBinding.lyCancel)
        {
            dismiss()
            mIfCallback.onClickCancel()
        }
    }

    interface ifCallback{
        fun onClickOK()
        fun onClickCancel()
    }
}