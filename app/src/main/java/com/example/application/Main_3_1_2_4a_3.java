package com.example.application;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class Main_3_1_2_4a_3 extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data, date_view, day_view,count_view;
    Button chart_view;
    MyDBHelper myDB;
    ArrayList<String> book_id, book_date, book_money, book_caption, book_spinner1, book_spinner2, book_note;
    CustomAdapter customAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_1_2_4a_3);
        recyclerView = findViewById(R.id.recyclerView);
        date_view = findViewById(R.id.date_view);
        day_view = findViewById(R.id.day_view);
        chart_view=findViewById(R.id.chart_view);
        count_view=findViewById(R.id.count_view);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_3_1_2_4a_3.this, Main_2.class);
                startActivity(intent);
            }
        });
        chart_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_3_1_2_4a_3.this, Main_3_2_2.class);
                startActivity(intent);
            }
        });
        myDB = new MyDBHelper(Main_3_1_2_4a_3.this);
        book_id = new ArrayList<>();
        book_date = new ArrayList<>();
        book_money = new ArrayList<>();
        book_caption = new ArrayList<>();
        book_spinner1 = new ArrayList<>();
        book_spinner2 = new ArrayList<>();
        book_note = new ArrayList<>();
        storeDataInArrays();
        querySettlement();
        queryBalance();
        customAdapter = new CustomAdapter(Main_3_1_2_4a_3.this, this, book_id, book_date, book_money, book_caption, book_spinner1, book_spinner2, book_note);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Main_3_1_2_4a_3.this));
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = this.queryData();
        if (cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_date.add(cursor.getString(1));
                book_money.add(cursor.getString(2));
                book_caption.add(cursor.getString(3));
                book_spinner1.add(cursor.getString(4));
                book_spinner2.add(cursor.getString(5));
                book_note.add(cursor.getString(6));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    Cursor queryData() {
        Intent myIntent = getIntent();
        Bundle bundle = myIntent.getExtras();
        String OneDay = bundle.getString("OneDay");
        String date = "\uD83D\uDDD3" + " " + OneDay;
        date_view.setText(date);
        String query = "SELECT * FROM library WHERE 日期 =  '" + OneDay + "'";
        Log.d("query",query);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
//    本日結算
    void querySettlement() {
        int num=0;
        String string="";
        Intent myIntent = getIntent();
        Bundle bundle = myIntent.getExtras();
        String OneDay = bundle.getString("OneDay");
        String income = "SELECT * FROM library WHERE (日期 = '" + OneDay + "') AND (狀態 = '收入' )";
        String expense = "SELECT *FROM library WHERE (日期 = '" + OneDay + "') AND (狀態 = '支出' )";
        Log.d("query1",income);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = null;
        Cursor cursor1 = null;
        if (db != null) {
            cursor = db.rawQuery(income, null);
            cursor1 = db.rawQuery(expense, null);
        }
        if (cursor.getCount() == 0) {
            day_view.setText("本日結算:0");
        } else {
            while (cursor.moveToNext()) {
                num+=(cursor.getInt(2));
            }
        }
        if (cursor1.getCount() == 0) {
            day_view.setText("本日結算:0");
        } else {
            while (cursor1.moveToNext()) {
                num-=(cursor1.getInt(2));
            }

        }
        string+="本日結算 "+num;
        Log.d("string",string);
        day_view.setText(string);
    }
//累計餘絀
    void queryBalance() {
        int num=0;
        String string="";
        Intent myIntent = getIntent();
        Bundle bundle = myIntent.getExtras();
        String OneDay = bundle.getString("OneDay").substring(0,7);
        Log.d("YearMonth",OneDay);
        String income = "SELECT * FROM library WHERE (日期 LIKE '"+OneDay+"%') AND (狀態 = '收入' )";
        String expense = "SELECT *FROM library WHERE (日期 LIKE '"+OneDay+"%') AND (狀態 = '支出' )";
        Log.d("query1",income);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = null;
        Cursor cursor1 = null;
        if (db != null) {
            cursor = db.rawQuery(income, null);
            cursor1 = db.rawQuery(expense, null);
        }
        if (cursor.getCount() == 0) {
            count_view.setText("累計餘絀:0");
        } else {
            while (cursor.moveToNext()) {
                num+=(cursor.getInt(2));
            }
        }
        if (cursor1.getCount() == 0) {
            count_view.setText("累計餘絀:0");
        } else {
            while (cursor1.moveToNext()) {
                num-=(cursor1.getInt(2));
            }

        }
        string+="累計餘絀 "+num;
        Log.d("string",string);
        count_view.setText(string);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDBHelper myDB = new MyDBHelper(Main_3_1_2_4a_3.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(Main_3_1_2_4a_3.this, Main_3_1_2_4a_3.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}


