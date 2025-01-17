package com.gameaffinity.service;

import com.gameaffinity.dao.FriendshipDAO;
import com.gameaffinity.dao.FriendshipDAOImpl;
import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;

import java.util.List;

public class FriendshipService {
    private final FriendshipDAO friendshipDAO;

    public FriendshipService() throws Exception {
        this.friendshipDAO = new FriendshipDAOImpl();
    }

    public List<UserBase> searchUsers(String keyword) {
        return friendshipDAO.searchUsers(keyword);
    }

    public boolean sendFriendRequest(int requesterId, int receiverId) {
        return friendshipDAO.sendFriendRequest(requesterId, receiverId);
    }

    public boolean checkValidRequest(int requesterId, int receiverId) {
        return friendshipDAO.checkValidRequest(requesterId, receiverId);
    }

    public List<Friendship> getFriendRequests(int userId) {
        return friendshipDAO.getFriendRequests(userId);
    }

    public boolean respondToFriendRequest(int friendshipId, String status) {
        return friendshipDAO.updateFriendRequestStatus(friendshipId, status);
    }

    public List<UserBase> getFriends(int userId) {
        return friendshipDAO.getFriends(userId);
    }

    public int getUserIdByEmail(String userEmail) {
        return friendshipDAO.getUserIdByEmail(userEmail);
    }

    public boolean deleteFriend(int userId, int friendId) {
        return friendshipDAO.deleteFriend(userId, friendId);
    }
}