package com.service.gamesinpocket.sealed

import com.service.gamesinpocket.emun.GameMode
import com.service.gamesinpocket.emun.Player

sealed class HomeEvent {
    data class UserMode(val gameMode: GameMode) : HomeEvent()
    data class PlayerOption(val selectedPlayer: Player) : HomeEvent()
    data class GameOn(val gameMode: GameMode) : HomeEvent()
    object PlayerBack : HomeEvent()
}