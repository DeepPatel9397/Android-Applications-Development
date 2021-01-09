package com.example.assignment1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private RadioButton kilometertomiles;
    private RadioButton milestokilometers;
    private EditText UserInput;
    private TextView ConvertedOutput;
    private TextView UnitofInputValue;
    private TextView UnitofOutputValue;
    private TextView RecentConversions;
    private Button ConvertButton;
    private String string="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kilometertomiles = (RadioButton) findViewById(R.id.convertktom);
        milestokilometers = (RadioButton) findViewById(R.id.convertmtok);
        UserInput = (EditText) findViewById(R.id.Input);
        ConvertedOutput = (TextView) findViewById(R.id.Output);
        UnitofInputValue = (TextView) findViewById(R.id.UnitOfInput);
        UnitofOutputValue = (TextView) findViewById(R.id.UnitOfOutput);
        RecentConversions = (TextView) findViewById(R.id.ConversionHistoryView);
        ConvertButton = (Button) findViewById(R.id.ConvertButton);
        RecentConversions.setMovementMethod(new ScrollingMovementMethod());

    }
    @SuppressLint("SetTextI18n")
    public void selectUnit(View v)
    {
        switch (v.getId())
        {
            case R.id.convertktom:
                UnitofInputValue.setText("Kilometers Value:");
                UnitofOutputValue.setText("Miles Value:");
                break;

            case R.id.convertmtok:
                UnitofInputValue.setText("Miles Value:");
                UnitofOutputValue.setText("Kilometers Value:");
                break;
        }

    }

    public void onClickClear(View v)
    {
        string="";
        RecentConversions.setText(string);
    }

    @SuppressLint("DefaultLocale")
    public void DistanceConversion (View v)
    {
        if (UserInput.getText().length() == 0)
        {
            Toast.makeText(getApplicationContext(), "Please Enter Correct Distance", Toast.LENGTH_SHORT).show();
        }
        else{
            double DistanceInput = Double.parseDouble(UserInput.getText().toString());
            double Result;
            if (kilometertomiles.isChecked())
            {
                Result = (DistanceInput * 0.621371);
                ConvertedOutput.setText(String.format("%,.1f", Result));
                double tempinput = Double.parseDouble(UserInput.getText().toString());
                string =  (String.format("%,.1f", tempinput)) + "Km"+" ==> " + (String.format("%,.1f", Result))+"Mi" + "\n" + string;
               RecentConversions.setText(string);

            }

            if (milestokilometers.isChecked())
            {

                Result = (DistanceInput * 1.60934);
                ConvertedOutput.setText(String.format("%,.1f", Result));
                double tempinput =  Double.parseDouble(UserInput.getText().toString());
                string =  (String.format("%,.1f", tempinput))+ "Mi" + " ==> " + (String.format("%,.1f", Result)) + "Km"+ "\n" + string;
                RecentConversions.setText(string);
            }
        }
    }
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("RecentConversions", string);
        outState.putString("UnitofInputValue", UnitofInputValue.getText().toString());
        outState.putString("UnitofOutputValue", UnitofOutputValue.getText().toString());
        outState.putString("inputValue", UserInput.getText().toString());
        outState.putString("outputValue", ConvertedOutput.getText().toString());
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        string = savedInstanceState.getString("RecentConversions");
        RecentConversions.setText(string);
        UserInput.setText(savedInstanceState.getString("inputValue"));
        ConvertedOutput.setText(savedInstanceState.getString("outputValue"));
        UnitofInputValue.setText(savedInstanceState.getString("UnitofInputValue"));
        UnitofOutputValue.setText(savedInstanceState.getString("UnitofOutputValue"));
        if(savedInstanceState.getString("UnitofInputValue").toString().equalsIgnoreCase("Kilometers Value:"))
        {
            kilometertomiles.setChecked(true);
            milestokilometers.setChecked(false);
        }
        else
        {
            kilometertomiles.setChecked(false);
            milestokilometers.setChecked(true);
        }
    }
}