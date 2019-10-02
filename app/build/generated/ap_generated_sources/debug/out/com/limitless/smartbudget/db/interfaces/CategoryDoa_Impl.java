/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: CategoryDoa_Impl.java                                        ////////
 * ////////Class Name: CategoryDoa_Impl                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 10/2/19 4:31 PM                                       ////////
 * ////////Author: yazan                                                   ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.limitless.smartbudget.db.interfaces;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.limitless.smartbudget.db.model.Category;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CategoryDoa_Impl implements CategoryDoa {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Category> __insertionAdapterOfCategory;

  private final EntityDeletionOrUpdateAdapter<Category> __deletionAdapterOfCategory;

  private final EntityDeletionOrUpdateAdapter<Category> __updateAdapterOfCategory;

  public CategoryDoa_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCategory = new EntityInsertionAdapter<Category>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `categories` (`c_id`,`c_name`,`c_icon`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Category value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        stmt.bindLong(3, value.getColor());
      }
    };
    this.__deletionAdapterOfCategory = new EntityDeletionOrUpdateAdapter<Category>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `categories` WHERE `c_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Category value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfCategory = new EntityDeletionOrUpdateAdapter<Category>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `categories` SET `c_id` = ?,`c_name` = ?,`c_icon` = ? WHERE `c_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Category value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        stmt.bindLong(3, value.getColor());
        stmt.bindLong(4, value.getId());
      }
    };
  }

  @Override
  public void insert(final Category category) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCategory.insert(category);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Category category) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCategory.handle(category);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Category category) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCategory.handle(category);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Category> getAllCategories() {
    final String _sql = "SELECT * FROM categories";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "c_id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "c_name");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "c_icon");
      final List<Category> _result = new ArrayList<Category>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Category _item;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpColor;
        _tmpColor = _cursor.getInt(_cursorIndexOfColor);
        _item = new Category(_tmpName,_tmpColor);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Category getRecordById(final int id) {
    final String _sql = "SELECT * FROM categories WHERE c_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "c_id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "c_name");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "c_icon");
      final Category _result;
      if(_cursor.moveToFirst()) {
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpColor;
        _tmpColor = _cursor.getInt(_cursorIndexOfColor);
        _result = new Category(_tmpName,_tmpColor);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
