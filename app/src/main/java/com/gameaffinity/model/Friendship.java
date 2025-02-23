package com.gameaffinity.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private UserBase requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserBase receiver;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    public Friendship() {
    }

    public Friendship(UserBase requester, UserBase receiver, FriendshipStatus status) {
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public UserBase getRequester() {
        return requester;
    }

    public void setRequester(UserBase requester) {
        this.requester = requester;
    }

    public UserBase getReceiver() {
        return receiver;
    }

    public void setReceiver(UserBase receiver) {
        this.receiver = receiver;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", requester=" + requester.getName() +
                ", receiver=" + receiver.getName() +
                ", status=" + status +
                '}';
    }
}
