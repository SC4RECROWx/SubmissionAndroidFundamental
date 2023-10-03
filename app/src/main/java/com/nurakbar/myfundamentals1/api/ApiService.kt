package com.nurakbar.myfundamentals1.api

import com.nurakbar.myfundamentals1.response.DetailResponse
import com.nurakbar.myfundamentals1.response.FollowersResponseItem
import com.nurakbar.myfundamentals1.response.FollowingResponseItem
import retrofit2.Call
import com.nurakbar.myfundamentals1.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Detail
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailResponse>

    // Search
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<SearchResponse>

    // Followers
    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username: String
    ): Call<List<FollowersResponseItem>>

    // Following
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<FollowingResponseItem>>
}