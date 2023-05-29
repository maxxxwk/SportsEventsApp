@file:Suppress("FunctionNaming")

package com.maxxxwk.sportseventsapp.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.maxxxwk.sportseventsapp.R
import com.maxxxwk.sportseventsapp.core.uitext.UIText
import com.maxxxwk.sportseventsapp.presentation.models.SportEventUIModel
import com.maxxxwk.sportseventsapp.presentation.models.SportUIModel

@Composable
fun SportsEventsScreen(viewModel: SportsEventsScreenViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (state) {
        is SportsEventsScreenState.Content -> SportsEventsScreenContent(
            (state as SportsEventsScreenState.Content).sportsEvents,
            viewModel::changeIsFavorite,
            viewModel::hideEvents
        )
        is SportsEventsScreenState.Error -> SportsEventsScreenError(
            (state as SportsEventsScreenState.Error).message,
            viewModel::retryLoading
        )
        SportsEventsScreenState.Loading -> SportsEventsScreenLoading()
    }
}

@Composable
private fun SportsEventsScreenContent(
    sportsEvents: List<SportUIModel>,
    changeIsFavorite: (String) -> Unit,
    hideEvents: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(items = sportsEvents) { index, item ->
            SportItem(
                sport = item,
                changeIsFavorite = changeIsFavorite,
                hideEvents = { hideEvents(index) }
            )
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun SportItem(
    sport: SportUIModel,
    changeIsFavorite: (String) -> Unit,
    hideEvents: () -> Unit
) {
    val rotationState by animateFloatAsState(targetValue = if (sport.isExpanded) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = rememberAsyncImagePainter(sport.iconURL),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = sport.name,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = hideEvents,
                modifier = Modifier.rotate(rotationState)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
        if (sport.isExpanded) {
            SportEvents(sport.events, changeIsFavorite)
        }
    }
}

@Composable
private fun SportEvents(events: List<SportEventUIModel>, markAsFavorite: (String) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.secondary),
        contentPadding = PaddingValues(start = 8.dp)
    ) {
        items(items = events, key = { it.id }) {
            SportEventItem(it, markAsFavorite)
        }
    }
}

@Composable
private fun SportEventItem(event: SportEventUIModel, markAsFavorite: (String) -> Unit) {
    Column(
        Modifier
            .width(120.dp)
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(1.dp, MaterialTheme.colors.onSecondary),
            color = MaterialTheme.colors.secondary
        ) {
            Text(
                text = event.timeToStart,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(4.dp)
            )
        }
        IconButton(
            onClick = { markAsFavorite(event.id) },
            modifier = Modifier
                .size(32.dp)
                .padding(vertical = 4.dp)
        ) {
            if (event.isFavorite) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    tint = Color.Yellow,
                    contentDescription = null
                )
            } else {
                Icon(
                    imageVector = Icons.TwoTone.Star,
                    tint = Color.Gray,
                    contentDescription = null
                )
            }
        }
        Text(
            text = event.name,
            minLines = 2,
            maxLines = 2,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSecondary,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SportsEventsScreenError(message: UIText, retry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message.asString(),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = retry,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary
            ),
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.retry_button_text))
        }
    }
}

@Composable
private fun SportsEventsScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
    }
}
