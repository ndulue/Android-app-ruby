package app.calcounterapp.com.ruby.Interface;

import android.view.View;

public interface IOnCartClickListener {
    void onCalculatePriceListener(View view, int position, boolean isDecrease, boolean isDelete);
}
