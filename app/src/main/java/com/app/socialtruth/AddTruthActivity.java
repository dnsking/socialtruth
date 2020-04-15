package com.app.socialtruth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.socialtruth.actions.AddTruthAction;
import com.app.socialtruth.helpers.SocialTruthNetworkHelper;
import com.app.socialtruth.ui.steps.ImageSelectionStep;
import com.app.socialtruth.ui.steps.UrlStep;
import com.app.socialtruth.utils.Utils;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;
import io.github.cdimascio.essence.Essence;
import io.github.cdimascio.essence.EssenceResult;

public class AddTruthActivity extends AppCompatActivity implements StepperFormListener {
    private VerticalStepperFormView verticalStepperForm;
    private UrlStep urlStep;
    private ImageSelectionStep imageSelectionStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_truth);

        urlStep= new UrlStep("Source");
        imageSelectionStep = new ImageSelectionStep("False Image");
        verticalStepperForm = findViewById(R.id.stepper_form);
        verticalStepperForm
                .setup(this, urlStep,imageSelectionStep)
                .displayBottomNavigation(false).includeConfirmationStep(false).lastStepNextButtonText("Add False Report")
                .init();
    }

    @Override
    public void onCompletedForm() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    EssenceResult data = Essence.extract(Utils.UrlToHtml("https://www.factcheck.org/2020/03/gargling-water-with-salt-wont-eliminate-coronavirus/"));
                    App.Log("result "+data.getText());
                    App.Log("title "+data.getTitle());

                    final AddTruthAction addTruthAction = new AddTruthAction(
                            imageSelectionStep.getStepData(), urlStep.getStepData(),Boolean.toString(false)

,data.getText(),data.getTitle());
                    SocialTruthNetworkHelper.AddTruth(AddTruthActivity.this,addTruthAction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       finish();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onCancelledForm() {

    }
}
