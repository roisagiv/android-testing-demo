package com.roisagiv.aroundme.views.adapters;

import android.database.DataSetObserver;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.roisagiv.aroundme.managers.NetworkResponse;
import com.roisagiv.aroundme.managers.PlacesAutoCompleteAPI;
import java.util.List;
import org.exparity.stub.random.RandomBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Places list adapter test.
 */
@RunWith(Enclosed.class) public class PlacesListAdapterTest {

  /**
   * The type Base adapter test.
   */
  @RunWith(AndroidJUnit4.class) public static class BaseAdapterTests {

    /**
     * The Thread test rule.
     */
    @Rule public UiThreadTestRule threadTestRule = new UiThreadTestRule();

    /**
     * Gets count should return number of places.
     */
    @Test @UiThreadTest public void getCountShouldReturnNumberOfPlaces() {
      List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions =
          RandomBuilder.aRandomListOf(PlacesAutoCompleteAPI.AutoCompletePrediction.class);

      PlacesListAdapter adapter = new PlacesListAdapter(null);
      adapter.setAutoCompletePredictions(predictions);

      assertThat(adapter.getCount()).isEqualTo(predictions.size());
    }

    /**
     * Gets item should return prediction at position.
     */
    @Test @UiThreadTest public void getItemShouldReturnPredictionAtPosition() {
      List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions =
          RandomBuilder.aRandomListOf(PlacesAutoCompleteAPI.AutoCompletePrediction.class);

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
     * Get view should return simple list item 1 layout.
     */
    @Test @UiThreadTest public void getViewShouldReturnSimpleListItem1Layout() {
      List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions =
          RandomBuilder.aRandomListOf(PlacesAutoCompleteAPI.AutoCompletePrediction.class);

      PlacesListAdapter adapter = new PlacesListAdapter(null);
      adapter.setAutoCompletePredictions(predictions);

      View view = adapter.getView(1, null, new FrameLayout(InstrumentationRegistry.getContext()));

      assertThat(view.findViewById(android.R.id.text1)).isNotNull();
    }

    /**
     * Get view should set prediction in text view.
     */
    @Test @UiThreadTest public void getViewShouldSetPredictionInTextView() {
      List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions =
          RandomBuilder.aRandomListOf(PlacesAutoCompleteAPI.AutoCompletePrediction.class, 4, 4);

      PlacesListAdapter adapter = new PlacesListAdapter(null);
      adapter.setAutoCompletePredictions(predictions);

      View view;
      TextView textView;

      view = adapter.getView(0, null, new FrameLayout(InstrumentationRegistry.getContext()));
      textView = (TextView) view.findViewById(android.R.id.text1);
      assertThat(textView.getText()).isEqualTo(predictions.get(0).getDescription());

      view = adapter.getView(1, null, new FrameLayout(InstrumentationRegistry.getContext()));
      textView = (TextView) view.findViewById(android.R.id.text1);
      assertThat(textView.getText()).isEqualTo(predictions.get(1).getDescription());

      view = adapter.getView(2, null, new FrameLayout(InstrumentationRegistry.getContext()));
      textView = (TextView) view.findViewById(android.R.id.text1);
      assertThat(textView.getText()).isEqualTo(predictions.get(2).getDescription());

      view = adapter.getView(3, null, new FrameLayout(InstrumentationRegistry.getContext()));
      textView = (TextView) view.findViewById(android.R.id.text1);
      assertThat(textView.getText()).isEqualTo(predictions.get(3).getDescription());
    }

    /**
     * Get view should recycle views.
     */
    @Test @UiThreadTest public void getViewShouldRecycleViews() {
      // arrange
      List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions =
          RandomBuilder.aRandomListOf(PlacesAutoCompleteAPI.AutoCompletePrediction.class);

      PlacesListAdapter adapter = new PlacesListAdapter(null);
      adapter.setAutoCompletePredictions(predictions);

      View recyclableView = LayoutInflater.from(InstrumentationRegistry.getContext())
          .inflate(android.R.layout.simple_list_item_1, null);

      // make sure our text is different
      TextView textView = (TextView) recyclableView.findViewById(android.R.id.text1);
      textView.setText(android.R.string.untitled);

      // act
      View view =
          adapter.getView(1, recyclableView, new FrameLayout(InstrumentationRegistry.getContext()));

      // assert
      assertThat(view).isEqualTo(recyclableView);
      assertThat(textView.getText()).isEqualTo(predictions.get(1).getDescription());
    }
  }

  /**
   * The type Filterable tests.
   */
  @RunWith(AndroidJUnit4.class) public static class FilterableTests {

    /**
     * The Thread test rule.
     */
    @Rule public UiThreadTestRule threadTestRule = new UiThreadTestRule();

    /**
     * Perform filtering should call places auto complete.
     */
    @Test @UiThreadTest public void performFilteringShouldCallPlacesAutoComplete() {
      // arrange
      PlacesAutoCompleteAPI mockPlacesAutoCompleteAPI = Mockito.mock(PlacesAutoCompleteAPI.class);
      PlacesListAdapter adapter = new PlacesListAdapter(mockPlacesAutoCompleteAPI);
      String text = "some text";

      // act
      PlacesListAdapter.PlacesListAdapterFilter filter =
          (PlacesListAdapter.PlacesListAdapterFilter) adapter.getFilter();
      filter.performFiltering(text);

      // assert
      Mockito.verify(mockPlacesAutoCompleteAPI).autoComplete(text);
    }

    /**
     * Perform filtering should do nothing if text constraint null.
     */
    @Test @UiThreadTest public void performFilteringShouldDoNothingIfTextConstraintNull() {
      PlacesAutoCompleteAPI mockPlacesAutoCompleteAPI = Mockito.mock(PlacesAutoCompleteAPI.class);
      PlacesListAdapter adapter = new PlacesListAdapter(mockPlacesAutoCompleteAPI);

      // act
      PlacesListAdapter.PlacesListAdapterFilter filter =
          (PlacesListAdapter.PlacesListAdapterFilter) adapter.getFilter();
      filter.performFiltering(null);

      // assert
      Mockito.verifyZeroInteractions(mockPlacesAutoCompleteAPI);
    }

    /**
     * Publish results should clear predictions if results are empty.
     */
    @Test @UiThreadTest public void publishResultsShouldClearPredictionsIfResultsAreEmpty() {
      // arrange
      PlacesListAdapter adapter = new PlacesListAdapter(null);

      PlacesListAdapter.PlacesListAdapterFilter filter =
          (PlacesListAdapter.PlacesListAdapterFilter) adapter.getFilter();

      // act
      filter.publishResults(null, filter.performFiltering(null));

      // assert
      assertThat(adapter.getCount()).isEqualTo(0);
    }

    /**
     * Publish results should set new predictions in adapter.
     */
    @Test @UiThreadTest public void publishResultsShouldSetNewPredictionsInAdapter() {
      // arrange
      String text = "some text";

      List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions =
          RandomBuilder.aRandomListOf(PlacesAutoCompleteAPI.AutoCompletePrediction.class);
      NetworkResponse<List<PlacesAutoCompleteAPI.AutoCompletePrediction>> response =
          new NetworkResponse<>();
      response.setResults(predictions);
      response.setHttpCode(200);

      PlacesAutoCompleteAPI mockPlacesAutoCompleteAPI = Mockito.mock(PlacesAutoCompleteAPI.class);
      Mockito.when(mockPlacesAutoCompleteAPI.autoComplete(text)).thenReturn(response);

      // act
      PlacesListAdapter adapter = new PlacesListAdapter(mockPlacesAutoCompleteAPI);
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

      List<PlacesAutoCompleteAPI.AutoCompletePrediction> predictions =
          RandomBuilder.aRandomListOf(PlacesAutoCompleteAPI.AutoCompletePrediction.class);
      NetworkResponse<List<PlacesAutoCompleteAPI.AutoCompletePrediction>> response =
          new NetworkResponse<>();
      response.setResults(predictions);
      response.setHttpCode(200);

      PlacesAutoCompleteAPI mockPlacesAutoCompleteAPI = Mockito.mock(PlacesAutoCompleteAPI.class);
      DataSetObserver mockDataSetObserver = Mockito.mock(DataSetObserver.class);
      Mockito.when(mockPlacesAutoCompleteAPI.autoComplete(text)).thenReturn(response);

      // act
      PlacesListAdapter adapter = new PlacesListAdapter(mockPlacesAutoCompleteAPI);
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
}
