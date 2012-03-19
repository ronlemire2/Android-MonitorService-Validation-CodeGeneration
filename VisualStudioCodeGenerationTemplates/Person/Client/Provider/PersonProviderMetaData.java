package dev.ronlemire.personprovider;

import android.net.Uri;

public class PersonProviderMetaData {
    public static final String AUTHORITY = "dev.ronlemire.personprovider.PersonProvider";
    
    private PersonProviderMetaData() {}
    //inner class describing columns and their types
    public static final class PersonTableMetaData 
    {
        private PersonTableMetaData() {}
        public static final String TABLE_NAME = "datatypes";

        //uri and mime type definitions
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/person");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.androidbook.person";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.androidbook.person";

        //Additional Columns start here.
			public static final String PERSON_ID = "Id";
			public static final String PERSON_FIRSTNAME = "FirstName";
			public static final String PERSON_LASTNAME = "LastName";
			public static final String PERSON_EMAIL = "Email";
    }
}