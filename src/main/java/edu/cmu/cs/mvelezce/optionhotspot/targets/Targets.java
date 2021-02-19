package edu.cmu.cs.mvelezce.optionhotspot.targets;

import java.awt.image.BufferedImage;

public class Targets {

  public static boolean outputLargerThanDefault(boolean decision) {
    return decision;
  }

  public static int dstWidth(int value) {
    return value;
  }

  public static int dstHeight(int value) {
    return value;
  }

  public static BufferedImage bufferedImage(BufferedImage bufferedImage) {
    return bufferedImage;
  }
}
