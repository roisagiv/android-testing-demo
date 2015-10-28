package com.roisagiv.aroundme.views.map;

import android.support.annotation.VisibleForTesting;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
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

  }

  @VisibleForTesting protected List<Marker> getMarkersCache() {
    return markersCache;
  }
}
