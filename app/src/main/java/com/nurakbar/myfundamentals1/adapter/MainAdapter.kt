package com.nurakbar.myfundamentals1.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nurakbar.myfundamentals1.R
import com.nurakbar.myfundamentals1.databinding.UserListBinding
import com.nurakbar.myfundamentals1.response.SearchResponse

class MainAdapter(private val userList: MutableList<SearchResponse.ItemsItem>):
RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var onItemClickCallback: ((SearchResponse.ItemsItem) -> Unit)? = null
    private val spacingInPixels: Int = 2.dpToPx()

    fun setOnItemClickCallback(callback: (SearchResponse.ItemsItem) -> Unit) {
        onItemClickCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list, parent, false)
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0, spacingInPixels, 0, 0)  // Atur margin di sini (atas)
        view.layoutParams = params
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.username.text = user.login ?: "No Username"

        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .circleCrop()
            .placeholder(R.drawable.default_foto)
            .error(R.drawable.default_foto)
            .into(holder.avatar)

        ViewCompat.setTransitionName(holder.avatar, "avatar_transition_$position")
        holder.itemView.setOnClickListener {
            onItemClickCallback?.invoke(user)
        }

        // Tambahkan margin bawah pada item, kecuali untuk item terakhir
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0, spacingInPixels, 0, if (position == userList.size - 1) 0 else spacingInPixels)
        holder.itemView.layoutParams = params
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateUsers(newList: List<SearchResponse.ItemsItem?>) {
        userList.clear()
        userList.addAll(newList.filterNotNull())
        notifyDataSetChanged()
    }

    private fun Int.dpToPx(): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (this * density).toInt()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.iv_foto_userlist)
        val username: TextView = itemView.findViewById(R.id.tv_username_userlist)
    }
}