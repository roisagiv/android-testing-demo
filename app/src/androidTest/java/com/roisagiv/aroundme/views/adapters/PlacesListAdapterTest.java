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

  /**
   * Get view should return simple list item 1 layout.
   */
  @Test @UiThreadTest public void getViewShouldReturnSimpleListItem1Layout() {
    List<PlacesAutoComplete.AutoCompletePrediction> predictions =
        RandomBuilder.aRandomListOf(PlacesAutoComplete.AutoCompletePrediction.class);

    PlacesListAdapter adapter = new PlacesListAdapter(null);
    adapter.setAutoCompletePredictions(predictions);

    View view = adapter.getView(1, null, new FrameLayout(InstrumentationRegistry.getContext()));

    assertThat(view.findViewById(android.R.id.text1)).isNotNull();
  }

  /**
   * Get view should set prediction in text view.
   */
  @Test @UiThreadTest public void getViewShouldSetPredictionInTextView() {
    List<PlacesAutoComplete.AutoCompletePrediction> predictions =
        RandomBuilder.aRandomListOf(PlacesAutoComplete.AutoCompletePrediction.class, 4, 4);

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
    List<PlacesAutoComplete.AutoCompletePrediction> predictions =
        RandomBuilder.aRandomListOf(PlacesAutoComplete.AutoCompletePrediction.class);

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
