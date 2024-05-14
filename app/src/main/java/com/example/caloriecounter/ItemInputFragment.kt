package com.example.caloriecounter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.text.isDigitsOnly

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ItemInputFragment : Fragment() {
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_input, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listType = arguments?.getString("listType")
        val itemNameEditText = view.findViewById<EditText>(R.id.descriptionEditText)
        val servingSizeEditText = view.findViewById<EditText>(R.id.servingSizeEditText)
        val servingsContainerEditText = view.findViewById<EditText>(R.id.servingsContainerEditText)
        val calorieEditText = view.findViewById<EditText>(R.id.calorieEditText)
        val addFood = view.findViewById<ImageButton>(R.id.addFood)
        val goBack = view.findViewById<ImageButton>(R.id.goBackToLogFood)

        goBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        addFood.setOnClickListener {
            val itemName = itemNameEditText.text.toString()
            val servingSize = servingSizeEditText.text.toString()
            val servingsPerContainer = servingsContainerEditText.text.toString()
            val calorie = calorieEditText.text.toString()

            if (itemName.isEmpty() ||
                servingSize.isEmpty() ||
                servingsPerContainer.isEmpty() ||
                calorie.isEmpty()) {

                Toast.makeText(context, "Please enter all valid inputs.", Toast.LENGTH_SHORT).show()

            } else {
                if (!calorie.isDigitsOnly() || !servingsPerContainer.isDigitsOnly()) {
                        Toast.makeText(context, "Calorie amount and Servings per container should be a number.", Toast.LENGTH_SHORT).show()
                } else {
                    if (calorie < 0.toString()) {
                        Toast.makeText(context, "Calorie amount should not be less than 0.", Toast.LENGTH_SHORT).show()
                    } else {
                        val result = Bundle().apply {
                            putString("listName", listType)
                            putString("itemName", itemName)
                            putString("calorie", calorie)
                        }
                        parentFragmentManager.setFragmentResult("itemInputResult", result)

                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
            }
        }
    }
}

