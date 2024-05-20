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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InformationFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    // initialising variables to calculate BMI
    private var chosenSex: String = ""
    private var chosenActivity: String = ""
    private var chosenAge: Int = 0

    private lateinit var view: View
    private lateinit var back: ImageView
    private lateinit var maleButton: AppCompatButton
    private lateinit var femaleButton: AppCompatButton
    private lateinit var ageEditText: EditText
    private lateinit var next: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_information, container, false)
        this.view = view

        maleButton = view.findViewById(R.id.maleButton)
        femaleButton = view.findViewById(R.id.femaleButton)
        ageEditText = view.findViewById(R.id.ageEditText)
        back = view.findViewById(R.id.infoBackToActivityLevel)
        next = view.findViewById(R.id.nextFragmentBMIButton)

        loadData() // calls loadData() function

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // sets bundle string to a variable
        val data = arguments?.getString("data_activity")

        // onClickListener for male and female choices
        maleButton.setOnClickListener {
            chosenSex = "male" // sets the string variable to male

            // sets the background to indicate choice
            femaleButton.setBackgroundResource(R.drawable.log_in_bg)
            maleButton.setBackgroundResource(R.drawable.chosen_button_bg)
        }

        femaleButton.setOnClickListener {
            chosenSex = "female"

            maleButton.setBackgroundResource(R.drawable.log_in_bg)
            femaleButton.setBackgroundResource(R.drawable.chosen_button_bg)
        }

        // returns to previous choices
        back.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame,
                ActivityLevelFragment())?.commit()
        }

        // transitions to next fragment
        next.setOnClickListener {
            // ends onClickListener if choice is empty
            if (chosenSex == "") {
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
                textView.text = "Choose your sex."

                dialog.show()

                return@setOnClickListener
            }

            // ends onClickListener if choice is empty
            if (ageEditText.text.toString().isEmpty()) {
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
                textView.text = "Enter your age."

                dialog.show()

                return@setOnClickListener
            }

            // ends onClickListener if age is less than 18
            if (ageEditText.text.toString().toInt() < 18) {
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
                textView.text = "You can not be below 18."

                dialog.show()

                return@setOnClickListener
            }

            // ends onClickListener if age is more than 18
            if (ageEditText.text.toString().toInt() > 80) {
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
                textView.text = "You can not be older than 80."

                dialog.show()

                return@setOnClickListener
            }

            // changes var to user's choice
            chosenAge = ageEditText.text.toString().toInt()

            // using bundle to send data to next fragment
            val fragment = BmiFragment()
            val result = Bundle().apply {
                if (data != null) {
                    putString("data_activity", data.toString())
                    chosenActivity = data.toString()
                }
                putString("data_sex", chosenSex)
                putInt("data_age", chosenAge)
            }

            saveData() // calls saveData function()

            fragment.arguments = result // saves the bundle results

            // transition to next fragment
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame, fragment)?.commit()
        }
    }

    // function to save the data in a txt file
    private fun saveData() {
        val dataFile = "bmi_data.txt"
        val file = File(requireContext().filesDir, dataFile)

        // saves the variables in a string separated by semi-colons
        val dataString = "$chosenActivity;$chosenSex;$chosenAge;0;0"

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
            val dataValues = dataString.split(";") // splits each variable into a list
            Log.d("LoadData", "dataString: $dataString")

            // gets the first index of the txt file
            chosenActivity = dataValues[0].trim()
        }
    }
}