package com.rm.business_lib.share

import android.app.Activity

/**
 *
 * @author yuanfang
 * @date 12/18/20
 * @description
 *
 */
class ShareManage {


    companion object {
        private const val SHARE_TEST_URL = "http://10.1.9.197:8481"

        @JvmStatic
        fun shareSheet(activity: Activity, sheetId: String, sheetName: String) {
            val shareUrl = "$SHARE_TEST_URL/index/listen/detail?id=$sheetId"
            val stringBuffer = StringBuffer("“听单名称-$sheetName”")
            stringBuffer.append("听单分享，快来听听吧！")
            stringBuffer.append("\n")
            stringBuffer.append(shareUrl)

            Share2.Builder(activity).setContentType(ShareContentType.TEXT)
                .setTitle("分享测试")
                .setTextContent(stringBuffer.toString())
                .build()
                .shareBySystem()
        }

        @JvmStatic
        fun shareAudio(activity: Activity, audioId: String, audioName: String) {
            val shareUrl = "$SHARE_TEST_URL/book-detail?id=$audioId"
            val stringBuffer = StringBuffer("“音频名称-$audioName”")
            stringBuffer.append("音频分享，快来听听吧！")
            stringBuffer.append("\n")
            stringBuffer.append(shareUrl)

            Share2.Builder(activity).setContentType(ShareContentType.TEXT)
                .setTitle("分享测试")
                .setTextContent(stringBuffer.toString())
                .build()
                .shareBySystem()
        }

        @JvmStatic
        fun shareChapter(
            activity: Activity,
            audioId: String,
            chapterId: String,
            audioName: String
        ) {
            val shareUrl = "$SHARE_TEST_URL/book-detail?id=$audioId&chapterId=$chapterId"
            val stringBuffer = StringBuffer("“章节名称-$audioName”")
            stringBuffer.append("章节分享，快来听听吧！")
            stringBuffer.append("\n")
            stringBuffer.append(shareUrl)

            Share2.Builder(activity).setContentType(ShareContentType.TEXT)
                .setTitle("分享测试")
                .setTextContent(stringBuffer.toString())
                .build()
                .shareBySystem()
        }
    }
}