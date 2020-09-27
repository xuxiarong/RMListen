package com.rm.business_lib.wedgit.coordinator

import android.widget.ImageView
import com.google.android.material.tabs.TabLayout

interface LoadHeaderImagesListener {
    fun loadHeaderImages(
        imageView: ImageView?,
        tab: TabLayout.Tab?
    )
}

/*
public interface LoadHeaderImagesListener {
    void loadHeaderImages(ImageView imageView, TabLayout.Tab tab);
}*/
