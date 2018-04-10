package com.example.quang.projectappcoffee.Fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quang.projectappcoffee.Adapter.TabAdapter
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.ui.InfoActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_drinks.*
import kotlinx.android.synthetic.main.fragment_food.*

/**
 * Created by quang on 12/27/17.
 */
class FoodFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    lateinit var dialogUser:DialogUser
    lateinit var number:String
    lateinit var mAuth: FirebaseAuth
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menu_account->{
                val menuItemView:View = toolbar_food.findViewById(R.id.menu_account)
                val popupMenu = PopupMenu(activity,menuItemView)
                popupMenu.inflate(R.menu.menu_user)
                number = if (mAuth.currentUser == null ){
                    "Login"
                }else{
                    mAuth.currentUser!!.phoneNumber.toString()
                }
                popupMenu.menu.getItem(0).title =  number
                popupMenu.setOnMenuItemClickListener { item ->
                    when(item.itemId){
                        R.id.menu_Login->{
                            dialogUser.show(activity.supportFragmentManager,"User")
                        }
                        R.id.menu_Signout->{
                            mAuth.signOut()
                        }
                    }
                    true
                }
                popupMenu.show()

                return true
            }
            R.id.menu_info->{
                startActivity(Intent(activity, InfoActivity::class.java))
                return true
            }
        }
        return false
   }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_food, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity.fab.visibility = View.VISIBLE
        activity.window.statusBarColor = ContextCompat.getColor(activity,R.color.colorStatusBarFoods)
        initTabLayout()

        toolbar_food.inflateMenu(R.menu.menu_order)
        toolbar_food.setOnMenuItemClickListener(this)
        dialogUser = DialogUser()
        mAuth = FirebaseAuth.getInstance()

    }

    fun initTabLayout() {
        val allFragment = AllFragment()
        val bundle = Bundle()
        bundle.putString("allFood","allFood")
        allFragment.arguments = bundle
        val tab = TabAdapter(activity.supportFragmentManager)
        tab.addFragment(allFragment, "All")
        tab.addFragment(HamburgerFragment(), getString(R.string.field_hamburger))
        tab.addFragment(SaladFragment(),  getString(R.string.field_salad))
        tab.addFragment(SandwichFragment(),  getString(R.string.field_sandwich))
        tab.addFragment(ChipsFragment(), getString(R.string.field_chip))
        tab.addFragment(KidsFragment(), getString(R.string.field_kid))
        container_food.adapter = tab
        tabs_food.setupWithViewPager(container_food)

        container_food.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs_food))
        tabs_food.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container_food))
        tabs_food.tabMode = TabLayout.MODE_SCROLLABLE
    }


}