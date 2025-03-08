package com.android.mcplay.view

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.android.mcplay.controler.DBClient
import com.android.mcplay.databinding.FragmentConfigBinding
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import java.io.File
import java.io.FileOutputStream

/**
 * Funcionalidades:
 * 1. Selecionar um banco de dados dos clientes
 * 2. Compartilhar o banco de dados dos clientes
 *
 * @author Marco Augusto
 */

class Config : Fragment() {
    private var _binding: FragmentConfigBinding? = null
    private val binding get() = _binding!!
    private val FILE_SELECT_CODE = 0
    private lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)
        val view = binding.root
        ctx = view.context

        binding.btnUpdateClients.setOnClickListener {
            downloadDataBase()
        }

        binding.btnShareClients.setOnClickListener {
            shareDatabase()
        }

        // Copiar o Banco de dados para o diretório Files
        val sourceFile = view.context.getDatabasePath(DBClient.DATABASE_NAME)
        val destinationFile = File(view.context.filesDir, "ClientDatabase.db")

        try {
            sourceFile.copyTo(destinationFile, overwrite = true)
            Log.d("Log - McPlay", "Banco de dados exportado com sucesso")
        } catch (e: Exception) {
            Log.e("Log - McPlay", "Erro ao exportar banco de dados: ${e.message}")
        }

        return view
    }

    private fun downloadDataBase() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }

        try {
            startActivityForResult(intent, FILE_SELECT_CODE)
        } catch (e: android.content.ActivityNotFoundException) {
            DynamicToast.makeError(ctx, "Gerenciador de arquivos não encontrado").show()
            Log.d("Log - McPlay", "downloadDataBase: ${e.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            val selectedFileUri = data?.data

            val inputStream = ctx.contentResolver.openInputStream(selectedFileUri!!)
            val outputStream = FileOutputStream(File("/data/data/com.android.mcplay/databases", "ClientDatabase.db"))

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            inputStream?.close()
            outputStream.close()
        }
        super.onActivityResult(requestCode, resultCode, data)
        DynamicToast.makeSuccess(ctx, "Banco de dados atualizado!")
    }




    // Compartilhar o Banco de Dados
    private fun shareDatabase() {
        val file = File(ctx.filesDir, "ClientDatabase.db")
        val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/octet-stream"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        ctx.startActivity(Intent.createChooser(shareIntent, "Compartilhar banco de dados via"))
        DynamicToast.makeSuccess(ctx, "Compartilhado com sucesso!")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}