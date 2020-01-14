package com.app.jobfizzer.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.jobfizzer.Model.FavouriteBannerImages;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Poyyamozhi on 30-Apr-18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Fave_Category_Db";
    private static final Integer DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Fave_Category_Table";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table Favorite (id integer primary key AUTOINCREMENT, categoryId text, subCategoryId text," +
                    " categoryStatus text, categoryName text, categoryImage text)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public long insertData(boolean categoryStatus, String categoryId, String categoryName, String categoryImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (categoryStatus) {
            contentValues.put("categoryId", categoryId);
        } else {
            contentValues.put("subCategoryId", categoryId);
        }
        contentValues.put("categoryName", categoryName);
        contentValues.put("categoryImage", categoryImage);
        contentValues.put("categoryStatus", String.valueOf(categoryStatus));
        long val = db.insert("Favorite", null, contentValues);
        db.close();
        return val;
    }

    public boolean isLikedCategory(String id, boolean categoryStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        if (categoryStatus) {
            cursor = db.rawQuery("SELECT * FROM Favorite where categoryId =" + id, null);
        } else {
            cursor = db.rawQuery("SELECT * FROM Favorite where subCategoryId =" + id, null);
        }
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }


    public int deleteCategory(String id, boolean categoryStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affected;
        if (categoryStatus) {
            affected = db.delete("Favorite", "categoryId =? ", new String[]{id});
        } else {
            affected = db.delete("Favorite", "subCategoryId =? ", new String[]{id});
        }
        db.close();
        return affected;
    }

    public List<FavouriteBannerImages> getFavList() {
        List<FavouriteBannerImages> favList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Favorite order by id desc", null);
        cursor.moveToNext();
        while (!cursor.isAfterLast()) {
            FavouriteBannerImages favouriteBannerImages = new FavouriteBannerImages();
            favouriteBannerImages.setId(cursor.getString(1));
            favouriteBannerImages.setSubId(cursor.getString(2));
            favouriteBannerImages.setToSubCategory(cursor.getString(3));
            favouriteBannerImages.setBanner_name(cursor.getString(4));
            favouriteBannerImages.setBanner_logo(cursor.getString(5));
            favList.add(favouriteBannerImages);
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return favList;
    }
}