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
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InformationFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var chosenSex = 0
    private var chosenAge = 0

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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getInt("data_activity")

        maleButton = view.findViewById(R.id.maleButton)
        femaleButton = view.findViewById(R.id.femaleButton)
        ageEditText = view.findViewById(R.id.ageEditText)

        back = view.findViewById(R.id.infoBackToActivityLevel)
        back.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame, ActivityLevelFragment())?.commit()
        }

        maleButton.setOnClickListener {
            chosenSex = 1

            femaleButton.setBackgroundResource(R.drawable.log_in_bg)
            maleButton.setBackgroundResource(R.drawable.activity_level_bg)
        }

        femaleButton.setOnClickListener {
            chosenSex = 2

            maleButton.setBackgroundResource(R.drawable.log_in_bg)
            femaleButton.setBackgroundResource(R.drawable.activity_level_bg)
        }

        next = view.findViewById(R.id.nextFragmentBMIButton)
        next.setOnClickListener {
            if (chosenSex == 0) {
                Toast.makeText(requireContext(), "Enter your sex.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (ageEditText.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Enter your age.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (ageEditText.text.toString().toInt() < 18) {
                Toast.makeText(requireContext(), "You need to be 18 or older.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (ageEditText.text.toString().toInt() > 80) {
                Toast.makeText(requireContext(), "You can't be older than 80.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            chosenAge = ageEditText.text.toString().toInt()

            val fragment = BmiFragment()
            val result = Bundle().apply {
                if (data != null) {
                    putInt("data_activity", data)
                    putInt("data_sex", chosenSex)
                    putInt("data_age", chosenAge)
                }
            }
            fragment.arguments = result
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame, fragment)?.commit()
            Log.d("data", result.toString())
        }
    }
}