package com.hannah.hannahworld;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by xuyong1 on 28/07/15.
 */
public final class DialogHelp extends DialogFragment{
    private Activity activity;
        private static final String TAG = "DialogHelp";
        private static final String KEY_MESSAGE = "message";
        public static void page(final FragmentManager fm, final int messageResId){

            final DialogFragment newFragment = DialogHelp.instance(messageResId);
            newFragment.show(fm, TAG);
        }

        private static DialogHelp instance(final int messageResId){
            final DialogHelp fragment = new DialogHelp();

            final Bundle args = new Bundle();
            args.putInt(KEY_MESSAGE, messageResId);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public void onAttach(final Activity activity){
            super.onAttach(activity);
            this.activity = activity;
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState){
            final Bundle args = getArguments();
            final int messageResId = args.getInt(KEY_MESSAGE);
            //final View view = LayoutInflater.from(activity).inflate(R.layout.backup_wallet_dialog, null);
            final DialogBuilder builder = new DialogBuilder(activity);
           // builder.setTitle(R.string.export_keys_dialog_title);
           // builder.setView(view);
            builder.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    activity.finish();

                }

            });
            builder.setNegativeButton(R.string.button_no,null);
                    builder.setCancelable(false);

            builder.setMessage(Html.fromHtml(getString(messageResId)));
            //dialog.singleDismissButton(null);
            return builder.create();
        }
    }