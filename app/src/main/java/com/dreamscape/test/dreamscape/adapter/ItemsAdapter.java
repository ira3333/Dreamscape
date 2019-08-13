package com.dreamscape.test.dreamscape.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import com.dreamscape.test.dreamscape.R;
import com.dreamscape.test.dreamscape.model.Items;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private Context context;
        private List<Items> list;

        public ItemsAdapter(Context context, List<Items> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Items item = list.get(position);

            holder.textTitle.setText(item.getTitle());
            holder.textDescription.setText(item.getDescription());
            holder.textPublished.setText(item.getPublished().substring(0,item.getPublished().indexOf("T")));
            holder.textTags.setText(item.getAuthor());
            holder.textAuthor.setText(item.getAuthor());
            holder.textDateTaken.setText(item.getDate_taken().substring(0,item.getDate_taken().indexOf("T")));
            Picasso.with(context).load(item.getMedia()).into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String image_url = list.get(position).getMedia();
                    showImage(image_url);
                    Toast.makeText(context ,list.get(position).getMedia(),Toast.LENGTH_LONG);

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textDescription, textPublished, textTags, textAuthor, textDateTaken;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.title);
            textDescription = itemView.findViewById(R.id.description);
            textPublished = itemView.findViewById(R.id.published);
            textTags = itemView.findViewById(R.id.tags);
            textAuthor = itemView.findViewById(R.id.author);
            textDateTaken = itemView.findViewById(R.id.date_taken);

            image = itemView.findViewById(R.id.image);
        }
    }

    public void showImage(String imageUrl) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(context);

        Picasso.with(context).load(imageUrl).resize(0, 1000).into(imageView);

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}
