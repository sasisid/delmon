package com.app.delmon.interfaces

import android.os.Bundle

interface MoveToFragment {
    fun moveTo(fragName : String,bundle : Bundle)
    fun onFragBackPressed()

}