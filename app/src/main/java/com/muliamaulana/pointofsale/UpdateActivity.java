package com.muliamaulana.pointofsale;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.muliamaulana.pointofsale.database.ItemHelper;
import com.muliamaulana.pointofsale.database.ItemModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    public final int REQUEST_CODE_GALLERY = 100;
    EditText edtName, edtPrice;
    ImageView imageView;
    Button btnSave, btnDelete;
    private ItemModel itemModel;
    private ItemHelper itemHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        edtName = findViewById(R.id.edit_name);
        edtPrice = findViewById(R.id.edit_price);
        imageView = findViewById(R.id.edit_image);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);

        getSupportActionBar().setTitle("Edit Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemHelper = new ItemHelper(this);
        itemHelper.open();
        itemModel = new ItemModel();

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        int price = intent.getIntExtra("price",0);
        final int id = intent.getIntExtra("id", -1);
        Cursor cursor = itemHelper.getItemImage(id);
        byte[] image = null;
        while (cursor.moveToNext()) {
            image = cursor.getBlob(3);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        String strPrice = String.valueOf(price);
        edtName.setText(name);
        edtPrice.setText(strPrice);
        imageView.setImageBitmap(bitmap);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        UpdateActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        edtPrice.addTextChangedListener(onTextChangedListener());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = edtName.getText().toString().trim();
                String newPrice = edtPrice.getText().toString().trim();
                if (newPrice.contains(",")) {
                    newPrice = newPrice.replaceAll(",", "");
                }
                int newIntPrice = Integer.parseInt(newPrice);
                boolean isEmpty = false;

                if (TextUtils.isEmpty(name)) {
                    isEmpty = true;
                    edtName.setError("Name can not be blank");

                } else if(TextUtils.isEmpty(newPrice)){
                    isEmpty = true;
                    edtPrice.setError("Price can not be blank");
                }

                if (!isEmpty) {
                    itemModel.setId(id);
                    itemModel.setName(newName);
                    itemModel.setPrice(newIntPrice);
                    byte[] image = imageViewToByte(imageView);
                    itemModel.setImage(image);
                    itemHelper.update(itemModel);

                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dialogTitle = "Delete";
                String dialogMessage = "Are you sure want to delete item " + name + "?";
                AlertDialog.Builder alert = new AlertDialog.Builder(UpdateActivity.this);
                alert.setTitle(dialogTitle);
                alert.setMessage(dialogMessage).setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                itemHelper.delete(id);
                                Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }

    private TextWatcher onTextChangedListener() {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtPrice.removeTextChangedListener(this);
                try{
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")|| originalString.contains(".")) {
                        originalString = originalString.replaceAll(",", "");
                        originalString = originalString.replace(".", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    edtPrice.setText(formattedString);
                    edtPrice.setSelection(edtPrice.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                edtPrice.addTextChangedListener(this);
            }
        };
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1, 1)// image will be square
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
