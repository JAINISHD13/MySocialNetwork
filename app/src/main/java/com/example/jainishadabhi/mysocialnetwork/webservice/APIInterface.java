package com.example.jainishadabhi.mysocialnetwork.webservice;

import java.util.List;

import com.example.jainishadabhi.mysocialnetwork.model.GroupDetails;
import com.example.jainishadabhi.mysocialnetwork.model.GroupInvitation;
import com.example.jainishadabhi.mysocialnetwork.model.GroupKeyDetails;
import com.example.jainishadabhi.mysocialnetwork.model.GroupMainDetails;
import com.example.jainishadabhi.mysocialnetwork.model.PostDetails;
import com.example.jainishadabhi.mysocialnetwork.model.TimeLineDetails;
import com.example.jainishadabhi.mysocialnetwork.model.UserDetails;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("api/Register/LoginAuth")
    Call<List<UserDetails>> authenticate(@Query("email_id") String email_id, @Query("password") String password);

    @POST("api/Register/RegisterUser")
    Call<String> addUser(@Body UserDetails userDetails,@Query("group_key1") String group_key1);

    @GET("api/Post/FinalPostShowDown")
    Call<List<TimeLineDetails>> getPost(@Query("email_id") String email_id);

    @GET("api/Register/UserDetails")
    Call<List<UserDetails>> getUserDetails(@Query("email_id") String email_id);

    @POST("api/Group/CreateGroup")
    Call<String> addGroup(@Body GroupDetails groupDetails, @Query("group_key") String group_key, @Query("email_id") String email_id);

    @POST("api/FriendReq/SendReq")
    Call<String> sendFriendRequest(@Query("from_email_id") String from_email_id,@Query("to_email_id") String to_email_id);

    @GET("api/FriendReq/GetReq")
    Call<List<GroupInvitation>> getFriendRequest(@Query("to_email_id") String to_email_id);

    @POST("api/FriendReq/CancelFriendReq")
    Call<String> cancelFriendRequest(@Query("from_email_id") String from_email_id,@Query("to_email_id") String to_email_id);

    @GET("api/FriendReq/GetPublickeyData")
    Call<List<UserDetails>> getPublicKeyData(@Query("from_email_id") String from_email_id);

    @GET("api/FriendReq/GetGroupkeyData")
    Call<String> getGroupKeyData(@Query("to_email_id") String to_email_id);

    @POST("api/FriendReq/PostDataAfterRequest")
    Call<String> updateKeyData(@Body GroupMainDetails groupMainDetails,@Query("to_email_id") String to_email_id,@Query("group_key1") String group_key1,@Query("group_version") int group_version);

    @GET("api/Post/GetGroupDetailForPost")
    Call<List<GroupKeyDetails>> getGroupDetailsForPost(@Query("postMode") String postMode,@Query("email_id") String email_id);

    @POST("api/Post/AddPostData")
    Call<String> addPost(@Body PostDetails postDetails);

    @GET("api/FriendReq/GetMyFriends")
    Call<List<UserDetails>> getMyFriends(@Query("to_email_id") String to_email_id);

    @POST("api/Group/CreateNewGroup")
    Call<String> create_new_group(@Body GroupMainDetails groupMainDetails,@Query("groupstatus") String groupstatus,@Query("group_key1") String group_key1,@Query("group_version") int group_version);

    @GET("api/Group/GetGroup")
    Call<List<GroupDetails>> getGroups(@Query("to_email_id") String to_email_id);

    @GET("api/Group/GetAllGroups")
    Call<List<GroupDetails>> getGroupDetailsForRequest(@Query("email_id") String email_id);

    @POST("api/Group/SendGroupRequest")
    Call<String> sendGroupRequest(@Query("from_email_id") String from_email_id, @Query("group_id") int group_id,@Query("group_name") String group_name);

    @GET("api/Group/GetGroupRequest")
    Call<List<GroupInvitation>> getGroupRequest(@Query("to_email_id") String to_email_id);

    @POST("api/Group/CancelGroupReq")
    Call<String> cancelGroupRequest(@Query("from_email_id") String from_email_id,@Query("to_email_id") String to_email_id,@Query("group_id") int group_id);

    @GET("api/Group/GetPublicKeyDataForAcccept")
    Call<String> getPublicKeyForAcceptData(@Query("from_email_id") String to_email_id,@Query("group_id") int group_id,@Query("group_name") String group_name);

    @GET("api/Group/GetGroupKeyDataForAccept")
    Call<String> getGroupKeyDataForG(@Query("to_email_id") String to_email_id,@Query("group_id") int group_id,@Query("group_name") String group_name);

    @POST("api/Group/GetUpdatedDataAcceptRequest")
    Call<String> updateKeyDataForGroup(@Body GroupMainDetails groupMainDetails,@Query("to_email_id") String to_email_id,@Query("group_key1") String group_key1,@Query("group_version") int group_version);

    @POST("api/Post/AddPostPublicData")
    Call<String> addPostPublic(@Body PostDetails postDetails);

    @GET("api/Post/GetGroupKeyDataForGroupPost")
    Call<List<GroupKeyDetails>> getGroupKey(@Query("group_id") int group_id,@Query("email_id") String email_id);

    @POST("api/Post/FinalPostForGroup")
    Call<String> groupPostData(@Body PostDetails postDetails);

    @POST("api/Post/AddPrivatePost")
    Call<String> addPrivate(@Body PostDetails postDetails);

    /*

    @POST("api/Group/PrivatePost")
    Call<String> addPostPrivatre(@Body PostDetails postDetails);
*/


}