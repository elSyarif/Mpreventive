package monitoring.com.mpreventive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import monitoring.com.mpreventive.app.AppController;

//public class Inspeksi_Fragment extends AppCompatActivity implements View.OnClickListener {
public class Inspeksi_Fragment extends AppCompatActivity {
    ActionBar actionBar;
    TextView text, txt_tgl;
    String KEY_TAG = "TAG_IN";
    String Tag_Mesin, id, name, ID_MESIN, NO_MESIN;
//    public String url = Server.URL_TambahIns;
    SharedPreferences sharedpreferences;
    private static final String TAG = Inspeksi_Fragment.class.getSimpleName();

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_ID_M = "ID";
    public static final String TAG_NO_M = "No_Mesin";

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    int success;

    EditText txt_id_mesin, txt_id_user, txt_tanggal, txt_ovH, txt_ovV, txt_bvH, txt_bvV, txt_temp_drive, txt_non_ovH, txt_non_ovV, txt_non_bvH, txt_non_bvV, txt_Temp_non_drive, txt_oil_status;
    Button btn_Simpan;
    ProgressDialog pd;

    // inisialisasi File dan Camera
    Intent intent;
    Uri fileUri;
    Button capture_Btn;
    ImageView imageView;
    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 100; // range 1 - 100
    int max_resolution_image = 800;
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspeksi_proses);

        actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Tambah Inspeksi");
        String title = actionBar.getTitle().toString();

        capture_Btn = (Button) findViewById(R.id.btn_take_img);
        imageView = (ImageView) findViewById(R.id.ins_img);

        text = (TextView) findViewById(R.id.text);
        txt_tgl = (TextView) findViewById(R.id.Tgl);

        txt_id_mesin = (EditText) findViewById(R.id.ins_id_Mesin);
        txt_id_user = (EditText) findViewById(R.id.ins_id_user);
        txt_tanggal = (EditText) findViewById(R.id.ins_tgl);
        txt_ovH = (EditText) findViewById(R.id.ins_ovH);
        txt_ovV = (EditText) findViewById(R.id.ins_ovV);
        txt_bvH = (EditText) findViewById(R.id.ins_bvH);
        txt_bvV = (EditText) findViewById(R.id.ins_bvV);
        txt_temp_drive = (EditText) findViewById(R.id.ins_Temp_Drive);
        txt_non_ovH = (EditText) findViewById(R.id.ins_non_ovH);
        txt_non_ovV = (EditText) findViewById(R.id.ins_non_ovV);
        txt_non_bvH = (EditText) findViewById(R.id.ins_non_bvH);
        txt_non_bvV = (EditText) findViewById(R.id.ins_non_bvV);
        txt_Temp_non_drive = (EditText) findViewById(R.id.ins_Temp_non_Drive);
        txt_oil_status = (EditText) findViewById(R.id.image_txt);

        imageView.getDrawable();
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int tahun, int bulan, int tgl) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, tahun);
                myCalendar.set(Calendar.MONTH, bulan);
                myCalendar.set(Calendar.DAY_OF_MONTH, tgl);
                updateLabel();
            }
        };
        txt_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Inspeksi_Fragment.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        capture_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    pilihGambar();
                }
            }
        });

        btn_Simpan = (Button) findViewById(R.id.ins_simpan);
//        btn_Simpan.setOnClickListener(this);


        sharedpreferences = getSharedPreferences(Inspeksi_Activity.my_shared_preferences, Context.MODE_PRIVATE);
        id = getIntent().getStringExtra(TAG_ID);  //set pada filed inspeksi
        name = getIntent().getStringExtra(TAG_NAME);
        ID_MESIN = getIntent().getStringExtra(TAG_ID_M); //set pada field inspeksii
        NO_MESIN = getIntent().getStringExtra(TAG_NO_M);


        txt_id_mesin.setText(NO_MESIN);
        txt_id_user.setText(name);

        btn_Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_tanggal.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "Tanggal Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_ovH.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "OV H Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_ovV.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "OV V Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_bvH.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "BV H Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_bvV.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "BV V Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_temp_drive.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "Temperatur Drive Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_non_ovH.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "NON OV H Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_non_ovV.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "NON OV V Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_non_bvH.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "NON BV H Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_non_bvV.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "NON BV V Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_Temp_non_drive.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "Temperatur NON Drive Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else if (txt_oil_status.length() == 0) {
                    Toast.makeText(Inspeksi_Fragment.this, "Status Oli Wajib Di Isi!", Toast.LENGTH_LONG).show();
                }
                else {
                    SimpanInspeksi();
                }
            }
        });
//        btn_Simpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TambahInspeksi();
//            }
//        });

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txt_tanggal.setText(sdf.format(myCalendar.getTime()));
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void SimpanInspeksi() {
        final String id_mesin = ID_MESIN;
        final String id_user = id;
        final String tanggal = txt_tanggal.getText().toString().trim();
        final String ovH = txt_ovH.getText().toString().trim();
        final String ovV = txt_ovV.getText().toString().trim();
        final String bvH = txt_bvH.getText().toString().trim();
        final String bvV = txt_bvV.getText().toString().trim();
        final String Temp_Drive = txt_temp_drive.getText().toString().trim();
        final String non_ovH = txt_non_ovH.getText().toString().trim();
        final String non_ovV = txt_non_ovV.getText().toString().trim();
        final String non_bvH = txt_non_bvH.getText().toString().trim();
        final String non_bvV = txt_non_bvV.getText().toString().trim();
        final String Temp_non_Drive = txt_Temp_non_drive.getText().toString().trim();
        final String oil_sail = getStringImage(decoded);
        final String oil_status = txt_oil_status.getText().toString().trim();


        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL_Inspeksi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.e(TAG, jObj.toString());

                        Toast.makeText(Inspeksi_Fragment.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

//                        intent = new Intent(Inspeksi_Fragment.this, Inspeksi_Activity.class);
//                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(Inspeksi_Fragment.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //menghilangkan progress dialog
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //menghilangkan progress dialog
                loading.dismiss();

                //menampilkan toast
                Toast.makeText(Inspeksi_Fragment.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                Log.e(TAG, error.getMessage().toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(Server.KEY_EMP_ID_MESIN, id_mesin);
                params.put(Server.KEY_EMP_ID_USER, id_user);
                params.put(Server.KEY_EMP_TGL_INS, tanggal);
                params.put(Server.KEY_EMP_OV_H, ovH);
                params.put(Server.KEY_EMP_OV_V, ovV);
                params.put(Server.KEY_EMP_BV_H, bvH);
                params.put(Server.KEY_EMP_BV_V, bvV);
                params.put(Server.KEY_EMP_TEMP_DRIVE, Temp_Drive);
                params.put(Server.KEY_EMP_NON_OV_H, non_ovH);
                params.put(Server.KEY_EMP_NON_OV_V, non_ovV);
                params.put(Server.KEY_EMP_NON_BV_H, non_bvH);
                params.put(Server.KEY_EMP_NON_BV_V, non_bvV);
                params.put(Server.KEY_EMP_TEMP_NON_DRIVE, Temp_non_Drive);
                params.put(Server.KEY_EMP_OIL, oil_sail);
                params.put(Server.KEY_EMP_OIL_KONDISI, oil_status);

                //kembali ke parameters
                Log.e(TAG, "Inspeksi" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void pilihGambar() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        Intent takePictureInten  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureInten.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureInten, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//&& data != null && data.getData() != null
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //menampilkan ke thumnuil
                Bundle extra = data.getExtras();
                Bitmap Imagebitmap = (Bitmap) extra.get("data");
//                Imagebitmap = BitmapFactory.decodeFile(fileUri.getPath());
                imageView.setImageBitmap(Imagebitmap);

                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(Imagebitmap, 1024));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    private void kosong() {
//        imageView.setImageResource(0);
//        txt_tanggal.setText(null);
//        txt_ovH.setText(null);
//        txt_ovV.setText(null);
//        txt_bvH.setText(null);
//        txt_bvV.setText(null);
//        txt_temp_drive.setText(null);
//        txt_non_ovH.setText(null);
//        txt_non_ovV.setText(null);
//        txt_non_bvH.setText(null);
//        txt_non_bvV.setText(null);
//        txt_Temp_non_drive.setText(null);
//        intent = new Intent(Inspeksi_Fragment.this, Inspeksi_Activity.class);
//    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MonPrev");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_MonPrev_" + timeStamp + ".png");

        return mediaFile;
    }
}
