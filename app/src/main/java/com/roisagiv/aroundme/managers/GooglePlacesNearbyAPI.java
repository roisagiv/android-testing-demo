package com.roisagiv.aroundme.managers;

import com.roisagiv.aroundme.models.Place;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Google places nearby api.
 */
public class GooglePlacesNearbyAPI implements PlacesNearbyAPI {

  private final String baseUrl;
  private final String apiKey;

  public GooglePlacesNearbyAPI(String baseUrl, String apiKey) {
    this.baseUrl = baseUrl;
    this.apiKey = apiKey;
  }

  /**
   * Nearby places network response.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the network response
   */
  @Override public NetworkResponse<List<Place>> nearbyPlaces(double latitude, double longitude) {
    OkHttpClient client = new OkHttpClient();

    HttpUrl.Builder httpBuilder = HttpUrl.get(URI.create(baseUrl)).newBuilder();
    httpBuilder.encodedPath("/maps/api/place/nearbysearch/json")
        .addQueryParameter("location", String.format("%s,%s", latitude, longitude))
        .addQueryParameter("radius", "500")
        .addQueryParameter("key", apiKey);

    Request.Builder requestBuilder = new Request.Builder();
    requestBuilder.url(httpBuilder.build());

    NetworkResponse<List<Place>> results = new NetworkResponse<>();
    Call call = client.newCall(requestBuilder.build());
    try {

      com.squareup.okhttp.Response response = call.execute();
      results.setHttpCode(response.code());

      JSONObject json = new JSONObject(response.body().string());
      JSONArray placesAsJson = json.getJSONArray("results");
      List<Place> places = new ArrayList<>(placesAsJson.length());

      for (int i = 0; i < placesAsJson.length(); i++) {
        JSONObject placeAsJson = placesAsJson.getJSONObject(i);
        Place place = new Place();

        place.setLatitude(
            placeAsJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
        place.setLongitude(
            placeAsJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

        places.add(place);
      }

      results.setResults(places);
    } catch (IOException | JSONException e) {
      results.setError(e);
    }

    return results;
  }
}
