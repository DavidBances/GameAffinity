package com.gameaffinity.service;

import com.gameaffinity.dao.FriendshipDAO;
import com.gameaffinity.dao.FriendshipDAOImpl;
import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipDAO friendshipDAO;

    public FriendshipService() {
        try {
            this.friendshipDAO = new FriendshipDAOImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean sendFriendRequest(String userEmail, String friendEmail) {
        if (friendshipDAO.checkValidRequest(getUserIdByEmail(userEmail), getUserIdByEmail(friendEmail))) {
            return friendshipDAO.sendFriendRequest(getUserIdByEmail(userEmail), getUserIdByEmail(friendEmail));
        } else {
            return false;
        }
    }

    public List<Friendship> getFriendRequests(int userId) {
        return friendshipDAO.getFriendRequests(userId);
    }

    public boolean respondToFriendRequest(Friendship friendship, String status) {
        friendship.setStatus(status);
        return friendshipDAO.updateFriendRequestStatus(friendship.getId(), status);
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
