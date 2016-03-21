package omrkhld.com.cs3218tut_omarkhalid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToSimpleCalcActivity(View view) {
        intent = new Intent(this, SimpleCalculatorActivity.class);
        startActivity(intent);
    }

    public void goToGraphActivity(View view) {
        intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    public void goToSoundActivity(View view) {
        intent = new Intent(this, SoundSamplingActivity.class);
        startActivity(intent);
    }

    public void goToCalculusActivity(View v) {
        intent = new Intent(this, CalculusActivity.class);
        startActivity(intent);
    }

    // Tutorial on FFT (Tutorial 5a)
    public void goToFFTActivity(View v) {
        intent = new Intent(this, FFTActivity.class);
        startActivity(intent);
    }

    // Tutorial on Live FFT (Tutorial 5b)
    public void goToLiveFFTActivity(View v) {
        intent = new Intent(this, LiveFFTActivity.class);
        startActivity(intent);
    }

    // Tutorial on Live FFT (Tutorial 5c)
    public void goToSpectrogramActivity(View v) {
        intent = new Intent(this, SpectrogramActivity.class);
        startActivity(intent);
    }
}
