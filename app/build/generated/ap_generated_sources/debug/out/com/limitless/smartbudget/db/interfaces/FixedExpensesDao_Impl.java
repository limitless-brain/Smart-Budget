/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: FixedExpensesDao_Impl.java                                        ////////
 * ////////Class Name: FixedExpensesDao_Impl                                  ////////
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
import com.limitless.smartbudget.db.TypeConverters;
import com.limitless.smartbudget.db.model.FixedExpenses;
import java.lang.Double;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FixedExpensesDao_Impl implements FixedExpensesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FixedExpenses> __insertionAdapterOfFixedExpenses;

  private final EntityDeletionOrUpdateAdapter<FixedExpenses> __deletionAdapterOfFixedExpenses;

  private final EntityDeletionOrUpdateAdapter<FixedExpenses> __updateAdapterOfFixedExpenses;

  public FixedExpensesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFixedExpenses = new EntityInsertionAdapter<FixedExpenses>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `fixed_expenses` (`id`,`date`,`value`,`description`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FixedExpenses value) {
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
      }
    };
    this.__deletionAdapterOfFixedExpenses = new EntityDeletionOrUpdateAdapter<FixedExpenses>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `fixed_expenses` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FixedExpenses value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfFixedExpenses = new EntityDeletionOrUpdateAdapter<FixedExpenses>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `fixed_expenses` SET `id` = ?,`date` = ?,`value` = ?,`description` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FixedExpenses value) {
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
        stmt.bindLong(5, value.getId());
      }
    };
  }

  @Override
  public void insert(final FixedExpenses fixedExpenses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFixedExpenses.insert(fixedExpenses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final FixedExpenses fixedExpenses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFixedExpenses.handle(fixedExpenses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final FixedExpenses fixedExpenses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFixedExpenses.handle(fixedExpenses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<FixedExpenses> getRecordsBetween(final Long d1, final Long d2) {
    final String _sql = "SELECT * FROM fixed_expenses WHERE date BETWEEN ? AND ?";
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
      final List<FixedExpenses> _result = new ArrayList<FixedExpenses>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FixedExpenses _item;
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
        _item = new FixedExpenses(_tmpDate,_tmpValue,_tmpDescription);
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
  public List<FixedExpenses> getAllRecords() {
    final String _sql = "SELECT * FROM fixed_expenses";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final List<FixedExpenses> _result = new ArrayList<FixedExpenses>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FixedExpenses _item;
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
        _item = new FixedExpenses(_tmpDate,_tmpValue,_tmpDescription);
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
  public List<FixedExpenses> getRecordsByDescription(final String des) {
    final String _sql = "SELECT * FROM fixed_expenses WHERE description = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (des == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, des);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final List<FixedExpenses> _result = new ArrayList<FixedExpenses>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FixedExpenses _item;
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
        _item = new FixedExpenses(_tmpDate,_tmpValue,_tmpDescription);
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
  public Double getTotalExpenses() {
    final String _sql = "SELECT SUM(value) FROM fixed_expenses";
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
  public Double getTotalPaidBetween(final Long d1, final Long d2) {
    final String _sql = "SELECT SUM(value) FROM fixed_expenses WHERE date BETWEEN ? AND ?";
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
  public FixedExpenses getRecordById(final int id) {
    final String _sql = "SELECT * FROM fixed_expenses WHERE id = ?";
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
      final FixedExpenses _result;
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
        _result = new FixedExpenses(_tmpDate,_tmpValue,_tmpDescription);
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
