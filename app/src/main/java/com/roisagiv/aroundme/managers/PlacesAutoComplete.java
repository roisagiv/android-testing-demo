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

  /**
   * Prediction details response.
   *
   * @param id the id
   * @return the response
   */
  Response<PredictionDetails> predictionDetails(String id);

  /**
   * The type Response.
   *
   * @param <T> the type parameter
   */
  class Response<T> {
    private T results;
    private int httpCode;
    private Throwable error;

    /**
     * Gets results.
     *
     * @return the results
     */
    public T getResults() {
      return results;
    }

    /**
     * Sets results.
     *
     * @param results the results
     */
    public void setResults(T results) {
      this.results = results;
    }

    /**
     * Gets http code.
     *
     * @return the http code
     */
    public int getHttpCode() {
      return httpCode;
    }

    /**
     * Sets http code.
     *
     * @param httpCode the http code
     */
    public void setHttpCode(int httpCode) {
      this.httpCode = httpCode;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public Throwable getError() {
      return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
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

  /**
   * The type Prediction details.
   */
  class PredictionDetails {
    private String id;
    private String description;
    private double latitude;
    private double longitude;

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
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
      return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
      this.latitude = latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
      return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
      this.longitude = longitude;
    }
  }
}
