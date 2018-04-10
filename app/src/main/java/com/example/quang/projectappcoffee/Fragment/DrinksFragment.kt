package com.example.quang.projectappcoffee.Fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import com.example.quang.projectappcoffee.R
import kotlinx.android.synthetic.main.fragment_drinks.*
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Toast
import com.example.quang.projectappcoffee.Adapter.TabAdapter
import com.example.quang.projectappcoffee.ui.InfoActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*


class DrinksFragment : Fragment(),Toolbar.OnMenuItemClickListener{
    lateinit var dialogUser:DialogUser
    lateinit var number:String
    lateinit var mAuth: FirebaseAuth
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menu_account->{
                val menuItemView:View = toolbar.findViewById(R.id.menu_account)
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
                startActivity(Intent(activity,InfoActivity::class.java))
                return true
            }
        }
        return false
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_drinks, container,false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity.fab.visibility = View.VISIBLE
        activity.window.statusBarColor = ContextCompat.getColor(activity,R.color.colorStatusBarDrinks)
        toolbar.inflateMenu(R.menu.menu_order)
        toolbar.setOnMenuItemClickListener(this)
        initTabLayout()
        dialogUser = DialogUser()
        mAuth = FirebaseAuth.getInstance()

    }



    private fun initTabLayout() {
        val tab = TabAdapter(activity.supportFragmentManager)

        val allFragment = AllFragment()
        val bundle = Bundle()
        bundle.putString("allDrinks","allDrinks")
        allFragment.arguments = bundle

        tab.addFragment(allFragment, "All")
        tab.addFragment(CoffeeFragment(), getString(R.string.field_coffee))
        tab.addFragment(ChocolaFragment(),  getString(R.string.field_chocola))
        tab.addFragment(LatteFragment(),  getString(R.string.field_latte))
        tab.addFragment(SmoothieFragment(), getString(R.string.field_smoothie))
        tab.addFragment(TeaFragment(), getString(R.string.field_tea))

        container.adapter = tab
        tabs.setupWithViewPager(container)

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
    }


}
