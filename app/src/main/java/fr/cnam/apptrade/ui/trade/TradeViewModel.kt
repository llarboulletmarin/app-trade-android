package fr.cnam.apptrade.ui.trade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TradeViewModel : ViewModel() {

        private val _text = MutableLiveData<String>().apply {
            value = "This is trade Fragment"
        }
        val text: LiveData<String> = _text

}