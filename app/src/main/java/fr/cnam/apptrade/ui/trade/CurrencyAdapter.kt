package fr.cnam.apptrade.ui.trade

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.cnam.apptrade.R
import fr.cnam.apptrade.network.models.Currency
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class CurrencyAdapter(private var currencyList: List<Currency>) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nameTextView: TextView = view.findViewById(R.id.currency_name)
        val codeTextView: TextView = view.findViewById(R.id.currency_code)
        val priceTextView: TextView = view.findViewById(R.id.currency_price)
        val iconImageView: View = view.findViewById(R.id.currency_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }


    @SuppressLint("SetTextI18n", "DiscouragedApi")
    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencyList[position]
        holder.nameTextView.text = currency.name
        holder.codeTextView.text = currency.code.uppercase(Locale.getDefault())

        val symbols = DecimalFormatSymbols().apply { groupingSeparator = ' ' }
        val formatter = DecimalFormat("#,###.########", symbols)
        val formattedPrice = formatter.format(currency.price.toFloat())
        holder.priceTextView.text = "$$formattedPrice"

        val context = holder.itemView.context
        val resourceId = context.resources.getIdentifier(currency.code.lowercase(Locale.getDefault()), "drawable", context.packageName)
        if (resourceId != 0) {
            holder.iconImageView.setBackgroundResource(resourceId)
        } else {
            holder.iconImageView.setBackgroundResource(R.drawable.ic_launcher_foreground)
        }
    }

    override fun getItemCount() = currencyList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCurrencies: List<Currency>) {
        currencyList = newCurrencies
        notifyDataSetChanged()
    }
}
