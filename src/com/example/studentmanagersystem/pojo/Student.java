package com.example.studentmanagersystem.pojo;

public class Student {

	private String name;
	private String sex;
	private String mingZu;
	private String id;
	private String birthday;
	private String phone;
	private String more;
	private boolean checked;

	public Student(String name, String sex, String mingZu,
			String id, String birthday, String phone, String more) {
		super();
		this.name = name;
		this.sex = sex;
		this.mingZu = mingZu;
		this.id = id;
		this.birthday = birthday;
		this.phone = phone;
		this.more = more;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMingZu() {
		return mingZu;
	}

	public void setMingZu(String mingZu) {
		this.mingZu = mingZu;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

}
