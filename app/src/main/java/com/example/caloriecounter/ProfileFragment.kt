package com.example.caloriecounter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var heightTextView: TextView
    private lateinit var weightTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var emailTextView: TextView

    private var chosenSex: String ="n/a"
    private var chosenAge: Int = 0
    private var weight: Int = 0
    private var height: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        loadData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        val back = view.findViewById<ImageButton>(R.id.goBackToSettings)
        back.setOnClickListener {
            val settingsFragment = SettingsFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, settingsFragment)
                .commit()
        }

        heightTextView = view.findViewById(R.id.profileHeight)
        weightTextView = view.findViewById(R.id.profileWeight)
        ageTextView = view.findViewById(R.id.profileAge)
        genderTextView = view.findViewById(R.id.profileSex)

        heightTextView.text = "${height}cm"
        weightTextView.text = "${weight}kg"
        ageTextView.text = "$chosenAge"
        genderTextView.text = chosenSex
    }

    private fun loadData() {
        val dataFile = "bmi_data.txt"
        val file = File(requireContext().filesDir, dataFile)
        val dataString = file.readText()

        if (dataString.isNotEmpty()) {
            val dataValues = dataString.split(";")
            Log.d("LoadData", "dataString: $dataString")
            chosenSex = dataValues[1].trim()
            chosenAge = dataValues[2].trim().toInt()
            weight = dataValues[3].trim().toInt()
            height = dataValues[4].trim().toInt()
        }
    }
}