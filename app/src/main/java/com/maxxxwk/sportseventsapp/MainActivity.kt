package com.maxxxwk.sportseventsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maxxxwk.sportseventsapp.presentation.SportsEventsScreen
import com.maxxxwk.sportseventsapp.presentation.SportsEventsScreenViewModel
import com.maxxxwk.sportseventsapp.ui.theme.SportsEventsAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: SportsEventsScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).component.inject(this)
        setContent {
            SportsEventsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.primary
                ) {
                    SportsEventsScreen(viewModel = viewModel { viewModel })
                }
            }
        }
    }
}