package com.android.mcplay.controler

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val selectedFragment = MutableLiveData<Fragment>()

    fun setFragment(fragment: Fragment) {
        selectedFragment.value = fragment
    }

    fun getCurrentFragment(): LiveData<Fragment> = selectedFragment
}
