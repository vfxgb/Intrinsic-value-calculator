package edu.harvard.cs50.intrinsic_value;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class Intrinsic_Value_Adapter extends RecyclerView.Adapter<Intrinsic_Value_Adapter.Intrinsic_ViewHolder> {
    public static class Intrinsic_ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView nametextView;

        public Intrinsic_ViewHolder(View view) {
            super(view);
            this.containerView = view.findViewById(R.id.intrinsic_row);

            this.nametextView = view.findViewById(R.id.intrinsic_row_name);


            this.containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    versions_intrinsic current = (versions_intrinsic) containerView.getTag();
                    if (current.getName() == "Intrinsic Value Calculator v1") {
                        Intent intent = new Intent(v.getContext(), IntrinsicActivity.class);

                        intent.putExtra("Name", current.getName());
                        intent.putExtra("Number", current.getNumber());


                        context.startActivity(intent);
                    } else if (current.getName() == "Intrinsic Value Calculator v2") {
                        Intent intent1 = new Intent(v.getContext(), IntrinsicActivity2.class);
                        intent1.putExtra("Name", current.getName());
                        intent1.putExtra("Number", current.getNumber());

                        context.startActivity(intent1);
                    }


                }
            });

        }

    }



    private List<versions_intrinsic> intrinsic = Arrays.asList(
            new versions_intrinsic("Intrinsic Value Calculator v1", 1),
            new versions_intrinsic("Intrinsic Value Calculator v2", 2)
    );

    @NonNull
    @Override
    public Intrinsic_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.intrinsic_row, parent, false);

        return new Intrinsic_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Intrinsic_ViewHolder holder, int position) {
        versions_intrinsic current = intrinsic.get(position);
        holder.nametextView.setText(current.getName());
        holder.containerView.setTag(current);


    }

    @Override
    public int getItemCount() {
        return intrinsic.size();
    }

}
