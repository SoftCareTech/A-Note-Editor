package com.softcare.raphnote


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ShareCompat.IntentReader.from
import androidx.core.content.res.ResourcesCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat.from
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.softcare.raphnote.databinding.ActivityMainBinding
import com.softcare.raphnote.model.ChangeObserver


class MainActivity : AppCompatActivity() ,SearchView.OnQueryTextListener
{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
     val  orderAscending:Boolean
         get(){
             val s: SharedPreferences = this.getSharedPreferences(
                 "RaphNote",
                 MODE_PRIVATE
             )
           return (s.getInt("orderBy",1)==0)
         }
    val column:String
        get(){
            val s: SharedPreferences = this.getSharedPreferences(
                "RaphNote",
                MODE_PRIVATE
            )
           return  resources.getStringArray(R.array.sort_values)[s.getInt("sortBy",0)]
        }

    private val navController1: NavController
        get() {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            return navController
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.addNote.setOnClickListener {   changeObserver.editNote()  }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(isList)
        menuInflater.inflate(R.menu.menu_main, menu)
        else
        menuInflater.inflate(R.menu.menu_view, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(changeObserver.optionMenu(item.itemId))
            return  true
        return   super.onOptionsItemSelected(item)
        }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
      val   mSearchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        mSearchView.setOnQueryTextListener(this)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(isList ) changeObserver.searchNotes(orderAscending,column ,query)
        else changeObserver.searchText(query)
 return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(isList)changeObserver.searchNotes(orderAscending,column ,newText)
      else changeObserver.searchText(newText)
        return true
    }

    lateinit var changeObserver:ChangeObserver
            var isList= true;

    fun changeMenu( changeObserver: ChangeObserver, isList:Boolean){
        this.changeObserver=changeObserver
        this.isList=isList
        val fab=findViewById<FloatingActionButton>(R.id.add_note)
        if( fab!=null)
        if( isList) {
          fab.setImageDrawable(
              ResourcesCompat.getDrawable( resources,
                    R.drawable.ic_baseline_add_24, null
                )
            )
            fab.contentDescription=getString(R.string.create_note)

        } else {
           fab.setImageDrawable(
               ResourcesCompat.getDrawable( resources,  R.drawable.ic_baseline_edit_24,null  )
            )
            fab.contentDescription=getString(R.string.edit_note)
        }
        invalidateOptionsMenu()
       }


}