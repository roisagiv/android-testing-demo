package com.roisagiv.aroundme.views.map;

import com.google.android.gms.maps.GoogleMap;
import com.roisagiv.aroundme.models.Place;
import java.util.List;

/**
 * The interface Map places renderer.
 */
public interface IMapPlacesRenderer {

  /**
   * Draw places on map.
   *
   * @param googleMap the google map
   * @param places the places
   */
  void drawPlacesOnMap(GoogleMap googleMap, List<Place> places);
}
