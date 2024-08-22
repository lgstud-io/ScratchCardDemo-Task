package sk.lg.scratchCard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import sk.lg.scratchCard.model.MainViewModel
import sk.lg.scratchCard.ui.theme.ScratchCardDemoTheme
import sk.lg.scratchCard.util.ObserveAsEvents
import sk.lg.scratchCard.util.SnackBarController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ScratchCardDemoTheme {

                val vm = viewModel<MainViewModel>()


                val context = LocalContext.current
                val snackBarHostState = remember {
                    SnackbarHostState()
                }
                val scope = rememberCoroutineScope()
                ObserveAsEvents(
                    flow = SnackBarController.events,
                    snackBarHostState
                ) { event ->
                    scope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()

                        val result = snackBarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Long
                        )

                        if(result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackBarHostState
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) {paddingValues ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues) ,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(if (vm.voucherText.isEmpty()) Color.LightGray else Color.DarkGray)
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 32.dp),
                                text = vm.voucherText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }

                        HorizontalDivider(thickness = 8.dp, color = Color.Transparent)

                        Row {
                            Button(
                                enabled = vm.voucherText.isEmpty(),
                                onClick = { vm.scratchVoucher() }
                            ) {
                                    Text(text = stringResource(id = R.string.scratch_code))
                            }

                            VerticalDivider(thickness = 8.dp, color = Color.Transparent)

                            Button(
                                enabled = vm.voucherText.isNotEmpty(),
                                onClick = { vm.validateVoucher(context) }
                            ) {
                                    Text(text = stringResource(id = R.string.validate_code))
                            }
                        }
                    }
                }
            }
        }
    }
}



