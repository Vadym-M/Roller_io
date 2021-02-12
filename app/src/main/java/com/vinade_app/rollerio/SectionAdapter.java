package com.vinade_app.rollerio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {


    private final LayoutInflater inflater;
    private static ArrayList<Section> sections;
    private OnSectionListener onSectionListener;
    Context context;

    public SectionAdapter(Context context, ArrayList<Section> sections, OnSectionListener mListener) {
        this.onSectionListener = mListener;
        this.inflater = LayoutInflater.from(context);
        this.sections = sections;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.section_view, parent, false);
        return new ViewHolder(view, onSectionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionAdapter.ViewHolder holder, int position) {
        Section section = sections.get(position);
        holder.sectionName.setText(sections.get(position).getNameSection());
        holder.imageButton.setImageResource(Integer.parseInt(section.getImg()));

    }



    @Override
    public int getItemCount() {
        return sections.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageButton;
        TextView sectionName;
        OnSectionListener onSectionListener;
        public ViewHolder(@NonNull View itemView, OnSectionListener onSectionListener) {
            super(itemView);
            imageButton = (ImageView) itemView.findViewById(R.id.imageButton);
            sectionName = itemView.findViewById(R.id.sectionName);
            this.onSectionListener= onSectionListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) { onSectionListener.onClick(sections.get(getAdapterPosition()).getNameSection());
        }
    }
    public interface  OnSectionListener
    {
        void onClick(String position);
    }
}
