package com.example.kausthubram.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>{

    View.OnClickListener clickListener;
    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private List<Project> projectList;
    private Context mContext;

    public ProjectAdapter(List<Project> projectList,Context context) {
        this.projectList = projectList;
        this.mContext = context;

    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_view_test, parent, false);
        //itemView.setOnClickListener(mOnClickListener);
        return new ProjectViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        holder.title.setText(projectList.get(position).getName());
        holder.skills.setText(projectList.get(position).getType());
        holder.short_desc.setText(projectList.get(position).getType());

        holder.title.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView skills;
        public TextView short_desc;

        public ProjectViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleView);
            skills = (TextView) view.findViewById(R.id.skillsView);
            short_desc = (TextView) view.findViewById(R.id.short_desc_view);
            view.setOnClickListener(this);
        }

        public void onClick(View v){
            //Log.d("WHAT", "onClick " + getLayoutPosition() + " ");
            Project clicked = projectList.get(getLayoutPosition());

            if(mContext instanceof ViewProjects){
                ((ViewProjects) mContext).changePage(clicked);
            }
        }
    }
}