package com.example.caloriecounter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentTransaction
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ActivityLevelFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var chosenButton: Int = 0

    private lateinit var view: View

    private lateinit var back: ImageView
    private lateinit var notVeryActive: AppCompatButton
    private lateinit var lightlyActive: AppCompatButton
    private lateinit var Active: AppCompatButton
    private lateinit var VeryActive: AppCompatButton
    private lateinit var nextButton: Button

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
        val view = inflater.inflate(R.layout.fragment_activity_level, container, false)
        this.view = view

        val dataFile = "bmi_data.txt"
        val file = File(requireContext().filesDir, dataFile)
        file.writeText("")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notVeryActive = view.findViewById(R.id.notVeryActiveButton)
        lightlyActive = view.findViewById(R.id.lightlyActiveButton)
        Active = view.findViewById(R.id.ActiveButton)
        VeryActive = view.findViewById(R.id.VeryActiveButton)

        back = view.findViewById(R.id.signUpBackToMainMenu)
        back.setOnClickListener {
            val intent = Intent(requireActivity(), MainMenu::class.java)
            startActivity(intent)
        }

        notVeryActive.setOnClickListener {
            chosenButton = 1
            lightlyActive.setBackgroundResource(R.drawable.log_in_bg)
            Active.setBackgroundResource(R.drawable.log_in_bg)
            VeryActive.setBackgroundResource(R.drawable.log_in_bg)

            notVeryActive.setBackgroundResource(R.drawable.activity_level_bg)
        }

        lightlyActive.setOnClickListener {
            chosenButton = 2
            notVeryActive.setBackgroundResource(R.drawable.log_in_bg)
            Active.setBackgroundResource(R.drawable.log_in_bg)
            VeryActive.setBackgroundResource(R.drawable.log_in_bg)

            lightlyActive.setBackgroundResource(R.drawable.activity_level_bg)
        }

        Active.setOnClickListener {
            chosenButton = 3
            notVeryActive.setBackgroundResource(R.drawable.log_in_bg)
            lightlyActive.setBackgroundResource(R.drawable.log_in_bg)
            VeryActive.setBackgroundResource(R.drawable.log_in_bg)

            Active.setBackgroundResource(R.drawable.activity_level_bg)
        }

        VeryActive.setOnClickListener {
            chosenButton = 4
            notVeryActive.setBackgroundResource(R.drawable.log_in_bg)
            lightlyActive.setBackgroundResource(R.drawable.log_in_bg)
            Active.setBackgroundResource(R.drawable.log_in_bg)

            VeryActive.setBackgroundResource(R.drawable.activity_level_bg)
        }

        nextButton = view.findViewById(R.id.nextFragmentGendersButton)

        nextButton.setOnClickListener {
            if (chosenButton != 0) {
                val dataFile = "bmi_data.txt"
                val file = File(requireContext().filesDir, dataFile)
                val dataString = "$chosenButton;"
                file.appendText(dataString)

                val fragment = InformationFragment()
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.add(R.id.frameLayout, fragment)
                transaction.addToBackStack("InformationFragment")
                transaction.commit()
            } else {
                Toast.makeText(context, "Choose an option.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}