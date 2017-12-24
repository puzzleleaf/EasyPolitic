package leesd.crossithackathon.Proposal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import leesd.crossithackathon.R;

/**
 * Created by leesd on 2017-12-23.
 */

public class ProposalListAdapter extends BaseAdapter {

    private ArrayList<ProposalItem> itemList = new ArrayList<ProposalItem>();

    public ProposalListAdapter(){}

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.proposal_item,parent,false);
        }

        TextView numberText = (TextView)convertView.findViewById(R.id.number);
        TextView titleText = (TextView)convertView.findViewById(R.id.title);
        TextView instituteText = (TextView)convertView.findViewById(R.id.institute);
        TextView reportDateText = (TextView)convertView.findViewById(R.id.report_date);
        TextView situationText = (TextView)convertView.findViewById(R.id.situation);

        ProposalItem proposalItem = itemList.get(position);

        numberText.setText(proposalItem.getNumber());
        titleText.setText(proposalItem.getTitle());
        instituteText.setText(proposalItem.getInstitute());
        reportDateText.setText(proposalItem.getReportDate());
        situationText.setText(proposalItem.getSituation());



        return convertView;
    }

    public void addItem(String number, String title, String institute, String reportDate, String situation){
        ProposalItem item = new ProposalItem();

        item.setNumber(number);
        item.setTitle(title);
        item.setInstitute(institute);
        item.setReportDate(reportDate);
        item.setSituation(situation);

        itemList.add(item);
    }
}
