package com.gameaffinity.repository;

import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.FriendshipStatus;
import com.gameaffinity.model.UserBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    // Buscar una amistad específica entre dos usuarios

    Optional<Friendship> findByRequesterAndReceiverOrReceiverAndRequester(UserBase user1, UserBase user2, UserBase user2Again, UserBase user1Again);

    // Obtener solicitudes de amistad recibidas por un usuario
    List<Friendship> findByReceiverAndStatus(UserBase receiver, FriendshipStatus status);

    // Obtener lista de amigos (amistades aceptadas)
    @Query("SELECT f FROM Friendship f WHERE (f.requester = :user OR f.receiver = :user) AND f.status = 'ACCEPTED'")
    List<Friendship> findFriends(UserBase user);
}
