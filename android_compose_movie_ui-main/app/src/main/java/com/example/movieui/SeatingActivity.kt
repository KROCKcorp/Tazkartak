package com.example.movieui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.movieui.databinding.ActivitySeatingBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SeatingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeatingBinding
    private lateinit var rowViews: List<View>
    private lateinit var colTextViewArrays: List<Array<TextView?>>
    private var textViewCount = 8
    private var quantityCount = 0
    private var price = 100
    private var rbString = "10:00 am to 12:00 am"
    private lateinit var dateString: String
    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupInitialValues()
        setupSeatListeners()
        setupDatePicker()
        setupRadioGroup()
        setupBuyButton()
    }

    private fun initializeViews() {
        rowViews = listOf(
            findViewById(R.id.row_layout_A),
            findViewById(R.id.row_layout_B),
            findViewById(R.id.row_layout_C),
            findViewById(R.id.row_layout_D),
            findViewById(R.id.row_layout_E)
        )
        var textIdList = arrayOf(
            R.id.column_text_1,
            R.id.column_text_2,
            R.id.column_text_3,
            R.id.column_text_4,
            R.id.column_text_5,
            R.id.column_text_6,
            R.id.column_text_7,
            R.id.column_text_8,
        )

        colTextViewArrays = rowViews.map { rowView ->
            val colTextViewArray = arrayOfNulls<TextView>(textViewCount)
            for (i in 0 until textViewCount) {
                colTextViewArray[i] = rowView.findViewById(textIdList[i])
            }
            colTextViewArray
        }
    }

    private fun setupInitialValues() {
        val intent = intent
        price = intent.getIntExtra("price", 100)
        binding.txtPrice.text = "EGP $price"

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateString = "$day/${month + 1}/$year"
        binding.pickDateBtn.text = dateString

        loadingDialog = Dialog(this)
        // Initialize loading dialog
    }

    private fun setupSeatListeners() {
        colTextViewArrays.forEach { colTextViewArray ->
            colTextViewArray.forEachIndexed { index, textView ->
                textView?.setOnClickListener {
                    toggleSeatSelection(textView, index)
                }
            }
        }
    }

    private fun toggleSeatSelection(textView: TextView?, index: Int) {
        textView?.isSelected = !(textView?.isSelected ?: false)
        quantityCount = if (textView?.isSelected == true) {
            quantityCount + 1
        } else {
            quantityCount.coerceAtLeast(0) - 1
        }
        setBackgroundTextView(textView, textView?.isSelected ?: false)
        updateQuantity()
    }

    private fun setBackgroundTextView(textView: TextView?, isSelected: Boolean) {
        val backgroundResource = if (isSelected) {
            R.drawable.button_light_set_dialog_outline
        } else {
            R.drawable.button_outline
        }
        textView?.background = ResourcesCompat.getDrawable(resources, backgroundResource, null)
    }

    private fun updateQuantity() {
        binding.txtQuality.text = quantityCount.toString()
        if (quantityCount > 0) {
            binding.txtTotalPrice.text = "EGP ${price * quantityCount}"
        } else {
            Toast.makeText(this, "Please select at least 1 seat", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupDatePicker() {
        val pickDateBtn = findViewById<Button>(R.id.pickDateBtn)
        val calendar = Calendar.getInstance()

        pickDateBtn.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    pickDateBtn.text = selectedDate
                    dateString = selectedDate
                    resetSeatsAndQuantity()
                    checkAvailability()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Set minimum date as today
            datePicker.datePicker.minDate = calendar.timeInMillis

            // Set maximum date
            val string_date = "15-April-2025"
            val f = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            try {
                val d: Date = f.parse(string_date)
                val milliseconds = d.time
                datePicker.datePicker.maxDate = milliseconds
                datePicker.show()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    private fun setupRadioGroup() {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val rb = findViewById<RadioButton>(checkedId)
            rbString = rb.text.toString()
            checkAvailability()
        }
    }

    private fun resetSeatsAndQuantity() {
        // Reset seat selections
        colTextViewArrays.forEach { colTextViewArray ->
            colTextViewArray.forEach { textView ->
                textView?.isSelected = false
                setBackgroundTextView(textView, false)
            }
        }
        // Reset quantity count
        quantityCount = 0
        updateQuantity()
    }


    private fun checkAvailability(/* Required parameters */) {
        loadingDialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            // Simulated logic for checking seat availability
            // ...
            loadingDialog.dismiss()
        }, 1000)
    }

    private fun setupBuyButton() {
        binding.buyBtn.setOnClickListener {
            if (quantityCount > 0) {
                val totalPrice = price * quantityCount
                binding.txtTotalPrice.text = "EGP $totalPrice"

                // Navigate to PaymentActivity
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("totalPrice", totalPrice.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select at least 1 seat", Toast.LENGTH_LONG).show()
            }
        }
    }
}
