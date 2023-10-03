package com.nurakbar.myfundamentals1.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurakbar.myfundamentals1.ViewModel
import com.nurakbar.myfundamentals1.adapter.FollowersAdapter
import com.nurakbar.myfundamentals1.adapter.FollowingAdapter
import com.nurakbar.myfundamentals1.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private lateinit var followFragmentBinding: FragmentFollowBinding
    private lateinit var viewModel: ViewModel
    private lateinit var username: String

    private fun showLoading() {
        followFragmentBinding.progressBarFollow.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        followFragmentBinding.progressBarFollow.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followFragmentBinding = FragmentFollowBinding.inflate(inflater, container,false)
        val view = followFragmentBinding.root

        followFragmentBinding.recyclerViewFollow
        followFragmentBinding.progressBarFollow
        followFragmentBinding.tvErrorInfo

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        followFragmentBinding.recyclerViewFollow.layoutManager = layoutManager

        username = arguments?.getString(USERNAME) ?: ""
        arguments?.getInt(POSITION,0)?.let { index ->
            showLoading()
            if (index == 1) {
                viewModel.daftarFollower.observe(viewLifecycleOwner) { followers ->
                    if (followers != null && followers.isNotEmpty()) {
                        followFragmentBinding.recyclerViewFollow.visibility = View.VISIBLE
                        followFragmentBinding.tvErrorInfo.visibility = View.GONE
                        val adapter = FollowersAdapter(followers)
                        followFragmentBinding.recyclerViewFollow.adapter = adapter
                    }
                    else {
                        followFragmentBinding.recyclerViewFollow.visibility = View.GONE
                        followFragmentBinding.tvErrorInfo.visibility = View.VISIBLE
                        followFragmentBinding.tvErrorInfo.text = "No Followers"
                    }
                    hideLoading()
                }
            }
            else {
                viewModel.daftarFollowing.observe(viewLifecycleOwner) { followings ->
                    if (followings != null && followings.isNotEmpty()) {
                        followFragmentBinding.recyclerViewFollow.visibility = View.VISIBLE
                        followFragmentBinding.tvErrorInfo.visibility = View.GONE
                        val adapter = FollowingAdapter(followings)
                        followFragmentBinding.recyclerViewFollow.adapter = adapter
                    } else {
                        followFragmentBinding.recyclerViewFollow.visibility = View.GONE
                        followFragmentBinding.tvErrorInfo.visibility = View.VISIBLE
                        followFragmentBinding.tvErrorInfo.text = "Not following anyone."
                    }
                    hideLoading()
                }
            }
        }
    }
    companion object {
        const val POSITION = "position"
        const val USERNAME = "username"
    }
}