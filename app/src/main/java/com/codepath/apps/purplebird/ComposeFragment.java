package com.codepath.apps.purplebird;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.raizlabs.android.dbflow.config.FlowLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeFragment extends Fragment {

    EditText etCompose;
    TextView tvCounter;
    Button btnSend;
    Integer charCount = 0;
    Integer LIMIT_140 = 140;
    String COLOR_RED = "#ffcc0000";
    String COLOR_GREEN = "#ff669900";

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_compose, container, false);
        return view;
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        Log.d(TAG, "sup: onInflate");
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        Log.d(TAG, "sup: onAttachFragment");
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "sup: onAttach");
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "sup: onViewCreated");
        etCompose = (EditText) view.findViewById(R.id.etCompose);
        tvCounter = (TextView) view.findViewById(R.id.tvCounter);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        if(etCompose == null) {
            Log.d(TAG, "etCompose is null");

        }
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charCount = s.length();
                Log.d(TAG, "sup: chars = " + charCount.toString());
                Log.d(TAG, "sup: " + tvCounter.getText().toString());
                tvCounter.setText(charCount.toString() + "/140");

                if(charCount > LIMIT_140 || charCount == 0) {
                    Log.d(TAG, "RED");
                    tvCounter.setTextColor(Color.parseColor(COLOR_RED));
                    btnSend.setEnabled(false);
                } else {
                    Log.d(TAG, "GREEN");
                    tvCounter.setTextColor(Color.parseColor(COLOR_GREEN));
                    btnSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "sup: onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "sup: onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "sup: onInflate");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "sup: onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "sup: onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "sup: onDestroyView");
        // Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "sup: onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "sup: onDetach");
        super.onDetach();
    }
}
