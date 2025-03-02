package com.gameaffinity.service;

import com.gameaffinity.exception.GameAffinityException;
import com.gameaffinity.exception.UserNotFoundException;
import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.FriendshipStatus;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.repository.FriendshipRepository;
import com.gameaffinity.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    // 📌 Enviar solicitud de amistad
    @Transactional
    public boolean sendFriendRequest(String userEmail, String friendEmail) {
        UserBase requester = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
        UserBase receiver = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new UserNotFoundException(friendEmail));

        if (requester.equals(receiver)) {
            throw new GameAffinityException("❌ No puedes enviarte una solicitud a ti mismo.");
        }

        // ✅ Verificar si ya hay una solicitud pendiente en **cualquier dirección**
        Optional<Friendship> existingFriendship = friendshipRepository.findByRequesterAndReceiverOrReceiverAndRequester(
                requester, receiver, receiver, requester
        );

        if (existingFriendship.isPresent()) {
            throw new GameAffinityException("⚠️ Ya hay una solicitud pendiente o ya son amigos.");
        }

        // ✅ Crear la solicitud de amistad
        Friendship newFriendship = new Friendship(requester, receiver, FriendshipStatus.PENDING);
        friendshipRepository.save(newFriendship);

        return true;
    }


    // 📌 Obtener solicitudes de amistad recibidas
    public List<Friendship> getFriendRequests(String userEmail) {
        UserBase user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
        return friendshipRepository.findByReceiverAndStatus(user, FriendshipStatus.PENDING);
    }

    // 📌 Aceptar o rechazar solicitud de amistad
    @Transactional
    public boolean respondToFriendRequest(int friendshipId, FriendshipStatus status) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findById(friendshipId);
        if (friendshipOpt.isPresent()) {
            Friendship friendship = friendshipOpt.get();
            friendship.setStatus(status);
            friendshipRepository.save(friendship);
            return true;
        }
        return false;
    }

    // 📌 Obtener lista de amigos de un usuario
    public List<UserBase> getFriends(String userEmail) {
        UserBase user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
        return friendshipRepository.findFriends(user).stream()
                .map(friendship -> friendship.getRequester().equals(user) ? friendship.getReceiver() : friendship.getRequester())
                .toList();
    }

    // 📌 Obtener ID de usuario a partir del email
    public int getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserBase::getId)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    // 📌 Eliminar amistad
    @Transactional
    public boolean deleteFriend(String userEmail, String friendEmail) {
        UserBase user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
        UserBase friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new UserNotFoundException(friendEmail));

        Optional<Friendship> friendshipOpt = friendshipRepository.findByRequesterAndReceiverOrReceiverAndRequester(user, friend, friend, user);
        if (friendshipOpt.isPresent()) {
            friendshipRepository.delete(friendshipOpt.get());
            return true;
        }
        return false;
    }
}
