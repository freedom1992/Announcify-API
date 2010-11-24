package com.announcify.sql.model;

import android.database.Cursor;

public interface BaseModel {
	public Cursor getAll();

	public void add(String name);

	public void remove(int id);

	public Cursor get(int id);

	public void close();
}
