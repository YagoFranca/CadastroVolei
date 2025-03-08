package com.android.mcplay.controler

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

class CurrencyTextWatcher(private val editText: EditText) : TextWatcher {

    private var current = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if (editable.toString() != current) {
            editText.removeTextChangedListener(this)

            val cleanString = editable.toString().replace("[^\\d]".toRegex(), "")
            val parsed = cleanString.toDouble()
            val formatted = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(parsed / 100)

            current = formatted
            editText.setText(formatted)
            editText.setSelection(formatted.length)

            editText.addTextChangedListener(this)
        }
    }
}
