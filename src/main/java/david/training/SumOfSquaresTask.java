package david.training;

import java.util.concurrent.RecursiveAction;

public class SumOfSquaresTask extends RecursiveAction {
    private final double[] array;
    private final int lo;
    private final int hi;
    private double result;
    private SumOfSquaresTask next;

    public SumOfSquaresTask(double[] array) {
        this.array = array;
        this.lo = 0;
        this.hi = array.length - 1;
        this.next = null;
    }

    private SumOfSquaresTask(double[] array, int lo, int hi, SumOfSquaresTask next) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
        this.next = next;
    }


    private double atLeaf(int l, int h) {
        double sum = 0;
        for (int i = l; i < h; ++i) {
            sum += array[i] * array[i];
        }
        return sum;
    }

    public double getResult() {
        return result;
    }

    @Override
    protected void compute() {
        int l = lo;
        int h = hi;
        SumOfSquaresTask right = null;

        while (h - l > 1) {
            int mid = (l + h) >>> 1;
            right = new SumOfSquaresTask(array, mid, h, right);
            right.fork();
            h = mid;
        }

        double sum = atLeaf(l, h);

        while (right != null) {
            right.join();
            sum += right.result;
            right = right.next;
        }

        result = sum;
    }
}
