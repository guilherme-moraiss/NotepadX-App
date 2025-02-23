package com.example.mindlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Patterns;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Database extends SQLiteOpenHelper {

    // Nome do banco de dados e versão
    private static final String DATABASE_NAME = "notex.db";
    private static final int DATABASE_VERSION = 1;

    // Tabelas
    private static final String TABLE_USERS = "users";
    private static final String TABLE_NOTES = "notes";

    // Colunas da tabela de usuários
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADM = "adm";

    // Colunas da tabela de notas
    private static final String COLUMN_NOTE_ID = "id";
    private static final String COLUMN_NOTE_TITLE = "title";
    private static final String COLUMN_NOTE_DESCRIPTION = "description";
    private static final String COLUMN_USER = "user";
    private static final String COLUMN_NOTE_DATE = "date";

    // Construtor
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Criação das tabelas
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela de usuários
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COLUMN_ADM + " INTEGER NOT NULL DEFAULT 0)";
        db.execSQL(createUsersTable);

        // Tabela de notas
        String createNotesTable = "CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_DESCRIPTION + " TEXT, " +
                COLUMN_USER + " TEXT, " +
                COLUMN_NOTE_DATE + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_USER + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USERNAME + "))";
        db.execSQL(createNotesTable);
    }

    // Atualização do banco de dados
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // Adicionar usuário
    public String addUser(String username, String password, String email, int adm) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar se o usuário ou email já existem
        if (isUsernameExists(username)) return "O nome de usuário já existe.";
        if (isEmailExists(email)) return "O email já está cadastrado.";
        if (!isValidEmail(email)) return "Email inválido.";
        if (!isValidPassword(password)) return "A senha deve ter pelo menos 8 caracteres, incluindo letra maiúscula, número e símbolo.";

        // Criptografar senha
        String encryptedPassword = encryptPassword(password);

        // Inserir usuário no banco de dados
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, encryptedPassword);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ADM, adm);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result == -1 ? "Erro ao cadastrar usuário." : "Usuário cadastrado com sucesso!";
    }

    // Verifica se um nome de usuário já existe
    private boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Verifica se um email já está cadastrado
    private boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Valida formato do email
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Valida força da senha
    private boolean isValidPassword(String password) {
        String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(pattern, password);
    }

    // Criptografa senha com SHA-256
    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Autentica usuário no login
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_PASSWORD}, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(0);
            cursor.close();
            return encryptPassword(password).equals(storedPassword);
        }

        return false;
    }

    // Adiciona nota para um usuário específico
    public long addNote(String title, String description, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        if ((title == null || title.isEmpty()) && (description == null || description.isEmpty())) return -1;
        if (username == null || username.isEmpty()) return -1;

        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE_DESCRIPTION, description);
        values.put(COLUMN_USER, username);
        values.put(COLUMN_NOTE_DATE, currentDateTime);

        long result = db.insert(TABLE_NOTES, null, values);
        db.close();

        return result;
    }

    // Obtém todas as notas de um usuário específico
    public Cursor getUserNotes(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_USER + " = ?", new String[]{username});
    }

    // Atualiza uma nota específica
    public boolean updateNote(int noteId, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE_DESCRIPTION, description);

        int result = db.update(TABLE_NOTES, values, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(noteId)});
        db.close();

        return result > 0;
    }

    // Exclui uma nota específica
    public boolean deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NOTES, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(noteId)});
        db.close();
        return result > 0;
    }
}
