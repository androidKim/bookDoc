package com.midasgo.bookdoc.com.midasgo.bookdoc.common

open class Constant {
    companion object{
        val INTENT_DATA_BOOK_INFO:String = "INTENT_DATA_BOOK_INFO"
        val INTENT_DATA_NOTE_INFO:String = "INTENT_DATA_NOTE_INFO"
        val INTENT_DATA_ALARM_STATE:String = "INTENT_DATA_ALARM_STATE"
        val INTENT_DATA_NOTIFICATION_CHANNEL_ID:String = "INTENT_DATA_NOTIFICATION_CHANNEL_ID"
        val INTENT_DATA_BOOK_KEY:String = "INTENT_DATA_BOOK_KEY"
        val INTENT_DATA_NOTE_KEY:String = "INTENT_DATA_NOTE_KEY"
        val INTENT_DATA_NOTE_TEXT:String = "INTENT_DATA_NOTE_TEXT"

        //REQUESTCODE
        val FOR_RESULT_DELETE:Int = 9999
        val FOR_RESULT_NOTE_CHANGE:Int = 9998//노트 상태값변경 추가 / 삭제 / 변경

        val ALARM_ON:String = "ALARM_ON"
        val ALARM_OFF:String = "ALARM_OFF"
    }
}