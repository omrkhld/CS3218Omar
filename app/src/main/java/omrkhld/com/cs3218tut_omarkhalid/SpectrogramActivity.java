package omrkhld.com.cs3218tut_omarkhalid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Omar on 8/3/2016.
 */
public class SpectrogramActivity extends Activity {
    public static CSurfaceViewSpectrogram surfaceView;
    public SoundSamplerSpectrogram soundSampler;
    public static int bufferSize;
    public static short[] buffer;

    public void goToMainActivity (View v) {

        try {
            CSurfaceViewSpectrogram.drawFlag = false;
            surfaceView.drawThread.join();
            soundSampler.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        finish();

    }

    public void captureSoundSpectrogram (View v) {
        if(surfaceView.drawThread.soundCapture) {
            surfaceView.drawThread.soundCapture = Boolean.valueOf(false);
            surfaceView.drawThread.segmentIndex = -1;
            ((Button) v).setText("Capture Spectrogram");
        } else {
            surfaceView.drawThread.soundCapture = Boolean.valueOf(true);
            ((Button) v).setText("Pause Spectrogram");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectrogram);

        try {
            soundSampler = new SoundSamplerSpectrogram(this);
        } catch(Exception e) {
            Log.e("SpectrogramActivity", "SoundSamplerSpectrogram cannot be instantiated");
        }

        try {
            soundSampler.init();
        } catch(Exception e) {
            Log.e("SpectrogramActivity", "SoundSamplerSpectrogram cannot be initialized");
        }

        surfaceView = (CSurfaceViewSpectrogram) findViewById(R.id.surfaceView);
        surfaceView.drawThread.setBuffer(buffer);
    }
}
