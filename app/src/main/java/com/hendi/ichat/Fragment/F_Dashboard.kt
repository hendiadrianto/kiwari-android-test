package com.hendi.ichat.Fragment

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hendi.ichat.Adapter.A_Dashboard
import com.hendi.ichat.Help.User
import com.hendi.ichat.Help.sharedPrefManager
import com.hendi.ichat.LoginActivity
import com.hendi.ichat.R
import com.hendi.kandangku.Helper.mNetworkAvailable
import com.hendi.kandangku.Helper.mProgress
import com.hendi.kandangku.Helper.mToast
import com.hendi.penetasantelur.Model.RecyclerItemClickListener
import kotlinx.android.synthetic.main.d_account.*
import kotlinx.android.synthetic.main.f_dashboard.view.*

class F_Dashboard : Fragment() {

    lateinit var v : View
    lateinit var mContext : Context
    lateinit var sp : sharedPrefManager
    lateinit var adapterUser : A_Dashboard
    var list : MutableList<User> = ArrayList()

    lateinit var progress : Dialog
    val grey = "#636363"
    val blue = "#1c8ef9"

    var nama = ""
    var email = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.f_dashboard,container,false)

        mContext = this.context!!
        sp = sharedPrefManager(mContext)
        progress = Dialog(mContext)

        if (mNetworkAvailable(mContext)) fecthUser() else mToast(mContext,"Aktifkan koneksi internet terlebih dahulu !")

        v.id_swipe_dashboard.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                if (mNetworkAvailable(mContext)) {
                    fecthUser()
                } else {
                    v.id_swipe_dashboard.isRefreshing = false
                    mToast(mContext, "Aktifkan koneksi internet terlebih dahulu !")
                }
            }
        })

        menuBtn()

        return v
    }

    private fun menuBtn() {
        v.id_klik_home_main.setOnClickListener{
            v.id_home_main.setBackgroundResource(R.drawable.ic_home)
            v.id_t_home_main.setTextColor(Color.parseColor(blue))
            v.id_t_profile_main.setTextColor(Color.parseColor(grey))
            v.id_profile_main.setBackgroundResource(R.drawable.ic_account_grey)
        }
        v.id_klik_account_main.setOnClickListener {
            v.id_home_main.setBackgroundResource(R.drawable.ic_home_grey)
            v.id_t_home_main.setTextColor(Color.parseColor(grey))
            v.id_t_profile_main.setTextColor(Color.parseColor(blue))
            v.id_profile_main.setBackgroundResource(R.drawable.ic_account)

            accountView()
        }
    }

    private fun accountView() {
        val account = Dialog(mContext)
        account.setContentView(R.layout.d_account)
        account.window!!.setLayout(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT)
        account.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        account.window!!.attributes.windowAnimations = R.style.Widget_AppCompat_PopupWindow
        account.setCancelable(false)
        account.show()

        if (sp.spImageFrom != null){
            Glide.with(mContext).load(sp.spImageFrom).into(account.id_image_account)
        }

        account.id_title_account.text = nama
        account.id_email_account.text = email

        account.id_btn_back.setOnClickListener {
            v.id_home_main.setBackgroundResource(R.drawable.ic_home)
            v.id_t_home_main.setTextColor(Color.parseColor(blue))
            v.id_t_profile_main.setTextColor(Color.parseColor(grey))
            v.id_profile_main.setBackgroundResource(R.drawable.ic_account_grey)
            account.dismiss()
        }

        account.id_btn_logout.setOnClickListener {
            account.dismiss()
            sp.saveSPBoolean(sharedPrefManager.SP_SUDAH_LOGIN,false)
            startActivity(Intent(mContext,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        }

    }

    private fun fecthUser() {
        list.clear()
        mProgress(progress)

        val ref = FirebaseDatabase.getInstance().getReference("/user")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    val user = it.getValue(User::class.java)!!
                    if (user.nama != sp.spChatFrom){
                        list.add(user)
                    } else {
                        nama = user.nama!!
                        email = user.email!!
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
                val chat = F_Chat()
                val bundle = Bundle()
                bundle.putString("nama",adapterUser.list[position].nama)
                bundle.putString("email",adapterUser.list[position].email)

                chat.arguments = bundle

                sp.saveSPString(sharedPrefManager.SP_CHAT_TO,adapterUser.list[position].nama!!)
                fragmentManager!!.beginTransaction().replace(R.id.id_fl_content,chat).addToBackStack(null).commit()
            }
        }))

        progress.dismiss()
        v.id_swipe_dashboard.isRefreshing = false
    }
}