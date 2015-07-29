package com.hannah.hannahworld;

/**
 * Created by xuyong1 on 28/07/15.
 */
import android.content.DialogInterface.OnClickListener;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
//import javax.annotation.Nullable;
import com.hannah.hannahworld.R;
import com.hannah.hannahworld.Constants;

public class DialogBuilder extends AlertDialog.Builder {

    private final View customTitle;
    private final ImageView iconView;
    private final TextView titleView;

    public static DialogBuilder warn(final Context context, final int titleResId){

        final DialogBuilder builder = new DialogBuilder(context);
       // builder.setIcon(R.drawable.ic_warning_grey600_24dp);
        builder.setTitle(titleResId);
        return builder;
    }

    public DialogBuilder(final Context context){
        super(context, Build.VERSION.SDK_INT < Constants.LOLLIPOP_SDK ? AlertDialog.THEME_HOLO_LIGHT : AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        this.customTitle = LayoutInflater.from(context).inflate(R.layout.dialog_head, null);
        this.iconView = (ImageView) customTitle.findViewById(android.R.id.icon);
        this.titleView = (TextView) customTitle.findViewById(android.R.id.title);
    }

    @Override
    public DialogBuilder setIcon(final Drawable icon){
        if (icon != null){
            setCustomTitle(customTitle);
            iconView.setImageDrawable(icon);
            iconView.setVisibility(View.VISIBLE);
        }

        return this;
    }

    @Override
    public DialogBuilder setIcon(final int iconResId){
        if (iconResId != 0){
            setCustomTitle(customTitle);
            iconView.setImageResource(iconResId);
            iconView.setVisibility(View.VISIBLE);
        }

        return this;
    }

    @Override
    public DialogBuilder setTitle(final CharSequence title){

        if (title != null)
        {
            setCustomTitle(customTitle);
            titleView.setText(title);
        }

        return this;
    }

    @Override
    public DialogBuilder setTitle(final int titleResId)
    {
        if (titleResId != 0)
        {
            setCustomTitle(customTitle);
            titleView.setText(titleResId);
        }

        return this;
    }

    @Override
    public DialogBuilder setMessage(final CharSequence message)
    {
        super.setMessage(message);

        return this;
    }

    @Override
    public DialogBuilder setMessage(final int messageResId)
    {
        super.setMessage(messageResId);

        return this;
    }

    public DialogBuilder singleDismissButton( final OnClickListener dismissListener)
    {
        setNeutralButton("No", dismissListener);

        return this;
    }
}

