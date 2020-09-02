package com.rm.business_lib.wedgit.expandtextview

import androidx.databinding.BindingAdapter

@BindingAdapter("expandText")
fun ExpandableTextView.setExpandText(mText:String?){
    setText(mText)
}