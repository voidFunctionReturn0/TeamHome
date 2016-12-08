package com.example.myapplication.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.Teams;
import com.example.myapplication.network.Network2;

import java.util.List;

/**
 * Created by Administrator on 2016-06-23.
 */

public class ItemViewAdapter extends BaseAdapter{
    private List<Teams> list;

    public Context context;
    public ItemViewAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Teams> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //itemView를 새로 만들어야 될 경우
        if(convertView == null){
            //inflation
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.team_grid_item_view, parent, false);
        }
        //data setting
        ImageView imageView;
        TextView txtTname;
        TextView txtTdescr;
        imageView = (ImageView) convertView.findViewById(R.id.team_grid_imageView);
        txtTname = (TextView) convertView.findViewById(R.id.team_grid_item_txtTname);
        txtTdescr = (TextView) convertView.findViewById(R.id.team_grid_item_txtTdescr);
        CircleBitmap make = new CircleBitmap();
        int width = 120;
        if(list.size() > 0) {
            Teams team = list.get(position);

            txtTname.setText(team.getTname());
            txtTdescr.setText(team.getTdescr());
            /*if (team.getTid() == 0 ) {
                Bitmap bitmap =  BitmapFactory.decodeResource( imageView.getResources(),R.drawable.btn_add_team);
                bitmap = make.getCircleBitmap(bitmap, width);

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(bitmap);
                imageView.setAdjustViewBounds(true);
            } else {
                if(team.getTprofile().length() < 1){
                    Bitmap bitmap =  BitmapFactory.decodeResource( imageView.getResources(),R.drawable.base_team);
                    bitmap = make.getCircleBitmap(bitmap, width);

                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(bitmap);
                    imageView.setAdjustViewBounds(true);
                }else {
                    Network2.getBitmap(team.getTprofile(), imageView);
                }
            }*/
            if (team.getTid() == 0 ) {
                Bitmap bitmap =  BitmapFactory.decodeResource( imageView.getResources(),R.drawable.btn_add_team);
                bitmap = make.getCircleBitmap(bitmap, width);

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(bitmap);
               // imageView.setAdjustViewBounds(true);
            } else {
                Network2.getBitmap(team.getTprofile(), imageView);
            }
            //itemView return
        }
        return convertView;
    }

}
