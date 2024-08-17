package com.service.gamesinpocket.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.service.gamesinpocket.emun.GameMode
import com.service.gamesinpocket.emun.Player
import com.service.gamesinpocket.sealed.HomeEvent
import com.service.gamesinpocket.view.tictac.TicTacUiState


class TicTacToeViewModel : ViewModel() {
    var ticTacUiState by mutableStateOf(TicTacUiState())
    var board by mutableStateOf(List(9) { Player.NONE })

    var currentPlayer by mutableStateOf(Player.NONE)

    var gameMode by mutableStateOf(GameMode.SINGLE_PLAYER)

    var winner by mutableStateOf<Player?>(null)

    var isGameOver by mutableStateOf(false)

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.PlayerBack -> {
                ticTacUiState = TicTacUiState()
            }
            is HomeEvent.UserMode -> {
                setTicTacGameMode(event.gameMode)
                ticTacUiState = ticTacUiState.copy(modeSelection = event.gameMode)
            }
            is HomeEvent.PlayerOption -> {
                setTicTacPlayer(event.selectedPlayer)
                ticTacUiState = ticTacUiState.copy(userSelection = event.selectedPlayer)
            }
            is HomeEvent.GameOn -> {
               // ticTacUiState = ticTacUiState.copy(gameOn = gameMode)
            }
        }
    }

    fun resetGame() {
        board = List(9) { Player.NONE }
        winner = null
        isGameOver = false
    }

    fun setTicTacGameMode(mode: GameMode) {
        resetGame()
        gameMode = mode
    }

    fun setTicTacPlayer(player: Player) {
        currentPlayer = player
    }

    fun makeMove(index: Int) {
        if (board[index] != Player.NONE || isGameOver) return

        board = board.toMutableList().also { it[index] = currentPlayer }
        checkWinner()

        if (!isGameOver && gameMode == GameMode.TWO_PLAYER) {
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
        }else if (!isGameOver && gameMode == GameMode.SINGLE_PLAYER) {
            makeComputerMove(if (currentPlayer == Player.X) Player.O else Player.X)
        }
    }

    private fun makeComputerMove(oponentPlayers:Player) {
        val emptyIndices = board.indices.filter { board[it] == Player.NONE }
        if (emptyIndices.isNotEmpty()) {
            val move = emptyIndices.random()
            board = board.toMutableList().also { it[move] = oponentPlayers}
            checkWinner()
        }
    }

    private fun checkWinner() {
        val winningCombinations = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Rows
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columns
            listOf(0, 4, 8), listOf(2, 4, 6)                   // Diagonals
        )

        for (combination in winningCombinations) {
            val (a, b, c) = combination
            if (board[a] == board[b] && board[a] == board[c] && board[a] != Player.NONE) {
                winner = board[a]
                isGameOver = true
                return
            }
        }

        if (board.none { it == Player.NONE }) {
            isGameOver = true
        }
    }
}