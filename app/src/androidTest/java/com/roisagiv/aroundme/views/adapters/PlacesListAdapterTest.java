package com.roisagiv.aroundme.views.adapters;

import android.database.DataSetObserver;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.roisagiv.aroundme.managers.PlacesAutoComplete;
import java.util.List;
import org.exparity.stub.random.RandomBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Places list adapter test.
 */
@RunWith(AndroidJUnit4.class) public class PlacesListAdapterTest {

  /**
   * The Thread test rule.
   */
  @Rule public UiThreadTestRule threadTestRule = new UiThreadTestRule();

  /**
   * Gets count should return number of places.
   */
  @Test @UiThreadTest public void getCountShouldReturnNumberOfPlaces() {
    List<PlacesAutoComplete.AutoCompletePrediction> predictions =
        RandomBuilder.aRandomListOf(PlacesAutoComplete.AutoCompletePrediction.class);

    PlacesListAdapter adapter = new PlacesListAdapter(null);
    adapter.setAutoCompletePredictions(predictions);

    assertThat(adapter.getCount()).isEqualTo(predictions.size());
  }

  /**
   * Gets item should return prediction at position.
   */
  @Test @UiThreadTest public void getItemShouldReturnPredictionAtPosition() {
    List<PlacesAutoComplete.AutoCompletePrediction> predictions =
        RandomBuilder.aRandomListOf(PlacesAutoComplete.AutoCompletePrediction.class);

    PlacesListAdapter adapter = new PlacesListAdapter(null);
    adapter.setAutoCompletePredictions(predictions);

    assertThat(adapter.getItem(1)).isEqualTo(predictions.get(1));
  }

  /**
   * Gets item id should return the position.
   */
  @Test @UiThreadTest public void getItemIdShouldReturnThePosition() {
    PlacesListAdapter adapter = new PlacesListAdapter(null);

    assertThat(adapter.getItemId(1)).isEqualTo(1);
    assertThat(adapter.getItemId(3)).isEqualTo(3);
    assertThat(adapter.getItemId(100)).isEqualTo(100);
  }

  /**
   * Perform filtering should call places auto complete.
   */
  @Test @UiThreadTest public void performFilteringShouldCallPlacesAutoComplete() {
    // arrange
    PlacesAutoComplete mockPlacesAutoComplete = Mockito.mock(PlacesAutoComplete.class);
    PlacesListAdapter adapter = new PlacesListAdapter(mockPlacesAutoComplete);
    String text = "some text";

    // act
    PlacesListAdapter.PlacesListAdapterFilter filter =
        (PlacesListAdapter.PlacesListAdapterFilter) adapter.getFilter();
    filter.performFiltering(text);

    // assert
    Mockito.verify(mockPlacesAutoComplete).autoComplete(text);
  }

  /**
   * Publish results should set new predictions in adapter.
   */
  @Test @UiThreadTest public void publishResultsShouldSetNewPredictionsInAdapter() {
    // arrange
    String text = "some text";

    List<PlacesAutoComplete.AutoCompletePrediction> predictions =
        RandomBuilder.aRandomListOf(PlacesAutoComplete.AutoCompletePrediction.class);
    PlacesAutoComplete.Response<List<PlacesAutoComplete.AutoCompletePrediction>> response =
        new PlacesAutoComplete.Response<>();
    response.setResults(predictions);
    response.setHttpCode(200);

    PlacesAutoComplete mockPlacesAutoComplete = Mockito.mock(PlacesAutoComplete.class);
    Mockito.when(mockPlacesAutoComplete.autoComplete(text)).thenReturn(response);

    // act
    PlacesListAdapter adapter = new PlacesListAdapter(mockPlacesAutoComplete);
    // make sure we are empty
    assertThat(adapter.getCount()).isEqualTo(0);

    PlacesListAdapter.PlacesListAdapterFilter filter =
        (PlacesListAdapter.PlacesListAdapterFilter) adapter.getFilter();
    filter.publishResults(text, filter.performFiltering(text));

    // assert
    assertThat(adapter.getCount()).isEqualTo(predictions.size());
    assertThat(adapter.getItem(1)).isEqualTo(predictions.get(1));
  }

  /**
   * Publish results should call notify data set changed.
   */
  @Test @UiThreadTest public void publishResultsShouldCallNotifyDataSetChanged() {
    // arrange
    String text = "some text";

    List<PlacesAutoComplete.AutoCompletePrediction> predictions =
        RandomBuilder.aRandomListOf(PlacesAutoComplete.AutoCompletePrediction.class);
    PlacesAutoComplete.Response<List<PlacesAutoComplete.AutoCompletePrediction>> response =
        new PlacesAutoComplete.Response<>();
    response.setResults(predictions);
    response.setHttpCode(200);

    PlacesAutoComplete mockPlacesAutoComplete = Mockito.mock(PlacesAutoComplete.class);
    DataSetObserver mockDataSetObserver = Mockito.mock(DataSetObserver.class);
    Mockito.when(mockPlacesAutoComplete.autoComplete(text)).thenReturn(response);

    // act
    PlacesListAdapter adapter = new PlacesListAdapter(mockPlacesAutoComplete);
    adapter.registerDataSetObserver(mockDataSetObserver);

    // make sure we are empty
    assertThat(adapter.getCount()).isEqualTo(0);

    PlacesListAdapter.PlacesListAdapterFilter filter =
        (PlacesListAdapter.PlacesListAdapterFilter) adapter.getFilter();
    filter.publishResults(text, filter.performFiltering(text));

    // assert
    Mockito.verify(mockDataSetObserver).onChanged();
  }
}
