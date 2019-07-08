package com.example.microdiary

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.preference.PreferenceFragmentCompat
import com.example.microdiary.EnumClasses.Settings
import com.example.microdiary.LayoutControls.SettingsCards

class SettingsActivity : AppCompatActivity() {

    lateinit var settingsLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        settingsLayout = findViewById<LinearLayout>(R.id.SettingsLayout)

        settingsLayout.addView(SettingsCards(this@SettingsActivity,Settings.DoseDayColour))

        settingsLayout.getChildAt(0)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}