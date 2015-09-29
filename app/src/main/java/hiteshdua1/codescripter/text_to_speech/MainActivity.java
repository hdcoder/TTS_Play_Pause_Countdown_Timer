package hiteshdua1.codescripter.text_to_speech;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech mTts;
    private int mStatus = 0;
    private MediaPlayer mMediaPlayer;
    private boolean mProcessed = false;
    private final String FILENAME = "/wpta_tts.wav";
    private ProgressDialog mProgressDialog;
    String Question_Text="I am an Android Developer, Who are You ?";
    TextView tv; // textview to display the countdown
    MediaPlayer mMediaPlayer_audio;
    MyCount counter;
    long seconds_left;
    Context context;
    @SuppressWarnings("deprecation")
    @TargetApi(15)
    public void setTts(TextToSpeech tts) {
        this.mTts = tts;

        if (Build.VERSION.SDK_INT >= 15) {
            this.mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onDone(String utteranceId) {
                    // Speech file is created
                    mProcessed = true;

                    // Initializes Media Player
                    initializeMediaPlayer();

                    // Start Playing Speech
                    playMediaPlayer(0);
                }

                @Override
                public void onError(String utteranceId) {
                }

                @Override
                public void onStart(String utteranceId) {
                }
            });
        } else {
            this.mTts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                @Override
                public void onUtteranceCompleted(String utteranceId) {
                    // Speech file is created
                    mProcessed = true;

                    // Initializes Media Player
                    initializeMediaPlayer();

                    // Start Playing Speech
                    playMediaPlayer(0);
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiating TextToSpeech class
        mTts = new TextToSpeech(this, this);

        tv = (TextView)findViewById(R.id.countdown_timer);

        // 10000 is the starting number (in milliseconds)
        // 1000 is the number to count down each time (in milliseconds)
        mMediaPlayer = new MediaPlayer();
        counter = new MyCount(15000, 1000);
        counter.start();

        context = this;
        // Getting reference to the button btn_speek
        ImageButton btn_play = (ImageButton) findViewById(R.id.btn_play);
        ImageButton question_btn = (ImageButton) findViewById(R.id.question_btn);
        question_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("QUESTION");
                alertDialogBuilder.setMessage(Question_Text);

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ImageButton btn_stop = (ImageButton) findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
            }
        });
        // Creating a progress dialog window
        mProgressDialog = new ProgressDialog(this);

        // Creating an instance of MediaPlayer
        mMediaPlayer = new MediaPlayer();

        // Close the dialog window on pressing back button
        mProgressDialog.setCancelable(true);

        // Setting a horizontal style progress bar
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        /** Setting a message for this progress dialog
         * Use the method setTitle(), for setting a title
         * for the dialog window
         *  */
        mProgressDialog.setMessage("Please wait ...");

        // Defining click event listener for the button btn_speak
        View.OnClickListener btnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mStatus == TextToSpeech.SUCCESS) {

                    // Getting reference to the Button
                    ImageButton btn_play = (ImageButton) findViewById(R.id.btn_play);
                    btn_play.setImageResource(R.drawable.pause_);
                    counter.cancel();
                    counter = new MyCount(seconds_left, 1000);
                    counter.start();

                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        playMediaPlayer(1);
                        btn_play.setImageResource(R.drawable.play_);
                        counter.cancel();
                        return;
                    }

                    mProgressDialog.show();

                    // Getting reference to the EditText et_content
//                    EditText etContent = (EditText) findViewById(R.id.et_content);

                    HashMap<String, String> myHashRender = new HashMap();
                    String utteranceID = "wpta";
                    myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceID);

                    String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + FILENAME;


                    File fileTTS = new File(fileName);

                    if (fileTTS.exists()) {
                        Log.d("PROJECT", "successfully created fileTTS");
                    } else {
                        Log.d("PROJECT", "failed while creating fileTTS");
                    }

                    if (!mProcessed) {
                        int status = mTts.synthesizeToFile(Question_Text, null, fileTTS, fileName);
                    } else {
                        playMediaPlayer(0);
                    }
                } else {
                    String msg = "TextToSpeech Engine is not initialized";
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Set Click event listener for the button btn_speak
        btn_play.setOnClickListener(btnClickListener);

//        // Getting reference to the EditText et_content
//        EditText etContent = (EditText) findViewById(R.id.et_content);
//
//        etContent.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                mProcessed = false;
//                mMediaPlayer.reset();
//
//                // Getting reference to the button btn_speek
//                Button btnSpeek = (Button) findViewById(R.id.btn_speak);
//
//                // Changing button Text to Speek
//                btnSpeek.setText("Speek");
//            }
//        });

        MediaPlayer.OnCompletionListener mediaPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // Getting reference to the button btn_speek
                ImageButton btn_play = (ImageButton) findViewById(R.id.btn_play);

                // Changing button Text to Speek
                btn_play.setImageResource(R.drawable.play_);
            }
        };

        mMediaPlayer.setOnCompletionListener(mediaPlayerCompletionListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {

        // Stop the TextToSpeech Engine
        mTts.stop();

        // Shutdown the TextToSpeech Engine
        mTts.shutdown();

        // Stop the MediaPlayer
        mMediaPlayer.stop();

        // Release the MediaPlayer
        mMediaPlayer.release();

        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        mStatus = status;
        setTts(mTts);
    }

    private void initializeMediaPlayer() {
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + FILENAME;

        Uri uri = Uri.parse("file://" + fileName);

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mMediaPlayer.setDataSource(getApplicationContext(), uri);
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playMediaPlayer(int status) {
        mProgressDialog.dismiss();

        // Start Playing
        if (status == 0) {
            mMediaPlayer.start();
        }

        // Pause Playing
        if (status == 1) {
            mMediaPlayer.pause();
        }
    }

    // countdowntimer is an abstract class, so extend it and fill in methods
    public class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mMediaPlayer_audio.release();
            tv.setText("done!");
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            seconds_left =millisUntilFinished;
            tv.setText("Left: " + millisUntilFinished / 1000);

            if ((millisUntilFinished / 1000) <= 3) {
                mMediaPlayer_audio = MediaPlayer.create(getApplicationContext(), R.raw.ding);
                mMediaPlayer_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer_audio.start();
            }

        }
    }
    private void stopPlaying() {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.stop();
        }
    }
}
