package com.hendi.ichat.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hendi.ichat.Adapter.A_Dashboard
import com.hendi.ichat.Help.User
import com.hendi.ichat.Help.sharedPrefManager
import com.hendi.ichat.R
import kotlinx.android.synthetic.main.f_dashboard.view.*

class F_Dashboard : Fragment() {

    lateinit var v : View
    lateinit var mContext : Context
    lateinit var sp : sharedPrefManager
    lateinit var adapterUser : A_Dashboard
    var user = User()
    var list : MutableList<User> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.f_dashboard,container,false)

        mContext = this.context!!

        fecthUser()
        return v
    }

    private fun fecthUser() {
        list.clear()
        val ref = FirebaseDatabase.getInstance().getReference("/user")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d("User", it.toString())
                    val a = it.getValue(User::class.java)!!
                    list.add(a)
                }
                v.id_rv_dashboard.layoutManager = LinearLayoutManager(mContext)
                adapterUser = A_Dashboard(mContext,list)
                v.id_rv_dashboard.adapter = adapterUser
                adapterUser.notifyDataSetChanged()
            }

        })
    }
}