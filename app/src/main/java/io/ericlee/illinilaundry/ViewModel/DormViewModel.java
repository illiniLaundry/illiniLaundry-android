package io.ericlee.illinilaundry.ViewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import io.ericlee.illinilaundry.Model.Dorm;

/**
 * @author dl-eric
 */

public class DormViewModel extends BaseObservable {

    private Context context;
    private Dorm dorm;

    public DormViewModel(Context context, Dorm dorm) {
        this.context = context;
        this.dorm = dorm;
    }

    public View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }
}
