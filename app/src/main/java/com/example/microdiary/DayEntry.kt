package com.example.microdiary

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.microdiary.Database.AppDatabase
import com.example.microdiary.Database.Entities.Memos
import com.example.microdiary.LayoutControls.LockableScrollView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_day_entry.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols

class DayEntry : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_entry)

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val date = intent.getStringExtra("date")

        val scrollView: LockableScrollView = findViewById<LockableScrollView>(R.id.DayEntryScrollView)
        val DateText = findViewById<TextView>(R.id.textView2)
        val MemoEdt = findViewById<TextInputEditText>(R.id.MemoEdt)
        val EntryLayout = findViewById<LinearLayout>(R.id.EntryLayout)
        val MemoLayout = findViewById<TextInputLayout>(R.id.edtLayout)
        val SaveMemoBtn = findViewById<Button>(R.id.SaveMemoBtn)

        DateText.text = HandleDate(date)
        scrollView.setScrollingEnabled(true)
        val db = AppDatabase(this)

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

        SaveMemoBtn.setOnClickListener{
            /*GlobalScope.launch {
                var newMemo:Memos = Memos(date,MemoEdt.text.toString())
                db.MemoDAO().insertAll(newMemo)
            }*/
        }
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
