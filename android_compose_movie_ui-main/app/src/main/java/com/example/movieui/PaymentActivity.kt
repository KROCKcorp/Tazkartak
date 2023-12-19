package com.example.movieui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.movieui.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedValue = "Total: " + intent.getStringExtra("totalPrice")
        binding.PriceReceivedValue.text = receivedValue ?: "No value received"

        setupTextChangeListeners()
        setupPayButton()
    }

    private fun setupTextChangeListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.paybutton.isEnabled = checkFields()
                binding.yourname.text = binding.namee.text.toString().uppercase()
                binding.validthruhere.text = binding.validthru.text.toString()
                val text = binding.bankcardnumber.text.toString()
                binding.bankcardnumber1.text = if (text.length >= 4) text.substring(0, 4) else ""
                binding.bankcardnumber2.text = if (text.length >= 8) text.substring(4, 8) else if (text.length > 4) text.substring(4) else ""
                binding.bankcardnumber3.text = if (text.length >= 12) text.substring(8, 12) else if (text.length > 8) text.substring(8) else ""
                binding.bankcardnumber4.text = if (text.length >= 16) text.substring(12, 16) else if (text.length > 12) text.substring(12) else ""
                binding.cvvhere.text = binding.cvv.text.toString()
            }
        }

        binding.namee.addTextChangedListener(textWatcher)
        binding.validthru.addTextChangedListener(textWatcher)
        binding.bankcardnumber.addTextChangedListener(textWatcher)
        binding.cvv.addTextChangedListener(textWatcher)
    }

    private fun setupPayButton() {
        binding.paybutton.isEnabled = checkFields()

        binding.paybutton.setOnClickListener {
            val dialog = PaymentSuccessDialogFragment()
            dialog.show(supportFragmentManager, "PaymentSuccessDialogFragment")
        }
    }

    private fun checkFields(): Boolean {
        val name = binding.namee.text.toString()
        val validThru = binding.validthru.text.toString()
        val cardNumber = binding.bankcardnumber.text.toString()
        val cvv = binding.cvv.text.toString()

        return name.isNotBlank() && validThru.isNotBlank() && cardNumber.isNotBlank() && cvv.isNotBlank()
    }
}
