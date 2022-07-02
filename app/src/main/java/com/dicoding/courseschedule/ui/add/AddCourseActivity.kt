package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.isEmpty
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var viewModel: AddCourseViewModel
    private lateinit var startTime: String
    private lateinit var endTime: String
    private lateinit var editStartTime: TextView
    private lateinit var editEndTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        editStartTime = findViewById(R.id.edit_start_time)
        editEndTime = findViewById(R.id.edit_end_time)

        val timePickerFragment = TimePickerFragment()
        val startPicker = findViewById<ImageButton>(R.id.clock_add)
        val endPicker = findViewById<ImageButton>(R.id.clock_end)
        startPicker.setOnClickListener {
            timePickerFragment.show(supportFragmentManager, "start")
        }
        endPicker.setOnClickListener {
            timePickerFragment.show(supportFragmentManager, "end")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_insert -> {
                val editCourseName: TextInputEditText = findViewById(R.id.edit_coursename)
                val courseName = editCourseName.text.toString().trim()

                val daySpinner: Spinner = findViewById(R.id.day_spinner)
                val day = daySpinner.selectedItemPosition

                val editLecturer: TextInputEditText = findViewById(R.id.edit_lecturer)
                val lecturer =
                    findViewById<TextInputEditText>(R.id.edit_lecturer).text.toString().trim()

                val editNote: TextInputEditText = findViewById(R.id.edit_note)
                val note = editNote.text.toString().trim()

                if (courseName.isEmpty() || lecturer.isEmpty() || note.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                    Toast.makeText(applicationContext, "Please fill the form!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                    finish()
                    Toast.makeText(applicationContext, "Course has been added!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        if (tag == "start") {
            editStartTime.text = timeFormat.format(calender.time)
            startTime = timeFormat.format(calender.time)
        } else {
            editEndTime.text = timeFormat.format(calender.time)
            endTime = timeFormat.format(calender.time)
        }
    }
}