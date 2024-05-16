package com.example.caloriecounter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
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

    private lateinit var breakfastAdapter: CustomAdapter
    private lateinit var lunchAdapter: CustomAdapter
    private lateinit var dinnerAdapter: CustomAdapter
    private lateinit var snackAdapter: CustomAdapter

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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        breakfastAdapter = CustomAdapter(requireContext(), breakfastItemList)
        val breakfastListView = view.findViewById<ListView>(R.id.breakfastListView)
        breakfastListView.adapter = breakfastAdapter

        lunchAdapter = CustomAdapter(requireContext(), lunchItemList)
        val lunchListView = view.findViewById<ListView>(R.id.lunchListView)
        lunchListView.adapter = lunchAdapter

        dinnerAdapter = CustomAdapter(requireContext(), dinnerItemList)
        val dinnerListView = view.findViewById<ListView>(R.id.dinnerListView)
        dinnerListView.adapter = dinnerAdapter

        snackAdapter = CustomAdapter(requireContext(), snackItemList)
        val snackListView = view.findViewById<ListView>(R.id.snackListView)
        snackListView.adapter = snackAdapter

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

        breakfastListView.setOnItemLongClickListener { _, _, position, _ ->
            val item = breakfastItemList[position]
            val calories = item.split("|")[1].trim().split(" ")[0].toInt()

            breakfastListAmount -= calories
            breakfastItemList.removeAt(position)

            val adapter = CustomAdapter(requireContext(), breakfastItemList)
            breakfastListView.adapter = adapter
            adapter.notifyDataSetChanged()

            breakfastAmountTextView?.text = breakfastListAmount.toString()

            saveData()

            true
        }

        lunchListView.setOnItemLongClickListener { _, _, position, _ ->
            val item = lunchItemList[position]
            val calories = item.split("|")[1].trim().split(" ")[0].toInt()

            lunchListAmount -= calories
            lunchItemList.removeAt(position)

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, lunchItemList)
            lunchListView.adapter = adapter
            adapter.notifyDataSetChanged()


            lunchAmountTextView?.text = lunchListAmount.toString()

            saveData()

            true
        }

        dinnerListView.setOnItemLongClickListener { _, _, position, _ ->
            val item = dinnerItemList[position]
            val calories = item.split("|")[1].trim().split(" ")[0].toInt()

            dinnerListAmount -= calories
            dinnerItemList.removeAt(position)

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dinnerItemList)
            dinnerListView.adapter = adapter
            adapter.notifyDataSetChanged()

            dinnerAmountTextView?.text = dinnerListAmount.toString()

            saveData()

            true
        }

        snackListView.setOnItemLongClickListener { _, _, position, _ ->
            val item = snackItemList[position]
            val calories = item.split("|")[1].trim().split(" ")[0].toInt()

            snackListAmount -= calories
            snackItemList.removeAt(position)

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, snackItemList)
            snackListView.adapter = adapter
            adapter.notifyDataSetChanged()

            snackAmountTextView?.text = snackListAmount.toString()

            saveData()

            true
        }

        parentFragmentManager.setFragmentResultListener(
            "itemInputResult",
            this
        ) { _, result ->
            val listName = result.getString("listName")
            val itemName = result.getString("itemName")
            val calorieAmount = result.getString("calorie").toString()

            when (listName) {
                "breakfast" -> updateListView(
                    breakfastItemList, itemName, calorieAmount, R.id.breakfastListView
                )

                "lunch" -> updateListView(
                    lunchItemList, itemName, calorieAmount, R.id.lunchListView
                )

                "dinner" -> updateListView(
                    dinnerItemList, itemName, calorieAmount, R.id.dinnerListView
                )

                "snack" -> updateListView(
                    snackItemList, itemName, calorieAmount, R.id.snackListView
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

                val adapter = CustomAdapter(requireContext(), breakfastItemList)
                val listView = view.findViewById<ListView>(listViewId)
                listView?.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            R.id.lunchListView -> {
                lunchListAmount += calorieInt
                val lunchAmountTextView =
                    view.findViewById<TextView>(R.id.lunchCalListAmount)
                lunchAmountTextView?.text = lunchListAmount.toString()

                val adapter = CustomAdapter(requireContext(), lunchItemList)
                val listView = view.findViewById<ListView>(listViewId)
                listView?.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            R.id.dinnerListView -> {
                dinnerListAmount += calorieInt
                val dinnerAmountTextView =
                    view.findViewById<TextView>(R.id.dinnerCalListAmount)
                dinnerAmountTextView?.text = dinnerListAmount.toString()

                val adapter = CustomAdapter(requireContext(), dinnerItemList)
                val listView = view.findViewById<ListView>(listViewId)
                listView?.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            R.id.snackListView -> {
                snackListAmount += calorieInt
                val snackAmountTextView =
                    view.findViewById<TextView>(R.id.snackCalListAmount)
                snackAmountTextView?.text = snackListAmount.toString()

                val adapter = CustomAdapter(requireContext(), snackItemList)
                val listView = view.findViewById<ListView>(listViewId)
                listView?.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun saveData() {
        val dataFile = "calorie_data.txt"
        val file = File(requireContext().filesDir, dataFile)

        val dataString = "$breakfastListAmount;" +
            "$lunchListAmount;" +
            "$dinnerListAmount;" +
            "$snackListAmount;" +
            "${breakfastItemList.joinToString()};" +
            "${lunchItemList.joinToString()};" +
            "${dinnerItemList.joinToString()};" +
            "${snackItemList.joinToString()};"

        Log.d("SaveData", "dataString: $dataString")

        file.writeText(dataString)
    }

    private fun loadData() {
        val dataFile = "calorie_data.txt"
        val file = File(requireContext().filesDir, dataFile)

        try {
            val calorieDataString = file.readText()

            if (calorieDataString.isNotEmpty()) {
                val dataValues = calorieDataString.split(";")
                Log.d("LoadData", "dataString: $calorieDataString")

                breakfastListAmount = dataValues[0].trim().toInt()
                lunchListAmount = dataValues[1].trim().toInt()
                dinnerListAmount = dataValues[2].trim().toInt()
                snackListAmount = dataValues[3].trim().toInt()

                if (dataValues[4].isNotEmpty()) {
                    breakfastItemList.addAll(dataValues[4].trim()
                        .replace("[", "").replace
                            ("]", "").split(",").map { it.trim() })
                }
                if (dataValues[5].isNotEmpty()) {
                    lunchItemList.addAll(dataValues[5].trim()
                        .replace("[", "").replace
                            ("]", "").split(",").map { it.trim() })
                }
                if (dataValues[6].isNotEmpty()) {
                    dinnerItemList.addAll(dataValues[6].trim()
                        .replace("[", "").replace
                            ("]", "").split(",").map { it.trim() })
                }
                if (dataValues[7].isNotEmpty()) {
                    snackItemList.addAll(dataValues[7].trim()
                        .replace("[", "").replace
                            ("]", "").split(",").map { it.trim() })
                }

            } else {
                resetData()
            }
        } catch (e: Exception) {
            Log.e("LoadData", "Error loading data", e)
        }
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    private fun resetData() {
        breakfastListAmount = 0
        lunchListAmount = 0
        dinnerListAmount = 0
        snackListAmount = 0
        breakfastItemList.clear()
        lunchItemList.clear()
        dinnerItemList.clear()
        snackItemList.clear()
    }
}