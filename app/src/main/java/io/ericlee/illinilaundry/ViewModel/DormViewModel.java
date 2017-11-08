package io.ericlee.illinilaundry.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.DormImages;
import io.ericlee.illinilaundry.View.Activities.DormActivity;

/**
 * @author dl-eric
 */

public class DormViewModel extends BaseObservable {
    private Context context;
    private Dorm dorm;

    private int imageResource;

    public DormViewModel(Context context, Dorm dorm) {
        this.context = context;
        this.dorm = dorm;
        imageResource = DormImages.getInstance().getImages().get(dorm.getName());
    }

    public String getName() {
        return dorm.getName();
    }

    public int getImageResource() {
        return imageResource;
    }

    public View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDormActivity();
            }
        };
    }

    private void launchDormActivity() {
        Intent intent = new Intent(context, DormActivity.class);
        intent.putExtra("Dorm", dorm);
        context.startActivity(intent);
    }
}
