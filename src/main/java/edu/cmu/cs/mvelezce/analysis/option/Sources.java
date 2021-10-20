package edu.cmu.cs.mvelezce.analysis.option;

import edu.columbia.cs.psl.phosphor.control.ControlFlowStack;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import edu.columbia.cs.psl.phosphor.runtime.TaintInstrumented;
import edu.columbia.cs.psl.phosphor.struct.TaintedIntWithObjTag;
import edu.columbia.cs.psl.phosphor.struct.TaintedWithObjTag;

@TaintInstrumented
public class Sources implements TaintedWithObjTag {
    public Taint<Integer> PHOSPHOR_TAG;

    public static TaintedIntWithObjTag SCALE_0$$PHOSPHORTAGGED(
            int value,
            Taint<Integer> originalTag,
            ControlFlowStack ctrl,
            TaintedIntWithObjTag ret) {
        ret.val = value;
        ret.taint = Taint.withLabel(0);
        return ret;
    }

    public static int SCALE_0(int value) {
        return value;
    }

    @Override
    public Object getPHOSPHOR_TAG() {
        return this.PHOSPHOR_TAG;
    }

    @Override
    public void setPHOSPHOR_TAG(Object var1) {
        this.PHOSPHOR_TAG = (Taint) var1;
    }

}
