package com.roisagiv.aroundme.managers;

/**
 * The type Response.
 *
 * @param <T> the type parameter
 */
public class NetworkResponse<T> {
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
