package se.mau.devergne.a1_benjamin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Money money;



    private  String username;
    private boolean choice;
    Button validate_btn;
    Spinner mySpinner;
    TextView date, title, amount;

    public IncomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1
     * @param param2 Parameter 2
     * @return A new instance of fragment IncomeFragment.
     */
    public static IncomeFragment newInstance(boolean param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            choice = getArguments().getBoolean(ARG_PARAM1);
        username = getArguments().getString(ARG_PARAM2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_income, container, false);
        date = (TextView)v.findViewById(R.id.date_txt2);
        title = (TextView)v.findViewById(R.id.title_txt2);
        amount = (TextView)v.findViewById(R.id.amount_txt2);
        validate_btn = (Button) v.findViewById(R.id.validate_btn2);
        validate_btn.setOnClickListener(v1 -> {
            try {
                float price = Float.parseFloat(amount.getText().toString());
                Enum cate;
                if (choice)
                    cate = (IncomeCategory) mySpinner.getSelectedItem();
                else {
                    cate = (OutcomeCategory) mySpinner.getSelectedItem();
                    price = -price;
                }
                money = new Money(cate,price , title.getText().toString(), date.getText().toString(),username, getContext());
                Log.i("newly created", money.toString());
                if(!money.saveCurrToDB())
                    throw  new  Exception("couldn't connect to DataBase");
            }
            catch (Exception e){
                Log.e("Error", e.toString());
            }
            finally {
                updateGUI();
            }
        });
        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mySpinner = (Spinner) getView().findViewById(R.id.category_spinner2);
        if(choice)
            mySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, IncomeCategory.values()));
        else
            mySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, OutcomeCategory.values()));
    }

    public void updateGUI(){
        mySpinner.setSelection(0);
        date.setText("");
        title.setText("");
        amount.setText("");
    }
}