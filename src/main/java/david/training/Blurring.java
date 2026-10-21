package david.training;

import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Blurring extends RecursiveAction {
    protected static int THRESHOLD = 10_000;
    private final int[] mSource;
    private final int mStart;
    private final int mLength;
    private final int[] mDestination;

    public Blurring(int[] mSource, int mStart, int mLength, int[] mDestination) {
        this.mSource = mSource;
        this.mStart = mStart;
        this.mLength = mLength;
        this.mDestination = mDestination;
    }

    private void computeDirectly() {
        int mBlurWidth = 15;
        int sidePixels = (mBlurWidth - 1) / 2;
        for (int i = mStart; i < mStart + mLength; i++) {
            float rt = 0;
            float gt = 0;
            float bt = 0;
            for (int mi = -sidePixels; mi <= sidePixels; mi++) {
                int mIndex = Math.min(Math.max(mi + i, 0), mSource.length - 1);
                int pixel = mSource[mIndex];
                rt += (float) ((pixel & 0x00ff0000) >> 16) / mBlurWidth;
                gt += (float) ((pixel & 0x0000ff00) >> 8) / mBlurWidth;
                bt += (float) (pixel & 0x000000ff) / mBlurWidth;
            }

            int dPixel = 0xff000000 | (((int) rt) << 16) | (((int) gt) << 8) | (int) bt;
            mDestination[i] = dPixel;
        }
    }

    @Override
    protected void compute() {
        if (mLength < THRESHOLD) {
            computeDirectly();
            return;
        }

        int split = mLength / 2;

        invokeAll(
                new Blurring(mSource, mStart, split, mDestination),
                new Blurring(mSource, mStart + split, mLength - split, mDestination)
        );
    }

    public static BufferedImage blur(BufferedImage srcImage) {
        int w = srcImage.getWidth();
        int h = srcImage.getHeight();

        int[] src = srcImage.getRGB(0, 0, w, h, null, 0 ,w);
        int[] dst = new int[src.length];

        System.out.printf("Array size is: %d%n", src.length);
        System.out.printf("Threshold is: %d%n", THRESHOLD);
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.printf("Processors available: %d%n", processors);
        Blurring fBlurring = new Blurring(src, 0, src.length, dst);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(fBlurring);
        BufferedImage dstImage = new BufferedImage(w, h, srcImage.getType());
        dstImage.setRGB(0, 0, w, h, dst, 0, w);
        return dstImage;
    }
}
