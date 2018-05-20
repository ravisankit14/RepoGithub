package com.repogithub.database;

public class ItemsTable {
    public static final String TABLE_ITEMS = "repo";
    public static final String COLUMN_ID = "repoId";
    public static final String COLUMN_NAME = "projName";
    public static final String COLUMN_HTTP_URL = "httpurl";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_WATCHER = "watcher";
    public static final String COLUMN_ISSUES = "issues";
    public static final String COLUMN_IMAGE = "image";

    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
                    COLUMN_HTTP_URL, COLUMN_SIZE, COLUMN_WATCHER, COLUMN_ISSUES, COLUMN_IMAGE};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_HTTP_URL + " TEXT," +
                    COLUMN_SIZE + " TEXT," +
                    COLUMN_WATCHER + " TEXT," +
                    COLUMN_ISSUES + " TEXT," +
                    COLUMN_IMAGE + " TEXT" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;
}
