package com.example.astroweather

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class FragmentCollectionAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {



    override fun getItem(position: Int): Fragment? {

        var fragment: Fragment? = null

        when(position){
            0 -> fragment = SunFragment()
            1 -> fragment = MoonFragment()
        }

        return fragment

    }

    override fun getCount(): Int {
        return 2
    }

}
