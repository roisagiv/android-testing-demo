package com.roisagiv.aroundme;

import android.support.annotation.VisibleForTesting;
import com.roisagiv.aroundme.managers.GooglePlacesAutoCompleteAPI;
import com.roisagiv.aroundme.managers.GooglePlacesNearbyAPI;
import com.roisagiv.aroundme.managers.PlacesAutoCompleteAPI;
import com.roisagiv.aroundme.managers.PlacesNearbyAPI;
import com.roisagiv.aroundme.views.map.IMapPlacesRenderer;
import com.roisagiv.aroundme.views.map.MapPlacesRenderer;

/**
 * The enum Injector.
 */
public enum Injector {
  /**
   * Singleton injector.
   */
  Singleton;

  private PlacesAutoCompleteAPI injectedPlacesAutoCompleteAPI;
  private PlacesNearbyAPI injectedPlacesNearbyAPI;
  private IMapPlacesRenderer injectedMapPlacesRenderer;

  /**
   * Gets places auto complete api.
   *
   * @return the places auto complete api
   */
  public PlacesAutoCompleteAPI getPlacesAutoCompleteAPI() {
    if (injectedPlacesAutoCompleteAPI != null) {
      return injectedPlacesAutoCompleteAPI;
    } else {
      return new GooglePlacesAutoCompleteAPI("https://maps.googleapis.com",
          BuildConfig.GOOGLE_PLACES_API_KEY);
    }
  }

  /**
   * Gets places nearby api.
   *
   * @return the places nearby api
   */
  public PlacesNearbyAPI getPlacesNearbyAPI() {
    if (injectedPlacesNearbyAPI != null) {
      return injectedPlacesNearbyAPI;
    } else {
      return new GooglePlacesNearbyAPI("https://maps.googleapis.com",
          BuildConfig.GOOGLE_PLACES_API_KEY);
    }
  }

  /**
   * Gets map places renderer.
   *
   * @return the map places renderer
   */
  public IMapPlacesRenderer getMapPlacesRenderer() {
    if (injectedMapPlacesRenderer != null) {
      return injectedMapPlacesRenderer;
    } else {
      return new MapPlacesRenderer();
    }
  }

  /**
   * Sets places auto complete api.
   *
   * @param placesAutoCompleteAPI the places auto complete api
   */
  @VisibleForTesting public void setPlacesAutoCompleteAPI(
      PlacesAutoCompleteAPI placesAutoCompleteAPI) {
    this.injectedPlacesAutoCompleteAPI = placesAutoCompleteAPI;
  }

  /**
   * Sets places nearby api.
   *
   * @param placesNearbyAPI the places nearby api
   */
  @VisibleForTesting public void setPlacesNearbyAPI(PlacesNearbyAPI placesNearbyAPI) {
    this.injectedPlacesNearbyAPI = placesNearbyAPI;
  }

  /**
   * Sets map places renderer.
   *
   * @param mapPlacesRenderer the map places renderer
   */
  @VisibleForTesting public void setMapPlacesRenderer(IMapPlacesRenderer mapPlacesRenderer) {
    this.injectedMapPlacesRenderer = mapPlacesRenderer;
  }
}
