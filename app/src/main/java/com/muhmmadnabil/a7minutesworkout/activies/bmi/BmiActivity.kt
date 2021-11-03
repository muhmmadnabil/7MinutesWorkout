package com.muhmmadnabil.a7minutesworkout.activies.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.muhmmadnabil.a7minutesworkout.R
import com.muhmmadnabil.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {

    private var binding: ActivityBmiBinding? = null

    private val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    private val US_UNITS_VIEW = "US_UNITS_VIEW"

    private var currentVisibleView = METRIC_UNITS_VIEW


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener {
            calculateUnits()
        }
    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW

        binding?.llMetric?.visibility = View.GONE
        binding?.llUs?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear()
        binding?.etUsMetricUnitHeightInch?.text!!.clear()
        binding?.etUsMetricUnitHeightFeet?.text!!.clear()

        binding?.llDiplayBMIResult?.visibility = View.GONE
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW

        binding?.llMetric?.visibility = View.VISIBLE
        binding?.llUs?.visibility = View.GONE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDiplayBMIResult?.visibility = View.GONE
    }

    private fun displayBmiResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDiplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription
    }

    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (binding?.etMetricUnitHeight?.text.toString()
                .isEmpty() || binding?.etMetricUnitWeight?.text.toString().isEmpty()
        ) {
            isValid = false
        }

        return isValid
    }

    private fun validateUsUnits(): Boolean {
        var isValid = true

        if (binding?.etUsMetricUnitWeight?.text.toString()
                .isEmpty() || binding?.etUsMetricUnitHeightInch?.text.toString()
                .isEmpty() || binding?.etUsMetricUnitHeightFeet?.text.toString().isEmpty()
        ) {
            isValid = false
        }

        return isValid
    }

    private fun calculateUnits() {
        if (currentVisibleView == METRIC_UNITS_VIEW) {
            if (validateMetricUnits()) {
                val heightValue = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue * heightValue)

                displayBmiResult(bmi)

            } else {
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
        } else {
            if (validateUsUnits()) {
                val heightValueFeet = binding?.etUsMetricUnitHeightFeet?.text.toString()
                val heightValueInch = binding?.etUsMetricUnitHeightInch?.text.toString()
                val weightValue = binding?.etUsMetricUnitWeight?.text.toString().toFloat()

                val heightValue = heightValueInch.toFloat() + heightValueFeet.toFloat() * 12

                val bmi = 703 * (weightValue / (heightValue * heightValue))

                displayBmiResult(bmi)

            } else {
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
        }
    }
}