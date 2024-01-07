package fr.cnam.apptrade.ui.trade

import androidx.recyclerview.widget.DiffUtil
import fr.cnam.apptrade.network.models.Currency

class CurrencyDiffCallback(private val oldList: List<Currency>, private val newList: List<Currency>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Retourne vrai si les éléments représentent la même devise.
        return oldList[oldItemPosition].code == newList[newItemPosition].code
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare les contenus détaillés et retourne vrai si les données n'ont pas changé.
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
