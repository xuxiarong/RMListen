package com.rm.baselisten.util

import android.content.Context
import kotlin.properties.Delegates


class Cxt {
    companion object {
        var context: Context by Delegates.notNull()
    }
}