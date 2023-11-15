package com.example.pulseplanner.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate
import com.example.pulseplanner.R
import com.example.pulseplanner.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val themeSwitch = root.findViewById<Switch>(R.id.themeSwitch)

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Dark mode
                setTheme(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Light mode
                setTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
            requireActivity().recreate()
        }
        return root
    }

    private fun setTheme(themeMode: Int) {
        if (isAdded && activity != null && AppCompatDelegate.getDefaultNightMode() != themeMode) {
            AppCompatDelegate.setDefaultNightMode(themeMode)
            // Delay recreation by a short interval
            Handler(Looper.getMainLooper()).postDelayed({
                if (isAdded && activity != null) {
                    requireActivity().recreate()
                }
            }, 100) // Adjust the delay as needed
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
