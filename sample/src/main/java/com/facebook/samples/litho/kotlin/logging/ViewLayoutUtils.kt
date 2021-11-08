package com.facebook.samples.litho.kotlin.logging

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout


fun <T : ViewGroup> flexGrowHeight(view: T): T {
    view.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f)
    return view
}

fun <T : ViewGroup> setHeight(view: T, height: Int): T {
    view.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, height)
    return view
}

fun <T : View> setWeight(view: T, weight: Float): T {
    if (view.layoutParams is LinearLayout.LayoutParams) {
        val params = view.layoutParams as LinearLayout.LayoutParams
        params.weight = weight
        view.layoutParams = params
    }
    return view
}

