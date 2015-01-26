package com.signify.intouch.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by critchie on 21/01/2015.
 */
public class ContactInformation {

    private static String mContactID;
    private static Uri mContactURI;
    private static ContentResolver mContentResolver;
    private static Map mContactDetails;

    public ContactInformation(ContentResolver content_resolver){
        mContentResolver = content_resolver;
        mContactDetails = new HashMap();
        mContactDetails.put("number", null);
        mContactDetails.put("name", null);
    }

    public Map getContactDetails() {
        return mContactDetails;
    }

    public void changeURI(Uri contact_uri){
        mContactURI = contact_uri;
        mContactID = mContactURI.getLastPathSegment();
    }

    public void refresh(){
        Cursor cursor = mContentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone._ID + "=?",
                new String[]{mContactID},null
        );
        Boolean exists = cursor.moveToFirst();
        int phoneNumberColumnIndex;
        int nameColumnIndex;
        phoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        while(exists){
            mContactDetails.put("name",cursor.getString(nameColumnIndex));
            mContactDetails.put("number",cursor.getString(phoneNumberColumnIndex));
            exists = cursor.moveToNext();
        }
        cursor.close();
    }
}
