package com.gameaffinity.model;

public enum GameState {
    AVAILABLE,   // Disponible en la biblioteca
    PLAYING,     // Actualmente en juego
    PAUSED,
    COMPLETED,   // Completado por el usuario
    DROPPED,   // Abandonado sin terminar
    WISHLIST
}
