package com.roisagiv.aroundme.loaders;

import android.content.Context;
import com.roisagiv.aroundme.managers.NetworkResponse;
import com.roisagiv.aroundme.managers.PlacesNearbyAPI;
import com.roisagiv.aroundme.models.Place;
import java.util.List;

/**
 * The type Places nearby loader.
 */
public class PlacesNearbyLoader extends AsyncTaskLoader2<NetworkResponse<List<Place>>> {

  private final PlacesNearbyAPI placesNearbyAPI;
  private final double latitude;
  private final double longitude;

  public PlacesNearbyLoader(Context context, PlacesNearbyAPI placesNearbyAPI, double latitude,
      double longitude) {
    super(context);

    this.placesNearbyAPI = placesNearbyAPI;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override protected void onDiscardResult(NetworkResponse<List<Place>> result) {
    // do nothing
  }

  @Override public NetworkResponse<List<Place>> loadInBackground() {
    return placesNearbyAPI.nearbyPlaces(latitude, longitude);
  }
}
