package com.roisagiv.aroundme.loaders;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.roisagiv.aroundme.managers.NetworkResponse;
import com.roisagiv.aroundme.managers.PlacesAutoCompleteAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
@RunWith(AndroidJUnit4.class) public class PredictionDetailsLoaderTest {

  @Test public void shouldCallPlacesAutoCompleteAPI() {
    // arrange
    String placeId = "place id";
    PlacesAutoCompleteAPI placesAutoCompleteAPI = Mockito.mock(PlacesAutoCompleteAPI.class);
    NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails> expectedResponse =
        Mockito.mock(NetworkResponse.class);
    Mockito.when(placesAutoCompleteAPI.predictionDetails(Mockito.anyString()))
        .thenReturn(expectedResponse);

    PredictionDetailsLoader loader =
        new PredictionDetailsLoader(InstrumentationRegistry.getContext(), placeId,
            placesAutoCompleteAPI);

    // act
    NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails> response = loader.loadInBackground();

    // assert
    Mockito.verify(placesAutoCompleteAPI).predictionDetails(placeId);
    assertThat(response).isEqualTo(expectedResponse);
  }
}
