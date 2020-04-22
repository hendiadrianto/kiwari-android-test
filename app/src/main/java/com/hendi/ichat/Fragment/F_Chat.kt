package com.hendi.ichat.Fragment

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.hendi.ichat.Help.Chat
import com.hendi.ichat.Help.sharedPrefManager
import com.hendi.ichat.R
import com.hendi.kandangku.Helper.mDate
import com.hendi.kandangku.Helper.mNetworkAvailable
import com.hendi.kandangku.Helper.mProgress
import com.hendi.kandangku.Helper.mToast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from.view.*
import kotlinx.android.synthetic.main.chat_to.view.*
import kotlinx.android.synthetic.main.d_account.*
import kotlinx.android.synthetic.main.f_chat.view.*

class F_Chat : Fragment() {

    lateinit var v: View
    lateinit var mContext: Context

    lateinit var mDatabase: FirebaseDatabase
    lateinit var mReference: DatabaseReference

    lateinit var sp: sharedPrefManager
    lateinit var progress : Dialog

    var nama = ""
    var email = ""

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("On create","menu")

        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.f_chat, container, false)

        (activity as AppCompatActivity).setSupportActionBar(v.id_toolbar_chat)
        if (arguments != null){
            if (arguments!!.getString("nama") != null){
                nama = arguments!!.getString("nama")!!
                (activity as AppCompatActivity).supportActionBar!!.setTitle(nama)
            }
            if (arguments!!.getString("email") != null){
                email = arguments!!.getString("email")!!
            }
        }

        mContext = this.context!!
        sp = sharedPrefManager(mContext)
        mDatabase = FirebaseDatabase.getInstance()
        progress = Dialog(mContext)
        mReference = mDatabase.getReference("message")

        sp.saveSPBoolean(sharedPrefManager.SP_BACK,true)

        v.id_btn_kirim.setOnClickListener {
            if (v.id_pesan_chat.text.isNotEmpty()){
                if (mNetworkAvailable(mContext)) sendChat() else mToast(mContext,"Aktifkan koneksi internet terlebih dahulu !")
            } else {
                v.id_pesan_chat.setError("Tidak boleh kosong !")
            }
        }

        readChat()

        return v
    }

    private fun readChat() {
        adapter.clear()
        v.swiperefresh.isEnabled = true
        v.swiperefresh.isRefreshing = true

        val ref = FirebaseDatabase.getInstance().getReference("/message/${sp.spChatFrom}/${sp.spChatTo}")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", "database error: " + databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("TAG", "has children: " + dataSnapshot.hasChildren())
                if (!dataSnapshot.hasChildren()) {
                    v.swiperefresh.isRefreshing = false
                    v.swiperefresh.isEnabled = false
                }
            }
        })

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                adapter.notifyDataSetChanged()
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                dataSnapshot.getValue(Chat::class.java)?.let {
                    if (it.id == sp.spChatFrom) {
                        adapter.add(ChatTo(mContext,it.isi!!, it.tanggal!!, it.id!!))
                    } else {
                        adapter.add(ChatFrom(mContext,it.isi!!, it.tanggal!!, it.id!!))
                    }
                }

                v.id_rv_chat.adapter = adapter
                v.id_rv_chat.scrollToPosition(adapter.itemCount - 1)
                adapter.notifyDataSetChanged()
                v.swiperefresh.isRefreshing = false
                v.swiperefresh.isEnabled = false
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

        })
    }

    private fun sendChat() {
        mProgress(progress)
        val chatID1 = mReference.push().key
        val isi1 = Chat(v.id_pesan_chat.text.toString(), mDate(), sp.spChatFrom!!)

        mReference.child(sp.spChatFrom!!).child(sp.spChatTo!!).child(chatID1!!).setValue(isi1).addOnSuccessListener {
            v.id_pesan_chat.text.clear()
            v.id_rv_chat.smoothScrollToPosition(adapter.itemCount - 1)
            Log.d("Send","success")
        }

        val chatID2 = mReference.push().key
        val isi2 = Chat(v.id_pesan_chat.text.toString(), mDate(), sp.spChatFrom!!)

        mReference.child(sp.spChatTo!!).child(sp.spChatFrom!!).child(chatID2!!).setValue(isi2).addOnSuccessListener {
            Log.d("send To","success")
        }

        progress.dismiss()
    }

    class ChatFrom(internal var mContext : Context,val text: String, val tanggal: String, id: String) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {
            val sp = sharedPrefManager(mContext)
            viewHolder.itemView.id_text_from.text = text
            viewHolder.itemView.id_tanggal_from.text = tanggal
            if (sp.spImageTo != null){
                Glide.with(mContext).load(sp.spImageTo).into(viewHolder.itemView.id_image_from)
            }

        }

        override fun getLayout(): Int {
            return R.layout.chat_from
        }

    }

    class ChatTo(internal var mContext : Context,val text: String, val tanggal : String, id: String) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {
            val sp = sharedPrefManager(mContext)
            viewHolder.itemView.id_text_to.text = text
            viewHolder.itemView.id_tanggal_to.text = tanggal
            if (sp.spImageFrom != null){
                Glide.with(mContext).load(sp.spImageFrom).into(viewHolder.itemView.id_image_to)
            }

        }

        override fun getLayout(): Int {
            return R.layout.chat_to
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account,menu)
        Log.d("On create tollbar","menu")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.id_menu_profile_account -> {profile_account()}
        }

        return super.onOptionsItemSelected(item)
    }

    private fun profile_account() {
        val account = Dialog(mContext)
        account.setContentView(R.layout.d_account)
        account.window!!.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        account.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        account.window!!.attributes.windowAnimations = R.style.Widget_AppCompat_PopupWindow
        account.setCancelable(false)
        account.show()

        if (sp.spImageFrom != null){
            Glide.with(mContext).load(sp.spImageTo).into(account.id_image_account)
        }

        account.id_title_account.text = nama
        account.id_email_account.text = email

        account.id_btn_back.setOnClickListener {
            account.dismiss()
        }

        account.id_btn_logout.visibility = View.GONE
    }

}