package com.example.caloriecounter

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
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

    private var height: Int = 0
    private var weight: Int = 0
    private var chosenActivity: String = "n/a"
    private var chosenSex: String = "n/a"
    private var chosenAge: Int = 0

    private var totalBMR: Int = 0
    private var totalCalorieIntake: Int = 0

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

        // loads data from txt file
        loadCaloricData()
        loadBMIData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show info popup
        val infoButton = view.findViewById<ImageView>(R.id.tutorialPopup)
        infoButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.info_modal_popup)
            dialog.window?.setBackgroundDrawableResource(R.drawable.modal_bg)
            dialog.window?.setLayout(
                900,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            dialog.show()
        }

        // Calculate BMR depending on chosen sex
        totalBMR = if (chosenSex == "male") {
            (10 * weight + (6.25 * height) - (5 * chosenAge) + 5).toInt()
        } else {
            (10 * weight + (6.25 * height) - (5 * chosenAge) - 161).toInt()
        }

        // Calculate total calorie intake depending on chosen activity
        when (chosenActivity) {
            "very_light" -> {
                totalCalorieIntake = "%.0f".format(totalBMR * 1.2).toInt()
            }
            "light" -> {
                totalCalorieIntake = "%.0f".format(totalBMR * 1.375).toInt()
            }
            "active" -> {
                totalCalorieIntake  = "%.0f".format(totalBMR * 1.55).toInt()
            }
            "very_active" -> {
                totalCalorieIntake  = "%.0f".format(totalBMR * 1.725).toInt()
            }
        }

        // calculates the total calorie intake from ListView
        val total = (breakfastListAmount + lunchListAmount + dinnerListAmount + snackListAmount)

        // updates progress bar
        val goalProgressBar = view.findViewById<ProgressBar>(R.id.goalProgressBar)
        goalProgressBar.max = totalCalorieIntake
        goalProgressBar.progress = total

        val progressText = view.findViewById<TextView>(R.id.progressText)

        // keeps number on 0 if total calorie intake is less than total calorie intake
        if (total - totalCalorieIntake <= 0) {
            progressText.text = "${totalCalorieIntake - total}"
        } else {
            progressText.text = "0"
        }

        // updates mini bars
        val miniGoalProgress = view.findViewById<TextView>(R.id.miniGoalProgress)
        miniGoalProgress.text = totalCalorieIntake.toString()

        val miniFoodProgress = view.findViewById<TextView>(R.id.miniFoodProgress)
        miniFoodProgress.text = total.toString()

        // updates calorie count text
        val breakfastCalCount = view.findViewById<TextView>(R.id.breakfastCalCount)
        breakfastCalCount.text =
            "$breakfastListAmount / ${"%.0f".format(totalCalorieIntake * 0.30)}"

        val lunchCalCount = view.findViewById<TextView>(R.id.lunchCalCount)
        lunchCalCount.text =
            "$lunchListAmount / ${"%.0f".format(totalCalorieIntake * 0.40)}"

        val dinnerCalCount = view.findViewById<TextView>(R.id.dinnerCalCount)
        dinnerCalCount.text =
            "$dinnerListAmount / ${"%.0f".format(totalCalorieIntake * 0.30)}"

        val snackCalCount = view.findViewById<TextView>(R.id.snackCalCount)
        snackCalCount.text =
            "$snackListAmount / ${"%.0f".format(totalCalorieIntake * 0.10)}"

        // updates progress bar for each meal type
        val breakfastProgressBar = view.findViewById<ProgressBar>(R.id.breakfastProgressBar)
        breakfastProgressBar.max = (totalCalorieIntake * 0.30).toInt()
        breakfastProgressBar.progress = breakfastListAmount

        val lunchProgressBar = view.findViewById<ProgressBar>(R.id.lunchProgressBar)
        lunchProgressBar.max = (totalCalorieIntake * 0.40).toInt()
        lunchProgressBar.progress = lunchListAmount

        val dinnerProgressBar = view.findViewById<ProgressBar>(R.id.dinnerProgressBar)
        dinnerProgressBar.max = (totalCalorieIntake * 0.30).toInt()
        dinnerProgressBar.progress = dinnerListAmount

        val snackProgressBar = view.findViewById<ProgressBar>(R.id.snackProgressBar)
        snackProgressBar.max = (totalCalorieIntake * 0.10).toInt()
        snackProgressBar.progress = snackListAmount
    }

    // loads data from txt file
    private fun loadCaloricData() {
        val dataFile = "calorie_data.txt"
        val file = File(requireContext().filesDir, dataFile)
        val dataString = file.readText()

        if (dataString.isNotEmpty()) {
            val dataValues = dataString.split(";")
            Log.d("LoadData", "dataString: $dataString")

            breakfastListAmount = dataValues[0].trim().toInt()
            lunchListAmount = dataValues[1].trim().toInt()
            dinnerListAmount = dataValues[2].trim().toInt()
            snackListAmount = dataValues[3].trim().toInt()
        }
    }

    // loads data from txt file
    private fun loadBMIData() {
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