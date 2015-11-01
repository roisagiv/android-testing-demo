package com.roisagiv.aroundme.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * taken from : https://android.googlesource.com/platform/packages/apps/UnifiedEmail/+/master/src/com/android/mail/ui/MailAsyncTaskLoader.java
 *
 * This class fills in some boilerplate for AsyncTaskLoader to actually load things.
 *
 * Subclasses need to implement {@link AsyncTaskLoader2#loadInBackground()} to perform the
 * actual
 * background task, and {@link AsyncTaskLoader2#onDiscardResult(T)} to clean up previously
 * loaded
 * results.
 */
public abstract class AsyncTaskLoader2<T> extends AsyncTaskLoader<T> {

  private T mResult;

  public AsyncTaskLoader2(Context context) {
    super(context);
  }

  @Override protected void onStartLoading() {
    if (mResult != null) {
      deliverResult(mResult);
    }
    if (takeContentChanged() || mResult == null) {
      forceLoad();
    }
  }

  @Override protected void onStopLoading() {
    cancelLoad();
  }

  @Override public void deliverResult(T data) {
    if (isReset()) {
      if (data != null) {
        onDiscardResult(data);
      }
      return;
    }
    final T oldResult = mResult;
    mResult = data;
    if (isStarted()) {
      super.deliverResult(data);
    }
    if (oldResult != null && oldResult != mResult) {
      onDiscardResult(oldResult);
    }
  }

  @Override protected void onReset() {
    super.onReset();

    onStopLoading();

    if (mResult != null) {
      onDiscardResult(mResult);
    }
    mResult = null;
  }

  @Override public void onCanceled(T data) {
    super.onCanceled(data);
    if (data != null) {
      onDiscardResult(data);
    }
  }

  /**
   * Called when discarding the load results so subclasses can take care of clean-up or
   * recycling tasks. This is not called if the same result (by way of pointer equality) is
   * returned again by a subsequent call to loadInBackground, or if result is null.
   *
   * Note that this may be called concurrently with loadInBackground(), and in some circumstances
   * may be called more than once for a given object.
   *
   * @param result The value returned from {@link AsyncTaskLoader2#loadInBackground()} which
   * is to be discarded.
   */
  protected abstract void onDiscardResult(final T result);
}
