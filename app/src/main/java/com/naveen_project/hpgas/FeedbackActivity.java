package com.naveen_project.hpgas;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class FeedbackActivity extends FragmentActivity implements RatingDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }
    String TAG = "RATING";
    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setTitle("Rate this application")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setDefaultComment("This app is pretty cool !")
                .setStarColor(R.color.starColor)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.descriptionTextColor)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.hintTextColor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(FeedbackActivity.this)
                //.setTargetFragment(this, TAG) // only if listener is implemented by fragment
                .show();
    }


    @Override
    public void onPositiveButtonClicked(int rate,@NotNull String comment) {
        Log.w(TAG, "Rating : "+rate+"\nComment : "+comment);
        Toast.makeText(this, "Rating : "+rate+"\nComment : "+comment, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNegativeButtonClicked() {
        Log.w(TAG, "Positive button clicked");
        Toast.makeText(this, "Positive button clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNeutralButtonClicked() {
        Log.w(TAG, "Neutral button clicked");
        Toast.makeText(this, "Neutral button clicked", Toast.LENGTH_LONG).show();
    }
}
