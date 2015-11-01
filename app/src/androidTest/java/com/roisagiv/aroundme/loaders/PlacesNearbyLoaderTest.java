package com.roisagiv.aroundme.loaders;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.roisagiv.aroundme.managers.NetworkResponse;
import com.roisagiv.aroundme.managers.PlacesNearbyAPI;
import com.roisagiv.aroundme.models.Place;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
@RunWith(AndroidJUnit4.class) public class PlacesNearbyLoaderTest {

  @Test public void shouldCallPlacesNearbyAPI() {
    // arrange
    double latitude = 1.0;
    double longitude = 2.0;
    PlacesNearbyAPI placesNearbyAPI = Mockito.mock(PlacesNearbyAPI.class);

    NetworkResponse<List<Place>> expectedResponse = Mockito.mock(NetworkResponse.class);

    Mockito.when(placesNearbyAPI.nearbyPlaces(Mockito.anyDouble(), Mockito.anyDouble()))
        .thenReturn(expectedResponse);

    // create the loader
    PlacesNearbyLoader loader =
        new PlacesNearbyLoader(InstrumentationRegistry.getContext(), placesNearbyAPI, latitude,
            longitude);

    // act
    NetworkResponse<List<Place>> response = loader.loadInBackground();

    // assert
    Mockito.verify(placesNearbyAPI).nearbyPlaces(latitude, longitude);
    assertThat(response).isEqualTo(expectedResponse);
  }
}
