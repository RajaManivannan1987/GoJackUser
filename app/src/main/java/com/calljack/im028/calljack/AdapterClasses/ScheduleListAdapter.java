package com.calljack.im028.calljack.AdapterClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.calljack.im028.calljack.ActivityClasses.EditScheduleActivity;
import com.calljack.im028.calljack.ModelClasses.ScheduleList;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Im033 on 1/12/2017.
 */

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.Customholder> {
    ArrayList<ScheduleList> list;
    Activity context;
    private LayoutInflater inflater;
    private final int scheduleRequestCode = 2;

    public ScheduleListAdapter(Activity scheduleTripListActivity, ArrayList<ScheduleList> list) {
        this.list = list;
        this.context = scheduleTripListActivity;
        inflater = context.getLayoutInflater();
    }

    @Override
    public Customholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Customholder(inflater.inflate(R.layout.item_list_schedule_trip_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(Customholder holder, final int position) {
        holder.dateTimeTextView.setText(list.get(position).getDatetime());
        holder.fromTextView.setText(list.get(position).getStartingaddress());
        holder.fromTextView.setSelected(true);
        holder.toTextView.setText(list.get(position).getEndingaddress());
        holder.toTextView.setSelected(true);

        holder.cancelSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Delete this schedule?";
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete!");
                dialog.setMessage(message);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new WebServices(context, "ScheduleListAdapter").cancelSchedule(list.get(position).getScheduleid(), new VolleyResponseListerner() {
                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                list.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String message, String title) {

                            }
                        });
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.create().show();

            }
        });
        holder.editSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditScheduleActivity.class);
                intent.putExtra("scheduleId", list.get(position).getScheduleid());
                intent.putExtra("date", list.get(position).getDatetime());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Customholder extends RecyclerView.ViewHolder {
        TextView dateTimeTextView, fromTextView, toTextView;
        Button cancelSchedule, editSchedule;

        public Customholder(View itemView) {
            super(itemView);
            dateTimeTextView = (TextView) itemView.findViewById(R.id.itemListScheduleRecyclerViewDateTimeTextView);
            fromTextView = (TextView) itemView.findViewById(R.id.itemListScheduleRecyclerViewFromTextView);
            toTextView = (TextView) itemView.findViewById(R.id.itemListScheduleRecyclerViewToTextView);
            cancelSchedule = (Button) itemView.findViewById(R.id.cancel_schedule);
            editSchedule = (Button) itemView.findViewById(R.id.edit_schedule);
        }
    }
}
