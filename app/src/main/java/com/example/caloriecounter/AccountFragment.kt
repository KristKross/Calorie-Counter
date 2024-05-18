package com.example.caloriecounter

import android.content.Context
import android.content.Intent
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
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AccountFragment : Fragment() {
    private lateinit var view: View

    private lateinit var back: ImageView
    private lateinit var next: Button

    private lateinit var email: EditText
    private lateinit var password: EditText

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
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        this.view = view

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = view.findViewById(R.id.emailEditText)
        password = view.findViewById(R.id.passwordEditText)

        back = view.findViewById(R.id.accountBackToBMI)
        back.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.signUpFrame, BmiFragment())?.commit()
        }

        next = view.findViewById(R.id.accountToMain)
        next.setOnClickListener {
            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.text.toString().contains(" ") || password.text.toString().contains(" ")) {
                Toast.makeText(context, "Please enter a valid email and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences = requireActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

            val editor = sharedPreferences.edit()

            editor.putString("email", email.text.toString())
            editor.putString("password", password.text.toString())
            Log.d("email", email.text.toString())
            Log.d("password", password.text.toString())

            editor.apply()

            resetCalorieData()

            val intent = Intent(requireActivity(), MainMenu::class.java)
            startActivity(intent)
        }
    }
    private fun resetCalorieData() {
        val dataFile = "calorie_data.txt"
        val file = File(requireContext().filesDir, dataFile)
        file.writeText("")
    }
}