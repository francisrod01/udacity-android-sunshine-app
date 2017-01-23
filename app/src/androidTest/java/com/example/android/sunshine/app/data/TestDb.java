package com.example.android.sunshine.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by Francis Rodrigues on 1/21/17.
 */

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate.
    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    /*
         This function gets called before each test is executed to delete the database. This makes
         sure that we always have a clean test.
     */

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteTheDatabase();
    }

    /*
         Students: Uncomment this test once you've written the code to create the Location
         table. Note that you will have to have chosen the same column names that I did in
         my solution for this test to compile, so if you haven't yet done that, this is
         a good time to change your column names to match mine.

         Note that this only tests that the Location table has the correct columns, since we
         give you the code for the weather table. This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        insertLocation();
    }

    /*
         Students: Here is where you will build code to test that we cant insert and query the
         database. We've done a lot of work for you. You'll want to look in TestUtilities
         where you can use the "createWeatherValues" function. You can also make use of
         valueCurrentRecord function from within TestUtilities.
     */
    public void testWeatherTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testLocationTable
        // we can move this code to insertLocation and then call insertLocation from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testLocationTable can only return void because it's a test.

        long locationRowId = insertLocation();

        // Make sure we have a valid row ID.
        assertFalse("Error: Location Not Inserted Correctly", locationRowId == -1L);

        // First step: Get reference to writable database.
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (Weather): Create weather values
        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);

        // Third Step (Weather): Insert ContentValues into database and get a row ID back.
        long weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back.
        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = db.query(
                WeatherContract.WeatherEntry.TABLE_NAME, // Table to query
                null, // leaving "columns" null just returns all the columns
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we have any rows.
        assertTrue("Error: No Records returned from location query", weatherCursor.moveToFirst());

        // Fifth Step: Validate the location Query.
        TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                weatherCursor, weatherValues);

        // Sixth Step: Close the cursor and database.
        weatherCursor.close();
        dbHelper.close();
    }

    /*
         Students: This is a helper method of the testWeatherTable quiz. You can move your
         code from testLocationTable to here so that you can call this code from both
         testWeatherTable and testLocationTable.
     */
    public long insertLocation() {
        // First step: Get reference to writable database.
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted. IN THEORY. Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back.
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME, // Table to query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // Columns to group by
                null, // Columns to filter by row groups
                null // sort order
        );


        // Move the cursor to a valid database row and check to see if we got any records back
        // for the query.
        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());


        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Location query validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database.
        assertFalse("Error: More than one record returned from location query", cursor.moveToNext());

        // Sixth Step: Close Cursor and Database.
        cursor.close();
        db.close();

        return locationRowId;
    }
}
