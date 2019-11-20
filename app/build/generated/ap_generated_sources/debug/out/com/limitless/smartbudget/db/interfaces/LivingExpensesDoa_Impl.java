/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: LivingExpensesDoa_Impl.java                                        ////////
 * ////////Class Name: LivingExpensesDoa_Impl                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 11/20/19 1:05 PM                                       ////////
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
import com.limitless.smartbudget.db.TypeConverters;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.db.model.LivingExpenses;
import java.lang.Double;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class LivingExpensesDoa_Impl implements LivingExpensesDoa {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LivingExpenses> __insertionAdapterOfLivingExpenses;

  private final EntityDeletionOrUpdateAdapter<LivingExpenses> __deletionAdapterOfLivingExpenses;

  private final EntityDeletionOrUpdateAdapter<LivingExpenses> __updateAdapterOfLivingExpenses;

  public LivingExpensesDoa_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLivingExpenses = new EntityInsertionAdapter<LivingExpenses>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `living_expenses` (`id`,`date`,`value`,`description`,`c_id`,`c_name`,`c_icon`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LivingExpenses value) {
        stmt.bindLong(1, value.getId());
        final Long _tmp;
        _tmp = TypeConverters.toTimestamp(value.getDate());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        if (value.getValue() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getValue());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
        }
        final Category _tmpCategory = value.getCategory();
        if(_tmpCategory != null) {
          stmt.bindLong(5, _tmpCategory.getId());
          if (_tmpCategory.getName() == null) {
            stmt.bindNull(6);
          } else {
            stmt.bindString(6, _tmpCategory.getName());
          }
          stmt.bindLong(7, _tmpCategory.getColor());
        } else {
          stmt.bindNull(5);
          stmt.bindNull(6);
          stmt.bindNull(7);
        }
      }
    };
    this.__deletionAdapterOfLivingExpenses = new EntityDeletionOrUpdateAdapter<LivingExpenses>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `living_expenses` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LivingExpenses value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfLivingExpenses = new EntityDeletionOrUpdateAdapter<LivingExpenses>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `living_expenses` SET `id` = ?,`date` = ?,`value` = ?,`description` = ?,`c_id` = ?,`c_name` = ?,`c_icon` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LivingExpenses value) {
        stmt.bindLong(1, value.getId());
        final Long _tmp;
        _tmp = TypeConverters.toTimestamp(value.getDate());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        if (value.getValue() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getValue());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
        }
        final Category _tmpCategory = value.getCategory();
        if(_tmpCategory != null) {
          stmt.bindLong(5, _tmpCategory.getId());
          if (_tmpCategory.getName() == null) {
            stmt.bindNull(6);
          } else {
            stmt.bindString(6, _tmpCategory.getName());
          }
          stmt.bindLong(7, _tmpCategory.getColor());
        } else {
          stmt.bindNull(5);
          stmt.bindNull(6);
          stmt.bindNull(7);
        }
        stmt.bindLong(8, value.getId());
      }
    };
  }

  @Override
  public void insert(final LivingExpenses livingExpenses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLivingExpenses.insert(livingExpenses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final LivingExpenses livingExpenses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfLivingExpenses.handle(livingExpenses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final LivingExpenses livingExpenses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfLivingExpenses.handle(livingExpenses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<LivingExpenses> getAllRecords() {
    final String _sql = "SELECT * FROM living_expenses";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "c_id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "c_name");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "c_icon");
      final List<LivingExpenses> _result = new ArrayList<LivingExpenses>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LivingExpenses _item;
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = TypeConverters.fromTimestamp(_tmp);
        final String _tmpValue;
        _tmpValue = _cursor.getString(_cursorIndexOfValue);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Category _tmpCategory;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpColor;
        _tmpColor = _cursor.getInt(_cursorIndexOfColor);
        _tmpCategory = new Category(_tmpName,_tmpColor);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId_1);
        _tmpCategory.setId(_tmpId);
        _item = new LivingExpenses(_tmpDate,_tmpCategory,_tmpValue,_tmpDescription);
        final int _tmpId_1;
        _tmpId_1 = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LivingExpenses> getRecordsForCategory(final int category) {
    final String _sql = "SELECT * FROM living_expenses WHERE c_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, category);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "c_id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "c_name");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "c_icon");
      final List<LivingExpenses> _result = new ArrayList<LivingExpenses>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LivingExpenses _item;
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = TypeConverters.fromTimestamp(_tmp);
        final String _tmpValue;
        _tmpValue = _cursor.getString(_cursorIndexOfValue);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Category _tmpCategory;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpColor;
        _tmpColor = _cursor.getInt(_cursorIndexOfColor);
        _tmpCategory = new Category(_tmpName,_tmpColor);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId_1);
        _tmpCategory.setId(_tmpId);
        _item = new LivingExpenses(_tmpDate,_tmpCategory,_tmpValue,_tmpDescription);
        final int _tmpId_1;
        _tmpId_1 = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LivingExpenses> getRecordsBetween(final Long d1, final Long d2) {
    final String _sql = "SELECT * FROM living_expenses WHERE date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (d1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, d1);
    }
    _argIndex = 2;
    if (d2 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, d2);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "c_id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "c_name");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "c_icon");
      final List<LivingExpenses> _result = new ArrayList<LivingExpenses>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LivingExpenses _item;
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = TypeConverters.fromTimestamp(_tmp);
        final String _tmpValue;
        _tmpValue = _cursor.getString(_cursorIndexOfValue);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Category _tmpCategory;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpColor;
        _tmpColor = _cursor.getInt(_cursorIndexOfColor);
        _tmpCategory = new Category(_tmpName,_tmpColor);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId_1);
        _tmpCategory.setId(_tmpId);
        _item = new LivingExpenses(_tmpDate,_tmpCategory,_tmpValue,_tmpDescription);
        final int _tmpId_1;
        _tmpId_1 = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LivingExpenses> getRecordsForCategoryBetween(final Long d1, final Long d2,
      final int category) {
    final String _sql = "SELECT * FROM living_expenses WHERE date BETWEEN ? AND ? AND c_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (d1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, d1);
    }
    _argIndex = 2;
    if (d2 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, d2);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, category);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "c_id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "c_name");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "c_icon");
      final List<LivingExpenses> _result = new ArrayList<LivingExpenses>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LivingExpenses _item;
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = TypeConverters.fromTimestamp(_tmp);
        final String _tmpValue;
        _tmpValue = _cursor.getString(_cursorIndexOfValue);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Category _tmpCategory;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpColor;
        _tmpColor = _cursor.getInt(_cursorIndexOfColor);
        _tmpCategory = new Category(_tmpName,_tmpColor);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId_1);
        _tmpCategory.setId(_tmpId);
        _item = new LivingExpenses(_tmpDate,_tmpCategory,_tmpValue,_tmpDescription);
        final int _tmpId_1;
        _tmpId_1 = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LivingExpenses> getRecordsByDescription(final String description) {
    final String _sql = "SELECT * FROM living_expenses WHERE description LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (description == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, description);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "c_id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "c_name");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "c_icon");
      final List<LivingExpenses> _result = new ArrayList<LivingExpenses>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LivingExpenses _item;
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = TypeConverters.fromTimestamp(_tmp);
        final String _tmpValue;
        _tmpValue = _cursor.getString(_cursorIndexOfValue);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Category _tmpCategory;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpColor;
        _tmpColor = _cursor.getInt(_cursorIndexOfColor);
        _tmpCategory = new Category(_tmpName,_tmpColor);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId_1);
        _tmpCategory.setId(_tmpId);
        _item = new LivingExpenses(_tmpDate,_tmpCategory,_tmpValue,_tmpDescription);
        final int _tmpId_1;
        _tmpId_1 = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Double getTotalExpenses() {
    final String _sql = "SELECT SUM(value) FROM living_expenses";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Double _result;
      if(_cursor.moveToFirst()) {
        final Double _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getDouble(0);
        }
        _result = _tmp;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Double getTotalExpensesBetween(final Long d1, final Long d2) {
    final String _sql = "SELECT SUM(value) FROM living_expenses WHERE date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (d1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, d1);
    }
    _argIndex = 2;
    if (d2 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, d2);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Double _result;
      if(_cursor.moveToFirst()) {
        final Double _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getDouble(0);
        }
        _result = _tmp;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Double getTotalExpensesForCategory(final String category) {
    final String _sql = "SELECT SUM(value) FROM living_expenses WHERE c_name = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Double _result;
      if(_cursor.moveToFirst()) {
        final Double _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getDouble(0);
        }
        _result = _tmp;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Double getTotalExpensesForCategory(final String category, final Long date) {
    final String _sql = "SELECT SUM(value) FROM living_expenses WHERE c_name = ? AND date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    _argIndex = 2;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, date);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Double _result;
      if(_cursor.moveToFirst()) {
        final Double _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getDouble(0);
        }
        _result = _tmp;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Double getTotalExpensesForDescription(final String description) {
    final String _sql = "SELECT SUM(value) FROM living_expenses WHERE description LIKE ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (description == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, description);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Double _result;
      if(_cursor.moveToFirst()) {
        final Double _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getDouble(0);
        }
        _result = _tmp;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LivingExpenses getRecordById(final int id) {
    final String _sql = "SELECT * FROM living_expenses WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "c_id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "c_name");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "c_icon");
      final LivingExpenses _result;
      if(_cursor.moveToFirst()) {
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = TypeConverters.fromTimestamp(_tmp);
        final String _tmpValue;
        _tmpValue = _cursor.getString(_cursorIndexOfValue);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final Category _tmpCategory;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpColor;
        _tmpColor = _cursor.getInt(_cursorIndexOfColor);
        _tmpCategory = new Category(_tmpName,_tmpColor);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId_1);
        _tmpCategory.setId(_tmpId);
        _result = new LivingExpenses(_tmpDate,_tmpCategory,_tmpValue,_tmpDescription);
        final int _tmpId_1;
        _tmpId_1 = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId_1);
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
