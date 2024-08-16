package com.service.gamesinpocket.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.service.gamesinpocket.sealed.HomeEvent
import com.service.gamesinpocket.view.theme.GamesInPocketTheme
import com.service.gamesinpocket.view.tictac.TicTacToeScreen
import com.service.gamesinpocket.viewmodel.TicTacToeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamesInPocketTheme {
                val viewModel: TicTacToeViewModel = viewModel()
                TicTacToeScreen(viewModel = viewModel){
                    finish()
                }
            }
        }
    }
}