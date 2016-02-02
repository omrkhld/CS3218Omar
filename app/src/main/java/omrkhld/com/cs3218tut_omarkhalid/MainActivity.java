package omrkhld.com.cs3218tut_omarkhalid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToSimpleCalcActivity(View view) {
        intent = new Intent(this, SimpleCalculatorActivity.class);
        startActivity(intent);
    }

    public void goToGraphActivity(View view) {
        intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }
}
