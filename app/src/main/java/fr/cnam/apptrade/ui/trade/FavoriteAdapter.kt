package fr.cnam.apptrade.ui.trade

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.cnam.apptrade.R
import fr.cnam.apptrade.network.models.Currency
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class FavoriteAdapter(private var currencyList: List<Currency>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    // ViewHolder pour chaque élément de la liste de devises.
    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val codeTextView: TextView = view.findViewById(R.id.favorite_code)
        val priceTextView: TextView = view.findViewById(R.id.favorite_price)
        val iconImageView: View = view.findViewById(R.id.favorite_icon)
    }

    // Crée et retourne un ViewHolder pour un élément de la liste.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    // Lie les données de la devise à chaque élément de la liste (ViewHolder).
    @SuppressLint("SetTextI18n", "DiscouragedApi")
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val currency = currencyList[position]
        holder.codeTextView.text = currency.code.uppercase(Locale.getDefault())

        // Formatage du prix avec séparation de milliers.
        val symbols = DecimalFormatSymbols().apply { groupingSeparator = ' ' }
        val formatter = DecimalFormat("#,###.########", symbols)
        val formattedPrice = formatter.format(currency.price.toFloat())
        holder.priceTextView.text = "$$formattedPrice"

        // Définition de l'icône de la devise.
        val context = holder.itemView.context
        val resourceId = context.resources.getIdentifier(currency.code.lowercase(Locale.getDefault()), "drawable", context.packageName)
        holder.iconImageView.setBackgroundResource(
            if (resourceId != 0) resourceId else R.drawable.ic_launcher_foreground
        )
    }

    // Retourne le nombre total d'éléments dans la liste.
    override fun getItemCount() = currencyList.size

    // Met à jour les données de l'adaptateur et rafraîchit la liste.
    fun updateData(newCurrencies: List<Currency>) {
        val diffCallback = CurrencyDiffCallback(currencyList, newCurrencies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        currencyList = newCurrencies
        diffResult.dispatchUpdatesTo(this)
    }
}
