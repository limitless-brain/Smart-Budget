/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: DatabaseManagement_Impl.java                                        ////////
 * ////////Class Name: DatabaseManagement_Impl                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 9/27/19 9:09 PM                                       ////////
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

package com.limitless.smartbudget.db;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.limitless.smartbudget.db.interfaces.CategoryDoa;
import com.limitless.smartbudget.db.interfaces.CategoryDoa_Impl;
import com.limitless.smartbudget.db.interfaces.FixedExpensesDao;
import com.limitless.smartbudget.db.interfaces.FixedExpensesDao_Impl;
import com.limitless.smartbudget.db.interfaces.IncomesDao;
import com.limitless.smartbudget.db.interfaces.IncomesDao_Impl;
import com.limitless.smartbudget.db.interfaces.LivingExpensesDoa;
import com.limitless.smartbudget.db.interfaces.LivingExpensesDoa_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DatabaseManagement_Impl extends DatabaseManagement {
  private volatile FixedExpensesDao _fixedExpensesDao;

  private volatile IncomesDao _incomesDao;

  private volatile LivingExpensesDoa _livingExpensesDoa;

  private volatile CategoryDoa _categoryDoa;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `fixed_expenses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER, `value` TEXT, `description` TEXT)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_fixed_expenses_id` ON `fixed_expenses` (`id`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `incomes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER, `value` TEXT, `description` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `living_expenses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER, `value` TEXT, `description` TEXT, `c_id` INTEGER NOT NULL, `c_name` TEXT, `c_icon` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`c_id` INTEGER NOT NULL, `c_name` TEXT, `c_icon` INTEGER NOT NULL, PRIMARY KEY(`c_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '9605ae8f38a1025110d66ab58c0e60eb')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `fixed_expenses`");
        _db.execSQL("DROP TABLE IF EXISTS `incomes`");
        _db.execSQL("DROP TABLE IF EXISTS `living_expenses`");
        _db.execSQL("DROP TABLE IF EXISTS `categories`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsFixedExpenses = new HashMap<String, TableInfo.Column>(4);
        _columnsFixedExpenses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedExpenses.put("date", new TableInfo.Column("date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedExpenses.put("value", new TableInfo.Column("value", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFixedExpenses.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFixedExpenses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFixedExpenses = new HashSet<TableInfo.Index>(1);
        _indicesFixedExpenses.add(new TableInfo.Index("index_fixed_expenses_id", true, Arrays.asList("id")));
        final TableInfo _infoFixedExpenses = new TableInfo("fixed_expenses", _columnsFixedExpenses, _foreignKeysFixedExpenses, _indicesFixedExpenses);
        final TableInfo _existingFixedExpenses = TableInfo.read(_db, "fixed_expenses");
        if (! _infoFixedExpenses.equals(_existingFixedExpenses)) {
          return new RoomOpenHelper.ValidationResult(false, "fixed_expenses(com.limitless.smartbudget.db.model.FixedExpenses).\n"
                  + " Expected:\n" + _infoFixedExpenses + "\n"
                  + " Found:\n" + _existingFixedExpenses);
        }
        final HashMap<String, TableInfo.Column> _columnsIncomes = new HashMap<String, TableInfo.Column>(4);
        _columnsIncomes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("date", new TableInfo.Column("date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("value", new TableInfo.Column("value", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysIncomes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesIncomes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoIncomes = new TableInfo("incomes", _columnsIncomes, _foreignKeysIncomes, _indicesIncomes);
        final TableInfo _existingIncomes = TableInfo.read(_db, "incomes");
        if (! _infoIncomes.equals(_existingIncomes)) {
          return new RoomOpenHelper.ValidationResult(false, "incomes(com.limitless.smartbudget.db.model.Incomes).\n"
                  + " Expected:\n" + _infoIncomes + "\n"
                  + " Found:\n" + _existingIncomes);
        }
        final HashMap<String, TableInfo.Column> _columnsLivingExpenses = new HashMap<String, TableInfo.Column>(7);
        _columnsLivingExpenses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivingExpenses.put("date", new TableInfo.Column("date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivingExpenses.put("value", new TableInfo.Column("value", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivingExpenses.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivingExpenses.put("c_id", new TableInfo.Column("c_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivingExpenses.put("c_name", new TableInfo.Column("c_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLivingExpenses.put("c_icon", new TableInfo.Column("c_icon", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLivingExpenses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLivingExpenses = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLivingExpenses = new TableInfo("living_expenses", _columnsLivingExpenses, _foreignKeysLivingExpenses, _indicesLivingExpenses);
        final TableInfo _existingLivingExpenses = TableInfo.read(_db, "living_expenses");
        if (! _infoLivingExpenses.equals(_existingLivingExpenses)) {
          return new RoomOpenHelper.ValidationResult(false, "living_expenses(com.limitless.smartbudget.db.model.LivingExpenses).\n"
                  + " Expected:\n" + _infoLivingExpenses + "\n"
                  + " Found:\n" + _existingLivingExpenses);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(3);
        _columnsCategories.put("c_id", new TableInfo.Column("c_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("c_name", new TableInfo.Column("c_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("c_icon", new TableInfo.Column("c_icon", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(_db, "categories");
        if (! _infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.limitless.smartbudget.db.model.Category).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "9605ae8f38a1025110d66ab58c0e60eb", "2e3070360ac342894dbd62d9f4733e91");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "fixed_expenses","incomes","living_expenses","categories");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `fixed_expenses`");
      _db.execSQL("DELETE FROM `incomes`");
      _db.execSQL("DELETE FROM `living_expenses`");
      _db.execSQL("DELETE FROM `categories`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public FixedExpensesDao fixedExpensesDao() {
    if (_fixedExpensesDao != null) {
      return _fixedExpensesDao;
    } else {
      synchronized(this) {
        if(_fixedExpensesDao == null) {
          _fixedExpensesDao = new FixedExpensesDao_Impl(this);
        }
        return _fixedExpensesDao;
      }
    }
  }

  @Override
  public IncomesDao incomesDao() {
    if (_incomesDao != null) {
      return _incomesDao;
    } else {
      synchronized(this) {
        if(_incomesDao == null) {
          _incomesDao = new IncomesDao_Impl(this);
        }
        return _incomesDao;
      }
    }
  }

  @Override
  public LivingExpensesDoa livingExpensesDoa() {
    if (_livingExpensesDoa != null) {
      return _livingExpensesDoa;
    } else {
      synchronized(this) {
        if(_livingExpensesDoa == null) {
          _livingExpensesDoa = new LivingExpensesDoa_Impl(this);
        }
        return _livingExpensesDoa;
      }
    }
  }

  @Override
  public CategoryDoa categoryDoa() {
    if (_categoryDoa != null) {
      return _categoryDoa;
    } else {
      synchronized(this) {
        if(_categoryDoa == null) {
          _categoryDoa = new CategoryDoa_Impl(this);
        }
        return _categoryDoa;
      }
    }
  }
}
