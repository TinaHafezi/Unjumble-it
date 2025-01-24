package com.first.unjumbleit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var planetsGridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        Log.d("MenuActivity", "onCreate: Activity created")

        planetsGridView = findViewById(R.id.planetsGridView)

        // Create a list of planets
        val planets = listOf(
            Planet("Parasites", R.drawable.planet_parasites, "Parasites"), // Replace with actual image resource
            Planet("Science", R.drawable.planet_science, "Science"), // Replace with actual image resource
            Planet("Supernatural", R.drawable.planet_supernatural, "Supernatural"),
            Planet("Monsters", R.drawable.planet_monsters, "Monsters"),
            Planet("Multiverse", R.drawable.planet_multiverse, "Multiverse") // Replace with actual image resource
// Replace with actual image resource
        )

        // Set up the adapter
        val adapter = PlanetAdapter(this, planets)
        planetsGridView.adapter = adapter

        // Handle planet clicks
        planetsGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedPlanet = planets[position]
            Log.d("MenuActivity", "Planet clicked: ${selectedPlanet.name}")

            // Start LevelsActivity and pass the planet ID
            // When starting LevelsActivity, pass the planet name instead of ID
            val intent = Intent(this, LevelsActivity::class.java)
            intent.putExtra("PLANET_NAME", selectedPlanet.planetName) // Pass planet name
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        MusicManager.playMusic(this)
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pauseMusic()
    }
}