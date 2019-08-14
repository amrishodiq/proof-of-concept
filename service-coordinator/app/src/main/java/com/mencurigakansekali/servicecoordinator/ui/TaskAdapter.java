package com.mencurigakansekali.servicecoordinator.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mencurigakansekali.servicecoordinator.R;
import com.mencurigakansekali.servicecoordinator.model.TaskModel;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.VH> {
    public interface ItemClickListener {
        void onItemClick(TaskModel clicked);
    }

    public static class VH extends RecyclerView.ViewHolder {
        private TextView name, serviceClass;
        private TaskModel model;
        private ItemClickListener listener;
        public VH(View root) {
            super(root);
            initView(root);
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        private void initView(View root) {
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model != null && listener != null) {
                        listener.onItemClick(model);
                    }
                }
            });

            name = root.findViewById(R.id.name);
            serviceClass = root.findViewById(R.id.service_class);
            refresh();
        }
        public void setModel(TaskModel model) {
            this.model = model;
            refresh();
        }
        private void refresh() {
            if (
                    model != null &&
                    name != null && serviceClass != null
            ) {
                name.setText(model.getName());
                serviceClass.setText(model.getClassName());
            }
        }
    }

    private List<TaskModel> items;
    private ItemClickListener listener;

    public TaskAdapter(List<TaskModel> items) {
        this.items = items;
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_task, viewGroup, false);
        VH vh = new VH(root);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int position) {
        TaskModel item = items.get(position);
        vh.setModel(item);
        vh.setListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
