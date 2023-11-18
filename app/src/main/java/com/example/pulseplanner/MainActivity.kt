package com.example.pulseplanner

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.pulseplanner.Repositories.ExerciseRepository
import com.example.pulseplanner.Repositories.TrainingRepository
import com.example.pulseplanner.databinding.ActivityMainBinding
import com.example.pulseplanner.model.Category
import com.example.pulseplanner.model.CategoryRepository
import com.example.pulseplanner.model.Exercise
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup the repositories
        CategoryRepository.setContext(this)
        ExerciseRepository.setContext(this)
        TrainingRepository.setContext(this)

//        // create 10 in a list
//        if (ExerciseRepository.getInstance().getExercises().size == 0) {
//            var exercises = mutableListOf<Exercise>()
//
//            val categories = listOf(
//                Category("Beginner"),
//                Category("Forehand"),
//                Category("Backhand")
//            )
//
//            val descriptions = listOf(
//                "Description for Exercise 1",
//                "Description for Exercise 2",
//                "Description for Exercise 3",
//                "Description for Exercise 4",
//                "Description for Exercise 5",
//                "Description for Exercise 6"
//            )
//            for (i in 0 until 6) {
//                val exerciseDescription = descriptions[i] + "ah ".repeat(50 * (i + 1))
//                exercises.add(Exercise("Exercise ${i + 1}", listOf(categories[i % 3], categories[i % 2]), exerciseDescription))
//            }
//
//            // save the exercises one by one
//            for (exercise in exercises) {
//                ExerciseRepository.getInstance().createExercise(exercise)
//            }
//        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_exercise, R.id.nav_category, R.id.nav_exercise_overview, R.id.nav_add_training, R.id.nav_settings_menu
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}