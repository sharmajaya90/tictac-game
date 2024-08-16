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

    var currentPlayer by mutableStateOf(Player.X)

    var gameMode by mutableStateOf(GameMode.TWO_PLAYER)

    var winner by mutableStateOf<Player?>(null)

    var isGameOver by mutableStateOf(false)

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.PlayerBack -> {
                ticTacUiState = ticTacUiState.copy(modeSelection = null)
            }
            is HomeEvent.UserMode -> {
                setTicTacGameMode(event.gameMode)
                ticTacUiState = ticTacUiState.copy(modeSelection = gameMode)
            }
            is HomeEvent.PlayerOption -> {
                setTicTacUserSelection(event.selectedPlayer)
                ticTacUiState = ticTacUiState.copy(userSelection = currentPlayer)
            }
            is HomeEvent.GameOn -> {
                setTicTacGameMode(event.gameMode)
                ticTacUiState = ticTacUiState.copy(gameOn = gameMode)
            }
        }
    }

    fun resetGame() {
        board = List(9) { Player.NONE }
        currentPlayer = Player.X
        winner = null
        isGameOver = false
    }

    fun setTicTacGameMode(mode: GameMode) {
        resetGame()
        gameMode = mode
    }

    private fun setTicTacUserSelection(selectedPlayer:Player) {
        currentPlayer = selectedPlayer
    }

    fun makeMove(index: Int) {
        if (board[index] != Player.NONE || isGameOver) return

        board = board.toMutableList().also { it[index] = currentPlayer }
        checkWinner()

        if (!isGameOver) {
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X

            if (gameMode == GameMode.SINGLE_PLAYER && currentPlayer == Player.O) {
                makeComputerMove()
            }
        }
    }

    private fun makeComputerMove() {
        val emptyIndices = board.indices.filter { board[it] == Player.NONE }
        if (emptyIndices.isNotEmpty()) {
            val move = emptyIndices.random()
            makeMove(move)
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