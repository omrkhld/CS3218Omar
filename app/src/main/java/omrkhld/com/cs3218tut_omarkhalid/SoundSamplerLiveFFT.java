package omrkhld.com.cs3218tut_omarkhalid;

import android.media.AudioRecord;
import android.util.Log;

/**
 * Created by ngtk on 22/1/15.
 */
public class SoundSamplerLiveFFT {


    private static final int  FS = 16000;     // sampling frequency
    public  AudioRecord       audioRecord;
    private int               audioEncoding = 2;
    private int               nChannels = 16;
    private LiveFFTActivity   liveFFTActivity;
    private Thread            recordingThread;


    public SoundSamplerLiveFFT(LiveFFTActivity mAct) throws Exception
    {

        liveFFTActivity = mAct;

        try {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
            }
            audioRecord = new AudioRecord(1, FS, nChannels, audioEncoding, AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding));

        }
        catch (Exception e) {
            Log.d("SamplerLiveFFT error", e.getMessage());
            throw new Exception();
        }


        return;


    }



    public void init() throws Exception
    {
        try {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
            }
            audioRecord = new AudioRecord(1, FS, nChannels, audioEncoding, AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding));

        }
        catch (Exception e) {
            Log.d("Error in Init() ", e.getMessage());
            throw new Exception();
        }


        LiveFFTActivity.bufferSize = AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding);
        LiveFFTActivity.buffer = new short[LiveFFTActivity.bufferSize];

        audioRecord.startRecording();

        recordingThread = new Thread()
        {
            public void run()
            {
                while (true)
                {

                    audioRecord.read(LiveFFTActivity.buffer, 0, LiveFFTActivity.bufferSize);
                    liveFFTActivity.surfaceView.drawThread.setBuffer(LiveFFTActivity.buffer);

                }
            }
        };
        recordingThread.start();

        return;

    }

    public void stop() {
        if(audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
        }
    }


}