package com.roisagiv.aroundme.utils;

import java.util.Random;

/**
 *
 */
public class LocationGenerator {

  // 38.660955, -121.342451 MTI college coordinates (center)
  // constants mark the boundaries within which to generate random locations
  private static final double MIN_LATITUDE = 38.600000;
  private static final double MAX_LATITUDE = 38.700000;
  private static final double MIN_LONGITUDE = -121.300000;
  private static final double MAX_LONGITUDE = -121.400000;

  public static double latitude() {
    Random random = new Random();
    return MIN_LATITUDE + ((MAX_LATITUDE - MIN_LATITUDE) * random.nextDouble());
  }

  public static double longitude() {
    Random random = new Random();
    return MIN_LONGITUDE + ((MAX_LONGITUDE - MIN_LONGITUDE) * random.nextDouble());
  }
}
