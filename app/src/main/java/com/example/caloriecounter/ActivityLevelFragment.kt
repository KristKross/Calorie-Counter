package com.example.caloriecounter

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ActivityLevelFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    // initialises a variable to calculate BMI
    private var chosenButton: String = ""

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

        notVeryActive = view.findViewById(R.id.notVeryActiveButton)
        lightlyActive = view.findViewById(R.id.lightlyActiveButton)
        Active = view.findViewById(R.id.ActiveButton)
        VeryActive = view.findViewById(R.id.VeryActiveButton)
        back = view.findViewById(R.id.signUpBackToMainMenu)
        nextButton = view.findViewById(R.id.nextFragmentGendersButton)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // onClickListeners for each button
        notVeryActive.setOnClickListener {
            chosenButton = "very_light" // changes the var depending on chosen button

            // sets previous choice outlines to normal
            lightlyActive.setBackgroundResource(R.drawable.log_in_bg)
            Active.setBackgroundResource(R.drawable.log_in_bg)
            VeryActive.setBackgroundResource(R.drawable.log_in_bg)

            // sets outline to green to indicate choice
            notVeryActive.setBackgroundResource(R.drawable.chosen_button_bg)
        }

        lightlyActive.setOnClickListener {
            chosenButton = "light"
            notVeryActive.setBackgroundResource(R.drawable.log_in_bg)
            Active.setBackgroundResource(R.drawable.log_in_bg)
            VeryActive.setBackgroundResource(R.drawable.log_in_bg)

            lightlyActive.setBackgroundResource(R.drawable.chosen_button_bg)
        }

        Active.setOnClickListener {
            chosenButton = "active"
            notVeryActive.setBackgroundResource(R.drawable.log_in_bg)
            lightlyActive.setBackgroundResource(R.drawable.log_in_bg)
            VeryActive.setBackgroundResource(R.drawable.log_in_bg)

            Active.setBackgroundResource(R.drawable.chosen_button_bg)
        }

        VeryActive.setOnClickListener {
            chosenButton = "very_active"
            notVeryActive.setBackgroundResource(R.drawable.log_in_bg)
            lightlyActive.setBackgroundResource(R.drawable.log_in_bg)
            Active.setBackgroundResource(R.drawable.log_in_bg)

            VeryActive.setBackgroundResource(R.drawable.chosen_button_bg)
        }

        // transitions back to MainMenu activity
        back.setOnClickListener {
            val intent = Intent(requireActivity(), MainMenu::class.java)
            startActivity(intent)
        }

        // transitions to the next question fragment
        nextButton.setOnClickListener {
            if (chosenButton == "") { // will end onClickListener if no button was chosen
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
                textView.text = "Choose an option."

                dialog.show()

                return@setOnClickListener
            }

            // using a bundle to send the variable to the next fragment
            val fragment = InformationFragment()
            val bundle = Bundle()
            bundle.putString("data_activity", chosenButton)
            fragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame, fragment)?.commit()
        }
    }
}