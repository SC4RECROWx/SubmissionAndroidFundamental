package com.nurakbar.myfundamentals1.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nurakbar.myfundamentals1.R
import com.nurakbar.myfundamentals1.response.FollowersResponseItem

class FollowersAdapter(
    private val userList: List<FollowersResponseItem>
): RecyclerView.Adapter<FollowersAdapter.FollowViewHolder>() {

    class FollowViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private val usernametv: TextView = v.findViewById(R.id.tv_username_userlist)
        private val useriv: ImageView = v.findViewById(R.id.iv_foto_userlist)

        fun bind(user: FollowersResponseItem) {
            usernametv.text = user.login
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .into(useriv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list,parent,false)
        return FollowViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val user = userList!![position]!!
        holder.bind(user)

        val spacingInPixels: Int = 2.dpToPx()
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0, 0, 0, if (position == userList.size - 1) 0 else spacingInPixels)
    }

    override fun getItemCount(): Int {
        return userList?.size ?: 0
    }

    private fun Int.dpToPx(): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (this * density).toInt()
    }
}