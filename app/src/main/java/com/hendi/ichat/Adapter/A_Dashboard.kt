package com.hendi.ichat.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hendi.ichat.Help.User
import com.hendi.ichat.R
import kotlinx.android.synthetic.main.c_dashboard.view.*

class A_Dashboard(internal var mContext : Context, internal var list : List<User>) : RecyclerView.Adapter<A_Dashboard.HolderView>(){
    inner class HolderView (view : View) : RecyclerView.ViewHolder(view){
        fun data(user: User, position: Int) {
            itemView.id_title_cd.text = user.nama
            itemView.id_isi_cd.text = user.email
            Glide.with(mContext).load(user.image).into(itemView.id_image_cd)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): A_Dashboard.HolderView {
        val v = LayoutInflater.from(mContext).inflate(R.layout.c_dashboard,parent,false)

        return HolderView(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: A_Dashboard.HolderView, position: Int) {
        holder.data(list[position],position)
    }

}