package omrkhld.com.cs3218tut_omarkhalid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

/**
 * Created by Omar on 11/3/2016.
 */
public class CSurfaceViewSpectrogram extends SurfaceView implements SurfaceHolder.Callback {
    private Context drawContext;
    public  DrawThread       drawThread;
    private SurfaceHolder drawSurfaceHolder;
    private Boolean          threadExists = false;
    public static volatile   Boolean drawFlag = false;
    private static  int    rectPos;

    private static final int RECTPOS = 1150;

    private static final Handler handler = new Handler(){

        public void handleMessage(Message paramMessage)
        {
        }
    };



    public CSurfaceViewSpectrogram(Context ctx, AttributeSet attributeSet)
    {
        super(ctx, attributeSet);

        drawContext = ctx;

        init();

    }


    public void init()
    {

        if (!threadExists) {

            drawSurfaceHolder = getHolder();
            drawSurfaceHolder.addCallback(this);

            drawThread = new DrawThread(drawSurfaceHolder, drawContext, handler);

            drawThread.setName("" +System.currentTimeMillis());
            drawThread.start();
        }

        threadExists = Boolean.valueOf(true);
        drawFlag = Boolean.valueOf(true);
        rectPos = 0;

        return;

    }


    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
    {
        drawThread.setSurfaceSize(paramInt2, paramInt3);
    }


    public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
    {

        init();

    }


    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
    {

        while (true)
        {
            if (!drawFlag)
                return;
            try
            {
                drawFlag = Boolean.valueOf(false);
                drawThread.join();
            }
            catch (InterruptedException localInterruptedException)
            {

            }
        }

    }


    class DrawThread extends Thread
    {
        private Bitmap soundBackgroundImage;
        private short[]        soundBuffer;
        private int[]          soundSegmented;
        private double[]       soundFFT;
        private double[]       soundFFTMag;
        private double[]       soundFFTTemp;
        public int             FFT_Len      = 1024;
        public  int            segmentIndex = -1;
        private int            soundCanvasHeight = 0;
        private Paint          soundLinePaint;
        private SurfaceHolder  soundSurfaceHolder;
        private int            drawScale = 7;
        public  Boolean        soundCapture = Boolean.valueOf(false);

        public DrawThread(SurfaceHolder paramContext, Context paramHandler, Handler arg4)
        {
            soundSurfaceHolder = paramContext;

            soundLinePaint     = new Paint();
            soundLinePaint.setARGB(255, 255, 0, 0);
            soundLinePaint.setStrokeWidth(3);

            soundBuffer          = new short[1024];

            soundBackgroundImage = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

            soundSegmented     = new int[FFT_Len];
            soundFFT           = new double[FFT_Len*2];
            soundFFTMag        = new double[FFT_Len];
            soundFFTTemp       = new double[FFT_Len*2];
        }

        public void doDraw(Canvas canvas)
        {

            soundCanvasHeight  = canvas.getHeight();

            int height         = soundCanvasHeight;

            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPaint(paint);

            // display the signal in temporal domain
            int xStart = 0;
            while (xStart < FFT_Len - 1) {
                int yStart = soundSegmented[xStart] / height * drawScale;
                int yStop = soundSegmented[xStart + 1] / height * drawScale;

                int yStart1 = yStart + height / 4;
                int yStop1 = yStop + height / 4;

                canvas.drawLine(xStart, yStart1, xStart + 1, yStop1, soundLinePaint);
                xStart++;
            }

            segmentIndex = 0;
            while (segmentIndex < FFT_Len) {
                soundSegmented[segmentIndex] = soundBuffer[segmentIndex];
                soundFFT[2 * segmentIndex] = (double) soundSegmented[segmentIndex];
                soundFFT[2 * segmentIndex + 1] = 0.0;
                segmentIndex++;
            }

            // fft
            DoubleFFT_1D fft = new DoubleFFT_1D(FFT_Len);
            fft.complexForward(soundFFT);

            // perform fftshift here
            for (int i = 0; i < FFT_Len; i++) {
                soundFFTTemp[i] = soundFFT[i + FFT_Len];
                soundFFTTemp[i + FFT_Len] = soundFFT[i];
            }
            for (int i = 0; i < FFT_Len * 2; i++) {
                soundFFT[i] = soundFFTTemp[i];
            }


            Paint spectrogramPaint = new Paint();
            paint.setStrokeWidth(2);

            double mx = -99999;
            double mi = Double.MAX_VALUE;
            for (int i = 0; i < FFT_Len; i++) {

                double re = soundFFT[2 * i];
                double im = soundFFT[2 * i + 1];

                soundFFTMag[i] = Math.log(re * re + im * im + 0.001);

                if (soundFFTMag[i] > mx)
                    mx = soundFFTMag[i];
                if (soundFFTMag[i] < mi)
                    mi = soundFFTMag[i];
            }

            int value = 0;
            int spectrumHeight = height;
            for(int i=0; i < FFT_Len; i++) {
                value = (int)((soundFFTMag[i] - mi) / (mx - mi) * 510);
                if(value <= 255) {
                    spectrogramPaint.setARGB(255, value, 255, 0);
                } else if(value < 510) {
                    spectrogramPaint.setARGB(255, 255, 2*255-value, 0);
                }

                canvas.drawLine(rectPos, spectrumHeight-1-i, rectPos, spectrumHeight-i, spectrogramPaint);
            }
        }

        public void setBuffer(short[] paramArrayOfShort)
        {
            synchronized (soundBuffer)
            {
                soundBuffer = paramArrayOfShort;
                return;
            }
        }


        public void setSurfaceSize(int canvasWidth, int canvasHeight)
        {
            synchronized (soundSurfaceHolder)
            {
                soundBackgroundImage = Bitmap.createScaledBitmap(soundBackgroundImage, canvasWidth, canvasHeight, true);
                return;
            }
        }


        public void run()
        {
            while (drawFlag)
            {

                Canvas localCanvas = null;
                try
                {
                    localCanvas = soundSurfaceHolder.lockCanvas(new Rect(rectPos, 0 ,rectPos+1, soundCanvasHeight));
                    synchronized (soundSurfaceHolder)
                    {
                        if (localCanvas != null) {
                            doDraw(localCanvas);
                            rectPos = (rectPos + 1) % RECTPOS;
                        }
                    }
                }
                finally
                {
                    if (localCanvas != null)
                        soundSurfaceHolder.unlockCanvasAndPost(localCanvas);
                }
            }
        }

    }
}
