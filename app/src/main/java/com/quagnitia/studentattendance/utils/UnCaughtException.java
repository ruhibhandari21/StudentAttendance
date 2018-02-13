package com.quagnitia.studentattendance.utils;

/*@Author: Ruhi Bhandari
* @Created Date: 15/4/2016
* @UnCaughtException
* */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.charset.Charset;
import java.util.Date;

public class UnCaughtException implements UncaughtExceptionHandler {

    public String xmlString;
    private Context context;
    private StringBuilder body;

    public UnCaughtException(Context ctx) {
        context = ctx;
        // ((Activity) context).setRequestedOrientation(context.getResources()
        // .getInteger(R.integer.ORIENTATION));
    }

    private StatFs getStatFs() {
        File path = Environment.getDataDirectory();
        return new StatFs(path.getPath());
    }

    @SuppressWarnings("deprecation")
    private long getAvailableInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    @SuppressWarnings("deprecation")
    private long getTotalInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    private void addInformation(StringBuilder message) {

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            message.append("Version: ").append(pi.versionName).append('\n');
            message.append("Package: ").append(pi.packageName).append('\n');
            message.append("Class Name: ").append(context.getClass().getName())
                    .append('\n');
        } catch (Exception e) {
            Log.e("CustomExceptionHandler", "Error", e);
            message.append("Could not get Version information for ").append(
                    context.getPackageName());
        }
        message.append("Phone Model: ").append(android.os.Build.MODEL)
                .append('\n');
        message.append("Android Version: ")
                .append(android.os.Build.VERSION.RELEASE).append('\n');
        message.append("Board: ").append(android.os.Build.BOARD).append('\n');
        message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
        message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
        message.append("Host: ").append(android.os.Build.HOST).append('\n');
        message.append("ID: ").append(android.os.Build.ID).append('\n');
        message.append("Model: ").append(android.os.Build.MODEL).append('\n');
        message.append("Product: ").append(android.os.Build.PRODUCT)
                .append('\n');
        message.append("Type: ").append(android.os.Build.TYPE).append('\n');
        StatFs stat = getStatFs();
        message.append("Total Internal memory: ")
                .append(getTotalInternalMemorySize(stat)).append('\n');
        message.append("Available Internal memory: ")
                .append(getAvailableInternalMemorySize(stat)).append('\n');
    }

    public void uncaughtException(Thread t, Throwable e) {
        try {
            StringBuilder report = new StringBuilder();
            Date curDate = new Date();
            report.append("Error Report collected on : ")
                    .append(curDate.toString()).append('\n').append('\n');
            report.append("Informations :").append('\n');
            addInformation(report);
            report.append('\n').append('\n');
            report.append("Stack:\n");
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            report.append(result.toString());
            printWriter.close();
            report.append('\n');
            report.append("**** End of current Report ***");
            Log.e(UnCaughtException.class.getName(),
                    "Error while sendErrorMail" + report);
            sendErrorMail(report);
        } catch (Throwable ignore) {
            Log.e(UnCaughtException.class.getName(),
                    "Error while sending error e-mail", ignore);
        }
        // previousHandler.uncaughtException(t, e);
    }

    /**
     * This method for call alert dialog when application crashed!
     */
    @SuppressLint("NewApi")
    public void sendErrorMail(final StringBuilder errorContent) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                builder.setTitle("Sorry...!");
                builder.create();

                builder.setPositiveButton("Report",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                body = new StringBuilder("Hi,");
                                body.append('\n').append('\n');
                                body.append(errorContent).append('\n')
                                        .append('\n');

                                String path = "detail_info.txt";

                                File file = new File("detail_info.txt");

                                FileOutputStream fos = null;

                                try {
                                    fos = new FileOutputStream(file);

                                    // min 9 api
                                    byte[] res = body.toString().getBytes(
                                            Charset.defaultCharset());
                                    fos.write(res);

                                } catch (SQLException | IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                // try {
                                // FileOutputStream fileout = context
                                // .openFileOutput("test.txt",
                                // context.MODE_PRIVATE);
                                // OutputStreamWriter outputWriter = new
                                // OutputStreamWriter(
                                // fileout);
                                // outputWriter.write(body.toString());
                                // outputWriter.close();
                                //
                                // } catch (Exception e) {
                                // e.printStackTrace();
                                // }

                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{


                                                "ruhi.bhandari@quagnitia.com"});//
                                email.putExtra(Intent.EXTRA_SUBJECT, context
                                        .getClass().getName());
                                email.putExtra(Intent.EXTRA_TEXT,
                                        body.toString());

                                File myFile = new File(path);
                                Uri uri = Uri.fromFile(myFile);
                                email.putExtra(Intent.EXTRA_STREAM, uri);

                                email.setType("message/rfc822");
                                context.startActivity(Intent.createChooser(
                                        email, "Choose an Email client :"));

                                // xmlString = new XMLWrite(context)
                                // .getDataForCrashReport(context
                                // .getClass().getName(), body
                                // .toString());

                                Log.e("EXP", body.toString());

                                System.exit(0);
                            }
                        });

                builder.setMessage("Unfortunately, this application has stopped");
                builder.show();

                Looper.loop();
            }
        }.start();
    }
    // //////

}
