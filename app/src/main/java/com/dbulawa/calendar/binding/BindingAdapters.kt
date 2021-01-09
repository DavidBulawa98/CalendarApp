package com.dbulawa.calendar.binding

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.text.Editable
import android.text.TextUtils
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class BindingAdapters {
    companion object{
        @JvmStatic
        @BindingAdapter("date")
        fun datePicker(editText: EditText, date: Date?) {
            emptyCheck(editText.text, editText);
            editText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    selectDate(editText);
                }
            }
            editText.setOnClickListener { selectDate(editText) }
            editText.doAfterTextChanged { emptyCheck(it, editText) }
        }

        @JvmStatic
        @BindingAdapter("time")
        fun timePicker(editText: EditText, time: Date?) {
            emptyCheck(editText.text, editText);
            editText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    selectTime(editText);
                }
            }
            editText.setOnClickListener { selectTime(editText) }
            editText.doAfterTextChanged { emptyCheck(it, editText) }
        }

        @JvmStatic
        @BindingAdapter("titleValidator")
        fun titleValidator(editText: EditText, title: String?) {
            emptyCheck(editText.text, editText)
            editText.doAfterTextChanged { emptyCheck(it, editText) }
        }

        private fun emptyCheck(editable: Editable?, editText: EditText){
            if (TextUtils.isEmpty(editable)) {
                editText.error = "Cannot be empty";
            }else{
                editText.error = null
            }
        }

        private fun selectDate(textView: EditText){
            val calendar = Calendar.getInstance();
            val sdformat = SimpleDateFormat.getDateInstance()
            val dateFromListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                textView.setText(sdformat.format(calendar.time))
            }

            DatePickerDialog(textView.context, dateFromListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        private fun selectTime(textView: EditText){
            val calendar = Calendar.getInstance();
            val timeFromListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                textView.setText("$hourOfDay:$minute:00")
            }

            TimePickerDialog(textView.context, timeFromListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }
    }
}