package com.sripadmanaban.googleplusphotos;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Android Google+ Quickstart activity.
 *
 * Demonstrates Google+ Sign-In and usage of the Google+ APIs to retrieve a
 * users profile information.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;

    // We use mSignInProgress to track whether user has clicked sign in.
    // mSignInProgress can be one of three values:
    //
    //       STATE_DEFAULT: The default state of the application before the user
    //                      has clicked 'sign in', or after they have clicked
    //                      'sign out'.  In this state we will not attempt to
    //                      resolve sign in errors and so will display our
    //                      Activity in a signed out state.
    //       STATE_SIGN_IN: This state indicates that the user has clicked 'sign
    //                      in', so resolve successive errors preventing sign in
    //                      until the user has successfully authorized an account
    //                      for our app.
    //   STATE_IN_PROGRESS: This state indicates that we have started an intent to
    //                      resolve an error, and so we should not start further
    //                      intents until the current intent completes.
    private int mSignInProgress;

    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    // Used to store the error code most recently returned by Google Play services
    // until the user clicks 'sign in'.
    private int mSignInError;

    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private Button mCheckPhotoButton;
    private TextView mStatus;

    private SharedPreferences preferences;

    private GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        /* onConnected is called when our Activity successfully connects to Google
      * Play services.  onConnected indicates that an account was selected on the
      * device, that the selected account has granted any requested permissions to
      * our app and that we were able to establish a service connection to Google
      * Play services.
      */
        @Override
        public void onConnected(Bundle connectionHint) {
            // Reaching onConnected means we consider the user signed in.
            Log.i(Constants.TAG, "onConnected");

            // Update the user interface to reflect that the user is signed in.
            mSignInButton.setEnabled(false);
            mSignOutButton.setEnabled(true);
            mRevokeButton.setEnabled(true);
            mCheckPhotoButton.setEnabled(true);

            // Retrieve some profile information to personalize our app for the user.
            Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            mStatus.setText(String.format(
                    getResources().getString(R.string.signed_in_as),
                    currentUser.getDisplayName()));


            AsyncAccessToken task = new AsyncAccessToken();
            task.execute();

            // Indicate that the sign in process is complete.
            mSignInProgress = Constants.STATE_DEFAULT;
        }

        @Override
        public void onConnectionSuspended(int cause) {
            // The connection to Google Play services was lost for some reason.
            // We call connect() to attempt to re-establish the connection or get a
            // ConnectionResult that we can attempt to resolve.
            mGoogleApiClient.connect();
        }

        class AsyncAccessToken extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {

                String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String scope = "oauth2:" + Scopes.PLUS_LOGIN + " " + Scopes.PLUS_ME;

                String accessToken = "";
                try {
                    accessToken = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scope);
                } catch (IOException | GoogleAuthException e) {
                    e.printStackTrace();
                }

                return accessToken;
            }

            @Override
            protected void onPostExecute(String token) {
                Log.i(Constants.TAG, "Access token retrieved:" + token);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.ACCESS_TOKEN, token);
                editor.apply();
            }

        }
    };

    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        /* onConnectionFailed is called when our Activity could not connect to Google
      * Play services.  onConnectionFailed indicates that the user needs to select
      * an account, grant permissions or resolve an error in order to sign in.
      */
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            // Refer to the javadoc for ConnectionResult to see what error codes might
            // be returned in onConnectionFailed.
            Log.i(Constants.TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                    + result.getErrorCode());

            if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
                // An API requested for GoogleApiClient is not available. The device's current
                // configuration might not be supported with the requested API or a required component
                // may not be installed, such as the Android Wear application. You may need to use a
                // second GoogleApiClient to manage the application's optional APIs.
                Log.d(Constants.TAG, "Hello");
            } else if (mSignInProgress != Constants.STATE_IN_PROGRESS) {
                // We do not have an intent in progress so we should store the latest
                // error resolution intent for use when the sign in button is clicked.
                mSignInIntent = result.getResolution();
                mSignInError = result.getErrorCode();

                if (mSignInProgress == Constants.STATE_SIGN_IN) {
                    // STATE_SIGN_IN indicates the user already clicked the sign in button
                    // so we should continue processing errors until the user is signed in
                    // or they click cancel.
                    resolveSignInError();
                }
            }

            // In this sample we consider the user signed out whenever they do not have
            // a connection to Google Play services.
            onSignedOut();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
        mCheckPhotoButton = (Button) findViewById(R.id.check_photos_button);

        mStatus = (TextView) findViewById(R.id.sign_in_status);

        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);
        mCheckPhotoButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(Constants.SAVED_PROGRESS, Constants.STATE_DEFAULT);
        }

        mGoogleApiClient = buildGoogleApiClient();
    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {
            // We only process button clicks when GoogleApiClient is not transitioning
            // between connected and not connected.
            switch (v.getId()) {
                case R.id.sign_in_button:
                    mStatus.setText(R.string.status_signing_in);
                    resolveSignInError();
                    break;
                case R.id.sign_out_button:
                    // We clear the default account on sign out so that Google Play
                    // services will not return an onConnected callback without user
                    // interaction.
                    mCheckPhotoButton.setEnabled(false);
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    break;
                case R.id.revoke_access_button:
                    // After we revoke permissions for the user with a GoogleApiClient
                    // instance, we must discard it and create a new one.
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    // Our sample has caches no user data from Google+, however we
                    // would normally register a callback on revokeAccessAndDisconnect
                    // to delete user data so that we comply with Google developer
                    // policies.
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                    mGoogleApiClient = buildGoogleApiClient();
                    mGoogleApiClient.connect();
                    break;
                case R.id.check_photos_button:
                    Intent intent = new Intent(LoginActivity.this, DisplayImagesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                    break;
            }
        }
    }

    /* Starts an appropriate intent or dialog for user interaction to resolve
     * the current error preventing the user from being signed in.  This could
     * be a dialog allowing the user to select an account, an activity allowing
     * the user to consent to the permissions being requested by your app, a
     * setting to enable device networking, etc.
     */
    private void resolveSignInError() {
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error.  For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback.  This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = Constants.STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        Constants.RC_SIGN_IN, null, 0, 0, 0);
            } catch (SendIntentException e) {
                Log.i(Constants.TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = Constants.STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // Google Play services wasn't able to provide an intent for some
            // error types, so we show the default Google Play services error
            // dialog which may still start an intent on our behalf if the
            // user can resolve the issue.
            showDialog(Constants.DIALOG_PLAY_SERVICES_ERROR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case Constants.RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = Constants.STATE_SIGN_IN;
                } else {
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
                    mSignInProgress = Constants.STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    private void onSignedOut() {
        // Update the UI to reflect that the user is signed out.
        mSignInButton.setEnabled(true);
        mSignOutButton.setEnabled(false);
        mRevokeButton.setEnabled(false);

        mStatus.setText(R.string.status_signed_out);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case Constants.DIALOG_PLAY_SERVICES_ERROR:
                if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
                    return GooglePlayServicesUtil.getErrorDialog(
                            mSignInError,
                            this,
                            Constants.RC_SIGN_IN,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Log.e(Constants.TAG, "Google Play services resolution cancelled");
                                    mSignInProgress = Constants.STATE_DEFAULT;
                                    mStatus.setText(R.string.status_signed_out);
                                }
                            });
                } else {
                    return new AlertDialog.Builder(this)
                            .setMessage(R.string.play_services_error)
                            .setPositiveButton(R.string.close,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.e(Constants.TAG, "Google Play services error could not be "
                                                    + "resolved: " + mSignInError);
                                            mSignInProgress = Constants.STATE_DEFAULT;
                                            mStatus.setText(R.string.status_signed_out);
                                        }
                                    }).create();
                }
            default:
                return super.onCreateDialog(id);
        }
    }
}
