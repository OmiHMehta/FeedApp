package com.telstra.feedapp.utility

import android.app.Activity
import android.util.DisplayMetrics

object ScreenDimensions {

    var screenWidth: Int = 0

    var screenHeight: Int = 0

    var profilePicDimensions = 0

    fun getScreenDimensions(activity: Activity): IntArray {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels
        profilePicDimensions = (ScreenDimensions.screenHeight * 0.142).toInt()
        return intArrayOf(screenHeight, screenWidth)
    }
}