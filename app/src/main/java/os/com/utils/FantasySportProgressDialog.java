package os.com.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;
import os.com.R;


/**
 * custom progress dialog
 */

public class FantasySportProgressDialog extends ProgressDialog {

    /**
     * declare java objects
     */
    public static FantasySportProgressDialog msGWProgressDialog;

    TextView titleTv;

    public FantasySportProgressDialog(Context context) {
        super(context);
    }

    public FantasySportProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_custom_progressbar);
        titleTv = (TextView) findViewById(R.id.title_tv);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setDimAmount(0.20f);
    }

    /**
     * generate singleton for custom progress dialog
     *
     * @param mContext
     * @return
     */
    public static FantasySportProgressDialog getProgressDialog(Context mContext) {

        try {
            if (msGWProgressDialog == null) {
                msGWProgressDialog = new FantasySportProgressDialog(mContext, R.style.AppCompatAlertDialogStyle);
            }
        } catch (Exception e) {
                msGWProgressDialog = null;
                msGWProgressDialog = new FantasySportProgressDialog(mContext, R.style.AppCompatAlertDialogStyle);
            e.printStackTrace();
        }

        return msGWProgressDialog;
    }

    /**
     * show dialog if not showing
     */
    public void showDialog() {

        try {
            if (msGWProgressDialog.isShowing()) {
                msGWProgressDialog.dismiss();
            }
            msGWProgressDialog.show();
            msGWProgressDialog.setCancelable(false);
            //  titleTv.setText(title);
        } catch (Exception ex) {
            msGWProgressDialog = null;
            ex.printStackTrace();
        }
    }

    public void showCancelableDialog() {

        try {
            if (msGWProgressDialog.isShowing()) {
                msGWProgressDialog.dismiss();
            }
            msGWProgressDialog.show();
            msGWProgressDialog.setCancelable(true);
            //  titleTv.setText(title);
        } catch (Exception ex) {
            msGWProgressDialog = null;
            ex.printStackTrace();
        }
    }
    /*
     * set title of progress dialog*/

    public void setTitle(String title) {
        try {
            if (msGWProgressDialog != null && msGWProgressDialog.isShowing()) {
                titleTv.setText(title);
            }
        } catch (Exception ex) {
            msGWProgressDialog = null;
            ex.printStackTrace();
        }

    }

    /**
     * hide dialog if not showing
     */
    public void dismissDialog() {
        try {
            try {
                if (msGWProgressDialog != null && msGWProgressDialog.isShowing()) {
                    msGWProgressDialog.dismiss();
                    msGWProgressDialog = null;
                } else if (msGWProgressDialog != null)
                    msGWProgressDialog = null;
            } catch (Exception ex) {
                msGWProgressDialog = null;
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
