package com.softcare.raphnote

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.softcare.raphnote.databinding.ActivityEditBinding
import com.softcare.raphnote.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val s: SharedPreferences = this.getSharedPreferences(
            "RaphNote",
            MODE_PRIVATE
        )
        binding.orderBy.setSelection(s.getInt("orderBy",0))
        binding.sortBy.setSelection(s.getInt("sortBy",0))
        binding.lockAfter.setSelection(s.getInt("lockAfter",0))

        binding.orderBy.setOnItemSelectedListener( object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val editor = s.edit()
                editor.putInt("orderBy", position)
                editor.apply()
                editor.commit()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        })
        binding.sortBy.setOnItemSelectedListener( object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val editor = s.edit()
                editor.putInt("sortBy", position)
                editor.apply()
                editor.commit()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        })
        binding.lockAfter.setOnItemSelectedListener( object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val editor = s.edit()
                editor.putInt("lockAfter", position)
                editor.apply()
                editor.commit()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        })

        }


    }