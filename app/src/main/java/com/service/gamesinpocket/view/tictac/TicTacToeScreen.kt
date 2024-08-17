package com.service.gamesinpocket.view.tictac

import android.service.quicksettings.Tile
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.service.gamesinpocket.emun.GameMode
import com.service.gamesinpocket.emun.Player
import com.service.gamesinpocket.sealed.HomeEvent
import com.service.gamesinpocket.viewmodel.TicTacToeViewModel


@Composable
fun TicTacToeScreen(viewModel: TicTacToeViewModel,onExit: () -> Unit) {
    val board = viewModel.board
    val isGameOver = viewModel.isGameOver

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Yellow),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        with(viewModel.ticTacUiState) {
            when {
                modeSelection == null -> {
                    GameModeSelector(onGameModeChange = { mode ->
                        viewModel.onEvent(HomeEvent.UserMode(mode))
                    })
                }

                userSelection == null -> {
                    UserModeSelector(onUserSelection = { player ->
                        if (player == Player.NONE){
                            viewModel.onEvent(HomeEvent.PlayerBack)
                        }else {
                            viewModel.onEvent(HomeEvent.PlayerOption(player))
                        }
                    })
                }

                else -> {
                    Box(modifier = Modifier
                        .fillMaxSize().background(Color.Yellow)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp, 30.dp, 16.dp, 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            IconButton(
                                onClick = {
                                    onExit()
                                }, modifier = Modifier
                                    .size(64.dp)
                                    .padding(0.dp, 20.dp, 0.dp, 0.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.End)
                            ) {
                                androidx.compose.material3.Icon(
                                    painter = rememberVectorPainter(
                                        image = Icons.Rounded.Close
                                    ),
                                    contentDescription = "Close",
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            Text(
                                text = when {
                                    isGameOver -> if (viewModel.winner != null) "Winner: ${viewModel.winner}" else "It's a Draw!"
                                    else -> "Current Player: ${userSelection}"
                                },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                            Column {
                                for (row in 0..2) {
                                    Row {
                                        for (col in 0..2) {
                                            val index = row * 3 + col
                                            Tile(board[index]) {
                                                viewModel.makeMove(index)
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = { viewModel.resetGame() }) {
                                Text("Reset Game")
                            }
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun Tile(player: Player, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .background(Color.LightGray)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (player) {
                Player.X -> "X"
                Player.O -> "O"
                else -> ""
            },
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GameModeSelector(onGameModeChange: (GameMode) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 30.dp, 16.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "How do you want to play?", modifier = Modifier
                .align(Alignment.CenterHorizontally), fontSize = 25.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
        Spacer(modifier = Modifier.padding(15.dp))
        Row(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Button(onClick = { onGameModeChange(GameMode.SINGLE_PLAYER) }) {
                Text("Single Player")
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(onClick = { onGameModeChange(GameMode.TWO_PLAYER) }) {
                Text("Two Player")
            }
        }
    }
}


@Composable
fun UserModeSelector(onUserSelection: (Player) -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Would you like to be X or O?", modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally), fontSize = 25.sp,
        )
        Spacer(modifier = Modifier.padding(15.dp))
        Row(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Button(onClick = { onUserSelection(Player.O) }) {
                Text("Select Player 'O'")
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(onClick = { onUserSelection(Player.X) }) {
                Text("Select Player 'X'")
            }
        }

        Spacer(modifier = Modifier.padding(15.dp))
        Button(onClick = { onUserSelection(Player.NONE) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back")
        }
    }
}
