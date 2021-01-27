package com.example.recover;

import android.app.AlertDialog;
import android.content.Context;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ProgressDialogUtil {

    private static AlertDialog mAlterDialog;

    public static void showProgressDialog(Context context) {
        if (mAlterDialog == null) {
            mAlterDialog = new AlertDialog.Builder(context).create();
        }

        View loadView = LayoutInflater.from(context).inflate(R.layout.recover_wait, null);
        mAlterDialog.setView(loadView, 0, 0, 0, 0);
        mAlterDialog.setCanceledOnTouchOutside(false);

        mAlterDialog.show();

        mAlterDialog.getWindow().setLayout(500,400);
    }

    public static void dismiss(){
        if(mAlterDialog!=null&&mAlterDialog.isShowing()){
            mAlterDialog.dismiss();
        }
    }
}
