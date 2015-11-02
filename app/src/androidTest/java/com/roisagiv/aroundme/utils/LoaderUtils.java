package com.roisagiv.aroundme.utils;

import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import org.junit.Assert;

/**
 *
 */
public class LoaderUtils {

  public static void waitForLoader(Loader<?> loader) {
    final AsyncTaskLoader<?> asyncTaskLoader = (AsyncTaskLoader<?>) loader;

    Thread waitThreads = new Thread("LoaderWaitingThread") {
      @Override public void run() {
        try {
          asyncTaskLoader.waitForLoader();
        } catch (Throwable e) {
          Assert.fail("Exception while waiting for loader: " + asyncTaskLoader.getId());
        }
      }
    };

    waitThreads.start();

    // Now we wait for all these threads to finish
    try {
      waitThreads.join();
    } catch (InterruptedException ignored) {
    }
  }
}
