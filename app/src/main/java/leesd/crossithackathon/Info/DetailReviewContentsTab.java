package leesd.crossithackathon.Info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import leesd.crossithackathon.R;

/**
 * Created by cmtyx on 2017-12-17.
 */

public class DetailReviewContentsTab extends Fragment{

    private EditText editText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_chat_review_contents, container, false);
        editText = (EditText)view.findViewById(R.id.tab_layout_detail_review_contents);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DetailChatActivity.userReviewContents = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

}
