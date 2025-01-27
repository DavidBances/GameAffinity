package com.gameaffinity.dao;

import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;

import java.util.List;

public interface FriendshipDAO {

    String sendFriendRequest(int requesterId, int receiverId);

    String checkValidRequest(int requesterId, int receiverId);

    List<Friendship> getFriendRequests(int userId);

    boolean updateFriendRequestStatus(int friendshipId, String status);

    List<UserBase> getFriends(int userId);

    int getUserIdByEmail(String email);

    boolean deleteFriend(int userId, int friendId);
}
