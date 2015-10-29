package com.roisagiv.aroundme.managers;

import com.roisagiv.aroundme.models.Place;
import java.util.List;

/**
 * The interface Places nearby api.
 */
public interface PlacesNearbyAPI {

  /**
   * Nearby places network response.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the network response
   */
  NetworkResponse<List<Place>> nearbyPlaces(double latitude, double longitude);
}
