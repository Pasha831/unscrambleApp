package com.example.unscrablenew

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscrablenew.model.Letter
import com.example.unscrablenew.ui.theme.UnscrableNewTheme
import com.example.unscrablenew.util.Constants.MAX_NO_OF_WORDS
import com.example.unscrablenew.viewmodels.Errors
import com.example.unscrablenew.viewmodels.GameEvent
import com.example.unscrablenew.viewmodels.GameViewModel
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnscrableNewTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    topBar = { TopBar() },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { contentPadding ->
                    GameScreen(
                        modifier = Modifier.padding(contentPadding),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.unscramble),
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel(),
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val eventHandler = viewModel::handleGameEvent

    val activity = LocalContext.current as? Activity
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val errorState = viewModel.errorStream.collectAsState()

    var isDialogOpen by remember { mutableStateOf(false) }

    EventEffect(
        event = errorState.value.error,
        onConsumed = { viewModel.onConsumedError() }
    ) { error ->
        scope.launch {
            when(error) {
                is Errors.NoSelectedLetters -> {
                    snackbarHostState.showSnackbar(
                        message = context.resources.getString(R.string.no_selected_letters)
                    )
                }
                is Errors.WrongWord -> {
                    snackbarHostState.showSnackbar(
                        message = context.resources.getString(R.string.wrong)
                    )
                }
                is Errors.GameOver -> { isDialogOpen = true }
            }
        }
    }

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = stringResource(id = R.string.congratulations)) },
            text = { Text(text = stringResource(id = R.string.you_scored, uiState.score)) },
            confirmButton = {
                Button(onClick = {
                    eventHandler(GameEvent.Restart)
                    isDialogOpen = false
                }) {
                    Text(text = stringResource(id = R.string.play_again))
                }
            },
            dismissButton = {
                Button(onClick = {
                    activity?.finish()
                }) {
                    Text(text = stringResource(id = R.string.exit))
                }
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InformationRow(
            counter = uiState.wordCounter,
            score = uiState.score
        )
        CurrentWord(
            word = viewModel.uiState.collectAsState().value.scrambledWord
        )
        GameDescription()
        Letters(
            letters = uiState.selectedLetters.toList(),
            onLetterClick = { letter ->
                eventHandler(GameEvent.OnSelectedLetterClick(letter))
            }
        )
        Buttons(
            onSkipClick = { eventHandler(GameEvent.Skip) },
            onRevertClick = { eventHandler(GameEvent.Revert) },
            onSubmitClick = { eventHandler(GameEvent.Submit) }
        )
        Letters(
            letters = uiState.freeLetters.toList(),
            onLetterClick = { letter ->
                eventHandler(GameEvent.OnFreeLetterClick(letter))
            }
        )
    }
}

@Composable
fun InformationRow(
    modifier: Modifier = Modifier,
    counter: Int = 0,
    score: Int = 0
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WordsCounter(counter = counter)
        CurrentScore(score = score)
    }
}

@Composable
fun WordsCounter(
    modifier: Modifier = Modifier,
    counter: Int = 0,
    maxNumber: Int = MAX_NO_OF_WORDS
) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.word_counter, counter, maxNumber),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun CurrentScore(
    modifier: Modifier = Modifier,
    score: Int = 0
) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.score, score),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun CurrentWord(
    modifier: Modifier = Modifier,
    word: String = "unscramble"
) {
    Text(
        modifier = modifier.padding(24.dp),
        text = word,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun GameDescription(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier.padding(24.dp),
        textAlign = TextAlign.Center,
        text = stringResource(id = R.string.description),
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun Buttons(
    modifier: Modifier = Modifier,
    onSkipClick: () -> Unit = { },
    onRevertClick: () -> Unit = { },
    onSubmitClick: () -> Unit = { },
) {
    val paddingModifier = Modifier.padding(4.dp)

    Column(modifier = modifier
        .padding(12.dp)
        .fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SkipButton(
                modifier = paddingModifier.weight(1f),
                onClick = onSkipClick
            )
            RevertButton(
                modifier = paddingModifier,
                onClick = onRevertClick
            )
        }
        SubmitButton(
            modifier = paddingModifier,
            onClick = onSubmitClick
        )
    }
}

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(text = stringResource(id = R.string.skip))
    }
}

@Composable
fun RevertButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_refresh_24),
            contentDescription = "Refresh"
        )
    }
}

@Composable
fun SubmitButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(text = stringResource(id = R.string.submit))
    }
}

@Composable
fun Letters(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    letters: List<Letter> = listOf(),
    onLetterClick: (Letter) -> Unit = { }
) {
    LazyRow(
        modifier = modifier.height(50.dp),
        state = listState
    ) {
        items(letters) { letter ->
            LetterItem(
                letter = letter,
                onClick = onLetterClick
            )
        }
    }
}

@Composable
fun LetterItem(
    modifier: Modifier = Modifier,
    letter: Letter = Letter(),
    onClick: (Letter) -> Unit = { }
) {
    Button(
        modifier = modifier
            .padding(2.dp)
            .size(40.dp)
        ,
        onClick = { onClick(letter) },
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text(text = letter.toString())
    }
}
