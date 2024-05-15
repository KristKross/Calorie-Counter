package com.example.caloriecounter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LogFoodFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var breakfastListAmount = 0
    private var lunchListAmount = 0
    private var dinnerListAmount = 0
    private var snackListAmount = 0

    private var breakfastItemList = mutableListOf<String>()
    private var lunchItemList = mutableListOf<String>()
    private var dinnerItemList = mutableListOf<String>()
    private var snackItemList = mutableListOf<String>()

    private lateinit var view: View

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
        val view = inflater.inflate(R.layout.fragment_logfood, container, false)
        this.view = view

        loadData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breakfastItems = mutableListOf<String>()
        val lunchItems = mutableListOf<String>()
        val dinnerItems = mutableListOf<String>()
        val snackItems = mutableListOf<String>()

        val breakfastAmountTextView =
            view.findViewById<TextView>(R.id.breakfastCalListAmount)
        breakfastAmountTextView?.text = breakfastListAmount.toString()

        val lunchAmountTextView =
            view.findViewById<TextView>(R.id.lunchCalListAmount)
        lunchAmountTextView?.text = lunchListAmount.toString()

        val dinnerAmountTextView =
            view.findViewById<TextView>(R.id.dinnerCalListAmount)
        dinnerAmountTextView?.text = dinnerListAmount.toString()

        val snackAmountTextView =
            view.findViewById<TextView>(R.id.snackCalListAmount)
        snackAmountTextView?.text = snackListAmount.toString()

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

    private fun saveData() {
        val fileName = "calorie_data.txt"
        val file = File(requireContext().filesDir, fileName)

        val dataString = "$breakfastListAmount, $lunchListAmount, $dinnerListAmount," +
                " $snackListAmount," +
                " $breakfastItemList," +
                " $lunchItemList," +
                " $dinnerItemList," +
                " $snackItemList,"
        file.writeText(dataString)
    }

    private fun loadData() {
        val fileName = "calorie_data.txt"
        val file = File(requireContext().filesDir, fileName)

        try {
            val dataString = file.readText()

            if (dataString.isNotEmpty()) {
                val dataValues = dataString.split(",")
                breakfastListAmount = dataValues[0].toInt()
                lunchListAmount = dataValues[1].toInt()
                dinnerListAmount = dataValues[2].toInt()
                snackListAmount = dataValues[3].toInt()
                breakfastItemList = dataValues[4].split(",").toMutableList()
                lunchItemList = dataValues[5].split(",").toMutableList()
                dinnerItemList = dataValues[6].split(",").toMutableList()
                snackItemList = dataValues[7].split(",").toMutableList()

            } else {
                breakfastListAmount = 0
                lunchListAmount = 0
                dinnerListAmount = 0
                snackListAmount = 0
                breakfastItemList = mutableListOf()
                lunchItemList = mutableListOf()
                dinnerItemList = mutableListOf()
                snackItemList = mutableListOf()
            }

        } catch (e: Exception) {
            val fileContents = file.readText()
            Log.d("LoadData", "File contents: $fileContents")
        }
    }

    override fun onStop() {
        super.onStop()
        saveData()
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
                breakfastItemList = itemList
            }

            R.id.lunchListView -> {
                lunchListAmount += calorieInt
                val lunchAmountTextView =
                    view.findViewById<TextView>(R.id.lunchCalListAmount)
                lunchAmountTextView?.text = lunchListAmount.toString()
                lunchItemList = itemList
            }

            R.id.dinnerListView -> {
                dinnerListAmount += calorieInt
                val dinnerAmountTextView =
                    view.findViewById<TextView>(R.id.dinnerCalListAmount)
                dinnerAmountTextView?.text = dinnerListAmount.toString()
                dinnerItemList = itemList
            }

            R.id.snackListView -> {
                snackListAmount += calorieInt
                val snackAmountTextView =
                    view.findViewById<TextView>(R.id.snackCalListAmount)
                snackAmountTextView?.text = snackListAmount.toString()
                snackItemList = itemList
            }
        }
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, itemList)
        val listView = view.findViewById<ListView>(listViewId)
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()
    }
    private fun resetData() {
        val fileName = "calorie_data.txt"
        val file = File(requireContext().filesDir, fileName)

        file.writeText("")
    }
}