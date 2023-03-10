package com.example.smsspamdetector;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.text.nlclassifier.BertNLClassifier;

import java.util.List;


public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestExp";
    EditText messageInput;
    Button predictBtn;
    TextView result;

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        messageInput = findViewById(R.id.msg_text_example);
        predictBtn = findViewById(R.id.predict_btn_example);
        result = findViewById(R.id.result_example);

        handler = new Handler();

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data = messageInput.getText().toString();
                getDataFromModel(data);

            }
        });

    }

    private void getDataFromModel(String data) {

        classify(data);

    }

    private void classify(final String text) {

        handler.post(
                () -> {

                    BertNLClassifier classifier = null;

                    try {

                        classifier = BertNLClassifier.createFromFile(TestActivity.this,"model.tflite");

                        List<Category> results = classifier.classify(text);

                        Log.e(TAG, "isSpamMessage: msg : "+text+"\n 0 : "+results.get(0).getLabel() + " : "+results.get(0).getScore()+"\n" );
                        Log.e(TAG, "isSpamMessage: 1 : "+results.get(1).getLabel() + " : "+results.get(1).getScore()+"\n" );

                        String data = results.get(0).getLabel()+":"+results.get(0).getScore();

                        data += "\n"+results.get(1).getLabel()+":"+results.get(1).getScore();

                        result.setText(data);
                    } catch (Exception e){

                        Log.e(TAG, "classify: "+e.getMessage());

                    }

                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");

    }

}