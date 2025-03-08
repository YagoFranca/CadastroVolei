package com.android.mcplay.controler

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DBClient(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val TAG = "Log - MCPLAY"

        private const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ClientDatabase.db"
        private const val TABLE_CLIENTS = "Clients"
        private const val COLUMN_ID = "id"
        private const val COLUMN_IND = "ind"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_NUMBER = "number"
        private const val COLUMN_PLATFORM = "platform"
        private const val COLUMN_USER = "user"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_ADULT = "adult"
        private const val COLUMN_TV = "tv"
        private const val COLUMN_PLAYER = "player"
        private const val COLUMN_MACPLAYER = "macPlayer"
        private const val COLUMN_KEYPLAYER = "keyplayer"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_HABITATIONDATE = "habitationDate"
        private const val COLUMN_RENEWALDATE = "renewalDate"
        private const val COLUMN_PAYDATE = "payDate"
        private const val COLUMN_BANK = "bank"
        private const val COLUMN_RESTDAYS = "restDays"
        private const val COLUMN_AMOUNT = "amount"

        // Criação da tabela SQL
        private const val CREATE_TABLE = "CREATE TABLE $TABLE_CLIENTS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_IND TEXT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_NUMBER TEXT," +
                "$COLUMN_PLATFORM TEXT," +
                "$COLUMN_USER TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_ADULT TEXT," +
                "$COLUMN_TV TEXT," +
                "$COLUMN_PLAYER TEXT," +
                "$COLUMN_MACPLAYER TEXT," +
                "$COLUMN_KEYPLAYER TEXT," +
                "$COLUMN_STATUS TEXT," +
                "$COLUMN_HABITATIONDATE TEXT," +
                "$COLUMN_RENEWALDATE TEXT," +
                "$COLUMN_PAYDATE TEXT," +
                "$COLUMN_BANK TEXT," +
                "$COLUMN_RESTDAYS TEXT," +
                "$COLUMN_AMOUNT TEXT" +
                ")"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
        Log.d(TAG, "Tabela criada")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTS")
        onCreate(db)
        Log.d(TAG, "Tabela atualizada")
    }

    fun addClient(client: Client): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_IND, client.ind)
        values.put(COLUMN_NAME, client.name)
        values.put(COLUMN_NUMBER, client.number)
        values.put(COLUMN_PLATFORM, client.platform)
        values.put(COLUMN_USER, client.user)
        values.put(COLUMN_PASSWORD, client.password)
        values.put(COLUMN_ADULT, client.adult)
        values.put(COLUMN_TV, client.tv)
        values.put(COLUMN_PLAYER, client.player)
        values.put(COLUMN_MACPLAYER, client.macPlayer)
        values.put(COLUMN_KEYPLAYER, client.keyPlayer)
        values.put(COLUMN_STATUS, client.status)
        values.put(COLUMN_HABITATIONDATE, client.habitationDate)
        values.put(COLUMN_RENEWALDATE, client.renewalDate)
        values.put(COLUMN_PAYDATE, client.payDate)
        values.put(COLUMN_BANK, client.bank)
        values.put(COLUMN_RESTDAYS, client.restDays)
        values.put(COLUMN_AMOUNT, client.amount)

        val id = db.insert(TABLE_CLIENTS, null, values)
        db.close()

        Log.d(TAG, "Cliente adicionado")
        return id
    }

    fun updateDataClient(client: Client, clientId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_IND, client.ind)
        values.put(COLUMN_NAME, client.name)
        values.put(COLUMN_NUMBER, client.number)
        values.put(COLUMN_PLATFORM, client.platform)
        values.put(COLUMN_USER, client.user)
        values.put(COLUMN_PASSWORD, client.password)
        values.put(COLUMN_ADULT, client.adult)
        values.put(COLUMN_TV, client.tv)
        values.put(COLUMN_PLAYER, client.player)
        values.put(COLUMN_MACPLAYER, client.macPlayer)
        values.put(COLUMN_KEYPLAYER, client.keyPlayer)
        values.put(COLUMN_STATUS, client.status)
        values.put(COLUMN_HABITATIONDATE, client.habitationDate)
        values.put(COLUMN_RENEWALDATE, client.renewalDate)
        values.put(COLUMN_PAYDATE, client.payDate)
        values.put(COLUMN_BANK, client.bank)
        values.put(COLUMN_RESTDAYS, client.restDays)
        values.put(COLUMN_AMOUNT, client.amount)

        db.update(TABLE_CLIENTS, values, "$COLUMN_ID = ?", arrayOf(clientId.toString()))
        db.close()
    }

    fun deleteClient(clientId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CLIENTS, "$COLUMN_ID = ?", arrayOf(clientId.toString()))
    }

    // Pegar todos os clientes
    @SuppressLint("Range")
    fun getAllClients(): List<Client> {
        val clients = mutableListOf<Client>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_CLIENTS"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val client = Client(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_IND)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLATFORM)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ADULT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TV)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_MACPLAYER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_KEYPLAYER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_HABITATIONDATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RENEWALDATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PAYDATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BANK)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RESTDAYS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT))
                )
                clients.add(client)
            } while (cursor.moveToNext())
        }
        cursor.close()

        Log.d(TAG, "Todos os clientes: $clients")

        return clients
    }

    // Função para atualizar os dias restantes para o pagamento de todos os clientes
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateRestDaysForAllClients() {
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")

        writableDatabase.use { db ->
            val clients = getAllClients()

            clients.forEach { client ->
                val renewalDate = LocalDate.parse(client.renewalDate, formatter)
                val plusMonth = renewalDate.plusMonths(1)
                val restDays = ChronoUnit.DAYS.between(LocalDate.now(), plusMonth).toString()

                val values = ContentValues().apply {
                    put(COLUMN_RESTDAYS, restDays)
                }
                Log.d(TAG, "values: $values")

                db.update(TABLE_CLIENTS, values, "$COLUMN_ID = ?", arrayOf(client.id.toString()))
            }
        }
    }



    // Consulta SQL para recuperar todos os clientes ordenados por restDays em ordem decrescente
    @SuppressLint("Range")
    fun getAllClientsByRestDays(): List<Client> {
        val clients = mutableListOf<Client>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_CLIENTS ORDER BY CAST($COLUMN_RESTDAYS AS INTEGER) ASC"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val client = Client(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_IND)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLATFORM)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ADULT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TV)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_MACPLAYER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_KEYPLAYER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_HABITATIONDATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RENEWALDATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PAYDATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BANK)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RESTDAYS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT))
                )
                clients.add(client)
            } while (cursor.moveToNext())
        }
        cursor.close()
//        db.close()

        Log.d(TAG, "Todos os clientes in ordem do restDays: $clients")

        return clients
    }


    // Retorna os clientes de acordo com a pesquisa por nome
    @SuppressLint("Range")
    fun getAllClientsBySearch(search: String, context: Context): List<Client> {
        val clients = mutableListOf<Client>()
        val db = this.readableDatabase

        // Consulta SQL para retornar os clientes filtrados pelo nome
        val query =
            "SELECT * FROM $TABLE_CLIENTS WHERE $COLUMN_NAME LIKE '%$search%' ORDER BY CAST($COLUMN_RESTDAYS AS INTEGER) ASC"

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val client = Client(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_IND)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLATFORM)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ADULT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TV)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_MACPLAYER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_KEYPLAYER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_HABITATIONDATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RENEWALDATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PAYDATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_BANK)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RESTDAYS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT))
                )
                clients.add(client)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        // verificação para caso não tenha retorno
        if (clients.isEmpty()) {
            DynamicToast.makeWarning(context,"Nenhum cliente encontrado").show()
        }

        return clients
    }

}