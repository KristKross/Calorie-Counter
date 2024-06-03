package com.example.caloriecounter

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import kotlin.time.times

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

        // returns to previous fragment
        goBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // adds food to database
        addFood.setOnClickListener {
            val itemName = itemNameEditText.text.toString()
            val servingSize = servingSizeEditText.text.toString()
            val servingsPerContainer = servingsContainerEditText.text.toString()
            val calorie = calorieEditText.text.toString()

            // checks if all fields are filled
            if (itemName.isEmpty() ||
                servingSize.isEmpty() ||
                servingsPerContainer.isEmpty() ||
                calorie.isEmpty()) {

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
                textView.text = "Please fill in all fields."

                dialog.show()
                return@setOnClickListener
            }

            // checks if description contains commas
            if (itemName.contains(",")) {
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
                textView.text = "Description can not contain commas."

                dialog.show()
                return@setOnClickListener
            }

            // checks if serving size is less than 1
            if (servingSize.toInt() < 0) {
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
                textView.text = "Serving size should not be less than 0."

                dialog.show()

                return@setOnClickListener
            }

            // checks if serving per container is less than 1
            if (servingsPerContainer.toInt() < 0) {
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
                textView.text = "Servings per container should not be less than 0."

                dialog.show()

                return@setOnClickListener
            }

            // checks if calorie is less than 1
            if (calorie.toInt() < 0) {
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
                textView.text = "Calorie amount should not be less than 0."

                dialog.show()

                return@setOnClickListener
            }

            // adds serving sizes to calories
            val newCalories = calorie.toInt() * servingSize.toInt()

            // passes data to previous fragment
            val result = Bundle().apply {
                putString("listName", listType)
                putString("itemName", itemName)
                putString("servingSize", servingSize)
                putString("calorie", newCalories.toString())
            }
            parentFragmentManager.setFragmentResult("itemInputResult", result)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}

