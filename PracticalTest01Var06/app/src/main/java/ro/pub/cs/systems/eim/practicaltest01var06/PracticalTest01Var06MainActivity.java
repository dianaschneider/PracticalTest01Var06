package ro.pub.cs.systems.eim.practicaltest01var06;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PracticalTest01Var06MainActivity extends AppCompatActivity {
    private EditText number1EditText;
    private EditText number2EditText;
    private EditText number3EditText;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private Button playButton;
    private HandleClickListener clickListener;
    private String number1 = "", number2 = "", number3 = "";
    private boolean checked1, checked2, checked3;
    private Intent secondaryActivityIntent;
    private String scor = "";
    Intent service;
    private HandleBroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var06_main);

        number1EditText = (EditText) findViewById(R.id.NUMBER1);
        number2EditText = (EditText) findViewById(R.id.NUMBER2);
        number3EditText = (EditText) findViewById(R.id.NUMBER3);
        checkBox1 = (CheckBox) findViewById(R.id.CHECK1);
        checkBox2 = (CheckBox) findViewById(R.id.CHECK2);
        checkBox3 = (CheckBox) findViewById(R.id.CHECK3);
        playButton = (Button) findViewById(R.id.PLAY);

        clickListener = new HandleClickListener();
        playButton.setOnClickListener(clickListener);
        checkBox1.setOnClickListener(clickListener);
        checkBox2.setOnClickListener(clickListener);
        checkBox3.setOnClickListener(clickListener);
        number1EditText.setOnClickListener(clickListener);
        number2EditText.setOnClickListener(clickListener);
        number3EditText.setOnClickListener(clickListener);

        if (savedInstanceState != null && savedInstanceState.getInt(Constants.SCOR_STATE) != 0) {
            scor = savedInstanceState.getString(Constants.SCOR_STATE);
            Toast.makeText(this, "Scor = " + scor, Toast.LENGTH_LONG).show();
        }

        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.SERVICE_ACTION);
        broadcastReceiver = new HandleBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        stopService(service);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private class HandleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.PLAY:
                    boolean addStar = true;
                    int numberCheckboxes = 3;
                    if (number1.equals("*") || number2.equals("*") || number3.equals("*")) {
                        addStar = false;
                    }
                    if (!checked1) {
                        number1 = randomStringFromSet(addStar);
                        number1EditText.setText(number1);
                        numberCheckboxes--;
                    }
                    if (!checked2) {
                        number2 = randomStringFromSet(addStar);
                        number2EditText.setText(number2);
                        numberCheckboxes--;
                    }
                    if (!checked3) {
                        number3 = randomStringFromSet(addStar);
                        number3EditText.setText(number3);
                        numberCheckboxes--;
                    }
                    String toastText = "number1=" + number1 + " number2="+ number2 + " number3=" + number3;
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                    if (scor.equals(calculateScor(numberCheckboxes)))
                        break;
                    secondaryActivityIntent = new Intent(Constants.SECONDARY_ACTIVITY_ACTION);
                    secondaryActivityIntent.putExtra(Constants.EXTRA_DATA_NUMBER1, number1);
                    secondaryActivityIntent.putExtra(Constants.EXTRA_DATA_NUMBER2, number2);
                    secondaryActivityIntent.putExtra(Constants.EXTRA_DATA_NUMBER3, number3);
                    secondaryActivityIntent.putExtra(Constants.EXTRA_DATA_NUMBER_CHECKBOXES, String.valueOf(numberCheckboxes));
                    startActivityForResult(secondaryActivityIntent, Constants.ANOTHER_ACTIVITY_REQUEST_CODE);
                    break;
                case R.id.CHECK1:
                    checked1 = checkBox1.isChecked();
                    break;
                case R.id.CHECK2:
                    checked2 = checkBox2.isChecked();
                    break;
                case R.id.CHECK3:
                    checked3 = checkBox3.isChecked();
                    break;
                case R.id.NUMBER1:
                    number1 = number1EditText.getText().toString();
                    break;
                case R.id.NUMBER2:
                    number2 = number2EditText.getText().toString();
                    break;
                case R.id.NUMBER3:
                    number3 = number3EditText.getText().toString();
                    break;
            }
        }

        public String randomStringFromSet(boolean addStar) {
            Set<String> set = new HashSet<>(Arrays.asList("1", "2", "3"));
            if (addStar) {
                set.add("*");
            }
            List<String> list = new ArrayList<>(set);
            int size = list.size();
            int randIdx = new Random().nextInt(size);
            String randomElem = list.get(randIdx);
            return randomElem;
        }

        public String calculateScor(int numberCheckboxes) {
            if (numberCheckboxes == 0)
                return "100";
            else if (numberCheckboxes == 1)
                return "50";
            else if (numberCheckboxes == 2)
                return "10";
            else
                return "0";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.ANOTHER_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        int prev;
                        if (scor.equals("")){
                            prev = 0;
                        } else {
                            prev = Integer.parseInt(scor);
                        }
                        String newsc = data.getStringExtra("SCOR");
                        prev += Integer.parseInt(newsc);
                        scor = String.valueOf(prev);
                        if (prev > 0) {
                            //start service when scor > 0 : D.1.b
                            service = new Intent();
                            service.setComponent(new ComponentName(Constants.SERVICE_PACKAGE,
                                    Constants.SERVICE_CLASS));
                            service.putExtra(Constants.SERVICE_DATA, scor);
                            getApplicationContext().startService(service);
                        }
                    }
                    Toast.makeText(this, "Scor = " + scor, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Secondary activity finished with result code CANCEL", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Constants.SCOR_STATE, scor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getString(Constants.SCOR_STATE) != null) {
            //restore:
         //   scor = savedInstanceState.getString(Constants.SCOR_STATE);
        }
    }

    private class HandleBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
             if (intent!= null) {
                String data = intent.getStringExtra(Constants.SERVICE_DATA);
                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
            }
        }
    }
}