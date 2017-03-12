package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract.WeatherEntry;

/**
 * Created by Francis Rodrigues on 3/12/17.
 *
 *{@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private Context mContext;

    /**
     * Just ForecastAdapter Constructor.
     *
     * @param context
     * @param c
     * @param flags
     */
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    /**
     * Prepare the weather high/lows for presentation.
     *
     * @param high
     * @param low
     * @return
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" +
                Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    /**
     * This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
     * string.
     *
     * @param cursor
     * @return
     */
    private String convertCursorToUXFormat(Cursor cursor) {
        // Get row indices for our cursor.
        int idx_max_temp = cursor.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP);
        int idx_min_temp = cursor.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP);
        int idx_date = cursor.getColumnIndex(WeatherEntry.COLUMN_DATE);
        int idx_short_desc = cursor.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC);

        String highAndLow = formatHighLows(cursor.getDouble(idx_max_temp),
                cursor.getDouble(idx_min_temp));

        return Utility.formatDate(cursor.getLong(idx_date)) +
                " - " + cursor.getString(idx_short_desc) +
                " - " + highAndLow;
    }

    /**
     * Remember that these views are reused as needed.
     *
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);

        return view;
    }

    /**
     * This is where we fill-in the views with the contents of the cursor.
     *
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        TextView textView = (TextView) view;
        textView.setText(convertCursorToUXFormat(cursor));
    }
}
