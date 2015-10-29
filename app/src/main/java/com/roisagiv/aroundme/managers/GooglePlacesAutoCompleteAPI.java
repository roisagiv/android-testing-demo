package com.roisagiv.aroundme.managers;

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
 * The type Google places auto complete.
 */
public class GooglePlacesAutoCompleteAPI implements PlacesAutoCompleteAPI {

  private final String baseUrl;
  private final String apiKey;

  public GooglePlacesAutoCompleteAPI(String baseUrl, String apiKey) {
    this.baseUrl = baseUrl;
    this.apiKey = apiKey;
  }

  /**
   * Auto complete list.
   *
   * @param text the text
   * @return the list
   */
  @Override public NetworkResponse<List<AutoCompletePrediction>> autoComplete(String text) {
    OkHttpClient client = new OkHttpClient();

    HttpUrl.Builder httpBuilder = HttpUrl.get(URI.create(baseUrl)).newBuilder();
    httpBuilder.encodedPath("/maps/api/place/autocomplete/json")
        .addQueryParameter("input", text)
        .addQueryParameter("key", apiKey);

    Request.Builder requestBuilder = new Request.Builder();
    requestBuilder.url(httpBuilder.build());

    NetworkResponse<List<AutoCompletePrediction>>
        results = new NetworkResponse<List<AutoCompletePrediction>>();
    Call call = client.newCall(requestBuilder.build());
    try {

      com.squareup.okhttp.Response response = call.execute();
      results.setHttpCode(response.code());

      JSONObject json = new JSONObject(response.body().string());
      JSONArray predictionsAsJson = json.getJSONArray("predictions");
      List<AutoCompletePrediction> predictions =
          new ArrayList<AutoCompletePrediction>(predictionsAsJson.length());

      for (int i = 0; i < predictionsAsJson.length(); i++) {
        JSONObject predictionAsJson = predictionsAsJson.getJSONObject(i);
        AutoCompletePrediction prediction = new AutoCompletePrediction();
        prediction.setDescription(predictionAsJson.getString("description"));
        prediction.setId(predictionAsJson.getString("id"));

        predictions.add(prediction);
      }

      results.setResults(predictions);
    } catch (IOException | JSONException e) {
      results.setError(e);
    }

    return results;
  }

  @Override public NetworkResponse<PredictionDetails> predictionDetails(String id) {
    OkHttpClient client = new OkHttpClient();

    HttpUrl.Builder httpBuilder = HttpUrl.get(URI.create(baseUrl)).newBuilder();
    httpBuilder.encodedPath("/maps/api/place/details/json")
        .addQueryParameter("placeid", id)
        .addQueryParameter("key", apiKey);

    Request.Builder requestBuilder = new Request.Builder();
    requestBuilder.url(httpBuilder.build());

    NetworkResponse<PredictionDetails> results = new NetworkResponse<>();
    Call call = client.newCall(requestBuilder.build());
    try {

      com.squareup.okhttp.Response response = call.execute();
      results.setHttpCode(response.code());

      JSONObject json = new JSONObject(response.body().string());
      JSONObject resultsAsJson = json.getJSONObject("result");
      PredictionDetails prediction = new PredictionDetails();

      prediction.setId(resultsAsJson.getString("id"));
      prediction.setDescription(resultsAsJson.getString("name"));
      prediction.setLatitude(
          resultsAsJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
      prediction.setLongitude(
          resultsAsJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

      results.setResults(prediction);
    } catch (IOException | JSONException e) {
      results.setError(e);
    }

    return results;
  }
}
