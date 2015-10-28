package com.roisagiv.aroundme.views.map;

import android.support.annotation.VisibleForTesting;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roisagiv.aroundme.models.Place;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MapPlacesRenderer {

  private final List<Marker> markersCache;

  public MapPlacesRenderer() {
    markersCache = new ArrayList<>();
  }

  public void drawPlacesOnMap(GoogleMap googleMap, List<Place> places) {
    // clear the cache
    for (Marker marker : markersCache) {
      marker.remove();
    }
    markersCache.clear();

    // add new places
    for (Place place : places) {
      Marker marker = googleMap.addMarker(
          new MarkerOptions().position(new LatLng(place.getLatitude(), place.getLongitude())));
      markersCache.add(marker);
    }
  }

  @VisibleForTesting protected List<Marker> getMarkersCache() {
    return markersCache;
  }
}
