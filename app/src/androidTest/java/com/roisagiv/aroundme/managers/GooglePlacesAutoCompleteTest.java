package com.roisagiv.aroundme.managers;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
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
 * The type Google places auto complete test.
 */
@RunWith(AndroidJUnit4.class) public class GooglePlacesAutoCompleteTest {

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
  @Test public void shouldPerformHttpRequestToGoogle() throws IOException, InterruptedException {
    // arrange
    server.enqueue(new MockResponse());
    HttpUrl url = server.url("/");
    GooglePlacesAutoComplete autoComplete = new GooglePlacesAutoComplete(url.toString(), "");

    // act
    autoComplete.autoComplete("never mind");

    // assert
    RecordedRequest recordedRequest = server.takeRequest(10, TimeUnit.MILLISECONDS);
    assertThat(recordedRequest.getPath()).contains("/maps/api/place/autocomplete/json?");
  }

  /**
   * Should add api key to request.
   *
   * @throws InterruptedException the interrupted exception
   */
  @Test public void shouldAddApiKeyToRequest() throws InterruptedException {
    // arrange
    server.enqueue(new MockResponse());
    HttpUrl url = server.url("/");
    String apiKey = "my api key";
    GooglePlacesAutoComplete autoComplete = new GooglePlacesAutoComplete(url.toString(), apiKey);

    // act
    autoComplete.autoComplete("never mind");

    // assert
    RecordedRequest recordedRequest = server.takeRequest(10, TimeUnit.MILLISECONDS);
    assertThat(recordedRequest.getPath()).contains("key=my%20api%20key");
  }

  /**
   * Should add text as input query parameter.
   *
   * @throws InterruptedException the interrupted exception
   */
  @Test public void shouldAddTextAsInputQueryParameter() throws InterruptedException {
    // arrange
    server.enqueue(new MockResponse());
    HttpUrl url = server.url("/");
    String text = "my super text";
    GooglePlacesAutoComplete autoComplete = new GooglePlacesAutoComplete(url.toString(), "key");

    // act
    autoComplete.autoComplete(text);

    // assert
    RecordedRequest recordedRequest = server.takeRequest(10, TimeUnit.MILLISECONDS);
    assertThat(recordedRequest.getPath()).contains("input=my%20super%20text");
  }

  @Test public void whenRequestSucceedShouldReturn5Results()
      throws IOException, InterruptedException {
    String json = AssetReader.readFile(InstrumentationRegistry.getContext(),
        "places_autocomplete_example.json");
    server.enqueue(new MockResponse().setBody(json).setResponseCode(200));

    HttpUrl url = server.url("/");
    GooglePlacesAutoComplete autoComplete = new GooglePlacesAutoComplete(url.toString(), "key");

    // act
    PlacesAutoComplete.Response<List<PlacesAutoComplete.AutoCompletePrediction>> results =
        autoComplete.autoComplete("some text");

    // assert
    List<PlacesAutoComplete.AutoCompletePrediction> predictions = results.getResults();
    assertThat(predictions).hasSize(5);

    assertThat(predictions.get(0).getDescription()).isEqualTo("Victoria, Australia");
    assertThat(predictions.get(0).getId()).isEqualTo("0328fb981d48f228012b5d4b7ab0d0f404f439fd");
    assertThat(predictions.get(1).getDescription()).isEqualTo("Victoria, BC, Canada");
    assertThat(predictions.get(1).getId()).isEqualTo("d5892cffd777f0252b94ab2651fea7123d2aa34a");
    assertThat(predictions.get(2).getDescription()).isEqualTo(
        "Victoria Station, London, United Kingdom");
    assertThat(predictions.get(2).getId()).isEqualTo("424c895c8d2d24a2a3b68d99c7e6bc782c8683f2");
    assertThat(predictions.get(3).getDescription()).isEqualTo(
        "Victoria Coach Station, Buckingham Palace Road, London, United Kingdom");
    assertThat(predictions.get(3).getId()).isEqualTo("118d77aff7358bda45eb91eedd725e62cae8468a");
    assertThat(predictions.get(4).getDescription()).isEqualTo("Victorville, CA, United States");
    assertThat(predictions.get(4).getId()).isEqualTo("dd296d3fde2a539b9279cdd817c01183f69d07a7");
  }

  @Test public void whenRequestFailsShouldReturnHttpCode() {
    server.enqueue(new MockResponse().setResponseCode(500));

    HttpUrl url = server.url("/");
    GooglePlacesAutoComplete autoComplete = new GooglePlacesAutoComplete(url.toString(), "key");

    // act
    PlacesAutoComplete.Response<List<PlacesAutoComplete.AutoCompletePrediction>> results =
        autoComplete.autoComplete("some text");

    // assert
    assertThat(results.getHttpCode()).isEqualTo(500);
    assertThat(results.getResults()).isNull();
  }

  @Test public void whenNetworkFailsShouldReturnError() throws IOException {
    // arrange
    String json = AssetReader.readFile(InstrumentationRegistry.getContext(),
        "places_autocomplete_example.json");
    server.enqueue(new MockResponse().setBody(json)
        .setResponseCode(200)
        .setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY));

    HttpUrl url = server.url("/");
    GooglePlacesAutoComplete autoComplete = new GooglePlacesAutoComplete(url.toString(), "key");

    // act
    PlacesAutoComplete.Response<List<PlacesAutoComplete.AutoCompletePrediction>> results =
        autoComplete.autoComplete("some text");

    // assert
    assertThat(results.getHttpCode()).isEqualTo(200);
    assertThat(results.getError()).isNotNull();
  }
}
