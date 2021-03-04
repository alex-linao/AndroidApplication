package com.example.studentmanagersystem.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqlHelper extends SQLiteOpenHelper {

	public MySqlHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table user(_id integer primary key autoincrement,"
				+ "username varchar(20)," + "password varchar(20),"
				+ "flag integer default 0)");
		db.execSQL("create table student(_id integer primary key autoincrement,"
				+ "name varchar(20),"
				+ "sex varchar(20),"
				+ "mingzu varchar(20),"
				+ "id varchar(20),"
				+ "birthday varchar(20),"
				+ "phone varchar(20),"
				+ "more varchar(20)," + "image blob)");
		db.execSQL("create table loginhistory(_id integer primary key autoincrement,"
				+ "name varchar(20))");

		System.out.println("onCreate 被调用");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// db.execSQL("drop table if exists student");
		System.out.println("onUpgrade 被调用" + oldVersion + "--" + newVersion);

	}

}
