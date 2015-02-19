package org.opencv.samples.puzzle15;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import android.util.Log;
/**
 * This class is a controller for puzzle game.
 * It converts the image from Camera into the shuffled image
 */
public class Puzzle15Processor {
    Mat mSepiaKernel;
    public double theta;
    double S;
    double C;
    int width;
    int height;
    public Puzzle15Processor() {
        theta = 1;
    }
    /* This method is to make the processor know the size of the frames that
     * will be delivered via puzzleFrame.
     * If the frames will be different size - then the result is unpredictable
     */
    public void initialize(int width, int height) {
        mSepiaKernel = new Mat(3, 4, CvType.CV_32F);
        this.width = width;
        this.height = height;
    }
    public void deliverTouchEvent(int xpos, int ypos) {
        synchronized (this) {
            this.theta = 360 * ypos / height;
        }
        return;
    }
    public synchronized Mat puzzleFrame(Mat inputPicture) {
        Imgproc.cvtColor(inputPicture, inputPicture, Imgproc.COLOR_BGR2YCrCb);
        // theta = (theta + 3) % 360;
        double S = Math.sin(theta * Math.PI / 180);
        double C = Math.cos(theta * Math.PI / 180);
        mSepiaKernel.put(0, 0, /* R */1, 0, 0, 0);
        mSepiaKernel.put(1, 0, /* G */0f, C, -S, 128 * (1 - C + S));
        mSepiaKernel.put(2, 0, /* B */0f, S, C, 128 * (1 - C - S));
        Core.transform(inputPicture, inputPicture, mSepiaKernel);
        Imgproc.cvtColor(inputPicture, inputPicture, Imgproc.COLOR_YCrCb2BGR);
        return inputPicture;
    }
}