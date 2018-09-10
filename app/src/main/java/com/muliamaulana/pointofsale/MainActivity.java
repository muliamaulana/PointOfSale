package com.muliamaulana.pointofsale;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.muliamaulana.pointofsale.adapter.ItemListAdapter;
import com.muliamaulana.pointofsale.database.ItemHelper;
import com.muliamaulana.pointofsale.database.ItemModel;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private LinkedList<ItemModel> list;
    private ItemListAdapter adapter;
    private ItemHelper itemHelper;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private LinearLayout layout_empty;
    private Button buttonNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.progressBar);
        layout_empty = findViewById(R.id.layout_no_item);
        buttonNewItem = findViewById(R.id.first_add_item);

        itemHelper = new ItemHelper(this);
        itemHelper.open();
        list = new LinkedList<>();

        adapter = new ItemListAdapter(this);
        adapter.notifyDataSetChanged();
        adapter.setListItem(list);
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        new loadItem().execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list.clear();
        adapter.notifyDataSetChanged();
        new loadItem().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (itemHelper != null){
            itemHelper.close();
        }
    }


    private class loadItem extends AsyncTask<Void,Void,ArrayList<ItemModel>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            if (list.size()>0){
                list.clear();
            }
        }

        @Override
        protected ArrayList<ItemModel> doInBackground(Void... voids) {
            return itemHelper.read();
        }

        @Override
        protected void onPostExecute(ArrayList<ItemModel> items) {
            super.onPostExecute(items);
            progressBar.setVisibility(View.GONE);
            list.addAll(items);
            adapter.setListItem(list);
            adapter.notifyDataSetChanged();

            if (list.size() == 0){
                layout_empty.setVisibility(View.VISIBLE);
                fab.setVisibility(View.INVISIBLE);
                buttonNewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,AddActivity.class);
                        startActivity(intent);
                    }
                });

            } else {
                layout_empty.setVisibility(View.INVISIBLE);
            }
        }
    }
}
