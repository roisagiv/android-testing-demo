package com.roisagiv.aroundme.managers;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The type Google places auto complete.
 */
public class GooglePlacesAutoComplete implements PlacesAutoComplete {

  private final String baseUrl;
  private final String apiKey;

  public GooglePlacesAutoComplete(String baseUrl, String apiKey) {
    this.baseUrl = baseUrl;
    this.apiKey = apiKey;
  }

  /**
   * Auto complete list.
   *
   * @param text the text
   * @return the list
   */
  @Override public Response<List<AutoCompletePrediction>> autoComplete(String text) {
    return new Response<>();
  }
}
