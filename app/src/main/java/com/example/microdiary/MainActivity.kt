package com.example.microdiary
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.widget.*
import java.util.*
import androidx.appcompat.app.AppCompatActivity
import com.example.microdiary.Database.AppDatabase
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    var dosedays = mutableListOf(CalendarDay.from(2019,6,23))
    lateinit var calendar: MaterialCalendarView
    lateinit var NewEntryButton: Button
    lateinit var CalendarProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch {
            LoadCalendar()
        }

        NewEntryButton = findViewById<Button>(R.id.btnRecordDay) as Button
        calendar = findViewById<MaterialCalendarView>(R.id.calendarView)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var height = displayMetrics.heightPixels

        SetUpCalendar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val mic = menu?.findItem(R.id.action_mic)
        mic?.setVisible(false)
        return super.onPrepareOptionsMenu(menu)
    }

    fun SetUpCalendar(){
        lateinit var date: String

        calendar.setOnDateChangedListener(OnDateSelectedListener { materialCalendarView, calendarDay, b ->
            date =  materialCalendarView?.selectedDate?.day?.toString() + "-" +
                    materialCalendarView?.selectedDate?.month.toString()+ "-" +
                    materialCalendarView?.selectedDate?.year.toString();
        })

        NewEntryButton.setOnClickListener {
            val intent = Intent(this, DayEntry::class.java)
            intent.putExtra("date", date)
            startActivity(intent)
        }
    }

    fun LoadCalendar(){
        var dosedays = mutableListOf<CalendarDay>()

        GlobalScope.launch {
            val db = AppDatabase(this@MainActivity)
            val data = db.MemoDAO().getAll()

            if(data.isNotEmpty()){
                for(m in 0..(data.size - 1)){
                    val dateSplitArr = data[m].date.split("-").toTypedArray()
                    dosedays.add(CalendarDay.from(dateSplitArr[2].toInt(),dateSplitArr[1].toInt(),dateSplitArr[0].toInt()))
                }
            }

            this@MainActivity.runOnUiThread(Runnable {
                calendar.addDecorator(EventDecorator(Color.BLUE,dosedays))
                calendar.addDecorator(EventDecorator(Color.GREEN,dosedays))
            })
        }
    }

    override fun onResume() {
        super.onResume()

        GlobalScope.launch {
            LoadCalendar()
        }

    }

}

class EventDecorator(private val color: Int, dates: Collection<CalendarDay>) : DayViewDecorator {
    private val dates: HashSet<CalendarDay>

    init {
        this.dates = HashSet(dates)
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5f,Color.BLUE))
    }
}

