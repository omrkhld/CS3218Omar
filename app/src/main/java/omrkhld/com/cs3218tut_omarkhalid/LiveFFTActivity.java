package omrkhld.com.cs3218tut_omarkhalid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by Omar on 8/3/2016.
 */
public class LiveFFTActivity extends Activity {
    public static CSurfaceViewLiveFFT surfaceView;
    public SoundSamplerLiveFFT fftSoundSampler;
    public static int bufferSize;
    public static short[] buffer;

    public void goToMainActivity (View v) {

        try {
            CSurfaceViewLiveFFT.drawFlag = false;
            surfaceView.drawThread.join();
            fftSoundSampler.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_fft);

        try {
            fftSoundSampler = new SoundSamplerLiveFFT(this);
        } catch(Exception e) {
            Log.e("LiveFFTActivity", "SoundSamplerLiveFFT cannot be instantiated");
        }

        try {
            fftSoundSampler.init();
        } catch(Exception e) {
            Log.e("LiveFFTActivity", "SoundSamplerLiveFFT cannot be initialized");
        }

        surfaceView = (CSurfaceViewLiveFFT) findViewById(R.id.surfaceView);
        surfaceView.drawThread.setBuffer(buffer);

    }

    public void captureSoundLiveFFT (View v) {
        if(surfaceView.drawThread.soundCapture) {
            surfaceView.drawThread.soundCapture = Boolean.valueOf(false);
            surfaceView.drawThread.segmentIndex = -1;
        } else {
            surfaceView.drawThread.soundCapture = Boolean.valueOf(true);
        }
    }
}
