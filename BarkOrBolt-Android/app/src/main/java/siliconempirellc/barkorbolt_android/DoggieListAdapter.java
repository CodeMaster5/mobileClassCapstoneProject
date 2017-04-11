package siliconempirellc.barkorbolt_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import siliconempirellc.barkorbolt_android.Models.dogObject;


/**
 * Created by khuramchaudhry on 4/10/17.
 * This is a RecyclerView Adapter. Creates a list of views from a given data.
 */

public class DoggieListAdapter extends RecyclerView.Adapter<DoggieListAdapter.ItemRowHolder> {

    private Context mContext;
    private ArrayList<dogObject> doggieList;


    public DoggieListAdapter(Context context, ArrayList<dogObject> dL) {
        this.mContext = context;
        this.doggieList = dL;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog, parent,
                false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(DoggieListAdapter.ItemRowHolder holder, int position) {
        Bitmap bitmap =((BitmapDrawable) doggieList.get(position).getDogImage()).getBitmap();
        int dimension = getSquareCropDimensionForBitmap(bitmap);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);

        holder.itemImage.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));
        holder.itemName.setText(doggieList.get(position).getName());
        holder.itemEmail.setText(doggieList.get(position).getEmail());
    }

    @Override
    public int getItemCount()  {
        return (doggieList != null ? doggieList.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        private ImageView itemImage;
        private TextView itemName;
        private TextView itemEmail;

        ItemRowHolder(View view) {
            super(view);
            if(doggieList != null && doggieList.size() != 0) {
                this.itemImage = (ImageView) view.findViewById(R.id.dogImage);
                this.itemName = (TextView) view.findViewById(R.id.dogName);
                this.itemEmail = (TextView) view.findViewById(R.id.dogEmail);
            }
        }
    }

    public int getSquareCropDimensionForBitmap(Bitmap bitmap)
    {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }
}