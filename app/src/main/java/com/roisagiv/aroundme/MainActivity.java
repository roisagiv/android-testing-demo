package com.roisagiv.aroundme;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import com.google.android.gms.maps.SupportMapFragment;
import com.roisagiv.aroundme.loaders.PlacesNearbyLoader;
import com.roisagiv.aroundme.loaders.PredictionDetailsLoader;
import com.roisagiv.aroundme.managers.NetworkResponse;
import com.roisagiv.aroundme.managers.PlacesAutoCompleteAPI;
import com.roisagiv.aroundme.models.Place;
import com.roisagiv.aroundme.views.adapters.PlacesListAdapter;
import com.roisagiv.aroundme.views.map.IMapPlacesRenderer;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  /**
   * The constant LOADER_ID_PREDICTION_DETAILS.
   */
  protected static final int LOADER_ID_PREDICTION_DETAILS = 100;
  /**
   * The constant LOADER_ID_PLACES_NEARBY.
   */
  protected static final int LOADER_ID_PLACES_NEARBY = 101;
  private static final String BUNDLE_ARG_PLACE_ID = "place_id";
  private static final String BUNDLE_ARG_LATITUDE = "latitude";
  private static final String BUNDLE_ARG_LONGITUDE = "longitude";

  private SupportMapFragment mapFragment;

  private PlacesListAdapter placesListAdapter;

  private IMapPlacesRenderer mapPlacesRenderer;

  /**
   * On create.
   *
   * @param savedInstanceState the saved instance state
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    AutoCompleteTextView autoCompleteTextView =
        (AutoCompleteTextView) findViewById(R.id.autocomplete);
    autoCompleteTextView.setOnItemClickListener(this);

    placesListAdapter = new PlacesListAdapter(Injector.Singleton.getPlacesAutoCompleteAPI());
    autoCompleteTextView.setAdapter(placesListAdapter);

    mapPlacesRenderer = Injector.Singleton.getMapPlacesRenderer();
  }

  /**
   * On create options menu boolean.
   *
   * @param menu the menu
   * @return the boolean
   */
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  /**
   * On options item selected boolean.
   *
   * @param item the item
   * @return the boolean
   */
  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Gets map fragment.
   *
   * @return the map fragment
   */
  @VisibleForTesting protected SupportMapFragment getMapFragment() {
    return mapFragment;
  }

  /**
   * Gets places list adapter.
   *
   * @return the places list adapter
   */
  @VisibleForTesting protected PlacesListAdapter getPlacesListAdapter() {
    return placesListAdapter;
  }

  /**
   * On item selected.
   *
   * @param parent the parent
   * @param view the view
   * @param position the position
   * @param id the id
   */
  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    PlacesAutoCompleteAPI.AutoCompletePrediction prediction =
        (PlacesAutoCompleteAPI.AutoCompletePrediction) placesListAdapter.getItem(position);

    Bundle args = new Bundle();
    args.putString(BUNDLE_ARG_PLACE_ID, prediction.getId());

    getSupportLoaderManager().restartLoader(LOADER_ID_PREDICTION_DETAILS, args,
        new PredictionDetailsLoaderCallback(this));
  }

  /**
   * On new location.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   */
  protected void onNewLocation(double latitude, double longitude) {
    Bundle args = new Bundle();
    args.putDouble(BUNDLE_ARG_LATITUDE, latitude);
    args.putDouble(BUNDLE_ARG_LONGITUDE, longitude);

    getSupportLoaderManager().restartLoader(LOADER_ID_PLACES_NEARBY, args,
        new NearbyPlacesLoaderCallbacks(this));
  }

  /**
   * On new places.
   *
   * @param places the places
   */
  protected void onNewPlaces(List<Place> places) {
    mapPlacesRenderer.drawPlacesOnMap(getMapFragment().getMap(), places);
  }

  /**
   * The type Prediction details loader callback.
   */
  protected static class PredictionDetailsLoaderCallback implements
      LoaderManager.LoaderCallbacks<NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails>> {

    private final WeakReference<MainActivity> activityWeakReference;

    /**
     * Instantiates a new Prediction details loader callback.
     *
     * @param mainActivity the main activity
     */
    public PredictionDetailsLoaderCallback(MainActivity mainActivity) {
      activityWeakReference = new WeakReference<>(mainActivity);
    }

    /**
     * On create loader loader.
     *
     * @param id the id
     * @param args the args
     * @return the loader
     */
    @Override
    public Loader<NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails>> onCreateLoader(int id,
        Bundle args) {
      String placeId = args.getString(BUNDLE_ARG_PLACE_ID);

      return new PredictionDetailsLoader(activityWeakReference.get(), placeId,
          Injector.Singleton.getPlacesAutoCompleteAPI());
    }

    /**
     * On load finished.
     *
     * @param loader the loader
     * @param data the data
     */
    @Override public void onLoadFinished(
        Loader<NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails>> loader,
        NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails> data) {
      if (data != null && data.getResults() != null) {
        PlacesAutoCompleteAPI.PredictionDetails results = data.getResults();
        activityWeakReference.get().onNewLocation(results.getLatitude(), results.getLongitude());
      }
    }

    /**
     * On loader reset.
     *
     * @param loader the loader
     */
    @Override public void onLoaderReset(
        Loader<NetworkResponse<PlacesAutoCompleteAPI.PredictionDetails>> loader) {

    }
  }

  /**
   * The type Nearby places loader callbacks.
   */
  protected static class NearbyPlacesLoaderCallbacks
      implements LoaderManager.LoaderCallbacks<NetworkResponse<List<Place>>> {

    private final WeakReference<MainActivity> activityWeakReference;

    /**
     * Instantiates a new Nearby places loader callbacks.
     *
     * @param mainActivity the main activity
     */
    public NearbyPlacesLoaderCallbacks(MainActivity mainActivity) {
      activityWeakReference = new WeakReference<>(mainActivity);
    }

    /**
     * On create loader loader.
     *
     * @param id the id
     * @param args the args
     * @return the loader
     */
    @Override public Loader<NetworkResponse<List<Place>>> onCreateLoader(int id, Bundle args) {
      double latitude = args.getDouble(BUNDLE_ARG_LATITUDE);
      double longitude = args.getDouble(BUNDLE_ARG_LONGITUDE);

      return new PlacesNearbyLoader(activityWeakReference.get(),
          Injector.Singleton.getPlacesNearbyAPI(), latitude, longitude);
    }

    /**
     * On load finished.
     *
     * @param loader the loader
     * @param data the data
     */
    @Override public void onLoadFinished(Loader<NetworkResponse<List<Place>>> loader,
        NetworkResponse<List<Place>> data) {
      if (data != null && data.getResults() != null) {
        activityWeakReference.get().onNewPlaces(data.getResults());
      }
    }

    /**
     * On loader reset.
     *
     * @param loader the loader
     */
    @Override public void onLoaderReset(Loader<NetworkResponse<List<Place>>> loader) {

    }
  }
}
