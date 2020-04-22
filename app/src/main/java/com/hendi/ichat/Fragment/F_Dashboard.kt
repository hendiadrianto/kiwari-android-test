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
import com.hendi.penetasantelur.Model.RecyclerItemClickListener
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
        sp = sharedPrefManager(mContext)

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
                list.clear()
                p0.children.forEach {
                    Log.d("User", it.toString())
                    val a = it.getValue(User::class.java)!!
                    if (a.nama != sp.spChatFrom){
                        list.add(a)
//                        val refChat = FirebaseDatabase.getInstance().getReference("/message/${sp.spChatFrom}/${a.nama}")
//                        refChat.addValueEventListener(object : ValueEventListener{
//                            override fun onCancelled(p1: DatabaseError) {
//                            }
//
//                            override fun onDataChange(p1: DataSnapshot) {
//
//
//                            }
//
//                        })

                    }
                }
                v.id_rv_dashboard.layoutManager = LinearLayoutManager(mContext)
                adapterUser = A_Dashboard(mContext,list)
                v.id_rv_dashboard.adapter = adapterUser
                adapterUser.notifyDataSetChanged()
            }

        })

        v.id_rv_dashboard.addOnItemTouchListener(RecyclerItemClickListener(mContext,object : RecyclerItemClickListener.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                sp.saveSPString(sharedPrefManager.SP_CHAT_TO,adapterUser.list[position].nama!!)
                fragmentManager!!.beginTransaction().replace(R.id.id_fl_content,F_Chat()).addToBackStack(null).commit()
            }
        }))
    }
}