package com.roisagiv.aroundme.managers;

import java.util.List;

/**
 * The interface Places auto complete.
 */
public interface PlacesAutoComplete {

  /**
   * Auto complete list.
   *
   * @param text the text
   * @return the list
   */
  Response<List<AutoCompletePrediction>> autoComplete(String text);

  class Response<T> {
    private T results;
    private int httpCode;
    private Throwable error;

    public T getResults() {
      return results;
    }

    public void setResults(T results) {
      this.results = results;
    }

    public int getHttpCode() {
      return httpCode;
    }

    public void setHttpCode(int httpCode) {
      this.httpCode = httpCode;
    }

    public Throwable getError() {
      return error;
    }

    public void setError(Throwable error) {
      this.error = error;
    }
  }

  /**
   * The type Auto complete prediction.
   */
  class AutoCompletePrediction {
    private String description;
    private String id;

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
      return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
      this.description = description;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
      return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
      this.id = id;
    }
  }
}
