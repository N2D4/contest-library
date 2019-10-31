package lib.ml.optimize.multistage;

import java.io.Serializable;

/**
 * A stage to be used with {@link MultiStageOptimizer}.
 */
public abstract class Stage<I extends Serializable, O extends Serializable> {
    /**
     * Returns a double score evaluating how useful this input is going to be. Higher score = better.
     */
    public abstract double evaluateInput(I input);
    public abstract O compute(I input);

    /**
     * Must be unique among all stages in the multi-stage optimizer.
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }

    static <T extends Serializable> Stage<? extends T, T> newIdentityStage(String name) {
        return new Stage<T, T>() {
            @Override
            public double evaluateInput(T input) {
                return 0;
            }

            @Override
            public T compute(T input) {
                return input;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}
