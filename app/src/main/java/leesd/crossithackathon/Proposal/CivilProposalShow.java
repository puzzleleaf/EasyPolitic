package leesd.crossithackathon.Proposal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import leesd.crossithackathon.DataManager.CivilProposal;
import leesd.crossithackathon.R;

/**
 * Created by leesd on 2017-12-23.
 */

public class CivilProposalShow extends AppCompatActivity {
    ListView listView;
    ProposalListAdapter proposalListAdapter;;
    CivilProposal civilProposal = new CivilProposal();
    HashMap<String,ArrayList<String>> pageList = new  HashMap<String,ArrayList<String>>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civil_proposal);

        proposalListAdapter = new ProposalListAdapter();

        listView = (ListView)findViewById(R.id.proposal_list);
        listView.setAdapter(proposalListAdapter);

        pageList = civilProposal.getList("2017",1);
       // System.out.println(result.get("writingNo").get(i)+"//"+result.get("title").get(i)+"//"+result.get("institution").get(i)+"//"+result.get("request_date").get(i)+"//"+result.get("hits").get(i));

        for(int i = 0 ; i < 10 ; i++)
        proposalListAdapter.addItem(pageList.get("writeNo").get(i), pageList.get("title").get(i), pageList.get("institution").get(i),
                pageList.get("request_date").get(i), pageList.get("hits").get(i));

    }
}
