package com.example.android.doctorsystem;

import android.app.Activity;
//import android.support.v4.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {


    public WordAdapter(Activity context, ArrayList<Word> words){

        super(context, 0, words);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Word currentWord = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name_text_view);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(currentWord.getmName());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView feeTextView = (TextView) listItemView.findViewById(R.id.fee_text_view);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        feeTextView.setText(currentWord.getmFee());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView addressTextView = (TextView) listItemView.findViewById(R.id.address_text_view);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        addressTextView.setText(currentWord.getmAddress());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView distanceTextView = (TextView) listItemView.findViewById(R.id.distance_text_view);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        distanceTextView.setText(Double.toString(currentWord.getmDistance()));



        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}

