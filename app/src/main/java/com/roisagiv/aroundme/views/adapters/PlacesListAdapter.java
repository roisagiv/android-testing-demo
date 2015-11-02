package com.roisagiv.aroundme.views.adapters;

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.roisagiv.aroundme.managers.NetworkResponse;
import com.roisagiv.aroundme.managers.PlacesAutoCompleteAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Places list adapter.
 */
public class PlacesListAdapter extends BaseAdapter implements Filterable {

  private final List<PlacesAutoCompleteAPI.AutoCompletePrediction> autoCompletePredictions;
  private final PlacesListAdapterFilter filter;

  /**
   * Instantiates a new Places list adapter.
   *
   * @param placesAutoCompleteAPI the places auto complete
   */
  public PlacesListAdapter(PlacesAutoCompleteAPI placesAutoCompleteAPI) {
    autoCompletePredictions = new ArrayList<>();
    filter = new PlacesListAdapterFilter(placesAutoCompleteAPI);
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
    PlacesAutoCompleteAPI.AutoCompletePrediction prediction = autoCompletePredictions.get(position);

    View view = convertView;

    if (view == null) {
      view = LayoutInflater.from(parent.getContext())
          .inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    ViewHolder viewHolder = (ViewHolder) view.getTag();
    if (viewHolder == null) {
      viewHolder = new ViewHolder();
      viewHolder.textView = (TextView) view.findViewById(android.R.id.text1);
      view.setTag(viewHolder);
    }

    viewHolder.textView.setText(prediction.getDescription());
    return view;
  }

  /**
   * Gets filter.
   *
   * @return the filter
   */
  @Override public Filter getFilter() {
    return filter;
  }

  /**
   * Sets auto complete predictions.
   *
   * @param predictions the predictions
   */
  @VisibleForTesting public void setAutoCompletePredictions(
      List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions) {
    autoCompletePredictions.clear();
    autoCompletePredictions.addAll(predictions);
  }

  /**
   * The type Places list adapter filter.
   */
  @VisibleForTesting protected class PlacesListAdapterFilter extends Filter {

    private final PlacesAutoCompleteAPI placesAutoCompleteAPI;

    /**
     * Instantiates a new Places list adapter filter.
     *
     * @param placesAutoCompleteAPI the places auto complete
     */
    public PlacesListAdapterFilter(PlacesAutoCompleteAPI placesAutoCompleteAPI) {
      this.placesAutoCompleteAPI = placesAutoCompleteAPI;
    }

    /**
     * Perform filtering filter results.
     *
     * @param constraint the constraint
     * @return the filter results
     */
    @Override protected FilterResults performFiltering(CharSequence constraint) {

      FilterResults results = new FilterResults();

      if (!TextUtils.isEmpty(constraint)) {
        NetworkResponse<List<PlacesAutoCompleteAPI.AutoCompletePrediction>> response =
            placesAutoCompleteAPI.autoComplete(constraint.toString());

        if (response != null) {
          results.values = response.getResults();
          results.count = response.getResults().size();
        }
      }

      return results;
    }

    /**
     * Publish results.
     *
     * @param constraint the constraint
     * @param results the results
     */
    @Override protected void publishResults(CharSequence constraint, FilterResults results) {

      List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions;
      if (results != null && results.values != null) {
        predictions = (List<PlacesAutoCompleteAPI.AutoCompletePrediction>) results.values;
      } else {
        predictions = Collections.emptyList();
      }

      setAutoCompletePredictions(predictions);
      notifyDataSetChanged();
    }
  }

  private static class ViewHolder {
    /**
     * The Text view.
     */
    protected TextView textView;
  }
}
