package com.rm.module_home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rm.module_home.R

class HomePagerTestFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.home_fragment_test,null,false)
        val content = arguments!!.getString("title")
        view.findViewById<TextView>(R.id.tv_title).text=String.format("Test\n\n%s", content)
        return view
    }

}