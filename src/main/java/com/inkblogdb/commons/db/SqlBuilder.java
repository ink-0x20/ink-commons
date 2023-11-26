package com.inkblogdb.commons.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.inkblogdb.commons.util.DateUtils;
import com.inkblogdb.commons.util.StringUtils;

/**
 * @author ink-0x20
 *
 */
public class SqlBuilder {

	/** DB接続情報 */
	private volatile String db = null;
	/** DB接続情報 */
	private String connectUrl = null;
	/** DB接続情報 */
	private String connectUser = null;
	/** DB接続情報 */
	private String connectPassword = null;
	/** テーブル */
	private volatile String table = null;

	/** カラム */
	private final List<String> columnList = new ArrayList<>();
	/** WHERE */
	private final List<String> whereList = new ArrayList<>();
	/** 挿入データ */
	private final List<Map<String, Object>> insertDataList = new ArrayList<>();
	/** 更新データ */
	private final List<Map<String, Object>> updateDataList = new ArrayList<>();
	/** ORDER BY */
	private final List<String> orderByList = new ArrayList<>();
	/** GROUP BY */
	private final List<String> groupByList = new ArrayList<>();
	/** JOIN */
	private final List<String> joinList = new ArrayList<>();
	/** LEFT JOIN */
	private final List<String> leftJoinList = new ArrayList<>();
	/** HAVING */
	private final List<String> havingList = new ArrayList<>();
	/** UNION */
	private final List<String> unionList = new ArrayList<>();

	/** LIMIT */
	private int limit = 0;
	/** OFFSET */
	private int offset = 0;

	/** EXPLAIN */
	private boolean isExplain = false;
	/** DISTINCT */
	private boolean isDistinct = false;
	/** FOR UPDATE */
	private boolean isForUpdate = false;
	/** NO WAIT */
	private boolean isNoWait = false;

	/** ステークホルダー使用有無 */
	private boolean isStakeHolder = true;

	/** DB接続 */
	private Connection connection = null;

	/**
	 * SQLのフォーマットに変換
	 * @param sql SQL
	 * @param values 値
	 * @param name 名前
	 * @param separatorStr 区切り文字
	 */
	private static void toSqlFormat(final StringBuilder sql, final List<String> values, final String name, final String separatorStr) {
		if (values.isEmpty()) {
			return;
		}
		if (StringUtils.isNotBlank(name)) {
			sql.append(" ").append(name).append(" ");
		}
		boolean isMultiple = false;
		for (String value : values) {
			if (isMultiple) {
				sql.append(separatorStr);
			} else {
				isMultiple = true;
			}
			sql.append(value);
		}
	}

	/**
	 * SQLで使える文字列に変換
	 * 基本的にdataやwhere時には、処理を行わずにSQL発行時にどうするかを判定
	 * dataやwhereのString1つだけ渡すパターンは、別途用意してあげよう（ダイレクトなSQLをかけるように
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	private static String toSqlString(final Object value) {
		if (value == null) {
			return "";
		}
		if (value instanceof String) {
			return StringUtils.join("'", (String) value, "'");
		} else if (value instanceof Integer) {
			return String.valueOf(value);
		} else if (value instanceof Long) {
			return String.valueOf(value);
		} else if (value instanceof Timestamp) {
			return StringUtils.enclose(DateUtils.toString(((Timestamp) value).toLocalDateTime(), "yyyy-MM-dd"), "'");
		} else if (value instanceof java.sql.Date) {
			return StringUtils.enclose(DateUtils.toString((java.sql.Date) value, "yyyy-MM-dd"), "'");
		} else if (value instanceof Double) {
			return String.valueOf(value);
		} else if (value instanceof Float) {
			return String.valueOf(value);
		} else if (value instanceof Boolean) {
			return String.valueOf(value);
		} else {
			return (String) value;
		}
	}

	/**
	 * ステートメントにObjectの値を格納
	 * @param preparedStatement ステートメント
	 * @param paramIndex 格納インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	@SuppressWarnings("boxing")
	private static void setStatementValue(final PreparedStatement preparedStatement, final int paramIndex, final Object value) throws SQLException {
		if (value instanceof String) {
			preparedStatement.setString(paramIndex, (String) value);
		} else if (value instanceof Integer) {
			preparedStatement.setInt(paramIndex, (Integer) value);
		} else if (value instanceof Long) {
			preparedStatement.setLong(paramIndex, (Long) value);
		} else if (value instanceof Timestamp) {
			preparedStatement.setTimestamp(paramIndex, (Timestamp) value);
		} else if (value instanceof java.sql.Date) {
			preparedStatement.setDate(paramIndex, (java.sql.Date) value);
		} else if (value instanceof Double) {
			preparedStatement.setDouble(paramIndex, (Double) value);
		} else if (value instanceof Float) {
			preparedStatement.setFloat(paramIndex, (Float) value);
		} else if (value instanceof Boolean) {
			preparedStatement.setBoolean(paramIndex, (Boolean) value);
		} else {
			preparedStatement.setObject(paramIndex, value);
		}
	}

	/**
	 * DB接続情報を付与
	 * コネクションプールの設定取得キーに使用
	 * @param db DB接続情報
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder db(final String db) {
		this.db = db;
		return this;
	}

	/**
	 * DB接続情報を付与
	 * 直接接続するために使用
	 * @param url 接続URL
	 * @param user 接続ユーザ
	 * @param password 接続パスワード
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder db(final String url, final String user, final String password) {
		this.connectUrl = url;
		this.connectUser = user;
		this.connectPassword = password;
		return this;
	}

	/**
	 * テーブルを付与
	 * @param tableName テーブル
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder table(final String tableName) {
		this.table = StringUtils.enclose(tableName, "`");
		return this;
	}

	/**
	 * テーブルを付与
	 * @param schema スキーマ
	 * @param tableName テーブル
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder table(final String schema, final String tableName) {
		return this.table(StringUtils.join(StringUtils.enclose(schema, "`"), ".", StringUtils.enclose(tableName, "`")));
	}

	/**
	 * 検索用のカラムを付与
	 * @param columns カラム
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder column(final String... columns) {
		for (String column : columns) {
			this.columnList.add(StringUtils.enclose(column, "`"));
		}
		return this;
	}

	/**
	 * 条件を付与
	 * @param column カラム
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder where(final String column, final Integer value) {
		return this.where(StringUtils.join(StringUtils.enclose(column, "`"), " = ", String.valueOf(value)));
	}

	/**
	 * 条件を付与
	 * @param column カラム
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder where(final String column, final String value) {
		return this.where(StringUtils.join(StringUtils.enclose(column, "`"), " = ", StringUtils.enclose(value, "'")));
	}

	/**
	 * 条件を付与
	 * @param column カラム
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder whereEqual(final String column, final Integer value) {
		return this.where(StringUtils.join(StringUtils.enclose(column, "`"), " = ", String.valueOf(value)));
	}

	/**
	 * 条件を付与
	 * @param column カラム
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder whereEqual(final String column, final String value) {
		return this.where(StringUtils.join(StringUtils.enclose(column, "`"), " = ", StringUtils.enclose(value, "'")));
	}

	/**
	 * 条件を付与
	 * @param column カラム
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder whereNot(final String column, final Integer value) {
		return this.where(StringUtils.join(StringUtils.enclose(column, "`"), " <> ", String.valueOf(value)));
	}

	/**
	 * 条件を付与
	 * @param column カラム
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder whereNot(final String column, final String value) {
		return this.where(StringUtils.join(StringUtils.enclose(column, "`"), " <> ", StringUtils.enclose(value, "'")));
	}

	/**
	 * 条件を付与
	 * @param column カラム
	 * @param where 条件
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder where(final String column, final String where, final String value) {
		return this.where(StringUtils.join(StringUtils.enclose(column, "`"), " ", where, " ", StringUtils.enclose(value, "'")));
	}

	/**
	 * 条件を付与
	 * @param where 条件
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder where(final String where) {
		this.whereList.add(where);
		return this;
	}

	/**
	 * 条件を付与
	 * @param column カラム
	 * @param values 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder whereIn(final String column, final String... values) {
		StringBuilder where = new StringBuilder(StringUtils.enclose(column, "`"));
		where.append(" IN (");
		toSqlFormat(where, Arrays.asList(values), null, ", ");
		where.append(")");
		return this.where(where.toString());
	}

	/**
	 * 条件を付与
	 * @param column カラム
	 * @param values 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder whereNotIn(final String column, final String... values) {
		StringBuilder where = new StringBuilder(StringUtils.enclose(column, "`"));
		where.append(" NOT IN (");
		toSqlFormat(where, Arrays.asList(values), null, ", ");
		where.append(")");
		return this.where(where.toString());
	}

	/**
	 * 昇順並び替えを付与
	 * @param column カラム
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder orderBy(final String column) {
		return orderByAsc(column);
	}

	/**
	 * 昇順並び替えを付与
	 * @param column カラム
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder orderByAsc(final String column) {
		this.orderByList.add(StringUtils.join(StringUtils.enclose(column, "`"), " ASC"));
		return this;
	}

	/**
	 * 降順並び替えを付与
	 * @param column カラム
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder orderByDesc(final String column) {
		this.orderByList.add(StringUtils.join(StringUtils.enclose(column, "`"), " DESC"));
		return this;
	}

	/**
	 * 挿入データを付与
	 * @param column カラム
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder insertData(final String column, final Object value) {
		if (this.insertDataList.isEmpty()) {
			Map<String, Object> data = new LinkedHashMap<>();
			data.put(StringUtils.enclose(column, "`"), value);
			this.insertDataList.add(data);
		} else {
			this.insertDataList.get(this.insertDataList.size() - 1).put(StringUtils.enclose(column, "`"), value);
		}
		return this;
	}

	/**
	 * 挿入データを付与
	 * @param dataMap データ [ カラム名 => 値, ]
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder insertData(final Map<String, Object> dataMap) {
		if (dataMap == null || dataMap.isEmpty()) {
			return this;
		}
		Map<String, Object> value = new LinkedHashMap<>();
		for (Entry<String, Object> data : dataMap.entrySet()) {
			value.put(StringUtils.enclose(data.getKey(), "`"), data.getValue());
		}
		this.insertDataList.add(value);
		return this;
	}

	/**
	 * 更新データを付与
	 * @param column カラム
	 * @param value 値
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder updateData(final String column, final Object value) {
		if (this.updateDataList.isEmpty()) {
			Map<String, Object> data = new LinkedHashMap<>();
			data.put(StringUtils.enclose(column, "`"), value);
			this.updateDataList.add(data);
		} else {
			this.updateDataList.get(this.updateDataList.size() - 1).put(StringUtils.enclose(column, "`"), value);
		}
		return this;
	}

	/**
	 * 更新データを付与
	 * @param dataMap データ [ カラム名 => 値, ]
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder updateData(final Map<String, Object> dataMap) {
		if (dataMap == null || dataMap.isEmpty()) {
			return this;
		}
		Map<String, Object> value = new LinkedHashMap<>();
		for (Entry<String, Object> data : dataMap.entrySet()) {
			value.put(StringUtils.enclose(data.getKey(), "`"), data.getValue());
		}
		this.updateDataList.add(value);
		return this;
	}

	/**
	 * LIMITを付与
	 * @param limit LIMIT
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder limit(final int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * OFFSETを付与
	 * @param offset OFFSET
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder offset(final int offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * EXPLAINを付与
	 * @param isExplain EXPLAIN
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder explain(final boolean isExplain) {
		this.isExplain = isExplain;
		return this;
	}

	/**
	 * DISTINCTを付与
	 * @param isDistinct DISTINCT
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder distinct(final boolean isDistinct) {
		this.isDistinct = isDistinct;
		return this;
	}

	/**
	 * ロックを付与
	 * @param isForUpdate ロック
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder forUpdate(final boolean isForUpdate) {
		this.isForUpdate = isForUpdate;
		return this;
	}

	/**
	 * NO WAITを付与
	 * @param isNoWait NO WAIT
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder noWait(final boolean isNoWait) {
		this.isNoWait = isNoWait;
		return this;
	}

	/**
	 * ステークホルダーを付与
	 * @param isStakeHolder ステークホルダー
	 * @return {@link SqlBuilder}
	 */
	public final SqlBuilder stakeHolder(final boolean isStakeHolder) {
		this.isStakeHolder = isStakeHolder;
		return this;
	}

	/**
	 * DB接続保持
	 * @param connection DB接続
	 * @return {@link SqlBuilder}
	 * @throws SQLException
	 */
	private SqlBuilder setConnection(Connection connection) throws SQLException {
		if (this.connection == null) {
			this.connection = connection;
		} else if (this.connection.isClosed()) {
			this.connection = connection;
		}
		return this;
	}

	/**
	 * DBへ接続
	 * 接続履歴がある場合はそのまま継続
	 * @return DB接続情報
	 * @throws SQLException
	 * @throws NamingException
	 */
	private Connection getConnection() throws SQLException, NamingException {
		Connection connect;
		if (this.connection == null || this.connection.isClosed()) {
			// 接続履歴なしのため接続
			// コネクションプールか通常接続か判定して接続させる
			if (StringUtils.isNotBlank(this.db)) {
				InitialContext context = new InitialContext();
				DataSource dataSource = (DataSource) context.lookup(this.db);
				connect = dataSource.getConnection();
			} else if (StringUtils.isNotBlank(this.connectUrl) && StringUtils.isNotBlank(this.connectUser) && StringUtils.isNotBlank(this.connectPassword)) {
				connect = DriverManager.getConnection(this.connectUrl, this.connectUser, this.connectPassword);
			} else {
				throw new IllegalArgumentException("db is empty");
			}
		} else {
			// 継続
			connect = this.connection;
		}
		connect.setAutoCommit(false);
		return connect;
	}

	/**
	 * 条件リセット
	 * @throws SQLException
	 */
	public final void reset() throws SQLException {
		this.columnList.clear();
		this.joinList.clear();
		this.leftJoinList.clear();
		this.whereList.clear();
		this.insertDataList.clear();
		this.groupByList.clear();
		this.havingList.clear();
		this.unionList.clear();
		this.orderByList.clear();
		this.limit = 0;
		this.offset = 0;
		this.isExplain = false;
		this.isDistinct = false;
		this.isForUpdate = false;
		this.isNoWait = false;
	}

	/**
	 * INSERT用にデータのカラムだけ抽出してSQLに追加
	 * @param sql SQL
	 */
	private int addDataColumns(final StringBuilder sql) {
		if (this.insertDataList.isEmpty()) {
			return 0;
		}
		int count = 0;
		int dataCount = 0;
		boolean isMultiple = false;
		StringBuilder columns = new StringBuilder();
		for (Map<String, Object> dataMap : this.insertDataList) {
			if (count == 0) {
				count = dataMap.size();
			} else if (count != dataMap.size()) {
				throw new IllegalArgumentException("data counts do not match");
			}
			for (Entry<String, Object> data : dataMap.entrySet()) {
				if (isMultiple) {
					if (columns.indexOf(data.getKey()) == -1) {
						dataCount++;
						columns.append(", ");
						columns.append(data.getKey());
					}
				} else {
					dataCount++;
					isMultiple = true;
					columns.append(data.getKey());
				}
			}
		}
		if (count != dataCount) {
			throw new IllegalArgumentException("data counts do not match");
		}
		sql.append(columns);
		return count;
	}

	/**
	 * INSERT用にデータの値だけ抽出してSQLに追加
	 * @param sql SQL
	 */
	private boolean addDataValues(final StringBuilder sql, final int columnCount) {
		if (this.insertDataList.isEmpty()) {
			return false;
		}
		boolean isValueMultiple = false;
		boolean isBulkMultiple = false;
		StringBuilder values = new StringBuilder();
		for (Map<String, Object> dataMap : this.insertDataList) {
			int valueCount = 0;
			if (dataMap.size() != columnCount) {
				throw new IllegalArgumentException("data counts do not match");
			}
			for (Entry<String, Object> data : dataMap.entrySet()) {
				if (isValueMultiple) {
					values.append(", ");
					if (this.isStakeHolder) {
						values.append("?");
					} else {
						values.append(toSqlString(data.getValue()));
					}
				} else {
					isValueMultiple = true;
					if (isBulkMultiple) {
						values.append(", (");
					} else {
						isBulkMultiple = true;
					}
					if (this.isStakeHolder) {
						values.append("?");
					} else {
						values.append(toSqlString(data.getValue()));
					}
				}
				valueCount++;
			}
			if (columnCount != valueCount) {
				throw new IllegalArgumentException("data counts do not match");
			}
			if (this.isStakeHolder) {
				break;
			}
			isValueMultiple = false;
		}
		sql.append(values);
		return true;
	}

	/**
	 * UPDATE用にデータをSQLに追加
	 * @param sql SQL
	 */
	private boolean addData(final StringBuilder sql) {
		if (this.updateDataList.isEmpty()) {
			return false;
		}
		boolean isMultiple = false;
		StringBuilder values = new StringBuilder();
		for (Map<String, Object> dataMap : this.updateDataList) {
			for (Entry<String, Object> data : dataMap.entrySet()) {
			if (isMultiple) {
					values.append(", ");
					values.append(data.getKey());
					values.append(" = ");
					if (this.isStakeHolder) {
						values.append("?");
					} else {
						values.append(toSqlString(data.getValue()));
					}
			} else {
				isMultiple = true;
					values.append(data.getKey());
					values.append(" = ");
					if (this.isStakeHolder) {
						values.append("?");
					} else {
						values.append(toSqlString(data.getValue()));
					}
				}
			}
		}
		sql.append(values);
		return true;
	}

	/**
	 * SELECTのSQLを返却
	 * @return SELECT SQL
	 */
	public final String toSelectSQL() {
		return toSelectSQL(null);
	}

	/**
	 * SELECTのSQLを返却
	 * @param count カウントするカラム、またはアスタリスク
	 * @return SELECT SQL
	 */
	public final String toSelectSQL(final String count) {
		StringBuilder sql = new StringBuilder();
		// EXPLAIN
		if (this.isExplain) {
			sql.append("EXPLAIN ");
		}
		sql.append("SELECT ");
		// DISTINCT
		if (this.isDistinct) {
			sql.append("DISTINCT ");
		}
		// カラム追加
		if (StringUtils.isBlank(count)) {
			if (this.columnList.isEmpty()) {
				sql.append("*");
			} else {
				toSqlFormat(sql, this.columnList, null, ", ");
			}
		} else {
			sql.append("COUNT(");
			sql.append(count);
			sql.append(") AS RECORD_COUNT_ALIAS");
		}
		// テーブル追加
		sql.append(" FROM ");
		sql.append(this.table);
		// 結合追加
		toSqlFormat(sql, this.joinList, "JOIN", " JOIN ");
		toSqlFormat(sql, this.leftJoinList, "LEFT JOIN", " LEFT JOIN ");
		// 条件追加
		toSqlFormat(sql, this.whereList, "WHERE", " AND ");
		// group
		toSqlFormat(sql, this.groupByList, "GROUP BY", ", ");
		// having
		toSqlFormat(sql, this.havingList, "HAVING", " AND ");
		// union
		toSqlFormat(sql, this.unionList, "UNION", " UNION ");
		// order
		toSqlFormat(sql, this.orderByList, "ORDER BY", ", ");
		// 検索個数指定
		if (0 < this.limit) {
			sql.append(" LIMIT ");
			sql.append(this.limit);
			if (0 < this.offset) {
				sql.append(" OFFSET ");
				sql.append(this.offset);
			}
		}
		// ロック
		if (this.isForUpdate) {
			sql.append(" FOR UPDATE");
			if (this.isNoWait) {
				sql.append(" NOWAIT");
			}
		}
		return sql.append(";").toString();
	}

	/**
	 * DELETEのSQLを返却
	 * @return DELETE SQL
	 */
	public final String toDeleteSQL() {
		StringBuilder sql = new StringBuilder("DELETE ");
		// テーブル追加
		sql.append(" FROM ");
		sql.append(this.table);
		// 条件追加
		toSqlFormat(sql, this.whereList, "WHERE", " AND ");
		return sql.append(";").toString();
	}

	/**
	 * INSERTのSQLを返却
	 * @return SELECT SQL
	 */
	public final String toInsertSQL() {
		if (this.insertDataList.isEmpty()) {
			return "";
		}
		// SQL作成
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		// テーブル追加
		sql.append(this.table);
		sql.append(" (");
		// 挿入データ追加
		int columnCount = this.addDataColumns(sql);
		sql.append(") VALUES (");
		this.addDataValues(sql, columnCount);
		sql.append(")");
		return sql.append(";").toString();
	}

	/**
	 * インクリメントのSQLを返却
	 * @param column インクリメントしたいカラム
	 * @return インクリメントSQL
	 */
	@SuppressWarnings("boxing")
	public final String toIncrementSQL(final String column) {
		if (this.insertDataList.isEmpty()) {
			return "";
		}
		// インクリメント用データ挿入
		insertData(column, 1);
		// SQL作成
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		// テーブル追加
		sql.append(this.table);
		sql.append(" (");
		// 挿入データ追加
		int columnCount = this.addDataColumns(sql);
		sql.append(") VALUES (");
		this.addDataValues(sql, columnCount);
		sql.append(") ON DUPLICATE KEY UPDATE ");
		// 更新データ追加
		sql.append(StringUtils.enclose(column, "`"));
		sql.append(" = ");
		sql.append(StringUtils.enclose(column, "`"));
		sql.append(" + 1");
		return sql.append(";").toString();
	}

	/**
	 * UPDATEのSQLを返却
	 * @return UPDATE SQL
	 */
	public final String toUpdateSQL() {
		if (this.updateDataList.isEmpty()) {
			return "";
		}
		// SQL作成
		StringBuilder sql = new StringBuilder("UPDATE ");
		// テーブル追加
		sql.append(this.table);
		// カラム追加
		sql.append(" SET ");
		this.addData(sql);
		// 条件追加
		toSqlFormat(sql, this.whereList, "WHERE", " AND ");
		return sql.append(";").toString();
	}

	/**
	 * レコード数を取得
	 * @return レコード数
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final long count() throws SQLException, NamingException {
		return count("*");
	}

	/**
	 * レコード数を取得
	 * @param count カウントするカラム、またはアスタリスク
	 * @return レコード数
	 * @throws SQLException
	 * @throws NamingException
	 */
	@SuppressWarnings("boxing")
	public final long count(final String count) throws SQLException, NamingException {
		this.limit = 1;
		List<Map<String, Object>> result = executeSelect(true, count);
		if (result.isEmpty()) {
			return 0;
		}
		return (long) result.get(0).get("RECORD_COUNT_ALIAS");
	}

	/**
	 * 1件のみ検索を実行
	 * @return 1件のみの検索結果
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final Map<String, Object> getFirst() throws SQLException, NamingException {
		this.limit = 1;
		List<Map<String, Object>> result = executeSelect(true, null);
		if (result.isEmpty()) {
			return new LinkedHashMap<>();
		}
		return result.get(0);
	}

	/**
	 * 1件のみの検索を実行
	 * @param endFunction 検索後処理
	 * @return {@link SqlBuilder}
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final Integer[] getFirst(BiFunction<Map<String, Object>, SqlBuilder, Integer[]> endFunction) throws SQLException, NamingException {
		if (StringUtils.isBlank(this.table)) {
			throw new IllegalArgumentException("table is empty");
		}
		this.limit = 1;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(toSelectSQL());
				ResultSet resultSet = preparedStatement.executeQuery()) {
			// メタデータ取得
			ResultSetMetaData meta = resultSet.getMetaData();
			// 検索
			Map<String, Object> resultMap = new LinkedHashMap<>();
			while (resultSet.next()) {
				// 取得した全カラムを保存
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					resultMap.put(meta.getColumnName(i), resultSet.getObject(i));
				}
				break;
			}
			// 条件をリセット
			reset();
			// DB接続を継続
			setConnection(connection);
			// 検索後処理を実行し結果を返却
			return endFunction.apply(resultMap, this);
		}
    }

	/**
	 * 複数の検索を実行
	 * @return 複数の検索結果
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final List<Map<String, Object>> get() throws SQLException, NamingException {
		return executeSelect(false, null);
	}

	/**
	 * 複数の検索を実行
	 * @param endFunction 検索後処理
	 * @return {@link SqlBuilder}
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final Integer[] get(BiFunction<List<Map<String, Object>>, SqlBuilder, Integer[]> endFunction) throws SQLException, NamingException {
		if (StringUtils.isBlank(this.table)) {
			throw new IllegalArgumentException("table is empty");
		}
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(toSelectSQL());
				ResultSet resultSet = preparedStatement.executeQuery()) {
			// メタデータ取得
			ResultSetMetaData meta = resultSet.getMetaData();
			// 検索
			List<Map<String, Object>> resultList = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> resultMap = new LinkedHashMap<>();
				// 取得した全カラムを保存
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					resultMap.put(meta.getColumnName(i), resultSet.getObject(i));
				}
				resultList.add(resultMap);
			}
			// 条件をリセット
			reset();
			// DB接続を継続
			setConnection(connection);
			// 検索後処理を実行し結果を返却
			return endFunction.apply(resultList, this);
		}
    }

	/**
	 * 検索を実行
	 * @param isFirst 1件のみの検索か否か
	 * @return 検索結果
	 * @throws SQLException
	 * @throws NamingException
	 */
	private List<Map<String, Object>> executeSelect(final boolean isFirst, final String count) throws SQLException, NamingException {
		if (StringUtils.isBlank(this.table)) {
			throw new IllegalArgumentException("table is empty");
		}
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(toSelectSQL(count));
				ResultSet resultSet = preparedStatement.executeQuery()) {
			// メタデータ取得
			ResultSetMetaData meta = resultSet.getMetaData();
			// 検索
			List<Map<String, Object>> resultList = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> resultMap = new LinkedHashMap<>();
				// 取得した全カラムを保存
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					resultMap.put(meta.getColumnName(i), resultSet.getObject(i));
				}
				resultList.add(resultMap);
				if (isFirst) {
					break;
				}
			}
			return resultList;
		}
    }

	/**
	 * insertSQLを実行
	 * @return 更新行数
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final Integer[] insert() throws SQLException, NamingException {
		if (StringUtils.isBlank(this.table)) {
			throw new IllegalArgumentException("table is empty");
		}
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(toInsertSQL())) {
			if (this.isStakeHolder) {
				// ステークホルダーに値をセット
				for (Map<String, Object> dataMap : this.insertDataList) {
					int paramIndex = 1;
					for (Entry<String, Object> data : dataMap.entrySet()) {
						setStatementValue(preparedStatement, paramIndex, data.getValue());
						paramIndex++;
					}
				}
				preparedStatement.addBatch();
			} else {
				preparedStatement.addBatch();
			}
			int[] count = preparedStatement.executeBatch();
			connection.commit();
			// int配列をInteger配列に変換して返す
			return Arrays.stream(count)
					.boxed()
					.toArray(Integer[]::new);
		}
	}

	/**
	 * updateSQLを実行
	 * @return 更新行数
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final Integer[] update() throws SQLException, NamingException {
		if (StringUtils.isBlank(this.table)) {
			throw new IllegalArgumentException("table is empty");
		}
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(toUpdateSQL())) {
			if (this.isStakeHolder) {
				// ステークホルダーに値をセット
				int paramIndex = 1;
				Map<String, Object> dataMap = this.updateDataList.get(0);
				for (Entry<String, Object> data : dataMap.entrySet()) {
					setStatementValue(preparedStatement, paramIndex, data.getValue());
					paramIndex++;
				}
				preparedStatement.addBatch();
			} else {
				preparedStatement.addBatch();
			}
			int[] count = preparedStatement.executeBatch();
			connection.commit();
			// int配列をInteger配列に変換して返す
			return Arrays.stream(count)
					.boxed()
					.toArray(Integer[]::new);
		}
	}

	/**
	 * deleteSQLを実行
	 * @return 更新行数
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final Integer[] delete() throws SQLException, NamingException {
		if (StringUtils.isBlank(this.table)) {
			throw new IllegalArgumentException("table is empty");
		}
		try (Connection connection = getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(toDeleteSQL())) {
			preparedStatement.addBatch();
			int[] count = preparedStatement.executeBatch();
			connection.commit();
			// int配列をInteger配列に変換して返す
			return Arrays.stream(count)
					.boxed()
					.toArray(Integer[]::new);
		}
	}

	/**
	 * インクリメントSQLを実行
	 * @param column インクリメントするカラム
	 * @return 更新行数
	 * @throws Exception
	 */
	public final Integer[] increment(final String column) throws Exception {
		if (StringUtils.isBlank(this.table)) {
			throw new IllegalArgumentException("table is empty");
		}
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(toIncrementSQL(column))) {
			if (this.isStakeHolder) {
				// ステークホルダーに値をセット
				int paramIndex = 1;
				Map<String, Object> dataMap = this.insertDataList.get(0);
				for (Entry<String, Object> data : dataMap.entrySet()) {
					setStatementValue(preparedStatement, paramIndex, data.getValue());
					paramIndex++;
				}
				preparedStatement.addBatch();
			} else {
				preparedStatement.addBatch();
			}
			int[] count = preparedStatement.executeBatch();
			connection.commit();
			// int配列をInteger配列に変換して返す
			return Arrays.stream(count)
					.boxed()
					.toArray(Integer[]::new);
		}
    }

}
