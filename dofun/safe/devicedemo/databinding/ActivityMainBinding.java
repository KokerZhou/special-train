package com.dofun.safe.devicedemo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.dofun.safe.devicedemo.R;

/* loaded from: classes4.dex */
public final class ActivityMainBinding implements ViewBinding {
    public final AppCompatButton bntStart;
    private final LinearLayout rootView;
    public final TextView tvContent;

    private ActivityMainBinding(LinearLayout rootView, AppCompatButton bntStart, TextView tvContent) {
        this.rootView = rootView;
        this.bntStart = bntStart;
        this.tvContent = tvContent;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_main, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityMainBinding bind(View rootView) {
        int id = R.id.bntStart;
        AppCompatButton bntStart = (AppCompatButton) ViewBindings.findChildViewById(rootView, id);
        if (bntStart != null) {
            id = R.id.tv_content;
            TextView tvContent = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (tvContent != null) {
                return new ActivityMainBinding((LinearLayout) rootView, bntStart, tvContent);
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
