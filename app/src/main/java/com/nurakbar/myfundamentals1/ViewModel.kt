package com.nurakbar.myfundamentals1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nurakbar.myfundamentals1.api.ApiConfig
import com.nurakbar.myfundamentals1.response.DetailResponse
import com.nurakbar.myfundamentals1.response.FollowersResponseItem
import com.nurakbar.myfundamentals1.response.FollowingResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel: ViewModel() {
    private val _userDetails = MutableLiveData<DetailResponse>()
    val userDetail: LiveData<DetailResponse> get() = _userDetails

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _daftarFollower = MutableLiveData<List<FollowersResponseItem>>()
    val daftarFollower: LiveData<List<FollowersResponseItem>> get() = _daftarFollower

    private val _daftarFollowing = MutableLiveData<List<FollowingResponseItem>>()
    val daftarFollowing: LiveData<List<FollowingResponseItem>> get() = _daftarFollowing

    fun fetchUserDetails(username: String) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getDetailUser(username)

        call.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    _userDetails.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun getFollowerText(followerCount: Int): String {
        return "Followers: $followerCount"
    }

    fun getFollowingText(followingCount: Int): String {
        return "Following: $followingCount"
    }

    fun dapatkandatafollower(username: String) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getFollower(username)

        call.enqueue(object : Callback<List<FollowersResponseItem>> {
            override fun onResponse(call: Call<List<FollowersResponseItem>>, response: Response<List<FollowersResponseItem>>) {
                if (response.isSuccessful) {
                    _daftarFollower.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<FollowersResponseItem>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun dapatkandatafollowing(username: String) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getFollowing(username)

        call.enqueue(object : Callback<List<FollowingResponseItem>> {
            override fun onResponse(call: Call<List<FollowingResponseItem>>, response: Response<List<FollowingResponseItem>>) {
                if (response.isSuccessful) {
                    _daftarFollowing.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<FollowingResponseItem>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}