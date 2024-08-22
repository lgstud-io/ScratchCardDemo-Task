package sk.lg.scratchCard.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sk.lg.scratchCard.R
import sk.lg.scratchCard.api.Api
import sk.lg.scratchCard.util.SnackBarController
import sk.lg.scratchCard.util.SnackBarEvent
import java.util.UUID

class MainViewModel() :ViewModel() {
    var voucherText by mutableStateOf("")

    fun validateVoucher(context: Context){
        viewModelScope.launch {
            Api.getValidationResult(context, voucherText) { result ->
                viewModelScope.launch {
                    SnackBarController.sendEvent(
                        SnackBarEvent(message = context.getString(if (result) R.string.validation_ok else R.string.validation_failed))
                    )
                    voucherText = ""
                }
            }
        }
    }

    fun scratchVoucher(){
        viewModelScope.launch {
            delay(2000)
            voucherText = UUID.randomUUID().toString()
        }
    }
}