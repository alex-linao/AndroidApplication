package com.example.studentmanagersystem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentmanagersystem.helper.MySqlHelper;
import com.example.studentmanagersystem.pojo.LoginUser;
import com.example.studentmanagersystem.pojo.Student;

public class UserManage extends Activity {

	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private ListView listView;
	private List<LoginUser> loginUsers = new ArrayList<LoginUser>();
	private Button user_delete;
	private Button user_cancel;
	private CheckBox user_checkboxsum;
	private boolean checkedAll = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermanagelayout);
		mySqlHelper = new MySqlHelper(UserManage.this, "student_inf.db", null,
				1);
		db = mySqlHelper.getWritableDatabase();
		showUser();
		listView = (ListView) findViewById(R.id.user_listview);
		listView.setAdapter(adapter);
		user_checkboxsum = (CheckBox) findViewById(R.id.user_checkboxsum);
		user_delete = (Button) findViewById(R.id.user_delete);
		user_cancel = (Button) findViewById(R.id.user_cancel);
		user_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for (LoginUser loginUser : loginUsers) {
					System.out.println(loginUser.isChecked());
					if (loginUser.isChecked()) {
						Cursor cursor = db.rawQuery("delete from user where username = '"
								+ loginUser.getUserName() + "'", null);
						cursor.moveToNext();
					}
				}
				showUser();
			}
		});
		user_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		user_checkboxsum.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				checkedAll = arg1;
				adapter.notifyDataSetChanged();
			}
		});
		
	}

	public void showUser(){
		loginUsers = new ArrayList<LoginUser>();
		Cursor cursor = db.rawQuery("select * from user", null);
		while (cursor.moveToNext()) {
			loginUsers.add(new LoginUser(cursor.getString(1), cursor.getString(2),
					cursor.getInt(3)));
		}
		adapter.notifyDataSetChanged();
	}
	
	BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(UserManage.this).inflate(
					R.layout.userlistitems, null);
			CheckBox user_checkbox = (CheckBox) view.findViewById(R.id.user_checkbox);
			TextView user_name = (TextView) view.findViewById(R.id.user_name);
			TextView user_password = (TextView) view.findViewById(R.id.user_password);
			final CheckBox user_flag = (CheckBox) view.findViewById(R.id.user_flag);
			LoginUser user = loginUsers.get(arg0);
			final int position = arg0;
			user_checkbox.setChecked(checkedAll);
			user_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					loginUsers.get(position).setChecked(isChecked);
				}
			});
			user_name.setText(user.getUserName().toString());
			user_password.setText(user.getPassword().toString());
			if(user.getFlag() == 1){
				user_flag.setChecked(true);
			}
			
			
			return view;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return loginUsers.size();
		}
	};
}
