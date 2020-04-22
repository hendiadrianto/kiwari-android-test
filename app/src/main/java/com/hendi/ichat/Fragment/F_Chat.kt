package com.hendi.ichat.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.hendi.ichat.Help.Chat
import com.hendi.ichat.Help.sharedPrefManager
import com.hendi.ichat.R
import com.hendi.kandangku.Helper.mDate
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from.view.*
import kotlinx.android.synthetic.main.chat_to.view.*
import kotlinx.android.synthetic.main.f_chat.view.*

class F_Chat : Fragment() {

    lateinit var v: View
    lateinit var mContext: Context

    lateinit var mDatabase: FirebaseDatabase
    lateinit var mReference: DatabaseReference

    lateinit var sp: sharedPrefManager

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = layoutInflater.inflate(R.layout.f_chat, container, false)

        mContext = this.context!!
        sp = sharedPrefManager(mContext)
        mDatabase = FirebaseDatabase.getInstance()
        mReference = mDatabase.getReference("message")

        Log.d("from", sp.spChatFrom)
        Log.d("to", sp.spChatTo)

        Log.d("Masuk Chat", "F Chat")


        v.id_btn_kirim.setOnClickListener {
            if (v.id_pesan_chat.text.isNotEmpty()){
                sendChat()
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
                        Log.d("Masuk Ismail", "LOG")
                        adapter.add(ChatFrom(it.isi!!, it.tanggal!!, it.id!!))
                    } else {
                        Log.d("Masuk Jarjit", "LOG")
                        adapter.add(ChatTo(it.isi!!, it.tanggal!!, it.id!!))
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

        val chatID2 = mReference.push().key
        val isi2 = Chat(v.id_pesan_chat.text.toString(), mDate(), sp.spChatTo!!)
        val fromRef = FirebaseDatabase.getInstance().getReference("/message/${sp.spChatFrom}/${sp.spChatTo}")
        val toRef = FirebaseDatabase.getInstance().getReference("/message/${sp.spChatTo}/${sp.spChatFrom}")

        mReference.child(sp.spChatFrom!!).child(sp.spChatTo!!).child(chatID2!!).setValue(isi2).addOnSuccessListener {
            v.id_pesan_chat.text.clear()
            v.id_rv_chat.smoothScrollToPosition(adapter.itemCount - 1)
            Log.d("Send","success")
        }

//        fromRef.setValue(isi2).addOnSuccessListener {
//            v.id_pesan_chat.text.clear()
//            v.id_rv_chat.smoothScrollToPosition(adapter.itemCount - 1)
//        }

        val chatID = mReference.push().key
        val isi1 = Chat(v.id_pesan_chat.text.toString(), mDate(), sp.spChatFrom!!)

        mReference.child(sp.spChatTo!!).child(sp.spChatFrom!!).child(chatID!!).setValue(isi1).addOnSuccessListener {
            Log.d("send To","success")
        }

    }

    class ChatFrom(val text: String, val timestamp: String, id: String) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            viewHolder.itemView.textview_from_row.text = text
            viewHolder.itemView.from_msg_time.text = timestamp

            val targetImageView = viewHolder.itemView.imageview_chat_from_row

//            if (!user.profileImageUrl!!.isEmpty()) {
//
//                val requestOptions = RequestOptions().placeholder(com.google.firebase.database.R.drawable.no_image2)
//
//
//                Glide.with(targetImageView.context)
//                    .load(user.profileImageUrl)
//                    .thumbnail(0.1f)
//                    .apply(requestOptions)
//                    .into(targetImageView)
//
//            }
        }

        override fun getLayout(): Int {
            return R.layout.chat_from
        }

    }

    class ChatTo(val text: String, val timestamp: String, id: String) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textview_to_row.text = text
            viewHolder.itemView.to_msg_time.text = timestamp

            val targetImageView = viewHolder.itemView.imageview_chat_to_row

            //if (!user.profileImageUrl!!.isEmpty()) {

//                val requestOptions = RequestOptions().placeholder(com.google.firebase.database.R.drawable.no_image2)
//
//                Glide.with(targetImageView.context)
//                    .load(user.profileImageUrl)
//                    .thumbnail(0.1f)
//                    .apply(requestOptions)
//                    .into(targetImageView)
//
//            }
        }

        override fun getLayout(): Int {
            return R.layout.chat_to
        }

    }

}