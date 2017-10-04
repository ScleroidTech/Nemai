package info.androidhive.navigationdrawer.adapter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    //location,pincode,state,area
    public static final String KEY_ROWID = "_id";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PIN = "pin";
    public static final String KEY_STATE = "state";
    public static final String KEY_AREA = "area";
    public static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME="DbNemai";
    private static final String TABLE_NAME="tblPincodes";
    private static final int DATABASE_VERSION=1;
	
	/*create table contacts (_id integer primary key autoincrement, name text not null, email text not null);*/

    private static final String DATABASE_CREATE ="create table "+ TABLE_NAME + "(" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_LOCATION + " text not null, " + KEY_PIN + " text not null, " + KEY_STATE + " text not null, "  + KEY_AREA + " text not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context=context;
        DBHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // it will destroy all old data
            db.execSQL("Drop TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    public DBAdapter open(){
        db=DBHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        db.close();
    }
    public long insertContact(String location, String pin, String state, String area){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LOCATION, location);
        initialValues.put(KEY_PIN, pin);
        initialValues.put(KEY_STATE,state);
        initialValues.put(KEY_AREA,area);
        return db.insert(TABLE_NAME, null, initialValues);

    }

    public Cursor getAllPincodes() {
        return db.query(TABLE_NAME, new String[]{KEY_ROWID, KEY_LOCATION, KEY_PIN, KEY_STATE, KEY_AREA}, null, null, null, null, null);

    }

}