package com.example.caloriecounter

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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


    // initialises all variables needed to calculate BMI
    private var height: Int = 0
    private var weight: Int = 0
    private var chosenActivity: String = ""
    private var chosenSex: String = ""
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

        heightEditText = view.findViewById(R.id.heightEditText)
        weightEditText = view.findViewById(R.id.weightEditText)
        back = view.findViewById(R.id.BMIBackToInformation)
        next = view.findViewById(R.id.nextFragmentAccountButton)

        loadData() // calls loadData() function

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // sets bundle data to variable
        val activityData = arguments?.getString("data_activity")
        val sexData = arguments?.getString("data_sex")
        val ageData = arguments?.getInt("data_age")

        // returns to previous fragment (InformationFragment)
        back.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(
                R.id.signUpFrame, InformationFragment())?.commit()
        }

        // transitions to next fragment (AccountFragment)
        next.setOnClickListener {
            // ends onClickListener if editText are blank.
            if (heightEditText.text.toString().isEmpty() ||
                weightEditText.text.toString().isEmpty())
            {
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setContentView(R.layout.error_modal_popup)
                dialog.window?.setBackgroundDrawableResource(R.drawable.modal_bg)
                dialog.window?.setLayout(
                    900,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val textView = dialog.findViewById<TextView>(R.id.errorTextView)
                textView.text = "Enter all options."

                dialog.show()

                return@setOnClickListener
            }

            // ends onClickListener if height is less than or equal to 0 or more than 300
            if (heightEditText.text.toString().toInt() <= 0 ||
                heightEditText.text.toString().toInt() > 300)
            {
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setContentView(R.layout.error_modal_popup)
                dialog.window?.setBackgroundDrawableResource(R.drawable.modal_bg)
                dialog.window?.setLayout(
                    900,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val textView = dialog.findViewById<TextView>(R.id.errorTextView)
                textView.text = "Enter correct height."

                dialog.show()

                return@setOnClickListener
            }

            // ends onClickListener if weight is more than or equal to 0 or more than 80
            if (weightEditText.text.toString().toInt() <= 0 ||
                weightEditText.text.toString().toInt() > 800)
            {
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setContentView(R.layout.error_modal_popup)
                dialog.window?.setBackgroundDrawableResource(R.drawable.modal_bg)
                dialog.window?.setLayout(
                    900,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val textView = dialog.findViewById<TextView>(R.id.errorTextView)
                textView.text = "Enter correct weight."

                dialog.show()

                return@setOnClickListener
            }

            /// sets variables to input
            height = heightEditText.text.toString().toInt()
            weight = weightEditText.text.toString().toInt()

            // using a bundle to send data to next fragment
            val fragment = AccountFragment()
            val result = Bundle().apply {
                if (activityData != null) {
                    putString("data_activity", activityData.toString())
                    chosenActivity = activityData.toString()
                }
                if (sexData != null) {
                    putString("data_sex", sexData.toString())
                    chosenSex = sexData.toString()
                }
                if (ageData != null) {
                    putInt("data_age", ageData)
                    chosenAge = ageData
                }
                putInt("data_height", height)
                putInt("data_weight", weight)
            }

            saveData() // calls saveData() function

            fragment.arguments = result
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame, fragment)?.commit()
        }
    }

    // function to save the data in a txt file
    private fun saveData() {
        val dataFile = "bmi_data.txt"
        val file = File(requireContext().filesDir, dataFile)

        val dataString = "$chosenActivity;$chosenSex;$chosenAge;$weight;$height"

        Log.d("SaveData", "dataString: $dataString")

        file.writeText(dataString)
    }

    // function to load the data in this fragment
    // used if user returns to this fragment
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