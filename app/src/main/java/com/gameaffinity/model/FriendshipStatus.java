package com.gameaffinity.model;

public enum FriendshipStatus {
    PENDING,   // Solicitud enviada, pendiente de aceptación
    ACCEPTED,  // Solicitud aceptada
    REJECTED,  // Solicitud rechazada
    BLOCKED    // Uno de los usuarios ha bloqueado al otro
}
