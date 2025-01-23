package com.first.unjumbleit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class PlanetAdapter(
    private val context: Context,
    private val planets: List<Planet>
) : BaseAdapter() {

    override fun getCount(): Int {
        return planets.size
    }

    override fun getItem(position: Int): Planet {
        return planets[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_planet, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val planet = getItem(position)
        viewHolder.planetImage.setImageResource(planet.imageResId)
        viewHolder.planetName.text = planet.name

        return view
    }

    private class ViewHolder(view: View) {
        val planetImage: ImageView = view.findViewById(R.id.planetImage)
        val planetName: TextView = view.findViewById(R.id.planetName)
    }
}