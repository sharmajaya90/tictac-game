package com.service.gamesinpocket.view.tictac

import com.service.gamesinpocket.emun.GameMode
import com.service.gamesinpocket.emun.Player


data class TicTacUiState(
    val modeSelection: GameMode? = null,
    val userSelection: Player? = null,
    val gameOn: GameMode? = null
)
