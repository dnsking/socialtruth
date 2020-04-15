package com.app.socialtruth;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;

import com.app.socialtruth.actions.AddTruthAction;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ResultFragment extends BottomSheetDialogFragment {
    private AddTruthAction addTruthAction;
    private View nothingLayout,openInBroswer,shareButton;
    private TextView titleTextView,summaryTextView;


    public ResultFragment(){

    }
    public static ResultFragment newInstance(AddTruthAction addTruthAction){
        ResultFragment resultFragment=   new ResultFragment();
        resultFragment.setAddTruthAction(addTruthAction);
     return   resultFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
      //  getActivity().finish();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().finish();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().finish();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(addTruthAction==null||addTruthAction.getIsTrue()==null){

        }
        else{
            nothingLayout.setVisibility(View.GONE);
            openInBroswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                            addTruthAction.getUrl()
                    ));
                    startActivity(browserIntent);
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ShareCompat.IntentBuilder.from(getActivity())
                            .setType("text/plain")
                            .setChooserTitle("Share Link")
                            .setText(
                                    addTruthAction.getUrl())
                            .startChooser();
                }
            });

            titleTextView.setText(addTruthAction.getTitle());
          //  summaryTextView.setText(testText);
            summaryTextView.setText(addTruthAction.getSummary());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view =inflater.inflate(R.layout.result_fragment_layout, container, false);
        nothingLayout = view.findViewById(R.id.nothingLayout);
        openInBroswer =view.findViewById(R.id.openInBroswer);
        shareButton =view.findViewById(R.id.shareButton);
        titleTextView = view.findViewById(R.id.titleTextView);
        summaryTextView = view.findViewById(R.id.summaryTextView);
        return view;
    }

    public AddTruthAction getAddTruthAction() {
        return addTruthAction;
    }

    public void setAddTruthAction(AddTruthAction addTruthAction) {
        this.addTruthAction = addTruthAction;
    }
}
