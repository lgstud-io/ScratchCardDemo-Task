package sk.lg.scratchCard.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel() :ViewModel() {
    var voucherText by mutableStateOf("")
}