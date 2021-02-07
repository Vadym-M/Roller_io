package com.vinade_app.rollerio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {


    private final LayoutInflater inflater;
    private static List<Section> sections;
    private OnSectionListener onSectionListener;
    Context context;

    public SectionAdapter(Context context, List<Section> sections, OnSectionListener mListener) {
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
        holder.imageButton.setImageResource(Integer.parseInt(section.getImg()));
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"it working + "+ sections.get(position) ,Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return sections.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageButton;
        OnSectionListener onSectionListener;
        public ViewHolder(@NonNull View itemView, OnSectionListener onSectionListener) {
            super(itemView);
            imageButton = (ImageView) itemView.findViewById(R.id.imageButton);
            this.onSectionListener= onSectionListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) { onSectionListener.onClick(getAdapterPosition());
        }
    }
    public interface  OnSectionListener
    {
        void onClick(int position);
    }
}
