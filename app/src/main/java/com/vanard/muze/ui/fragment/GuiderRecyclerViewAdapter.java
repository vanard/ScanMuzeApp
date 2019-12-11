package com.vanard.muze.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vanard.muze.R;
import com.vanard.muze.model.DataGuider;

import java.util.List;

public class GuiderRecyclerViewAdapter extends RecyclerView.Adapter<GuiderRecyclerViewAdapter.ViewHolder> {

    private final List<DataGuider> mValues;
    private Context context;

    public GuiderRecyclerViewAdapter(List<DataGuider> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guider, parent, false);
        return new ViewHolder(view);
    }

    public void addItem(DataGuider dataGuider) {
        mValues.add(dataGuider);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getName());

        holder.mWaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickWa(position);
            }
        });
    }

    private void onClickWa(int position) {
        try {
            String text = "Hello " + mValues.get(position).getName() + ".\nAre you available at " +
                    mValues.get(position).getMuseum() + "?";

            String toNumber = mValues.get(position).getPhoneNumber(); // Replace with mobile phone number without +Sign or leading zeros, but with country code
            toNumber = toNumber.substring(1);
            toNumber = "62"+toNumber;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            context.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    private void onClickWa(int position) {
//        PackageManager pm = context.getPackageManager();
//        try {
//
//            Intent waIntent = new Intent(Intent.ACTION_SEND);
//            waIntent.setType("text/plain");
//            String text = "Hello " + mValues.get(position).getName() + ". \n Are you available at " +
//                    mValues.get(position).getMuseum() + "?";
//
//            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//            waIntent.setPackage("com.whatsapp");
//
//            waIntent.putExtra(Intent.EXTRA_TEXT, text);
//            context.startActivity(Intent.createChooser(waIntent, "Share with"));
//
//        } catch (PackageManager.NameNotFoundException e) {
//            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
//                    .show();
//        }
//    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mWaView;
        public DataGuider mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mWaView = view.findViewById(R.id.icon_wa);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
