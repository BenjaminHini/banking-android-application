package se.mau.devergne.a1_benjamin;

import android.app.AlertDialog;
import android.database.MatrixCursor;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String username;
    private String mParam2;
    private List<Money> transaction, show;
    Spinner mySpinner;
    ImageView pic;
    TextView date_start, date_end;
    Button validate_btn;

    public ViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFragment newInstance(String param1) {
        ViewFragment fragment = new ViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transaction = new ArrayList<>();
        Money money = new Money(getActivity());
        transaction = money.getTransactionFromDB(username);
        Log.i("test", transaction.toString());
        float yrd = 0;
        for(int i=0; i<transaction.size(); ++i)
            yrd += transaction.get(i).getAmount();
        TextView txt = (TextView)getActivity().findViewById(R.id.solde2);
        txt.setText("Balance : "+yrd);
        mySpinner = (Spinner)getView().findViewById(R.id.viewSpinner2);
        String[] items = {"See every transactions", "See incomes", "See outcomes", "Between two dates"};
        mySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items));
        if(transaction != null)
            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    show = orderTable(position);
                    showTable(show);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

        ListView lv = (ListView)getActivity().findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder toast = new AlertDialog.Builder(getActivity());
                toast.setMessage(show.get(position).toString());
                toast.show();
            }
        });
    }

    public ArrayList<Money> orderTable(int spinSelec){
        date_start = (TextView)getActivity().findViewById(R.id.editTextDate);
        date_end = (TextView)getActivity().findViewById(R.id.editTextDate2);
        date_start.setVisibility(View.INVISIBLE);
        date_end.setVisibility(View.INVISIBLE);
        validate_btn = (Button) getActivity().findViewById(R.id.button);
        validate_btn.setVisibility(View.INVISIBLE);
        ArrayList<Money> index = new ArrayList<>();
        if(spinSelec == 0) {
            for (int j = 0; j < transaction.size(); ++j)
                index.add(transaction.get(j));
        }
        else if(spinSelec == 1) {
            for (int j = 0; j < transaction.size(); ++j)
                if (transaction.get(j).getAmount() > 0)
                    index.add(transaction.get(j));
        }
        else if(spinSelec == 2){
            for (int j = 0; j < transaction.size(); ++j)
                if (transaction.get(j).getAmount() < 0)
                    index.add(transaction.get(j));
        }
        else if(spinSelec == 3){
                date_start = (TextView)getActivity().findViewById(R.id.editTextDate);
                date_end = (TextView)getActivity().findViewById(R.id.editTextDate2);
                SimpleDateFormat dateform_start = new SimpleDateFormat("dd/MM/yyyy");
                date_start.setVisibility(View.VISIBLE);
                date_end.setVisibility(View.VISIBLE);
                validate_btn.setVisibility(View.VISIBLE);
                validate_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(date_start.getText().length() == 10 && date_end.getText().length() == 10)
                        {
                            Log.i("cur",date_start.toString());
                            for (int j = 0; j < transaction.size(); ++j) {
                                try {
                                    System.out.println("ici");
                                    Date one = dateform_start.parse(date_start.getText().toString());
                                    Date two = dateform_start.parse(date_end.getText().toString());
                                    Date three = dateform_start.parse(transaction.get(j).getDate());

                                    if (three.after(one) && three.before(two)) {
                                        Log.i("azertyuiop", "azertyuiop");
                                        index.add(transaction.get(j));
                                    }
                                } catch (ParseException e) {
                                    Log.e("rentre pas dans for",e.toString());
                                }
                            }
                            showTable(index);
                        }
                    }
                });
        }
        return  index;
    }
    public void showTable(List<Money> index){
        pic = (ImageView)getActivity().findViewById(R.id.pic_image);
        String[] columns = new String[] { "_id", "col1", "col2", "pic", "col3", "col4" };
        MatrixCursor matrixCursor= new MatrixCursor(columns);
        getActivity().startManagingCursor(matrixCursor);
        for(int i = 0; i<index.size(); ++i) {
            if (index.get(i).getCategory().toString() == "Food")
                matrixCursor.addRow(new Object[]{i, index.get(i).getDate(), index.get(i).getTitle(), R.drawable.food_benjamin, index.get(i).getCategory().toString(), index.get(i).getAmount() + " €"});
            if (index.get(i).getCategory().toString() == "Leisure")
                matrixCursor.addRow(new Object[]{i, index.get(i).getDate(), index.get(i).getTitle(), R.drawable.vetement_benjamin, index.get(i).getCategory().toString(), index.get(i).getAmount() + " €"});
            if(index.get(i).getCategory().toString() == "Accommodation")
                matrixCursor.addRow(new Object[] { i,index.get(i).getDate(),index.get(i).getTitle(), R.drawable.logement_benjamin, index.get(i).getCategory().toString(), index.get(i).getAmount() + " €"});
            if(index.get(i).getCategory().toString() == "Other")
                matrixCursor.addRow(new Object[] { i,index.get(i).getDate(),index.get(i).getTitle(), R.drawable.other_benjamin, index.get(i).getCategory().toString(), index.get(i).getAmount() + " €"});
            if(index.get(i).getCategory().toString() == "Salary")
                matrixCursor.addRow(new Object[] { i,index.get(i).getDate(),index.get(i).getTitle(), R.drawable.salary_benjamin, index.get(i).getCategory().toString(), index.get(i).getAmount() + " €"});
            if(index.get(i).getCategory().toString() == "Travel")
                matrixCursor.addRow(new Object[] { i,index.get(i).getDate(),index.get(i).getTitle(), R.drawable.travel_benjamin, index.get(i).getCategory().toString(), index.get(i).getAmount() + " €"});
        }
        String[] from = new String[] {"col1", "col2", "pic","col3", "col4"};
        int[] to = new int[] { R.id.textViewCol, R.id.textViewCol2, R.id.pic_image, R.id.textViewCol3, R.id.textViewCol4};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.table, matrixCursor, from, to, 0);
        ListView lv = (ListView) getView().findViewById(R.id.lv);
        lv.setAdapter(adapter);
    }
}