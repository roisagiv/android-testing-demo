package com.roisagiv.aroundme.views.map;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.roisagiv.aroundme.GoogleMapDemoActivity;
import com.roisagiv.aroundme.models.Place;
import com.roisagiv.aroundme.utils.LocationGenerator;
import java.util.List;
import org.exparity.stub.random.RandomBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Map places renderer test.
 */
@RunWith(AndroidJUnit4.class) public class MapPlacesRendererTest {
  /**
   * The Activity test rule.
   */
  @Rule public ActivityTestRule<GoogleMapDemoActivity> activityTestRule =
      new ActivityTestRule<>(GoogleMapDemoActivity.class);

  /**
   * The Thread test rule.
   */
  @Rule public UiThreadTestRule threadTestRule = new UiThreadTestRule();

  private GoogleMap googleMap;
  private CountingIdlingResource countingIdlingResource = new CountingIdlingResource("MapReady");

  /**
   * Before each.
   *
   * @throws Throwable the throwable
   */
  @Before public void beforeEach() throws Throwable {
    countingIdlingResource.increment();

    final SupportMapFragment mapFragment = activityTestRule.getActivity().getMapFragment();
    final OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
      @Override public void onMapReady(GoogleMap googleMap) {
        countingIdlingResource.decrement();

        MapPlacesRendererTest.this.googleMap = googleMap;
      }
    };

    threadTestRule.runOnUiThread(new Runnable() {
      @Override public void run() {
        mapFragment.getMapAsync(onMapReadyCallback);
      }
    });

    assertThat(Espresso.registerIdlingResources(countingIdlingResource)).isTrue();
  }

  /**
   * After each.
   */
  @After public void afterEach() {
    assertThat(Espresso.unregisterIdlingResources(countingIdlingResource)).isTrue();
  }

  /**
   * Draw places on map should add maker for each place.
   *
   * @throws Throwable the throwable
   */
  @Test public void drawPlacesOnMapShouldAddMakerForEachPlace() throws Throwable {
    final List<Place> places = RandomBuilder.aRandomListOf(Place.class);
    for (Place place : places) {
      place.setLatitude(LocationGenerator.latitude());
      place.setLongitude(LocationGenerator.longitude());
    }

    final MapPlacesRenderer mapPlacesRenderer = new MapPlacesRenderer();

    threadTestRule.runOnUiThread(new Runnable() {
      @Override public void run() {
        mapPlacesRenderer.drawPlacesOnMap(googleMap, places);
      }
    });

    List<Marker> markersCache = mapPlacesRenderer.getMarkersCache();
    assertThat(markersCache).hasSameSizeAs(places);

    for (int i = 0; i < markersCache.size(); i++) {
      final Marker marker = markersCache.get(i);
      final Place place = places.get(i);

      threadTestRule.runOnUiThread(new Runnable() {
        @Override public void run() {
          assertThat(marker.getPosition().latitude).isEqualTo(place.getLatitude());
          assertThat(marker.getPosition().longitude).isEqualTo(place.getLongitude());
        }
      });
    }
  }

  @Test public void drawPlacesOnMapShouldCleanPreviousMarkers() throws Throwable {
    final List<Place> firstPlacesList = RandomBuilder.aRandomListOf(Place.class);
    for (Place place : firstPlacesList) {
      place.setLatitude(LocationGenerator.latitude());
      place.setLongitude(LocationGenerator.longitude());
    }

    final List<Place> secondPlacesList = RandomBuilder.aRandomListOf(Place.class);
    for (Place place : secondPlacesList) {
      place.setLatitude(LocationGenerator.latitude());
      place.setLongitude(LocationGenerator.longitude());
    }

    final MapPlacesRenderer mapPlacesRenderer = new MapPlacesRenderer();

    threadTestRule.runOnUiThread(new Runnable() {
      @Override public void run() {
        mapPlacesRenderer.drawPlacesOnMap(googleMap, firstPlacesList);
      }
    });

    List<Marker> markersCache = mapPlacesRenderer.getMarkersCache();
    assertThat(markersCache).hasSameSizeAs(firstPlacesList);

    threadTestRule.runOnUiThread(new Runnable() {
      @Override public void run() {
        mapPlacesRenderer.drawPlacesOnMap(googleMap, secondPlacesList);
      }
    });

    assertThat(markersCache).hasSameSizeAs(secondPlacesList);
  }
}
