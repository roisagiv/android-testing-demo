package com.roisagiv.aroundme.views.adapters;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.roisagiv.aroundme.managers.PlacesAutoComplete;
import java.util.List;
import org.exparity.stub.random.RandomBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
}
