package com.example.android.doctorsystem;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SupportActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    static final ArrayList<Word> words = new ArrayList<Word>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        fStore = FirebaseFirestore.getInstance();


        Intent intent = getIntent();
        HashMap<String, Double> hmap = (HashMap<String, Double>) intent.getSerializableExtra("map");
        Map<String, Double> distanceData = sortByValue(hmap);
        Log.v("Map", ""+distanceData);


        for (Map.Entry mapElement : distanceData.entrySet()) {
            String key = (String) mapElement.getKey();
            double distance = ((double) mapElement.getValue());
            temp1(key, distance);

            //String[] arr1 = new String[3];

            //arr1 = temp1(key, );

            //words.add(new Word(arr1[0], arr1[2], arr1[1], distance));

            Log.v("word1", ""+words);

        }
        Log.v("word", ""+words);

        //WordAdapter adapter = new WordAdapter(this, words);
       // ListView listView = (ListView) findViewById(R.id.list);
        //listView.setAdapter(adapter);
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu1:
                FirebaseAuth.getInstance().signOut();//logout
                Log.v("HELLO I am","hey bro");
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list =
                new LinkedList<Map.Entry<String, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double > aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public void temp1(String s, double dis){
        String[] arr = new String[3];
        DocumentReference documentReference = fStore.collection("users").document(s);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {



            assert documentSnapshot != null;
            if (documentSnapshot.exists()) {

                arr[0]= documentSnapshot.getString("fName");
                arr[1]= documentSnapshot.getString("address");
                arr[2]= documentSnapshot.getString("fee");
                words.add(new Word(arr[0], arr[2], arr[1], dis));

                if(words.size()==2){
                    WordAdapter adapter = new WordAdapter(this, words);
                    ListView listView = (ListView) findViewById(R.id.list);
                    listView.setAdapter(adapter);

                    
                }



            } else {
                Log.v("tag", "onEvent: Document do not exists");
            }
            

        });

    }

}
