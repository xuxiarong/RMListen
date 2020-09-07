package com.rm.module_play.test

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_play.repository.BookPlayRepository
import java.util.*

/**
 *
 * @des:
 * @data: 9/3/20 5:46 PM
 * @Version: 1.0.0
 */
class TestViewModel(private val repository: BookPlayRepository) : BaseVMViewModel() {

    var text = ObservableField("")
    var books = ObservableField<ArrayList<SearchResultInfo>>()
    fun getBookList() {
        val params: MutableMap<String, String> = HashMap()
        params["format"] = "json"
        params["page"] = "1"
        params["pagesize"] = "20"
        params["showtype"] = "1"
        launchOnUI {
            text.get()?.let {
                params["keyword"] = text.get() ?: ""
                repository.getBookList(params).checkResult(
                    onSuccess = {
                        books.set(it.info as ArrayList<SearchResultInfo>?)
                    },
                    onError = {

                    }
                )
            }
        }

    }


    }