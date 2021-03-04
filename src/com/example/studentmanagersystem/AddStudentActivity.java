package com.example.studentmanagersystem;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.studentmanagersystem.helper.MySqlHelper;

public class AddStudentActivity extends Activity {

	private Spinner spinner;
	private String arrs[];
	private EditText stu_bir;
	private Calendar calendar;
	private DatePickerDialog datePickerDialog;
	private Button stu_bir_choose;
	private Button add_submit;
	private Button add_cancel;
	private EditText stu_name;
	private EditText stu_id;
	private EditText stu_phone;
	private EditText stu_more;
	private RadioGroup stu_rg;
	private RadioButton stu_rb1;
	private RadioButton stu_rb2;
	private String sex = "男";
	private String mingzu;
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private int headimageid = R.drawable.image;
	private ImageView headimage;
	private final int CODE = 1;

	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			stu_bir.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addstudentlayout);

		mySqlHelper = new MySqlHelper(AddStudentActivity.this,
				"student_inf.db", null, 1);
		db = mySqlHelper.getWritableDatabase();
		headimage = (ImageView) findViewById(R.id.headImage);
		headimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//selectImage();
				chooseImage();
			}
		});
		stu_name = (EditText) findViewById(R.id.stu_name);
		stu_id = (EditText) findViewById(R.id.stu_id);
		stu_phone = (EditText) findViewById(R.id.stu_phone);
		stu_more = (EditText) findViewById(R.id.stu_more);

		spinner = (Spinner) findViewById(R.id.spinner_mz);
		arrs = getResources().getStringArray(R.array.listArr);
		ArrayAdapter<String> arrsAdapter = new ArrayAdapter<String>(
				AddStudentActivity.this, android.R.layout.simple_list_item_1,
				arrs);
		spinner.setAdapter(arrsAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				mingzu = arrs[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		stu_rg = (RadioGroup) findViewById(R.id.stu_rg);
		stu_rb1 = (RadioButton) findViewById(R.id.stu_rb1);
		stu_rb2 = (RadioButton) findViewById(R.id.stu_rb2);
		stu_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1 == stu_rb1.getId()) {
					sex = "男";
				} else {
					sex = "女";
				}
			}
		});

		stu_bir = (EditText) findViewById(R.id.stu_bir);
		calendar = Calendar.getInstance();
		int year = calendar.get(calendar.YEAR);
		int month = calendar.get(calendar.MONTH);
		int day = calendar.get(calendar.DAY_OF_MONTH);
		datePickerDialog = new DatePickerDialog(AddStudentActivity.this,
				listener, year, month, day);
		stu_bir_choose = (Button) findViewById(R.id.stu_bir_choose);
		add_submit = (Button) findViewById(R.id.add_submit);
		add_cancel = (Button) findViewById(R.id.add_cancel);
		stu_bir_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				datePickerDialog.show();
			}
		});
		add_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addStudentInf();
			}
		});
		add_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}


	public void addStudentInf() {

		if (stu_name.getText().toString().equals("")
				|| stu_id.getText().toString().equals("")
				|| stu_bir.getText().toString().equals("")
				|| stu_phone.getText().toString().equals("")) {
			Toast.makeText(AddStudentActivity.this, "您输入的信息不完整！",
					Toast.LENGTH_SHORT).show();
		} else {
			Bitmap bitmap = ((BitmapDrawable)headimage.getDrawable()).getBitmap();
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			// 压缩bitmap到ByteArrayOutputStream
			bitmap.compress(CompressFormat.PNG, 100, byteOut);
			
			ContentValues values = new ContentValues();
			values.put("name", stu_name.getText().toString());
			values.put("sex", sex);
			values.put("mingzu", mingzu);
			values.put("id", stu_id.getText().toString());
			values.put("birthday", stu_bir.getText().toString());
			values.put("phone", stu_phone.getText().toString());
			values.put("more", stu_more.getText().toString());
			values.put("image", byteOut.toByteArray());
			db.insert("student", null, values);
			Toast.makeText(AddStudentActivity.this, "添加成功！", Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

	private void chooseImage() {
		// 选择相册
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CODE) {
			if (resultCode == Activity.RESULT_OK && null != data) {
				Uri selectedImagePath = data.getData();
				Cursor cursor = getContentResolver().query(selectedImagePath,
						null, null, null, null);

				if (cursor == null) {
					Toast.makeText(AddStudentActivity.this, "cursor == null",
							2000).show();
				} else {
					cursor.moveToFirst();
					String img = cursor.getString(1);
					// System.out.println("img:" + img);
					//
					// student.setImgPath(img);
					cursor.close();
					Bitmap bitmap = BitmapFactory.decodeFile(img);

					headimage.setImageBitmap(bitmap);
				}
			}
		}
	}
}
