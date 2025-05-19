package com.example.vacunacheck.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mi_base_datos.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE usuarios (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "cedula TEXT NOT NULL UNIQUE," +
                        "nombres TEXT NOT NULL," +
                        "apellidos TEXT NOT NULL," +
                        "fecha_nacimiento TEXT," +
                        "genero TEXT," +
                        "estado_civil TEXT," +
                        "email TEXT," +
                        "usuario TEXT," +
                        "contrasena TEXT," +
                        "perfil TEXT" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    public boolean insertarFamiliar(String cedula, String nombres, String apellidos,
                                   String genero, String perfil) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cedula", cedula);
        values.put("nombres", nombres);
        values.put("apellidos", apellidos);
        values.put("genero", genero);
        values.put("perfil", perfil);

        long result = db.insert("usuarios", null, values);
        return result != -1;
    }

    public boolean insertarUsuario(String cedula, String nombres, String apellidos, String fechaNacimiento,
                                   String genero, String estadoCivil, String email,
                                   String usuario, String contrasena, String perfil) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cedula", cedula);
        values.put("nombres", nombres);
        values.put("apellidos", apellidos);
        values.put("fecha_nacimiento", fechaNacimiento);
        values.put("genero", genero);
        values.put("estado_civil", estadoCivil);
        values.put("email", email);
        values.put("usuario", usuario);
        values.put("contrasena", contrasena);
        values.put("perfil", perfil);

        try {
            long result = db.insertOrThrow("usuarios", null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean cedulaExiste(String cedula) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE cedula = ?", new String[]{cedula});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    public boolean validarUsuario(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?",
                new String[]{usuario, contrasena}
        );

        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    public Cursor obtenerUsuarioPorCedula(String cedula) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuarios WHERE cedula = ?", new String[]{cedula});
    }

    public String obtenerCedula(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String cedula = null;

        Cursor cursor = db.rawQuery("SELECT cedula FROM usuarios WHERE usuario = ? AND contrasena = ?", new String[]{usuario, contrasena});

        if (cursor.moveToFirst()) {
            cedula = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return cedula;
    }

    public boolean actualizarUsuario(String cedula, String nombres, String apellidos, String fechaNacimiento,
                                     String genero, String email, String perfil) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombres", nombres);
        values.put("apellidos", apellidos);
        values.put("fecha_nacimiento", fechaNacimiento);
        values.put("genero", genero);
        values.put("email", email);
        values.put("perfil", perfil);

        int filasAfectadas = db.update("usuarios", values, "cedula = ?", new String[]{cedula});

        return filasAfectadas > 0;
    }

}
