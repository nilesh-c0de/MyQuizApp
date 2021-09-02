package com.example.myquiz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myquiz.mqUtils.StoreResult;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    //private ArrayList<ExampleItem> mExampleList;
    private ArrayList<StoreResult> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.textView24);
            mTextView2 = itemView.findViewById(R.id.textView11);
            mTextView3 = itemView.findViewById(R.id.textView22);
            mTextView4 = itemView.findViewById(R.id.textView23);
            mTextView5 = itemView.findViewById(R.id.textView27);
        }
    }

    /*public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }*/
    public ExampleAdapter(ArrayList<StoreResult> exampleList) {
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_item, viewGroup, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {

        //ExampleItem currentItem = mExampleList.get(i);
        StoreResult currentItem = mExampleList.get(i);

      /*  exampleViewHolder.mTextView1.setText(currentItem.getScore());
        exampleViewHolder.mTextView2.setText(currentItem.getSubject());
        exampleViewHolder.mTextView3.setText(currentItem.getDate());
        exampleViewHolder.mTextView4.setText(currentItem.getTime());*/
        exampleViewHolder.mTextView1.setText(currentItem.getHighscore());
        exampleViewHolder.mTextView2.setText(currentItem.getSubjectname());
        exampleViewHolder.mTextView3.setText(currentItem.getCurrentdate());
        exampleViewHolder.mTextView4.setText(currentItem.getCurrenttime());
        exampleViewHolder.mTextView5.setText(currentItem.getMarks());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
