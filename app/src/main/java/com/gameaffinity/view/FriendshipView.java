package com.gameaffinity.view;

import com.gameaffinity.controller.FriendshipController;
import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
    private TableColumn<Friendship, String> requesterEmailColumn;
    @FXML
    private TableColumn<Friendship, Void> actionsColumn;
    @FXML
    private Button sendRequestButton;
    @FXML
    private Button viewFriendLibraryButton;
    @FXML
    private Button deleteFriendButton;
    @FXML
    private Button backButton;

    private final FriendshipController friendshipController = new FriendshipController();
    private UserBase user;

    public void initialize() {
        friendsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        requestsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // Configure table columns for friendsTable
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Configure table columns for requestsTable
        requesterEmailColumn.setCellValueFactory(new PropertyValueFactory<>("requesterEmail"));
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button acceptButton = new Button("✔");
            private final Button rejectButton = new Button("❌");

            {
                acceptButton.setOnAction(event -> {
                    Friendship request = getTableRow().getItem();
                    acceptRequest(request);
                });

                rejectButton.setOnAction(event -> {
                    Friendship request = getTableRow().getItem();
                    rejectRequest(request);
                });

                acceptButton.getStyleClass().add("accept-button");
                rejectButton.getStyleClass().add("reject-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, acceptButton, rejectButton);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }
            }
        });

        // Event handlers for buttons
        sendRequestButton.setOnAction(e -> sendFriendRequest());
        viewFriendLibraryButton.setOnAction(e -> viewFriendLibrary());
        deleteFriendButton.setOnAction(e -> deleteFriend());
        backButton.setOnAction(e -> goBack());
    }

    public void setUser(UserBase user) {
        this.user = user;
        refreshFriendsList();
        refreshPendingRequests();
    }

    private void refreshFriendsList() {
        List<UserBase> friends = friendshipController.getFriends(this.user.getId());
        friendsTable.getItems().setAll(friends);
    }

    private void refreshPendingRequests() {
        List<Friendship> requests = friendshipController.getFriendRequests(this.user.getId());
        requestsTable.getItems().setAll(requests);
    }

    private void acceptRequest(Friendship selectedRequest) {
        if (selectedRequest != null) {
            boolean success = friendshipController.respondToFriendRequest(selectedRequest.getId(), "Accepted");
            showAlert(success ? "Request accepted!" : "Failed to accept request.", "", success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            refreshPendingRequests();
            refreshFriendsList();
        } else {
            showAlert("Please select a request to accept.", "Error",
                    Alert.AlertType.ERROR);
        }
    }

    private void rejectRequest(Friendship selectedRequest) {
        if (selectedRequest != null) {
            boolean success = friendshipController.respondToFriendRequest(selectedRequest.getId(), "Rejected");
            showAlert(success ? "Request rejected!" : "Failed to reject request.", "", success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            refreshPendingRequests();
        } else {
            showAlert("Please select a request to reject.", "Error",
                    Alert.AlertType.ERROR);
        }
    }

    private void sendFriendRequest() {
        String receiverEmail = showInputDialog("Enter the User email of the person you want to add:");
        int receiverId = friendshipController.getUserIdByEmail(receiverEmail);

        if (receiverId != -1) {
            boolean success = friendshipController.sendFriendRequest(this.user.getId(), receiverId);
            showAlert(success ? "Friend request sent!" : "Failed to send friend request.", "", success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        } else {
            showAlert("Invalid User Email.", "Error", Alert.AlertType.ERROR);
        }
    }

    private void viewFriendLibrary() {
        UserBase selectedFriend = friendsTable.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            try {
                Stage newStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/friendship/friend_library_view.fxml"));
                Parent friendLibraryView = loader.load();

                FriendLibraryView controller = loader.getController();
                controller.setUser(selectedFriend);

                Scene friendshipViewScene = new Scene(friendLibraryView);
                newStage.setScene(friendshipViewScene);

                newStage.setTitle("Friend's Library - " + selectedFriend.getName());
                newStage.show();
            } catch (Exception ex) {
                showAlert("Error loading friend's library: " + ex.getMessage(), "Error",
                        Alert.AlertType.ERROR);
                ex.printStackTrace();
            }
        } else {
            showAlert("Please select a friend to view their library.", "Error",
                    Alert.AlertType.ERROR);
        }
    }

    private void deleteFriend() {
        UserBase selectedFriend = friendsTable.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            int friendId = selectedFriend.getId();
            boolean success = friendshipController.deleteFriend(this.user.getId(), friendId);
            if (success) {
                showAlert("Friend deleted successfully.", "Success", Alert.AlertType.INFORMATION);
                refreshFriendsList();
            } else {
                showAlert("Friend can't be deleted.", "Error", Alert.AlertType.ERROR);
            }
        }
    }

    private void goBack() {
            try {
                Stage currentStage = (Stage) friendsTable.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/user_dashboard.fxml"));
                Parent userDashboard = loader.load();

                UserDashboardView controller = loader.getController();
                controller.setUser(this.user);

                Scene userScene = new Scene(userDashboard);
                currentStage.setScene(userScene);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private String showInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(message);
        dialog.setTitle("Input");
        return dialog.showAndWait().orElse(null);
    }
}
