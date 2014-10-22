package com.bettycc.webedittext.library;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;


/**
 * Created by ccheng on 10/22/14.
 */
public class WebEditText extends LinearLayout implements View.OnClickListener, Handler.Callback {

    private AsyncHttpServer mServer;
    private Handler mMainHandler = new Handler(this);
    private final EditText mEditText;
    private final Button mBtn;

    public WebEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);

        mEditText = new EditText(context);
        addView(mEditText);

        mBtn = new Button(context);
        mBtn.setText("start server");
        mBtn.setOnClickListener(this);
        addView(mBtn);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initServer();
    }

    private void initServer() {
        mServer = new AsyncHttpServer();
    }

    @Override
    public void onClick(View v) {
        startServer();
    }

    private void startServer() {
        mServer.get("/e", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                InputStream in = null;
                try {
                    in = getContext().getAssets().open("index.html");
                    response.sendStream(in, in.available());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mServer.post("/update-field", new HttpServerRequestCallback() {
            @Override
            public void onRequest(final AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
                final UrlEncodedFormBody body = (UrlEncodedFormBody) request.getBody();
                for (NameValuePair pair : body.get()) {
                    final String newContent = pair.getName();
                    System.out.println("pair.getName() = " + newContent);
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateEditText(newContent);
                        }
                    });
                }
                response.send("finish");
            }
        });


        mServer.listen(5001);

        showServerInfo();
        mBtn.setEnabled(false);
    }

    private void updateEditText(String newContent) {
        System.out.println("newContent = " + newContent);
        mEditText.setText(newContent);
        mEditText.setSelection(newContent.length());
    }

    private void showServerInfo() {
        String info = String.format("open %s", getIPAddress() + ":5001/e");
        mBtn.setText(info);
    }

    public static String getIPAddress() {
        try {
            String ipv4 = "";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (isIPv4) {
                            ipv4 = sAddr;
                        }
                    }
                }
            }
            return ipv4;
        } catch (Exception ex) {
            // for now eat exceptions
        }
        return "";
    }

    public void stopServer() {
        System.out.println("WebEditText.stopServer");
        mServer.stop();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
