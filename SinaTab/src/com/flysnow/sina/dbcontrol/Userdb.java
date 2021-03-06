package com.flysnow.sina.dbcontrol;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

public class Userdb  {
	
	
	private DBHelper dbHelper = null;
	public Userdb(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper = new DBHelper(context);
	}

	/**
	 * 创建表
	 * 
	 * @throws Exception
	 */
	public void createTable()  throws Exception {
		
		
		dbHelper.open();
		Log.i("aaaa","777777777777777777\n");
		String deleteSql = "drop table if exists user ";
		dbHelper.execSQL(deleteSql);

		// id是自动增长的主键，username和 password为字段名， text为字段的类型
		String sql = "CREATE TABLE user (id integer primary key autoincrement, username text, password text)";
		dbHelper.execSQL(sql);
		
		
		dbHelper.closeConnection();
	}

	/**
	 * 插入数据
	 * 
	 * @throws Exception
	 */
	public void insert() throws Exception {
		
		dbHelper.open();

		ContentValues values = new ContentValues(); // 相当于map

		values.put("username", "test111111");
		values.put("password", "12345611111111");

		dbHelper.insert("user", values);

		dbHelper.closeConnection();
	}

	/**
	 * 更新数据
	 * 
	 * @throws Exception
	 */
	public void update() throws Exception {
		
		dbHelper.open();
		ContentValues initialValues = new ContentValues();
		initialValues.put("username", "changename"); // 更新的字段和值
		// 第三个参数为条件语句
		dbHelper.update("user", initialValues, "id = ?", new String[] { "1" });

		dbHelper.closeConnection();
	}

	/**
	 * 删除数据
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		
		dbHelper.open();

		dbHelper.delete("user", "id =?'", new String[] { "1" });

		dbHelper.closeConnection();
	}

	/**
	 * 增加字段
	 * 
	 * @throws Exception
	 */
	public void addColumn() throws Exception {
		
		dbHelper.open();

		String updateSql = "alter table user add company text";
		dbHelper.execSQL(updateSql);
		
		dbHelper.closeConnection();
	}

	/**
	 * 查询列表
	 * 
	 * @throws Exception
	 */
	public void selectList() throws Exception {
		
		dbHelper.open();
		
		Cursor returnCursor = dbHelper.findList(false, "user", new String[] { "id", "username", "password" }, "username?", new String[] { "test" }, null, null, "id desc", null);
		while (returnCursor.moveToNext()) {
			String id = returnCursor.getString(returnCursor.getColumnIndexOrThrow("id"));
			String username = returnCursor.getString(returnCursor.getColumnIndexOrThrow("username"));
			String password = returnCursor.getString(returnCursor.getColumnIndexOrThrow("password"));
			//System.out.println("id=" + id + ";username=" + username + ";" + password + ";\n");
			
			Log.i("aaaa","id=" + id + ";username=" + username + ";" + password + ";\n");
		}
		
		dbHelper.closeConnection();
	}

	/**
	 * 某一条信息
	 * 
	 * @throws Exception
	 */
	public void selectInfo() throws Exception {
		
		dbHelper.open();
		Cursor returnCursor = dbHelper.findOne(false,"user", new String[] { "id", "username", "password" }, "id = '1'", null, null, null, "id desc",null);
		if (returnCursor != null) {
			String id = returnCursor.getString(returnCursor.getColumnIndexOrThrow("id"));
			String username = returnCursor.getString(returnCursor.getColumnIndexOrThrow("username"));
			String password = returnCursor.getString(returnCursor.getColumnIndexOrThrow("password"));
			System.out.println("id=" + id + ";username=" + username + ";" + password + ";\n");
		}
	}
}
