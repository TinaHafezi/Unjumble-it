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

        val planets = listOf(
            Planet("Monsters", R.drawable.planet_monsters, "Monsters"),
            Planet("Science", R.drawable.planet_science, "Science"),
            Planet("Supernatural", R.drawable.planet_supernatural, "Supernatural"),
            Planet("Parasites", R.drawable.planet_parasites, "Parasites"),
            Planet("Multiverse", R.drawable.planet_multiverse, "Multiverse"),
            Planet("Venus", R.drawable.planet_venus, "Venus")
        )

        val adapter = PlanetAdapter(this, planets)
        planetsGridView.adapter = adapter

        planetsGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedPlanet = planets[position]
            Log.d("MenuActivity", "Planet clicked: ${selectedPlanet.name}")


            val intent = Intent(this, LevelsActivity::class.java)
            intent.putExtra("PLANET_NAME", selectedPlanet.planetName)
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