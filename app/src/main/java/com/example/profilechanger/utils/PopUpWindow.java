package com.example.profilechanger.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.interfaces.ClickListener;

public class PopUpWindow extends ContextWrapper {

    Context context;
    ClickListener clickListener;

    public PopUpWindow(Context base, ClickListener clickListener) {
        super(base);
        this.context = base;
        this.clickListener = clickListener;

    }

    public PopupWindow popupWindowUpDel(int i) {
        PopupWindow circleSizePopupMenu = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_up_up_del_layout, null);

        ConstraintLayout edit_cl = view.findViewById(R.id.edit_cl);
        ConstraintLayout delete_cl = view.findViewById(R.id.delete_cl);
        ConstraintLayout set_cl = view.findViewById(R.id.set_cl);

        if (i == 1) {
            edit_cl.setVisibility(View.VISIBLE);
            delete_cl.setVisibility(View.GONE);
            set_cl.setVisibility(View.GONE);
        } else if (i == 2) {
            edit_cl.setVisibility(View.GONE);
            delete_cl.setVisibility(View.VISIBLE);
            set_cl.setVisibility(View.GONE);
        } else if (i == 3) {
            edit_cl.setVisibility(View.GONE);
            delete_cl.setVisibility(View.GONE);
            set_cl.setVisibility(View.VISIBLE);
        } else if (i == 4) {
            edit_cl.setVisibility(View.VISIBLE);
            delete_cl.setVisibility(View.VISIBLE);
            set_cl.setVisibility(View.GONE);
        } else {
            edit_cl.setVisibility(View.VISIBLE);
            delete_cl.setVisibility(View.VISIBLE);
            set_cl.setVisibility(View.VISIBLE);
        }

        circleSizePopupMenu.setFocusable(true);
        circleSizePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        circleSizePopupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        circleSizePopupMenu.setContentView(view);
        circleSizePopupMenu.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        circleSizePopupMenu.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        edit_cl.setOnClickListener(v ->
        {
            clickListener.click(MyAnnotations.edit);
            circleSizePopupMenu.dismiss();
        });
        delete_cl.setOnClickListener(v -> {
            clickListener.click(MyAnnotations.delete);
            circleSizePopupMenu.dismiss();

        });
        set_cl.setOnClickListener(v -> {
            clickListener.click(MyAnnotations.set);
            circleSizePopupMenu.dismiss();

        });

        return circleSizePopupMenu;
    }


}
