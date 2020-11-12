package com.naver.naverspeech.client;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.naverspeech.client.utils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends Activity {

    int w, h;
    private ClientNet CN;

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String CLIENT_ID = "g23iinslr3";
    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult, txt1;
    private ImageButton btnStart;
    private String mResult, iResult;

    private AudioWriterPCM writer;

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        Intent intent;
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txt1.setText("Connected");
                w = txtResult.getWidth();
                h = txtResult.getHeight();
                txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txt1.setText(mResult);
                iResult = mResult;
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
            	SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
            	List<String> results = speechRecognitionResult.getResults();
            	StringBuilder strBuf = new StringBuilder();
            	for(String result : results) {
            		strBuf.append(result);
            		strBuf.append("\n");
            	}
                mResult = strBuf.toString();

            	/*if(iResult.contains("전화")) {
            	    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0539505114"));
            	    startActivity(intent);
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                */
                if(iResult.contains("불") && iResult.contains("켜")) {
                    Toast.makeText(getApplicationContext(), iResult + " Light ON ", Toast.LENGTH_SHORT).show();
                    CN = new ClientNet();
                    CN.LightOn();
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    //txt1.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else if(iResult.contains("불") && iResult.contains("꺼")) {
                    Toast.makeText(getApplicationContext(), iResult + " Light OFF ", Toast.LENGTH_SHORT).show();
                    CN = new ClientNet();
                    CN.LightOff();
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    //txt1.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else if(iResult.contains("에어컨") && iResult.contains("켜")) {
                    Toast.makeText(getApplicationContext(), iResult + " AIR ON ", Toast.LENGTH_SHORT).show();
                    CN = new ClientNet();
                    CN.AirOn();
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    //txt1.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else if(iResult.contains("에어컨") && iResult.contains("꺼")) {
                    Toast.makeText(getApplicationContext(), iResult + " AIR OFF ", Toast.LENGTH_SHORT).show();
                    CN = new ClientNet();
                    CN.AirOff();
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    //txt1.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else if(iResult.contains("보일러") && iResult.contains("켜")) {
                    Toast.makeText(getApplicationContext(), iResult + " Boiler ON ", Toast.LENGTH_SHORT).show();
                    CN = new ClientNet();
                    CN.BoilerOn();
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    //txt1.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else if(iResult.contains("보일러") && iResult.contains("꺼")) {
                    Toast.makeText(getApplicationContext(), iResult + " Boiler OFF ", Toast.LENGTH_SHORT).show();
                    CN = new ClientNet();
                    CN.BoilerOff();
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    //txt1.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else {
            	    try{
            	        Thread.sleep(2000);
                    }
                    catch(Exception e){

                    }
                    //txt1.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "";////////////////////////////////
                txt1.setText("");/////////////////////////////
                mResult = "Error code : " + msg.obj.toString();
                txt1.setText(mResult);
                btnStart.setImageResource(R.drawable.ic_launcher1);
                btnStart.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }
                mResult = "";////////////////////////////////
                txt1.setText("");/////////////////////////////
                btnStart.setImageResource(R.drawable.ic_launcher1);
                btnStart.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.layout);

        CN = new ClientNet();
        //
        //

        txtResult = (TextView) findViewById(R.id.txt_result);
        txt1 = (TextView) findViewById(R.id.txt1);
        btnStart = (ImageButton) findViewById(R.id.btn_start);

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    int permissionResult = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
                    if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("권한이 필요합니다.").setMessage("이 기능을 사용하기 위해서는 권한이 필요합니다.").setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1000);
                                    }

                                }
                            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();

                                }
                            }).create().show();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1000);
                        }
                    } else {
                        if (!naverRecognizer.getSpeechRecognizer().isRunning()) {
                            // Start button is pushed when SpeechRecognizer's state is inactive.
                            // Run SpeechRecongizer by calling recognize().
                            mResult = "";
                            txt1.setText("Connecting...");
                            btnStart.setImageResource(R.drawable.ic_launcher2);
                            naverRecognizer.recognize();
                        } else {
                            Log.d(TAG, "stop and wait Final Result");
                            btnStart.setEnabled(false);
                            naverRecognizer.getSpeechRecognizer().stop();
                        }
                    }
                }
                else
                {
                    if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                        // Start button is pushed when SpeechRecognizer's state is inactive.
                        // Run SpeechRecongizer by calling recognize().
                        mResult = "";
                        txt1.setText("Connecting...");
                        btnStart.setImageResource(R.drawable.ic_launcher);
                        naverRecognizer.recognize();
                    } else {
                        Log.d(TAG, "stop and wait Final Result");
                        btnStart.setEnabled(false);
                        naverRecognizer.getSpeechRecognizer().stop();
                    }
                }
                /*if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    txtResult.setText("Connecting...");
                    btnStart.setImageResource(R.drawable.ic_launcher);
                    naverRecognizer.recognize();
                } else {
                    Log.d(TAG, "stop and wait Final Result");
                    btnStart.setEnabled(false);
                    naverRecognizer.getSpeechRecognizer().stop();
                }*/
                txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
            }
        });


    }



    @Override
    protected void onStart() {
    	super.onStart();
    	// NOTE : initialize() must be called on start time.
    	naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mResult = "";
        txt1.setText("");
        btnStart.setImageResource(R.drawable.ic_launcher1);
        btnStart.setEnabled(true);
    }

    @Override
    protected void onStop() {
    	super.onStop();
    	// NOTE : release() must be called on stop time.
    	naverRecognizer.getSpeechRecognizer().release();
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        RecognitionHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}
