package com.android.mcplay.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.mcplay.controler.Client
import com.android.mcplay.controler.SharedViewModel
import com.android.mcplay.databinding.FragmentHomeBinding

/**
 * Esta classe permite visualizar e gerenciar todos os clientes cadastrados.
 * Oferece funcionalidades como filtragem, adição de novos clientes
 * e acesso a informações detalhadas de cada cliente.
 *
 * Funcionalidades:
 * 1. Filtragem de clientes
 * 2. Adição de novo cliente
 * 3. Acesso a informações detalhadas de um cliente
 *
 * @author Marco Augusto
 */
class Home(database: List<Client>) : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private var dataBase = database

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(view.context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = CardUser(dataBase, sharedViewModel)

        return view
    }
}