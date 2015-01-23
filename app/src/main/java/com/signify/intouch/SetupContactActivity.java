package com.signify.intouch;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.signify.intouch.data.ContactInformation;
import com.signify.intouch.data.Settings;


public class SetupContactActivity extends ActionBarActivity {

    private static final int REQUEST_CONTACTPICKER = 1001;

    Settings mSettings;
    Uri contactUri;

    Button buttonSelectContact;
    Button buttonNextPage;
    ContactInformation contactInfo;
    TextView textContactDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_contact);
        mSettings = Settings.getInstance(this);

        contactInfo = new ContactInformation(getContentResolver());

        LinearLayout blurb = (LinearLayout) findViewById(R.id.intouch_welcom_blurb);

        textContactDetails = (TextView) findViewById(R.id.textContactDetails);



        buttonSelectContact = (Button) findViewById(R.id.buttonAddContact);
        buttonSelectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContact();
            }
        });

        buttonNextPage = (Button) findViewById(R.id.buttonContactNext);
        buttonNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });

        if(!mSettings.getFirstRun()){
            contactUri = mSettings.getContactUri();
            updateContactText();
            buttonNextPage.setVisibility(View.VISIBLE);
            blurb.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onBackPressed() {
        if(mSettings.getFirstRun()) {
            moveTaskToBack(true);
        } else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTACTPICKER) {
            if (resultCode == RESULT_OK) {
                contactPickedOk(data);
            }
        }
    }

    /**
     *
     */
    private void getContact(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CONTACTPICKER);
    }

    /**
     *
     */
    private void nextPage(){
        mSettings.setContactUri(contactUri);
        if(!mSettings.getFirstRun()){
            finish();
        } else{
            Intent intent = new Intent(this, SetupDayActivity.class);
            startActivity(intent);
        }

    }

    /**
     *
     */
    private void updateContactText(){
        contactInfo.changeURI(contactUri);
        contactInfo.refresh();
        buttonSelectContact.setText(contactInfo.getContactDetails().get("name") + " "
                + contactInfo.getContactDetails().get("number"));
    }

    /**
     *
     * @param data
     */
    public void contactPickedOk(Intent data){
        buttonNextPage.setVisibility(View.VISIBLE);
        contactUri = data.getData();

        updateContactText();
    }
}
