package com.example.android.doctorsystem;
import android.app.Activity;
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

        Word currentWord = getItem(position);
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentWord.getmName());
        TextView feeTextView = (TextView) listItemView.findViewById(R.id.fee_text_view);
        feeTextView.setText(currentWord.getmFee());
        TextView addressTextView = (TextView) listItemView.findViewById(R.id.address_text_view);
        addressTextView.setText(currentWord.getmAddress());
        TextView distanceTextView = (TextView) listItemView.findViewById(R.id.distance_text_view);
        distanceTextView.setText(Double.toString(currentWord.getmDistance()));
        return listItemView;
    }
}

