package com.example.caloriecounter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var breakfastListAmount = 0
    private var lunchListAmount = 0
    private var dinnerListAmount = 0
    private var snackListAmount = 0

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
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        loadData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goalProgressBar = view.findViewById<ProgressBar>(R.id.goalProgressBar)
        goalProgressBar.max = 2000
        goalProgressBar.progress =
            breakfastListAmount + lunchListAmount + dinnerListAmount + snackListAmount

        val progressText = view.findViewById<TextView>(R.id.progressText)
        val total = (breakfastListAmount + lunchListAmount + dinnerListAmount + snackListAmount)
        if (total - 2000 <= 0) {
            progressText.text = "${2000 - total}"
        } else {
            progressText.text = "0"
        }

        val miniFoodProgress = view.findViewById<TextView>(R.id.miniFoodProgress)
        miniFoodProgress.text =
            "${breakfastListAmount + lunchListAmount + dinnerListAmount + snackListAmount}"

        val breakfastCalCount = view.findViewById<TextView>(R.id.breakfastCalCount)
        breakfastCalCount.text = "$breakfastListAmount / 564"

        val lunchCalCount = view.findViewById<TextView>(R.id.lunchCalCount)
        lunchCalCount.text = "$lunchListAmount / 564"

        val dinnerCalCount = view.findViewById<TextView>(R.id.dinnerCalCount)
        dinnerCalCount.text = "$dinnerListAmount / 564"

        val snackCalCount = view.findViewById<TextView>(R.id.snackCalCount)
        snackCalCount.text = "$snackListAmount / 564"

        val breakfastProgressBar = view.findViewById<ProgressBar>(R.id.breakfastProgressBar)
        breakfastProgressBar.max = 564
        breakfastProgressBar.progress = breakfastListAmount

        val lunchProgressBar = view.findViewById<ProgressBar>(R.id.lunchProgressBar)
        lunchProgressBar.max = 564
        lunchProgressBar.progress = lunchListAmount

        val dinnerProgressBar = view.findViewById<ProgressBar>(R.id.dinnerProgressBar)
        dinnerProgressBar.max = 564
        dinnerProgressBar.progress = dinnerListAmount

        val snackProgressBar = view.findViewById<ProgressBar>(R.id.snackProgressBar)
        snackProgressBar.max = 564
        snackProgressBar.progress = snackListAmount
    }

    private fun loadData() {
        val dataFile = "calorie_data.txt"
        val file = File(requireContext().filesDir, dataFile)
        val calorieDataString = file.readText()

        if (calorieDataString.isNotEmpty()) {
            val dataValues = calorieDataString.split(";")
            Log.d("LoadData", "dataString: $calorieDataString")

            breakfastListAmount = dataValues[0].trim().toInt()
            lunchListAmount = dataValues[1].trim().toInt()
            dinnerListAmount = dataValues[2].trim().toInt()
            snackListAmount = dataValues[3].trim().toInt()

        }
    }
}