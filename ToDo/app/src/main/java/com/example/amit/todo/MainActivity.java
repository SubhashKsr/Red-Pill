package com.example.amit.todo;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity {
    ArrayList<String> itemslist;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemslist  = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemslist);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        try {
            Scanner scanner = new Scanner(openFileInput("storedData"));
            while(scanner.hasNextLine()) {
                itemslist.add(scanner.nextLine());
            }
            scanner.close();
            adapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            System.out.println("file not found");
        }
        registerForContextMenu(listView);
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            PrintWriter pw = new PrintWriter(openFileOutput("storedData", Context.MODE_PRIVATE));
            for(String task : itemslist) {
                pw.println(task);
            }
            pw.close();
        }
        catch (Exception e) {
            Log.d("onStop", e.getMessage());
        }
    }
    public void additem(View v) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String userInput = editText.getText().toString();
        itemslist.add(userInput);
        adapter.notifyDataSetChanged();
        editText.setText("");
        //editText.clearFocus();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("choose action");
        menu.add("Delete task");
        menu.add("Cancel");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getTitle() == "Delete task"){
            itemslist.remove(info.position);
            adapter.notifyDataSetChanged();
        }
        return true;
    }
}
