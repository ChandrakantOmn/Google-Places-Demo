package com.omniwyse.placessdksample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class PlacesActivity : AppCompatActivity() {
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapter: AutoCompleteAdapter
    private lateinit var responseView: TextView
    private lateinit var placesClient: PlacesClient

    @SuppressLint("SetTextI18n")
    private val autocompleteClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
        try {
            val item = adapter.getItem(i)
            var placeID: String? = null
            if (item != null) {
                placeID = item.placeId
            }
            //                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
            //                Use only those fields which are required.

            val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            var request: FetchPlaceRequest? = null
            if (placeID != null) {
                request = FetchPlaceRequest.builder(placeID, placeFields)
                        .build()
            }

            if (request != null) {
                placesClient.fetchPlace(request).addOnSuccessListener { task -> responseView.text = "${task.place.name}" + "\n" + " ${task.place.address}" }.addOnFailureListener { e ->
                    e.printStackTrace()
                    responseView.text = e.message
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        responseView = findViewById(R.id.response)
        val apiKey = getString(R.string.api_key)
        if (apiKey.isEmpty()) {
            responseView.text = getString(R.string.error)
            return
        }
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        placesClient = Places.createClient(this)
        initAutoCompleteTextView()

    }

    private fun initAutoCompleteTextView() {
        autoCompleteTextView = findViewById(R.id.auto)
        autoCompleteTextView.threshold = 1
        autoCompleteTextView.onItemClickListener = autocompleteClickListener
        adapter = AutoCompleteAdapter(this, placesClient)
        autoCompleteTextView.setAdapter(adapter)
    }
}
