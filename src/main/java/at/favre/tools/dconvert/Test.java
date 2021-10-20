package at.favre.tools.dconvert;

import at.favre.tools.dconvert.arg.Arguments;
import edu.columbia.cs.psl.phosphor.runtime.MultiTainter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

public class Test {

  private float scale;

  public Test() {
    this.scale = MultiTainter.taintedFloat(3f, Taint.withLabel(0));
  }

  public static void main(String[] args) {
    Test test = new Test();
    foo(test);
  }

  private static void foo(Test test) {
    boolean b = test.scale > Arguments.DEFAULT_SCALE;
    Taint t = MultiTainter.getTaint(b);
    System.out.println(t);
  }
}
