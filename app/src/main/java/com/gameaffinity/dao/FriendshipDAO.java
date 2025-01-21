package com.gameaffinity.dao;

import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;

import java.util.List;

public interface FriendshipDAO {

    boolean sendFriendRequest(int requesterId, int receiverId);

    boolean checkValidRequest(int requesterId, int receiverId);

    List<Friendship> getFriendRequests(int userId);

    boolean updateFriendRequestStatus(int friendshipId, String status);

    List<UserBase> getFriends(int userId);

    Friendship getFriendshipById(int friendshipId);

    int getUserIdByEmail(String email);

    boolean deleteFriend(int userId, int friendId);
}
