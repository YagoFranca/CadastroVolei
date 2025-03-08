package com.android.mcplay.view

import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.android.mcplay.R
import com.android.mcplay.controler.Client
import com.android.mcplay.controler.SharedViewModel

/**
 * Esta classe seleciona alguns dados do cliente para mostrar no card, sendo:
 * - Nome, status e os dias restantes para o vencimento
 *
 * Funcionalidades:
 * 1. Selecionar um cliente
 *
 * @author Marco Augusto
 */
class CardUser(private val dataList: List<Client>, sharedViewModel: SharedViewModel) :
    RecyclerView.Adapter<CardUser.CardViewHolder>() {
    val sharedViewModelHere = sharedViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_user, parent, false)

        return CardViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val innerList = dataList[position]
        holder.bind(innerList)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        private val name: TextView = itemView.findViewById(R.id.name)
        private val status: ImageView = itemView.findViewById(R.id.status)
        private val restDays: TextView = itemView.findViewById(R.id.restDays)

        // Método que define os valores do card
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: Client) {
            name.text = data.name

            status.setImageResource(
                when (data.restDays.toInt()) {
                    in -1000..0 -> R.drawable.status_cancel
                    in 1..5 -> R.drawable.status_alert
                    else -> R.drawable.status_on
                }
            )

            restDays.text = "${data.restDays} dias"
        }

        // Método chamado quando um card é clicado
        override fun onClick(v: View?) {
            val selectedClient = dataList[adapterPosition] // TESTAR getAdapterPosition
            val clientId = selectedClient.id

            sharedViewModelHere.setFragment(UpdateDataClient(selectedClient, clientId))
        }
    }
}
