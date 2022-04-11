package ro.pub.cs.systems.eim.practicaltest01var06secondaryactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondaryActivity extends AppCompatActivity {
    private TextView gainedText;
    private Button okButton;
    private Intent intent;
    private HandleClickListener clickListener;
    private String scor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        gainedText = (TextView) findViewById(R.id.GAINED_TEXT);
        okButton = (Button) findViewById(R.id.OK);

        intent = getIntent();
        if (intent != null) {
            String number1 = intent.getStringExtra(Constants.EXTRA_DATA_NUMBER1);
            String number2 = intent.getStringExtra(Constants.EXTRA_DATA_NUMBER2);
            String number3 = intent.getStringExtra(Constants.EXTRA_DATA_NUMBER3);
            String numberCheckboxes = intent.getStringExtra(Constants.EXTRA_DATA_NUMBER_CHECKBOXES);
            if (number1 != null && number2 != null && number3 != null) {
                if (number1.equals(number2) && (number1.equals(number3) || number3.equals("*"))) {
                    gainedText.setText("Gained ");
                } else if (number1.equals(number3) && (number1.equals(number2) || number2.equals("*"))) {
                    gainedText.setText("Gained ");
                } else if (number2.equals(number3) && (number2.equals(number1) || number1.equals("*"))) {
                    gainedText.setText("Gained ");
                }
            }
            if (numberCheckboxes != null) {
                if (numberCheckboxes.equals("0")) {
                    gainedText.setText(gainedText.getText() + "100");
                    scor = "100";
                } else if (numberCheckboxes.equals("1")) {
                    gainedText.setText(gainedText.getText() + "50");
                    scor = "50";
                } else if (numberCheckboxes.equals("2")) {
                    gainedText.setText(gainedText.getText() + "10");
                    scor = "10";
                } else {
                    scor = "0";
                }
            }
        }
        clickListener = new HandleClickListener();
        okButton.setOnClickListener(clickListener);
    }

    private class HandleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.OK:
                    Intent intentToParent = new Intent("ro.pub.cs.systems.eim.practicaltest01var06.intent.action.PracticalTest01Var06MainActivity");
                    intentToParent.putExtra("SCOR", String.valueOf(scor));
                    setResult(RESULT_OK, intentToParent);
                    break;
            }
            finish();
        }
    }
}