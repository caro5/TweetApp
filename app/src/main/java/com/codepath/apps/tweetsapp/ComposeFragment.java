package com.codepath.apps.tweetsapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.tweetsapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

public class ComposeFragment extends DialogFragment {
    @BindView(R.id.etTweet) TextInputEditText etTweet;
    @BindView(R.id.btnTweet) Button btnTweet;

    private Unbinder unbinder;
    private TwitterClient client;

    public ComposeFragment() {}

    public static ComposeFragment newInstance() {
        ComposeFragment frag = new ComposeFragment();
        return frag;
    }

    public interface ComposeFragmentListener {
        void onSuccessfulTweet(Tweet tweet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        getDialog().setCanceledOnTouchOutside(true);
        unbinder = ButterKnife.bind(this, view);
        client = TwitterApplication.getRestClient();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @OnClick(R.id.btnTweet)
    public void updateStatus() {
        String tweet = etTweet.getText().toString();
        if (tweet.length() > 140) {
            Toast.makeText(getContext(), "Tweet is too long. Remove some thoughts and try again", Toast.LENGTH_LONG).show();
            return;
        }
        client.postTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d("DEBUG", jsonObject.toString());
                Tweet tweet = Tweet.fromJSON(jsonObject);
                sendBackResult(tweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());

            }
        });
    }

    public boolean sendBackResult(Tweet tweet) {
        ComposeFragmentListener listener = (ComposeFragmentListener) getActivity();
        listener.onSuccessfulTweet(tweet);
        dismiss();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
