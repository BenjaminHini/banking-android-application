package se.mau.devergne.a1_benjamin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button income, outcome, see;
    EditText fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fname = (EditText) findViewById(R.id.editFirstName);
        fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                income.setEnabled(s.toString().length()!=0);
                outcome.setEnabled(s.toString().length()!=0);
                see.setEnabled(s.toString().length()!=0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        income = (Button)findViewById(R.id.income);
        income.setOnClickListener(v -> {
            IncomeFragment frag = IncomeFragment.newInstance(true, fname.getText().toString());
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
        });
        outcome = (Button)findViewById(R.id.expenditure);
        outcome.setOnClickListener(v -> {
            IncomeFragment frag = IncomeFragment.newInstance(false, fname.getText().toString());
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
        });

        see = (Button) findViewById(R.id.view_btn);
        see.setOnClickListener(v -> {
            ViewFragment frag = ViewFragment.newInstance(fname.getText().toString());
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
        });
    }
}