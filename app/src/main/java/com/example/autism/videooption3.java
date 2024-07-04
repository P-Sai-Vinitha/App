package com.example.autism;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class videooption3 extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;
    private String selectedVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videooption);
        Button selectBtn = findViewById(R.id.button15);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectVideoFromGallery();


            }
        });
        Button b1 = findViewById(R.id.button16);
        b1.setOnClickListener(view -> {
            if (selectedVideoPath != null) {
                System.out.println(selectedVideoPath);
                uploadVideo(selectedVideoPath);
            } else {
                System.out.println("adadadada");
                Toast.makeText(videooption3.this, "Please select a video first", Toast.LENGTH_SHORT).show();
            }

        });


    }
    private void selectVideoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedVideoUri = data.getData();
            selectedVideoPath = getPathFromUri(selectedVideoUri);
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
    private void uploadVideo(final String selectedPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String serverURL = ip.ipn+"videooption3.php";
                    HttpURLConnection connection = null;
                    DataOutputStream dataOutputStream = null;
                    FileInputStream fileInputStream = new FileInputStream(selectedPath);

                    URL url = new URL(serverURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=***");
                    connection.setRequestProperty("uploaded_file", selectedPath);

                    dataOutputStream = new DataOutputStream(connection.getOutputStream());
                    dataOutputStream.writeBytes("--***\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + selectedPath + "\"" + "\r\n");
                    dataOutputStream.writeBytes("\r\n");

                    int bytesRead, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1 * 1024 * 1024;

                    bufferSize = Math.min(fileInputStream.available(), maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bufferSize = Math.min(fileInputStream.available(), maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    dataOutputStream.writeBytes("\r\n");
                    dataOutputStream.writeBytes("--***--\r\n");

                    int serverResponseCode = connection.getResponseCode();
                    String serverResponseMessage = connection.getResponseMessage();

                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    if (serverResponseCode == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(videooption3.this, "Video uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(videooption3.this, "Failed to upload video", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (MalformedURLException e) {
                    System.out.println("111111");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("222222");
                    e.printStackTrace();
                }
            }
        }).start();
    }


}