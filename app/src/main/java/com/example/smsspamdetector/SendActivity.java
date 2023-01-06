package com.example.smsspamdetector;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SendActivity extends AppCompatActivity {

    EditText message;
    Button send;
    Button user;
    TextView name;
    TextView number;

    private String phNum;

    int SELECT_PHONE_NUMBER = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        message = findViewById(R.id.editTextMessage);
        send = findViewById(R.id.sendBtn);
        user = findViewById(R.id.userSelect);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, SELECT_PHONE_NUMBER);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("smsto:" + phNum)); // This ensures only SMS apps respond
                intent.putExtra("sms_body", message.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor cursor = this.getContentResolver().query(contactUri, projection,
                    null, null, null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int numberIndex1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String Number = cursor.getString(numberIndex);
                String Username = cursor.getString(numberIndex1);

                phNum = Number;

                name.setText(Username);
                name.setVisibility(View.VISIBLE);

                number.setText(Number);
                number.setVisibility(View.VISIBLE);

                // Do something with the phone number
            }

            assert cursor != null;
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}