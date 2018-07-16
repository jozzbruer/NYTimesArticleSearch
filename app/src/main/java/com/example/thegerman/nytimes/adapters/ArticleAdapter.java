package com.example.thegerman.nytimes.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thegerman.nytimes.R;
import com.example.thegerman.nytimes.model.Article;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context,android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article article = this.getItem(position);
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.article_model,parent,false);

        }
        ImageView  imageView = convertView.findViewById(R.id.imagView);
        TextView textView = convertView.findViewById(R.id.txtView);
        TextView textView1 = convertView.findViewById(R.id.txtViewS);
        imageView.setImageResource(0);

        textView.setText(article.getHeadline()); textView1.setText(article.getSnippet());
        String Thub = article.getThumbnail();
        if (!TextUtils.isEmpty(Thub)){
            Glide.with(getContext())
                    .load(Uri.parse(Thub))
                    .into(imageView);
        }
        return convertView;
    }
}
