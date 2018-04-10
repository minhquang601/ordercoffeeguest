package com.example.quang.projectappcoffee.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class TabAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    var listFragment= mutableListOf<Fragment>()
    var listTitle= mutableListOf<String>()

    fun addFragment(fragment: Fragment, title:String){
        listFragment.add(fragment)
        listTitle.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return listTitle[position]
    }

}