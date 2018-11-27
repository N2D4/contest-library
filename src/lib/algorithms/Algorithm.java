package lib.algorithms;

public abstract class Algorithm {

    private Object result;
    private boolean end;

    protected Object runCustomAlgorithm(Runnable runnable) {
        end = false;
        result = null;

        runnable.run();
        return result;
    }



    protected void end() {
        end(null);
    }
    protected void end(Object result) {
        this.result = result;
        this.end = true;
    }

    protected boolean shouldEnd() {
        return end;
    }






}
