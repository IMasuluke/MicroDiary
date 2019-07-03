package com.example.microdiary.LayoutControls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.microdiary.EnumClasses.Settings
import com.example.microdiary.R
import java.util.zip.Inflater

class SettingsCards: LinearLayout {

    constructor(context: Context, _cardName: Settings): super(context){
        CreateCard(_cardName)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    fun CreateCard(_cardName: Settings){
        inflate(context,R.layout.settings_options_layout,this)

        val settingText: TextView = findViewById(R.id.SettingText)
        settingText.setText(_cardName.toString())
    }
}