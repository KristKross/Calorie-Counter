package com.example.caloriecounter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LogFoodFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var view: View
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (this::view.isInitialized) {
            return view
        }
        val view = inflater.inflate(R.layout.fragment_logfood, container, false)
        this.view = view

        sharedPreferences = requireContext().getSharedPreferences(
            "caloric_data", Context.MODE_PRIVATE)

        val breakfastCalorieAmount = sharedPreferences.getInt(
            "breakfastCalorieAmount", breakfastListAmount)

        val lunchCalorieAmount = sharedPreferences.getInt(
            "lunchCalorieAmount", lunchListAmount)

        val dinnerCalorieAmount = sharedPreferences.getInt(
            "dinnerCalorieAmount", dinnerListAmount)

        val snackCalorieAmount = sharedPreferences.getInt(
            "snackCalorieAmount", snackListAmount)

        val breakfastAmountTextView = view.findViewById<TextView>(R.id.breakfastCalListAmount)
        breakfastAmountTextView.text = breakfastCalorieAmount.toString()

        val lunchAmountTextView = view.findViewById<TextView>(R.id.lunchCalListAmount)
        lunchAmountTextView.text = lunchCalorieAmount.toString()

        val dinnerAmountTextView = view.findViewById<TextView>(R.id.dinnerCalListAmount)
        dinnerAmountTextView.text = dinnerCalorieAmount.toString()

        val snackAmountTextView = view.findViewById<TextView>(R.id.snackCalListAmount)
        snackAmountTextView.text = snackCalorieAmount.toString()

        val breakfastListView = view.findViewById<ListView>(R.id.breakfastListView)
        val lunchListView = view.findViewById<ListView>(R.id.lunchListView)
        val dinnerListView = view.findViewById<ListView>(R.id.dinnerListView)
        val snackListView = view.findViewById<ListView>(R.id.snackListView)

        val breakfastItems = sharedPreferences.getStringSet("breakfast_items", mutableSetOf())?.toMutableList() ?: mutableListOf()
        val lunchItems = sharedPreferences.getStringSet("lunch_items", mutableSetOf())?.toMutableList() ?: mutableListOf()
        val dinnerItems = sharedPreferences.getStringSet("dinner_items", mutableSetOf())?.toMutableList() ?: mutableListOf()
        val snackItems = sharedPreferences.getStringSet("snack_items", mutableSetOf())?.toMutableList() ?: mutableListOf()

        val breakfastAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, breakfastItems)
        val lunchAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, lunchItems)
        val dinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dinnerItems)
        val snackAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, snackItems)

        breakfastListView.adapter = breakfastAdapter
        lunchListView.adapter = lunchAdapter
        dinnerListView.adapter = dinnerAdapter
        snackListView.adapter = snackAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breakfastItems = sharedPreferences.getStringSet("breakfast_items", mutableSetOf())?.toMutableList() ?: mutableListOf()
        val lunchItems = sharedPreferences.getStringSet("lunch_items", mutableSetOf())?.toMutableList() ?: mutableListOf()
        val dinnerItems = sharedPreferences.getStringSet("dinner_items", mutableSetOf())?.toMutableList() ?: mutableListOf()
        val snackItems = sharedPreferences.getStringSet("snack_items", mutableSetOf())?.toMutableList() ?: mutableListOf()

        parentFragmentManager.setFragmentResultListener(
            "itemInputResult",
            this
        ) { _, result ->
            val listName = result.getString("listName")
            val itemName = result.getString("itemName")
            val calorieAmount = result.getString("calorie").toString()

            when (listName) {
                "breakfast" -> updateListView(
                    breakfastItems, itemName, calorieAmount, R.id.breakfastListView
                )

                "lunch" -> updateListView(
                    lunchItems, itemName, calorieAmount, R.id.lunchListView
                )

                "dinner" -> updateListView(
                    dinnerItems, itemName, calorieAmount, R.id.dinnerListView
                )

                "snack" -> updateListView(
                    snackItems, itemName, calorieAmount, R.id.snackListView
                )
            }
        }

        val addBreakfast = view.findViewById<TextView>(R.id.addToBreakfastList)
        val addLunch = view.findViewById<TextView>(R.id.addToLunchList)
        val addDinner = view.findViewById<TextView>(R.id.addToDinnerList)
        val addSnack = view.findViewById<TextView>(R.id.addToSnackList)

        addBreakfast.setOnClickListener { goToItemInputFragment("breakfast") }
        addLunch.setOnClickListener { goToItemInputFragment("lunch") }
        addDinner.setOnClickListener { goToItemInputFragment("dinner") }
        addSnack.setOnClickListener { goToItemInputFragment("snack") }
    }

    private fun goToItemInputFragment(listType: String) {
        val itemInputFragment = ItemInputFragment()

        val bundle = Bundle()
        bundle.putString("listType", listType)
        itemInputFragment.arguments = bundle

        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        transaction.add(R.id.frameLayout, itemInputFragment)

        transaction.addToBackStack(null)

        transaction.commit()
    }

    private fun updateListView(
        itemList: MutableList<String>,
        itemName: String?,
        calorieAmount: String?,
        listViewId: Int) {

        if (itemName != null) {
            itemList.add("$itemName  |  $calorieAmount cal")
        }

        val calorieInt = calorieAmount?.toInt() ?: 0

        when (listViewId) {
            R.id.breakfastListView -> {
                breakfastListAmount += calorieInt
                val breakfastAmountTextView =
                    view.findViewById<TextView>(R.id.breakfastCalListAmount)
                breakfastAmountTextView?.text = breakfastListAmount.toString()
            }

            R.id.lunchListView -> {
                lunchListAmount += calorieInt
                val lunchAmountTextView =
                    view.findViewById<TextView>(R.id.lunchCalListAmount)
                lunchAmountTextView?.text = lunchListAmount.toString()
            }

            R.id.dinnerListView -> {
                dinnerListAmount += calorieInt
                val lunchAmountTextView =
                    view.findViewById<TextView>(R.id.dinnerCalListAmount)
                lunchAmountTextView?.text = dinnerListAmount.toString()
            }

            R.id.snackListView -> {
            snackListAmount += calorieInt
            val lunchAmountTextView =
                view.findViewById<TextView>(R.id.snackCalListAmount)
            lunchAmountTextView?.text = snackListAmount.toString()
        }
        }

        val editor = sharedPreferences.edit()
        when (listViewId) {
            R.id.breakfastListView -> editor.putStringSet("breakfast_items", itemList.toSet())
            R.id.lunchListView -> editor.putStringSet("lunch_items", itemList.toSet())
            R.id.dinnerListView -> editor.putStringSet("dinner_items", itemList.toSet())
            R.id.snackListView -> editor.putStringSet("snack_items", itemList.toSet())
        }
        editor.putInt("breakfastCalorieAmount", breakfastListAmount)
        editor.putInt("lunchCalorieAmount", lunchListAmount)
        editor.putInt("dinnerCalorieAmount", dinnerListAmount)
        editor.putInt("snackCalorieAmount", snackListAmount)
        editor.apply()

        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, itemList)
        val listView = view.findViewById<ListView>(listViewId)
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun resetSharedPreferences() {
        val breakfastItems = sharedPreferences.getStringSet("breakfast_items", mutableSetOf())?.toMutableList() ?: mutableListOf()
        val lunchItems = sharedPreferences.getStringSet("lunch_items", mutableSetOf())?.toMutableList() ?: mutableListOf()
        val dinnerItems = sharedPreferences.getStringSet("dinner_items", mutableSetOf())?.toMutableList() ?: mutableListOf()
        val snackItems = sharedPreferences.getStringSet("snack_items", mutableSetOf())?.toMutableList() ?: mutableListOf()

        breakfastItems.clear()
        lunchItems.clear()
        dinnerItems.clear()
        snackItems.clear()

        val breakfastAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, breakfastItems)
        val breakfastListView = view.findViewById<ListView>(R.id.breakfastListView)
        breakfastListView.adapter = breakfastAdapter

        val lunchAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, lunchItems)
        val lunchListView = view.findViewById<ListView>(R.id.lunchListView)
        lunchListView.adapter = lunchAdapter

        val dinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dinnerItems)
        val dinnerListView = view.findViewById<ListView>(R.id.dinnerListView)
        dinnerListView.adapter = dinnerAdapter

        val snackAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, snackItems)
        val snackListView = view.findViewById<ListView>(R.id.snackListView)
        snackListView.adapter = snackAdapter
    }
}