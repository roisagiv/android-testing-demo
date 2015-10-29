package com.roisagiv.aroundme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import com.roisagiv.aroundme.managers.GooglePlacesAutoCompleteAPI;
import com.roisagiv.aroundme.views.adapters.PlacesListAdapter;

public class PlacesAutoCompleteDemoActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_places_auto_complete_demo);

    AutoCompleteTextView autoCompleteTextView =
        (AutoCompleteTextView) findViewById(R.id.autocomplete_demo);

    PlacesListAdapter adapter = new PlacesListAdapter(
        new GooglePlacesAutoCompleteAPI("https://maps.googleapis.com",
            BuildConfig.GOOGLE_PLACES_API_KEY));

    autoCompleteTextView.setAdapter(adapter);
  }
}
