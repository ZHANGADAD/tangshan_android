package com.tangshan.gui.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tangshan.gui.R;
import com.tangshan.gui.bean.AppReleaseInfo;


/**
 *
 */
public class ShowDialogUtil {


    /**
     * 软件升级
     */
    public static void showV4UpdateDialog(final Context context, final AppReleaseInfo versionData) {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        View myView = View.inflate(context, R.layout.dialog_update, null);

        TextView title = (TextView) myView.findViewById(R.id.title);
        TextView content = (TextView) myView.findViewById(R.id.content);
        title.setText(versionData.getAppVersion() + "版全新上线");
        content.setText(versionData.getReleaseNote());

        //取消
        myView.findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //升级
        myView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setData(Uri.parse(versionData.getDownloadUrl()));
                context.startActivity(intent);
            }
        });
        dialog.setContentView(myView);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
}
