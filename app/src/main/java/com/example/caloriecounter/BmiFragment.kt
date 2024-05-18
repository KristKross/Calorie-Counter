package com.example.caloriecounter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.text.isDigitsOnly
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BmiFragment : Fragment() {
    private lateinit var view: View

    private lateinit var back: ImageView
    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var next: Button

    private var height: Int = 0
    private var weight: Int = 0
    private var chosenActivity: String = "n/a"
    private var chosenSex: String = "n/a"
    private var chosenAge: Int = 0

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bmi, container, false)
        this.view = view

        loadData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityData = arguments?.getString("data_activity")
        val sexData = arguments?.getString("data_sex")
        val ageData = arguments?.getInt("data_age")

        back = view.findViewById(R.id.BMIBackToInformation)
        back.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame, InformationFragment())?.commit()
        }

        heightEditText = view.findViewById(R.id.heightEditText)
        weightEditText = view.findViewById(R.id.weightEditText)

        next = view.findViewById(R.id.nextFragmentAccountButton)
        next.setOnClickListener {
            if (!heightEditText.text.toString().isDigitsOnly() || !weightEditText.text.toString().isDigitsOnly()) {
                Toast.makeText(context, "Enter correct data.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (heightEditText.text.toString().isEmpty() || weightEditText.text.toString().isEmpty())
            {
                Toast.makeText(context, "Enter all options.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (heightEditText.text.toString().toInt() < 100 || heightEditText.text.toString().toInt() > 250)
            {
                Toast.makeText(context, "Enter correct height.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (weightEditText.text.toString().toInt() < 30 || weightEditText.text.toString().toInt() > 250)
            {
                Toast.makeText(context, "Enter correct height.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            height = heightEditText.text.toString().toInt()
            weight = weightEditText.text.toString().toInt()

            val fragment = AccountFragment()
            val result = Bundle().apply {
                if (activityData != null) {
                    putString("data_activity", activityData.toString())
                }
                if (sexData != null) {
                    putString("data_sex", sexData.toString())
                }
                if (ageData != null) {
                    putInt("data_age", ageData)
                }
                putInt("data_height", height)
                putInt("data_weight", weight)
            }

            if (activityData != null) {
                chosenActivity = activityData.toString()
            }
            if (sexData != null) {
                chosenSex = sexData.toString()
            }
            if (ageData != null) {
                chosenAge = ageData
            }

            saveData()

            fragment.arguments = result
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame, fragment)?.commit()
        }
    }
    private fun saveData() {
        val dataFile = "bmi_data.txt"
        val file = File(requireContext().filesDir, dataFile)

        val dataString = "$chosenActivity;$chosenSex;$chosenAge;$weight;$height"

        Log.d("SaveData", "dataString: $dataString")

        file.writeText(dataString)
    }

    private fun loadData() {
        val dataFile = "bmi_data.txt"
        val file = File(requireContext().filesDir, dataFile)
        val dataString = file.readText()

        if (dataString.isNotEmpty()) {
            val dataValues = dataString.split(";")
            Log.d("LoadData", "dataString: $dataString")

            chosenActivity = dataValues[0].trim()
            chosenSex = dataValues[1].trim()
            chosenAge = dataValues[2].trim().toInt()
            weight = dataValues[3].trim().toInt()
            height = dataValues[4].trim().toInt()
        }
    }
}