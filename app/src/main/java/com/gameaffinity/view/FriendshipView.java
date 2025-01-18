package com.gameaffinity.view;

import com.gameaffinity.controller.FriendshipController;
import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.util.List;

public class FriendshipView {

    @FXML
    private TableView<UserBase> friendsTable;
    @FXML
    private TableView<Friendship> requestsTable;
    @FXML
    private TableColumn<UserBase, String> nameColumn;
    @FXML
    private TableColumn<UserBase, String> emailColumn;
    @FXML
    private TableColumn<Friendship, Integer> requestIdColumn;
    @FXML
    private TableColumn<Friendship, String> requesterEmailColumn;
    @FXML
    private TableColumn<Friendship, String> statusColumn;
    @FXML
    private Button acceptButton;
    @FXML
    private Button rejectButton;
    @FXML
    private Button sendRequestButton;
    @FXML
    private Button viewFriendLibraryButton;
    @FXML
    private Button deleteFriendButton;
    @FXML
    private Button backButton;
    @FXML
    private FlowPane bottomPanel;

    private final FriendshipController friendshipController = new FriendshipController();
    private int userId;
    private String userRole;

    public void initialize() {
        // Configure table columns for friendsTable
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Configure table columns for requestsTable
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        requesterEmailColumn.setCellValueFactory(new PropertyValueFactory<>("requesterEmail"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Event handlers for buttons
        acceptButton.setOnAction(e -> acceptRequest());
        rejectButton.setOnAction(e -> rejectRequest());
        sendRequestButton.setOnAction(e -> sendFriendRequest());
        viewFriendLibraryButton.setOnAction(e -> viewFriendLibrary());
        deleteFriendButton.setOnAction(e -> deleteFriend());
        backButton.setOnAction(e -> goBack());
    }

    public void setUserData(int userId, String userRole) {
        this.userId = userId;
        this.userRole = userRole;
        refreshFriendsList();
        refreshPendingRequests();
    }

    private void refreshFriendsList() {
        List<UserBase> friends = friendshipController.getFriends(userId);
        friendsTable.getItems().setAll(friends);
    }

    private void refreshPendingRequests() {
        List<Friendship> requests = friendshipController.getFriendRequests(userId);
        requestsTable.getItems().setAll(requests);
    }

    private void acceptRequest() {
        Friendship selectedRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            boolean success = friendshipController.respondToFriendRequest(selectedRequest.getId(), "Accepted");
            JOptionPane.showMessageDialog(null, success ? "Request accepted!" : "Failed to accept request.");
            refreshPendingRequests();
            refreshFriendsList();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a request to accept.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rejectRequest() {
        Friendship selectedRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            boolean success = friendshipController.respondToFriendRequest(selectedRequest.getId(), "Rejected");
            JOptionPane.showMessageDialog(null, success ? "Request rejected!" : "Failed to reject request.");
            refreshPendingRequests();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a request to reject.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendFriendRequest() {
        String receiverEmail = JOptionPane.showInputDialog("Enter the User email of the person you want to add:");
        int receiverId = friendshipController.getUserIdByEmail(receiverEmail);
        boolean success = false;

        if (receiverId != -1) {
            if (friendshipController.checkValidRequest(userId, receiverId)) {
                success = friendshipController.sendFriendRequest(userId, receiverId);
            }
            JOptionPane.showMessageDialog(null, success ? "Friend request sent!" : "Failed to send friend request.");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid User Email.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewFriendLibrary() {
        UserBase selectedFriend = friendsTable.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            try {
                int friendId = selectedFriend.getId();
                // Assuming you have a method to navigate to the friend's library view
                // (You can create another FXML for FriendLibraryView similar to the one for
                // Friendship)
                // If using JavaFX, you would typically use FXMLLoader to load a new view
                System.out.println("Viewing friend's library for ID: " + friendId);
                // Load friend library view and pass friendId (for example using FXMLLoader)
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error loading friend's library: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a friend to view their library.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFriend() {
        UserBase selectedFriend = friendsTable.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            int friendId = selectedFriend.getId();
            boolean success = friendshipController.deleteFriend(userId, friendId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Friend deleted successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshFriendsList();
            } else {
                JOptionPane.showMessageDialog(null, "Friend can't be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void goBack() {
        // Go back to the previous screen (e.g., User Dashboard)
        // You would need to handle this with appropriate view navigation logic
    }
}
