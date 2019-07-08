package com.example.microdiary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.microdiary.Database.AppDatabase
import com.example.microdiary.Database.Entities.Memos
import com.example.microdiary.LayoutControls.LockableScrollView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_day_entry.*
import kotlinx.android.synthetic.main.settings_options_layout.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.DateFormatSymbols
import java.util.*
import android.text.Editable



class DayEntry : AppCompatActivity() {

    private val REQUEST_CODE_SPEECH_TEXT = 324;
    lateinit var menuBar: Menu
    lateinit var saveButton: MenuItem
    val db = AppDatabase(this)
    lateinit var EntryDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_entry)

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val date = intent.getStringExtra("date")
        EntryDate = date
        val scrollView: LockableScrollView = findViewById<LockableScrollView>(R.id.DayEntryScrollView)
        val DateText = findViewById<TextView>(R.id.textView2)
        val MemoEdt = findViewById<TextInputEditText>(R.id.MemoEdt)
        val EntryLayout = findViewById<LinearLayout>(R.id.EntryLayout)
        val MemoLayout = findViewById<TextInputLayout>(R.id.edtLayout)
        val SaveMemoBtn = findViewById<Button>(R.id.SaveMemoBtn)

        DateText.text = HandleDate(date)
        scrollView.setScrollingEnabled(true)

        EntryLayout.setOnClickListener {
            MemoLayout.setFocusable(false)
            MemoLayout.setFocusableInTouchMode(false)
        }

        MemoEdt.setOnFocusChangeListener(View.OnFocusChangeListener{v, hasFocus ->
            if(hasFocus)
                scrollView.setScrollingEnabled(false)
            else
                scrollView.setScrollingEnabled(true)
        })

        GlobalScope.launch {
            val data = db.MemoDAO().findByDate(date)
            this@DayEntry.runOnUiThread(Runnable {
                if(data == null)
                    MemoEdt.setText("")
                else
                    MemoEdt.setText(data.Memo.toString())
            })
        }

        MemoEdt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (::saveButton.isInitialized){
                    if (s.isNotEmpty()){
                        saveButton.setVisible(true)
                        saveButton.setIcon(R.drawable.ic_done_black_24dp)
                    }else{
                        saveButton.setVisible(false)
                    }
                }



            }
        })
        SaveMemoBtn.setOnClickListener{
            /*GlobalScope.launch {
                var newMemo:Memos = Memos(date,MemoEdt.text.toString())
                db.MemoDAO().insertAll(newMemo)
            }*/
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_mic -> {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Tell me about your day.")

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_TEXT)
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }

                return true
            }
            R.id.action_settings ->{
                GlobalScope.launch {
                    var newMemo:Memos = Memos(EntryDate,MemoEdt.text.toString())
                    db.MemoDAO().insertAll(newMemo)
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            REQUEST_CODE_SPEECH_TEXT  ->{
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    MemoEdt.setText(result[0])
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        saveButton = menu!!.findItem(R.id.action_settings)
        saveButton?.setVisible(false)

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onBackPressed() {

        if (MemoEdt.hasFocus()){
            MemoEdt.setFocusable(false)
            MemoEdt.setFocusableInTouchMode(false)
        }
        else
            super.onBackPressed()
    }

    fun HandleDate(date: String): String{
        val dateSplitArr = date.split("-").toTypedArray()
        var month = dateSplitArr[1]
        var monthName: String = ""
        val dfs = DateFormatSymbols()
        val months = dfs.months

        val monthStrings = arrayOf("1", "2","3","4","5","6","7","8","9","10","11","12")

        for (m in 0..11) {
            if(month == monthStrings[m])
                monthName = months[m]
        }

        return dateSplitArr[0] + " " + monthName + " " + dateSplitArr[2]
    }
}
