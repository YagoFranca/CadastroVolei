package com.android.mcplay.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.mcplay.controler.Client
import com.android.mcplay.controler.CurrencyTextWatcher
import com.android.mcplay.controler.DBClient
import com.android.mcplay.controler.SharedViewModel
import com.android.mcplay.databinding.FragmentUpdateDataClientBinding
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar


/**
 * Esta classe permite visualizar e gerenciar todos os dados do cliente.
 *
 * Funcionalidades:
 * 1. Alterar dados do cliente
 * 2. Excluir o cliente
 *
 * @author Marco Augusto
 */
class UpdateDataClient(dataClient: Client, clientId: Int) : Fragment() {
    private var _binding: FragmentUpdateDataClientBinding? = null
    private lateinit var sharedViewModel: SharedViewModel
    private val binding get() = _binding!!
    val data = dataClient
    val clientId = clientId

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateDataClientBinding.inflate(inflater, container, false)
        val view = binding.root
        val dbClient = DBClient(view.context)
        val calendar = Calendar.getInstance()

        binding.fieldInd.text = data.ind.toEditable()
        binding.fieldName.text = data.name.toEditable()
        binding.fieldNumber.text = data.number.toEditable()
        binding.fieldPlatform.text = data.platform.toEditable()
        binding.fieldUser.text = data.user.toEditable()
        binding.fieldPassword.text = data.password.toEditable()
        binding.switchAdult.isChecked = if (data.adult == "true") true else false
        binding.fieldTv.text = data.tv.toEditable()
        binding.fieldPlayer.text = data.player.toEditable()
        binding.fieldMacPlayer.text = data.macPlayer.toEditable()
        binding.fieldKeyPlayer.text = data.keyPlayer.toEditable()
        binding.switchStatus.isChecked = if (data.status == "true") true else false
        binding.fieldHabitationDate.text = data.habitationDate
        binding.fieldRenewalDate.text = data.renewalDate
        binding.fieldPayDate.text = data.payDate
        binding.fieldBank.text = data.bank.toEditable()
        binding.fieldAmount.text = data.amount.toEditable()
        binding.fieldAmount.addTextChangedListener(CurrencyTextWatcher(binding.fieldAmount))


        binding.fieldHabitationDate.setOnClickListener {
            showCalendar(view.context, binding.fieldHabitationDate, calendar)
        }

        binding.fieldRenewalDate.setOnClickListener {
            showCalendar(view.context, binding.fieldRenewalDate, calendar)
        }

        binding.fieldPayDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                view.context,
                { _, _, _, dayOfMonth ->
                    binding.fieldPayDate.text = "Dia $dayOfMonth"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }


        // Exclusão do usuário
        binding.btnDelete.setOnClickListener {
            // Dialog para confirmar se deseja realmente excluir
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Excluir usuário")
            builder.setMessage("Você deseja continuar?")

            builder.setPositiveButton("Continuar") { _, _ ->
                dbClient.deleteClient(clientId)
                sharedViewModel.setFragment(Home(dbClient.getAllClientsByRestDays()))
                DynamicToast.makeSuccess(view.context, "Cliente excluido").show()
            }

            builder.setNegativeButton("Cancelar") { _, _ ->
                DynamicToast.makeSuccess(view.context, "Operação cancelada").show()
            }

            builder.show()
        }

        // Atualização de dados do usuário
        binding.btnUpdate.setOnClickListener {
            if (binding.fieldInd.text.isEmpty() ||
                binding.fieldName.text.isEmpty() ||
                binding.fieldNumber.text.isEmpty() ||
                binding.fieldPlatform.text.isEmpty() ||
                binding.fieldUser.text.isEmpty() ||
                binding.fieldPassword.text.isEmpty() ||
                binding.fieldTv.text.isEmpty() ||
                binding.fieldPlayer.text.isEmpty() ||
                binding.fieldMacPlayer.text.isEmpty() ||
                binding.fieldKeyPlayer.text.isEmpty() ||
                binding.fieldHabitationDate.text.isEmpty() ||
                binding.fieldRenewalDate.text.isEmpty() ||
                binding.fieldPayDate.text.isEmpty() ||
                binding.fieldBank.text.isEmpty() ||
                binding.fieldAmount.text.isEmpty()
            ) {
                DynamicToast.makeWarning(view.context, "Preencha todos os campos").show()
            } else {

                val dataClient = Client(
                    id,
                    ind = binding.fieldInd.text!!.toString(),
                    name = binding.fieldName.text.toString(),
                    number = binding.fieldNumber.text.toString(),
                    platform = binding.fieldPlatform.text.toString(),
                    user = binding.fieldUser.text.toString(),
                    password = binding.fieldPassword.text.toString(),
                    adult = binding.switchAdult.isChecked.toString(),
                    tv = binding.fieldTv.text.toString(),
                    player = binding.fieldPlayer.text.toString(),
                    macPlayer = binding.fieldMacPlayer.text.toString(),
                    keyPlayer = binding.fieldKeyPlayer.text.toString(),
                    status = binding.switchStatus.isChecked.toString(),
                    habitationDate = binding.fieldHabitationDate.text.toString(),
                    renewalDate = binding.fieldRenewalDate.text.toString(),
                    payDate = binding.fieldPayDate.text.toString(),
                    bank = binding.fieldBank.text.toString(),
                    restDays = restDaysToDue(binding.fieldRenewalDate.text.toString()),
                    amount = binding.fieldAmount.text.toString()
                )
                Log.d("LOG - MCPlay", "Update - id: ${dataClient.id}")
                dbClient.updateDataClient(dataClient, clientId)

                sharedViewModel.setFragment(Home(dbClient.getAllClientsByRestDays()))
                DynamicToast.makeSuccess(view.context, "Cliente atualizado").show()
            }
        }

        return view
    }


    // Mini Calendário
    private fun showCalendar(context: Context, textView: TextView, calendar: Calendar) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, dayOfMonth ->
                textView.text = "$dayOfMonth/${selectedMonth + 1}/$selectedYear"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


    // Soma um mês ao dia de renovação e retorna a difernça em dias até essa data
    @RequiresApi(Build.VERSION_CODES.O)
    fun restDaysToDue(restDays: String): String {
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val renewalDateFormat = LocalDate.parse(restDays, formatter)
        val plusMonth = renewalDateFormat.plusMonths(1)

        return ChronoUnit.DAYS.between(LocalDate.now(), plusMonth).toString()
    }

    private fun showDialog(context: Context): Boolean {
        val builder = AlertDialog.Builder(context)
        var ret = true

        builder.setTitle("Excluir usuário")
        builder.setMessage("Você deseja continuar?")

        builder.setPositiveButton("Sim") { dialog, which ->
            ret = true
        }

        builder.setNegativeButton("Não") { dialog, which ->
            ret = false
        }

        builder.show()
        return ret
    }

    // Função de extensão para converter String em Editable
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}