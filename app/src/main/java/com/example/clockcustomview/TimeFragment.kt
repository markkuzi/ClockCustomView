package com.example.clockcustomview

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.clockcustomview.databinding.TimeFragmentBinding


class TimeFragment : Fragment() {

    private lateinit var binding: TimeFragmentBinding
    private var preferences: SharedPreferences? = null
    private var timeZone1 : TimeZones? = null
    private var timeZone2 : TimeZones? = null
    private var timeZone3 : TimeZones? = null
    private var timeZone4 : TimeZones? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TimeFragmentBinding.inflate(inflater, container, false)
        preferences = requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        initTimeZone()
        btnTextInit()
        initClockTimeZone()
        actionWithDialogs()

        return binding.root
    }

    private fun initTimeZone() {
        timeZone1 = TimeZones(preferences?.getString(PREF_TIME_ZONE_NAME_1, null) ?: DEFAULT_TIME_ZONE_1.name,
        preferences?.getString(PREF_TIME_ZONE_VALUE_1, null) ?: DEFAULT_TIME_ZONE_1.value)
        timeZone2 = TimeZones(preferences?.getString(PREF_TIME_ZONE_NAME_2, null) ?: DEFAULT_TIME_ZONE_2.name,
            preferences?.getString(PREF_TIME_ZONE_VALUE_2, null) ?: DEFAULT_TIME_ZONE_2.value)
        timeZone3 = TimeZones(preferences?.getString(PREF_TIME_ZONE_NAME_3, null) ?: DEFAULT_TIME_ZONE_3.name,
            preferences?.getString(PREF_TIME_ZONE_VALUE_3, null) ?: DEFAULT_TIME_ZONE_3.value)
        timeZone4 = TimeZones(preferences?.getString(PREF_TIME_ZONE_NAME_4, null) ?: DEFAULT_TIME_ZONE_4.name,
            preferences?.getString(PREF_TIME_ZONE_VALUE_4, null) ?: DEFAULT_TIME_ZONE_4.value)
    }

    private fun initClockTimeZone() {
        binding.customCLock1.setTimeZone(timeZone1?.value ?: DEFAULT_TIME_ZONE_1.value)
        binding.customCLock2.setTimeZone(timeZone2?.value ?: DEFAULT_TIME_ZONE_2.value)
        binding.customCLock3.setTimeZone(timeZone3?.value ?: DEFAULT_TIME_ZONE_3.value)
        binding.customCLock4.setTimeZone(timeZone4?.value ?: DEFAULT_TIME_ZONE_4.value)
    }

    private fun btnTextInit() {
        binding.changeTimeZoneBtn1.text = timeZone1?.name ?: DEFAULT_TIME_ZONE_1.name
        binding.changeTimeZoneBtn2.text = timeZone2?.name ?: DEFAULT_TIME_ZONE_2.name
        binding.changeTimeZoneBtn3.text = timeZone3?.name ?: DEFAULT_TIME_ZONE_3.name
        binding.changeTimeZoneBtn4.text = timeZone4?.name ?: DEFAULT_TIME_ZONE_4.name
    }

    private fun actionWithDialogs() {
        binding.changeTimeZoneBtn1.setOnClickListener {
            showCitiesChoiceAlertDialog(CLOCK_ID_1, timeZone1 ?: DEFAULT_TIME_ZONE_1)
        }
        binding.changeTimeZoneBtn2.setOnClickListener {
            showCitiesChoiceAlertDialog(CLOCK_ID_2, timeZone2 ?: DEFAULT_TIME_ZONE_2)
        }
        binding.changeTimeZoneBtn3.setOnClickListener {
            showCitiesChoiceAlertDialog(CLOCK_ID_3, timeZone3 ?: DEFAULT_TIME_ZONE_3)
        }
        binding.changeTimeZoneBtn4.setOnClickListener {
            showCitiesChoiceAlertDialog(CLOCK_ID_4, timeZone4 ?: DEFAULT_TIME_ZONE_4)
        }
    }

    private fun showCitiesChoiceAlertDialog(clockId : Int, timeZone: TimeZones) {
        val timeZones = AvailableTimeZoneValues.createTimeZoneValues(timeZone)
        val timeZoneText = timeZones.values
            .map {it.name}
            .toTypedArray()

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.cities)
            .setSingleChoiceItems(timeZoneText, timeZones.currentIndex) { dialog, which ->
                when(clockId){
                    CLOCK_ID_1 -> {binding.customCLock1.setTimeZone(timeZones.values[which].value)
                            binding.changeTimeZoneBtn1.text = timeZones.values[which].name
                            timeZone1 = timeZones.values[which]
                            preferences?.edit()
                                ?.putString(PREF_TIME_ZONE_NAME_1, timeZones.values[which].name)
                                ?.putString(PREF_TIME_ZONE_VALUE_1, timeZones.values[which].value)
                                ?.apply()}
                    CLOCK_ID_2 -> {binding.customCLock2.setTimeZone(timeZones.values[which].value)
                            binding.changeTimeZoneBtn2.text = timeZones.values[which].name
                            timeZone2 = timeZones.values[which]
                        preferences?.edit()
                            ?.putString(PREF_TIME_ZONE_NAME_2, timeZones.values[which].name)
                            ?.putString(PREF_TIME_ZONE_VALUE_2, timeZones.values[which].value)
                            ?.apply()}
                    CLOCK_ID_3 -> {binding.customCLock3.setTimeZone(timeZones.values[which].value)
                            binding.changeTimeZoneBtn3.text = timeZones.values[which].name
                            timeZone3 = timeZones.values[which]
                        preferences?.edit()
                            ?.putString(PREF_TIME_ZONE_NAME_3, timeZones.values[which].name)
                            ?.putString(PREF_TIME_ZONE_VALUE_3, timeZones.values[which].value)
                            ?.apply()}
                    CLOCK_ID_4 -> {binding.customCLock4.setTimeZone(timeZones.values[which].value)
                            binding.changeTimeZoneBtn4.text = timeZones.values[which].name
                            timeZone4 = timeZones.values[which]
                        preferences?.edit()
                            ?.putString(PREF_TIME_ZONE_NAME_4, timeZones.values[which].name)
                            ?.putString(PREF_TIME_ZONE_VALUE_4, timeZones.values[which].value)
                            ?.apply()}
                }
                            dialog.dismiss()
                }
            .create()
        dialog.show()
    }

    companion object {

        const val CLOCK_ID_1 = 1
        const val CLOCK_ID_2 = 2
        const val CLOCK_ID_3 = 3
        const val CLOCK_ID_4 = 4
        val DEFAULT_TIME_ZONE_1 = TimeZones( "Москва, Россия", "Europe/Moscow")
        val DEFAULT_TIME_ZONE_2 = TimeZones( "Нью Йорк, США", "America/New_York")
        val DEFAULT_TIME_ZONE_3 = TimeZones( "Бангкок, Таиланд", "Asia/Bangkok")
        val DEFAULT_TIME_ZONE_4 = TimeZones( "Канберра, Австралия", "Australia/Canberra")
        const val APP_PREFERENCES = "APP_PREFERENCES"
        const val PREF_TIME_ZONE_NAME_1 = "PREF_TIME_ZONE_NAME_1"
        const val PREF_TIME_ZONE_NAME_2 = "PREF_TIME_ZONE_NAME_2"
        const val PREF_TIME_ZONE_NAME_3 = "PREF_TIME_ZONE_NAME_3"
        const val PREF_TIME_ZONE_NAME_4 = "PREF_TIME_ZONE_NAME_4"
        const val PREF_TIME_ZONE_VALUE_1 = "PREF_TIME_ZONE_VALUE_1"
        const val PREF_TIME_ZONE_VALUE_2 = "PREF_TIME_ZONE_VALUE_2"
        const val PREF_TIME_ZONE_VALUE_3 = "PREF_TIME_ZONE_VALUE_3"
        const val PREF_TIME_ZONE_VALUE_4 = "PREF_TIME_ZONE_VALUE_4"

    }
}
