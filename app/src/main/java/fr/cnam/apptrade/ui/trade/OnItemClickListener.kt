package fr.cnam.apptrade.ui.trade

import fr.cnam.apptrade.network.models.Currency

interface OnItemClickListener {
    fun onItemClicked(currency: Currency)
}