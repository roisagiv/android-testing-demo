package com.roisagiv.aroundme.loaders;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import com.roisagiv.aroundme.managers.NetworkResponse;
import com.roisagiv.aroundme.managers.PlacesAutoCompleteAPI;

/**
 *
 */
public class PredictionDetailsLoader
    extends AsyncTaskLoader2<NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails>> {

  private final String placeId;
  private final PlacesAutoCompleteAPI placesAutoCompleteAPI;

  public PredictionDetailsLoader(Context context, String placeId,
      PlacesAutoCompleteAPI placesAutoCompleteAPI) {
    super(context);
    this.placeId = placeId;
    this.placesAutoCompleteAPI = placesAutoCompleteAPI;
  }

  @Override
  protected void onDiscardResult(NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails> result) {

  }

  @Override public NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails> loadInBackground() {
    return placesAutoCompleteAPI.predictionDetails(placeId);
  }

  @VisibleForTesting public String getPlaceId() {
    return placeId;
  }
}
