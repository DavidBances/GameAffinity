package com.gameaffinity.service;

import com.gameaffinity.dao.FriendshipDAO;
import com.gameaffinity.dao.FriendshipDAOImpl;
import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;

import java.sql.SQLException;
import java.util.List;

public class FriendshipService {
    private final FriendshipDAO friendshipDAO;

    public FriendshipService(){
        try {
            this.friendshipDAO = new FriendshipDAOImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean sendFriendRequest(int requesterId, int receiverId) {
        if(friendshipDAO.checkValidRequest(requesterId, receiverId)){
            return friendshipDAO.sendFriendRequest(requesterId, receiverId);
        }else{
            return false;
        }
    }

    public List<Friendship> getFriendRequests(int userId) {
        return friendshipDAO.getFriendRequests(userId);
    }

    public boolean respondToFriendRequest(int friendshipId, String status) {
        Friendship friendship = friendshipDAO.getFriendshipById(friendshipId);
        friendship.setStatus(status);
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
