package com.roisagiv.aroundme;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.Loader;
import com.roisagiv.aroundme.loaders.PlacesNearbyLoader;
import com.roisagiv.aroundme.loaders.PredictionDetailsLoader;
import com.roisagiv.aroundme.managers.NetworkResponse;
import com.roisagiv.aroundme.managers.PlacesAutoCompleteAPI;
import com.roisagiv.aroundme.managers.PlacesNearbyAPI;
import com.roisagiv.aroundme.models.Place;
import com.roisagiv.aroundme.utils.LoaderUtils;
import com.roisagiv.aroundme.views.map.IMapPlacesRenderer;
import java.util.List;
import org.exparity.stub.random.RandomBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Main activity test.
 */
@RunWith(AndroidJUnit4.class) public class MainActivityTest {

  private PlacesAutoCompleteAPI mockedPlacesAutoComplete;
  private PlacesNearbyAPI mockedPlacesNearby;
  private IMapPlacesRenderer mockedMapPlacesRenderer;

  /**
   * The Activity test rule.
   */
  @Rule public ActivityTestRule<MainActivity> activityTestRule =
      new ActivityTestRule<MainActivity>(MainActivity.class) {
        @Override protected void beforeActivityLaunched() {

          mockedPlacesAutoComplete = Mockito.mock(PlacesAutoCompleteAPI.class);
          Injector.Singleton.setPlacesAutoCompleteAPI(mockedPlacesAutoComplete);

          mockedPlacesNearby = Mockito.mock(PlacesNearbyAPI.class);
          Injector.Singleton.setPlacesNearbyAPI(mockedPlacesNearby);

          mockedMapPlacesRenderer = Mockito.mock(IMapPlacesRenderer.class);
          Injector.Singleton.setMapPlacesRenderer(mockedMapPlacesRenderer);

          super.beforeActivityLaunched();
        }
      };

  /**
   * On item selected should restart details loader.
   *
   * @throws Throwable the throwable
   */
  @Test public void onItemSelectedShouldRestartDetailsLoader() throws Throwable {
    // arrange
    final List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions =
        RandomBuilder.aRandomListOf(PlacesAutoCompleteAPI.AutoCompletePrediction.class);

    final MainActivity activity = activityTestRule.getActivity();

    activity.getPlacesListAdapter().setAutoCompletePredictions(predictions);

    // act
    activity.onItemClick(null, null, 1, 1);

    // assert
    Loader<?> loader =
        activity.getSupportLoaderManager().getLoader(MainActivity.LOADER_ID_PREDICTION_DETAILS);

    assertThat(loader).isNotNull();

    PredictionDetailsLoader detailsLoader = (PredictionDetailsLoader) loader;
    assertThat(detailsLoader.getPlaceId()).isEqualTo(predictions.get(1).getId());
  }

  /**
   * On prediction details loader finishes should restart places nearby loader.
   */
  @Test public void onPredictionDetailsLoaderFinishesShouldRestartPlacesNearbyLoader() {
    // arrange
    NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails> networkResponse =
        new NetworkResponse<>();

    PlacesAutoCompleteAPI.PredictionDetails predictionDetails =
        new PlacesAutoCompleteAPI.PredictionDetails();
    predictionDetails.setLatitude(1.1);
    predictionDetails.setLongitude(2.2);

    networkResponse.setResults(predictionDetails);

    Mockito.when(mockedPlacesAutoComplete.predictionDetails(Mockito.anyString()))
        .thenReturn(networkResponse);

    final List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions =
        RandomBuilder.aRandomListOf(PlacesAutoCompleteAPI.AutoCompletePrediction.class);

    final MainActivity activity = activityTestRule.getActivity();

    // act
    activity.getPlacesListAdapter().setAutoCompletePredictions(predictions);
    activity.onItemClick(null, null, 1, 1);

    Loader<?> predictionDetailsLoader =
        activity.getSupportLoaderManager().getLoader(MainActivity.LOADER_ID_PREDICTION_DETAILS);
    LoaderUtils.waitForLoader(predictionDetailsLoader);
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();

    // assert
    Loader<?> loader =
        activity.getSupportLoaderManager().getLoader(MainActivity.LOADER_ID_PLACES_NEARBY);

    assertThat(loader).isNotNull();

    PlacesNearbyLoader placesNearbyLoader = (PlacesNearbyLoader) loader;
    assertThat(placesNearbyLoader.getLatitude()).isEqualTo(predictionDetails.getLatitude());
    assertThat(placesNearbyLoader.getLongitude()).isEqualTo(predictionDetails.getLongitude());
  }

  /**
   * On places nearby loader finishes should populate map with places.
   */
  @Test public void onPlacesNearbyLoaderFinishesShouldPopulateMapWithPlaces() {
    // arrange
    NetworkResponse<List<Place>> networkResponse = new NetworkResponse<>();
    List<Place> places = RandomBuilder.aRandomListOf(Place.class);
    networkResponse.setResults(places);

    Mockito.when(mockedPlacesNearby.nearbyPlaces(Mockito.anyDouble(), Mockito.anyDouble()))
        .thenReturn(networkResponse);

    final MainActivity activity = activityTestRule.getActivity();

    //act
    activity.onNewLocation(1.1, 2.2);
    Loader<?> loader =
        activity.getSupportLoaderManager().getLoader(MainActivity.LOADER_ID_PLACES_NEARBY);
    LoaderUtils.waitForLoader(loader);
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();

    // assert
    Mockito.verify(mockedMapPlacesRenderer)
        .drawPlacesOnMap(activity.getMapFragment().getMap(), places);
  }
}
