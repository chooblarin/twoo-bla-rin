package com.chooblarin.twooblarin.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.chooblarin.twooblarin.R;
import com.chooblarin.twooblarin.event.BusHolder;
import com.chooblarin.twooblarin.event.TweetEvent;

/**
 * Created by chooblarin on 2014/09/12.
 */
public class TweetDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tweet);

        final EditText input = (EditText) dialog.findViewById(R.id.input_tweet_text);
        dialog.findViewById(R.id.dialog_button_tweet)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tweetText = input.getText().toString();
                        if (!TextUtils.isEmpty(tweetText)) {
                            BusHolder.getInstance().post(new TweetEvent(tweetText));
                            dismiss();
                        }
                    }
                });
        return dialog;
    }
}
