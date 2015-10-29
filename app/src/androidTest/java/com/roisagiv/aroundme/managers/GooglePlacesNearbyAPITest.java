package com.roisagiv.aroundme.managers;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.roisagiv.aroundme.models.Place;
import com.roisagiv.aroundme.utils.AssetReader;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.squareup.okhttp.mockwebserver.SocketPolicy;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 */
@RunWith(AndroidJUnit4.class) public class GooglePlacesNearbyAPITest {
  /**
   * The Server.
   */
  @Rule public final MockWebServer server = new MockWebServer();

  /**
   * Should perform http request to google.
   *
   * @throws IOException the io exception
   * @throws InterruptedException the interrupted exception
   */
  @Test public void nearbyPlacesShouldPerformHttpRequestToGoogle()
      throws IOException, InterruptedException {
    // arrange
    server.enqueue(new MockResponse());
    HttpUrl url = server.url("/");
    GooglePlacesNearbyAPI placesNearbyAPI = new GooglePlacesNearbyAPI(url.toString(), "");

    // act
    placesNearbyAPI.nearbyPlaces(4, 5);

    // assert
    RecordedRequest recordedRequest = server.takeRequest(10, TimeUnit.MILLISECONDS);
    assertThat(recordedRequest.getPath()).contains("/maps/api/place/nearbysearch/output?");
  }

  /**
   * Should add api key to request.
   *
   * @throws InterruptedException the interrupted exception
   */
  @Test public void nearbyPlacesShouldAddApiKeyToRequest() throws InterruptedException {
    // arrange
    server.enqueue(new MockResponse());
    HttpUrl url = server.url("/");
    String apiKey = "my api key";
    GooglePlacesNearbyAPI placesNearbyAPI = new GooglePlacesNearbyAPI(url.toString(), apiKey);

    // act
    placesNearbyAPI.nearbyPlaces(4, 5);

    // assert
    RecordedRequest recordedRequest = server.takeRequest(10, TimeUnit.MILLISECONDS);
    assertThat(recordedRequest.getPath()).contains("key=my%20api%20key");
  }

  /**
   * Should add text as input query parameter.
   *
   * @throws InterruptedException the interrupted exception
   */
  @Test public void nearbyPlacesShouldAddTextAsInputQueryParameter() throws InterruptedException {
    // arrange
    server.enqueue(new MockResponse());
    HttpUrl url = server.url("/");
    GooglePlacesNearbyAPI placesNearbyAPI = new GooglePlacesNearbyAPI(url.toString(), "key");

    // act
    placesNearbyAPI.nearbyPlaces(-33.8670522, 151.1957362);

    // assert
    RecordedRequest recordedRequest = server.takeRequest(10, TimeUnit.MILLISECONDS);
    assertThat(recordedRequest.getPath()).contains("location=-33.8670522,151.1957362&radius=500");
  }

  @Test public void nearbyPlacesWhenRequestSucceedShouldReturn4Results()
      throws IOException, InterruptedException {
    String json =
        AssetReader.readFile(InstrumentationRegistry.getContext(), "places_search_example.json");
    server.enqueue(new MockResponse().setBody(json).setResponseCode(200));

    HttpUrl url = server.url("/");
    GooglePlacesNearbyAPI placesNearbyAPI = new GooglePlacesNearbyAPI(url.toString(), "key");

    // act
    NetworkResponse<List<Place>> results = placesNearbyAPI.nearbyPlaces(1, 2);

    // assert
    List<Place> places = results.getResults();
    assertThat(places).hasSize(4);

    assertThat(places.get(0).getLatitude()).isEqualTo(-33.870775);
    assertThat(places.get(0).getLongitude()).isEqualTo(151.199025);
    assertThat(places.get(1).getLatitude()).isEqualTo(-33.866891);
    assertThat(places.get(1).getLongitude()).isEqualTo(151.200814);
    assertThat(places.get(2).getLatitude()).isEqualTo(-33.870943);
    assertThat(places.get(2).getLongitude()).isEqualTo(151.190311);
    assertThat(places.get(3).getLatitude()).isEqualTo(-33.867591);
    assertThat(places.get(3).getLongitude()).isEqualTo(151.201196);
  }

  @Test public void nearbyPlacesWhenRequestFailsShouldReturnHttpCode() {
    server.enqueue(new MockResponse().setResponseCode(500));

    HttpUrl url = server.url("/");
    GooglePlacesNearbyAPI placesNearbyAPI = new GooglePlacesNearbyAPI(url.toString(), "key");

    // act
    NetworkResponse<List<Place>> results = placesNearbyAPI.nearbyPlaces(1, 2);

    // assert
    assertThat(results.getHttpCode()).isEqualTo(500);
    assertThat(results.getResults()).isNull();
  }

  @Test public void nearbyPlacesWhenNetworkFailsShouldReturnError() throws IOException {
    // arrange
    String json = AssetReader.readFile(InstrumentationRegistry.getContext(),
        "places_autocomplete_example.json");
    server.enqueue(new MockResponse().setBody(json)
        .setResponseCode(200)
        .setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY));

    HttpUrl url = server.url("/");
    GooglePlacesNearbyAPI placesNearbyAPI = new GooglePlacesNearbyAPI(url.toString(), "key");

    // act
    NetworkResponse<List<Place>> results = placesNearbyAPI.nearbyPlaces(1, 2);

    // assert
    assertThat(results.getHttpCode()).isEqualTo(200);
    assertThat(results.getError()).isNotNull();
  }
}
