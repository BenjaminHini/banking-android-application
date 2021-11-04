package se.mau.devergne.a1_benjamin;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class Money implements Parcelable {
    private Enum category;
    private float amount;
    private String title, date;
    private static  String username;
    static Context context;

    public Money(Context context) {
        this.context = context;
    }

    public Money(Parcel a){
        super();
    }

    public Money(Enum category, float amount, String title, String date, String username,Context context) {
        this.category = category;
        this.amount = amount;
        this.title = title;
        this.date = date;
        this.username = username;
        this.context = context;
    }

    public static final Creator<Money> CREATOR = new Creator<Money>() {
        @Override
        public Money createFromParcel(Parcel in) {
            return new Money(in);
        }

        @Override
        public Money[] newArray(int size) {
            return new Money[size];
        }
    };

    public Enum getCategory() {
        return category;
    }

    public void setCategory(Enum category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return date + " : "+title +"\t"+category.toString()+"\t"+amount+"\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(amount);
        dest.writeString(title);
        dest.writeString(date);
    }
    //TODO
    public static ArrayList<Money> getTransactionFromDB(String username){

        DataAccesObject dao = new DataAccesObject(context);
        Cursor res = dao.getData(username);
        if(res.getCount() == 0)
            return  new ArrayList<>();
        Log.i("res not empty", "in getTransactionFromDB");
        ArrayList<Money> transac = new ArrayList<>();
        Enum category;
        while (res.moveToNext()){
            String user = res.getString(0);
            String date = res.getString(1);
            String Cat = res.getString(2);
            String title = res.getString(3);
            float price = Float.parseFloat(res.getString(4));
            if(price<0)
                category = OutcomeCategory.valueOf(Cat);
            else
                category = IncomeCategory.valueOf(Cat);
            transac.add(new Money(category, price, title,date, user, context ));
        }
        dao.close();
        return  transac;
    }
    //TODO
    public boolean saveCurrToDB(){
        DataAccesObject dao = new DataAccesObject(context);
        boolean result = dao.insert(title, date, category.toString(), username, amount);
        dao.close();
        return  result;
    }
}