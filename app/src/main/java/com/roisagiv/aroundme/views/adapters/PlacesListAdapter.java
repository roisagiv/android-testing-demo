package com.roisagiv.aroundme.views.adapters;

import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import com.roisagiv.aroundme.managers.PlacesAutoComplete;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Places list adapter.
 */
public class PlacesListAdapter extends BaseAdapter implements Filterable {

  private final List<PlacesAutoComplete.AutoCompletePrediction> autoCompletePredictions;

  /**
   * Instantiates a new Places list adapter.
   *
   * @param placesAutoComplete the places auto complete
   */
  public PlacesListAdapter(PlacesAutoComplete placesAutoComplete) {
    autoCompletePredictions = new ArrayList<>();
  }

  /**
   * Gets count.
   *
   * @return the count
   */
  @Override public int getCount() {
    return autoCompletePredictions.size();
  }

  /**
   * Gets item.
   *
   * @param position the position
   * @return the item
   */
  @Override public Object getItem(int position) {
    return autoCompletePredictions.get(position);
  }

  /**
   * Gets item id.
   *
   * @param position the position
   * @return the item id
   */
  @Override public long getItemId(int position) {
    return position;
  }

  /**
   * Gets view.
   *
   * @param position the position
   * @param convertView the convert view
   * @param parent the parent
   * @return the view
   */
  @Override public View getView(int position, View convertView, ViewGroup parent) {
    return null;
  }

  /**
   * Gets filter.
   *
   * @return the filter
   */
  @Override public Filter getFilter() {
    return null;
  }

  /**
   * Sets auto complete predictions.
   *
   * @param predictions the predictions
   */
  @VisibleForTesting protected void setAutoCompletePredictions(
      List<PlacesAutoComplete.AutoCompletePrediction> predictions) {
    autoCompletePredictions.clear();
    autoCompletePredictions.addAll(predictions);
  }
}
