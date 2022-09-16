package com.ceni.recensementnumerique;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceni.model.Electeur;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class ImageFicheActivity extends AppCompatActivity {

    static ImageFicheActivity imageFicheActivity;
    private Button buttonImage;
    private ImageView imageView;
    private ImageView next;
    private ImageView previous;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private String dataFicheElect,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageFicheActivity = this;
        setContentView(R.layout.activity_imagefiche);

        this.buttonImage = (Button) this.findViewById(R.id.button_image);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        this.next = (ImageView) this.findViewById(R.id.imageViewNext);
        this.previous = (ImageView) this.findViewById(R.id.imageViewPrevious);
        user = getIntent().getStringExtra("user");

        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        this.next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Electeur electeur = gson.fromJson(getIntent().getStringExtra("newElect"), Electeur.class);
                if(dataFicheElect!=null) {
                    electeur.setFicheElect(dataFicheElect);
                    String myjson = gson.toJson(electeur);
                    Log.i("Electeur", myjson);
                    Intent i = new Intent(getApplicationContext(), ImageCinActivity.class);
                    i.putExtra("newElect", myjson);
                    i.putExtra("user", user);
                    startActivity(i);
                }else{
                    new AlertDialog.Builder(ImageFicheActivity.this)
                            .setTitle("Fahadisoana?")
                            .setMessage("Iangaviana ianao mba haka sary azafady.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // tsisy
                                }
                            }).show();
                }

            }
        });
        this.previous.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static ImageFicheActivity getInstance(){
        return imageFicheActivity;
    }
    private void captureImage() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }
    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");

                //Encode Image
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                dataFicheElect = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.i("IMAGE",""+Base64.encodeToString(byteArray, Base64.DEFAULT));
                this.imageView.setImageBitmap(bp);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}