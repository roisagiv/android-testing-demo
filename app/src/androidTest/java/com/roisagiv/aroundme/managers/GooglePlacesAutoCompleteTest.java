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
}
