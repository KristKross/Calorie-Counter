package com.example.caloriecounter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.text.isDigitsOnly

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BmiFragment : Fragment() {
    private lateinit var view: View

    private lateinit var back: ImageView
    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var next: Button

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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityData = arguments?.getInt("data_activity")
        val sexData = arguments?.getInt("data_sex")
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

            if (heightEditText.toString().isEmpty() || weightEditText.toString().isEmpty())
            {
                Toast.makeText(context, "Enter all options.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (heightEditText.toString().toInt() < 100 || heightEditText.toString().toInt() > 250)
            {
                Toast.makeText(context, "Enter correct height.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (weightEditText.toString().toInt() < 30 || weightEditText.toString().toInt() > 250)
            {
                Toast.makeText(context, "Enter correct height.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val height = heightEditText.text.toString().toInt()
            val weight = weightEditText.text.toString().toInt()

            val fragment = AccountFragment()
            val result = Bundle().apply {
                if (activityData != null) {
                    putInt("data_activity", activityData)
                }
                if (sexData != null) {
                    putInt("data_sex", sexData)
                }
                if (ageData != null) {
                    putInt("data_age", ageData)
                }
                putInt("data_height", height)
                putInt("data_weight", weight)
            }

            fragment.arguments = result
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame, fragment)?.commit()
            Log.d("data", result.toString())
        }
    }
}