package com.example.studentmanagersystem;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import com.example.studentmanagersystem.helper.MySqlHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	private AutoCompleteTextView userName;
	private EditText userPassword;
	private Button btn_register;
	private Button btn_login;
	private Button btn_exit;
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private List<String> lists = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mySqlHelper = new MySqlHelper(MainActivity.this, "student_inf.db",
				null, 1);
		db = mySqlHelper.getWritableDatabase();

		Cursor cursorstu = db.rawQuery("select * from loginhistory", null);
		while (cursorstu.moveToNext()) {
			lists.add(cursorstu.getString(1));
		}

		// System.out.println(lists.get(0).toString());
		userName = (AutoCompleteTextView) findViewById(R.id.userName);
		userName.setThreshold(1);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				MainActivity.this, android.R.layout.simple_list_item_1, lists);
		userName.setAdapter(arrayAdapter);

		userPassword = (EditText) findViewById(R.id.userPassword);
		btn_register = (Button) findViewById(R.id.register);
		btn_login = (Button) findViewById(R.id.login);
		btn_exit = (Button) findViewById(R.id.exit);
		btn_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMyDialog();
			}
		});
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
			}
		});
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	//注册
	public void showMyDialog() {
		Builder builder = new Builder(MainActivity.this);
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View view = inflater.inflate(R.layout.registerlayout, null);
		Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
		Button btn_back = (Button) view.findViewById(R.id.btn_back);
		final EditText editUserName = (EditText) view
				.findViewById(R.id.editText1);
		final EditText editPswd = (EditText) view.findViewById(R.id.editText2);
		final EditText editPswd_confirm = (EditText) view
				.findViewById(R.id.editText3);
		final AlertDialog dialog = builder.setTitle("学生信息注册").setView(view)
				.create();

		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((editPswd.getText().toString()).equals((editPswd_confirm
						.getText().toString()))) {
					Cursor cursor = db.rawQuery(
							"select count(*) from user where username = '"
									+ editUserName.getText().toString() + "'",
							null);
					cursor.moveToNext();
					int count = cursor.getInt(0);
					if (count == 0) {
						ContentValues values = new ContentValues();
						values.put("username", editUserName.getText()
								.toString());
						values.put("password", MD5(editPswd.getText().toString()));
						db.insert("user", null, values);
						dialog.dismiss();
						Toast.makeText(MainActivity.this, "注册成功！",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MainActivity.this, "您注册的用户名已存在！",
								Toast.LENGTH_SHORT).show();
					}
					cursor.close();
				} else {
					Toast.makeText(MainActivity.this, "您两次输入的密码不一样！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		dialog.show();
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

	}

	//登录
	public void login() {
		Cursor cursor1 = db.rawQuery(
				"select count(*) from user where username = '"
						+ userName.getText().toString() + "'", null);
		cursor1.moveToNext();
		int count = cursor1.getInt(0);
		if (count == 1) {
			Cursor cursor = db.rawQuery(
					"select username,password from user where username = '"
							+ userName.getText().toString() + "'", null);
			cursor.moveToNext();
			if ((userName.getText().toString()).equals(cursor.getString(0)
					.toString())
					&& (MD5(userPassword.getText().toString())).equals(cursor
							.getString(1).toString())) {
				Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(MainActivity.this,
						StudentInformationManagerActivity.class);
				startActivity(intent);
				Cursor cursor2 = db.rawQuery(
						"select count(*) from loginhistory where name = '"
								+ userName.getText().toString() + "'", null);
				cursor2.moveToNext();
				int count2 = cursor2.getInt(0);
				if (count2 == 0) {
					ContentValues values = new ContentValues();
					values.put("name", userName.getText().toString());
					db.insert("loginhistory", null, values);
				}
			} else {
				Toast.makeText(MainActivity.this, "您输入的用户名或密码错误！",
						Toast.LENGTH_SHORT).show();
			}
			cursor.close();
		} else {
			Toast.makeText(MainActivity.this, "您输入的用户名或密码错误！",
					Toast.LENGTH_SHORT).show();
		}
		cursor1.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// MD5加密
	public static String MD5(String string) {
		return encodeMD5String(string);
	}

	public static String encodeMD5String(String str) {
		return encode(str, "MD5");
	}

	private static String encode(String str, String method) {
		MessageDigest md = null;
		String dstr = null;
		try {
			md = MessageDigest.getInstance(method);
			md.update(str.getBytes());
			dstr = new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dstr;
	}

}
