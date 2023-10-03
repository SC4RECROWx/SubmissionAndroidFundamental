package com.nurakbar.myfundamentals1.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nurakbar.myfundamentals1.R
import com.nurakbar.myfundamentals1.ViewModel
import com.nurakbar.myfundamentals1.adapter.ViewPagerAdapter
import com.nurakbar.myfundamentals1.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var detailActivityBinding: ActivityDetailBinding
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailActivityBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailActivityBinding.root)

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        val usernameTextView: TextView = detailActivityBinding.tvNameDetail
        val followerTextView: TextView = detailActivityBinding.tvFollowersDetail
        val followingTextView: TextView = detailActivityBinding.tvFollowingDetail
        val avatarImageView = detailActivityBinding.ivFotoDetail

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL)

        username?.let {
            viewModel.fetchUserDetails(it)
            viewModel.dapatkandatafollower(it)
            viewModel.dapatkandatafollowing(it)
        }

        viewModel.userDetail.observe(this, Observer { userDetail ->
            userDetail.let {
                usernameTextView.text = it.login
                followerTextView.text = viewModel.getFollowerText(it.followers ?: 0)
                followingTextView.text = viewModel.getFollowingText(it.following ?: 0)

                HideLoading()
            }
        })

        avatarUrl?.let {
            Glide.with(this)
                .load(it)
                .circleCrop()
                .placeholder(R.drawable.default_foto)
                .error(R.drawable.default_foto)
                .into(avatarImageView)

            ShowLoading()
        }
        val viewPager: ViewPager2 = detailActivityBinding.viewPager
        val tabsDetail: TabLayout = detailActivityBinding.tabsDetail

        val sectionsPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabsDetail,viewPager) { tabsDetail, position ->
            tabsDetail.text =
                if (position == 0) {
                "Followers"
                }
                else {
                "Following"
                }
        }.attach()
    }

    private fun HideLoading() {
        detailActivityBinding.progressBarDetail.visibility = View.GONE
    }

    private fun ShowLoading() {
        detailActivityBinding.progressBarDetail.visibility = View.VISIBLE
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
    }
}