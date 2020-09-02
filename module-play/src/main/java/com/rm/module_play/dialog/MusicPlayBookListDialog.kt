package com.rm.module_play.dialog

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.base.dialogfragment.BottomDialogFragment
import com.rm.module_play.R
import kotlinx.android.synthetic.main.music_play_dialog_speed_setting.*

/**
 *
 * @des:书单
 * @data: 8/27/20 11:19 AM
 * @Version: 1.0.0
 */
fun FragmentActivity.showPlayBookListDialog() {
    MusicPlayBookListDialog().apply {

    }.show(supportFragmentManager, "MusicPlayTimeSettingDialog")
}

class MusicPlayBookListDialog : BottomDialogFragment() {
    private val itemDecoration by lazy {
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = dip(17)
                outRect.left = dip(17)

            }
        }
    }
    private val timeSAdapter by lazy {
        TimeSAdapter(timeList)
    }
    private val timeList by lazy {
        //占位 add


        arrayListOf<String>().apply {
            for (index in 30 downTo 1) {
                add("${index}分钟")
            }
        }
    }

    override fun onSetInflaterLayout(): Int = R.layout.music_play_dialog_book_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_music_play_time_setting.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_music_play_time_setting.addItemDecoration(itemDecoration)
        rv_music_play_time_setting.adapter = timeSAdapter

    }


    internal class TimeSAdapter(list: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.music_play_item_book_list, list) {

        override fun convert(holder: BaseViewHolder, item: String) {


        }

    }

}