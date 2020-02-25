package com.lab.sports.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.lab.sports.utils.NetworkUtils
import com.google.android.material.snackbar.Snackbar
import com.lab.sports.R
import com.lab.sports.data.local.AppDataBase
import com.lab.sports.viewmodel.MainActivityViewModel
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var dataBaseManager: AppDataBase
    private lateinit var waitingDialog: SpotsDialog
    lateinit var mViewModel: MainActivityViewModel
    private var snackbar: Snackbar? = null

    private lateinit var searchView: SearchView
    private lateinit var myActionMenuItem: MenuItem
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.all_teams)

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        waitingDialog = SpotsDialog(this, R.style.waiting_dialog)
        fragmentManager = supportFragmentManager

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.teams -> {
                    showNavigation()
                    setFragment(TeamsFragment())
                }
                R.id.favorites -> {
                    setFragment(FavoriteFragment())
                }
            }
            true
        }
        bottom_navigation.selectedItemId = R.id.teams
    }

    fun showNavigation() {
        bottom_navigation.visibility = View.VISIBLE
    }

    fun hideNavigation() {
        bottom_navigation.visibility = View.GONE
    }

    fun showLoader() = waitingDialog.show()
    fun dismissLoader() = waitingDialog.dismiss()


    fun getViewModel(): MainActivityViewModel = mViewModel

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        myActionMenuItem = menu!!.findItem(R.id.action_search)

        searchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                var fragment = fragmentManager.findFragmentById(R.id.frame_container)
                if (fragment is TeamsFragment) {
                    fragment.callSearchApi(query!!)

                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        return true
    }

    @Override
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        if (fragment is LastEventsFragment) {
            setFragment(TeamsFragment())
            showNavigation()
            searchView.isIconified = true
        } else if (fragment is TeamsFragment) {
            finish()
        }
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment, fragment::class.java.name)
            .commit()
    }
}
