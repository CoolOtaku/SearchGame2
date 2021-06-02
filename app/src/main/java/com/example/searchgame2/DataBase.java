package com.example.searchgame2;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    public  static  final  String HOST = "https://search-games.online/forProgram/";
    public  static  final  String DIRECTORI = "https://search-games.online/uploads/posts/";

    public  static  final  String DATABASE_NAME = "SG_DataBase";
    public  static final int DATABASE_VERSION = 1;

    public  static  final  String TABLE_GAMEALL="GameAll";
    public  static  final String TABLE_PROFILE="Profile";
    public  static  final String TABLE_MESSAGES="Messages";

    public  static  final  String ID="id";
    public  static  final  String TITLE="title";

    public  static  final String USER_GRUP="user_grup";
    public  static  final String NAME="Name";
    public  static  final String EMAIL="Email";
    public  static  final String COUNT_COMENTS="count_coments";
    public  static  final String PASSWORD="password";
    public  static  final String PHOTO="photo";
    public  static  final String REG_DATE="reg_date";
    public  static  final String LAST_DATE="last_date";
    public  static  final String INFO="info";
    public  static  final String IP="ip";
    public  static  final String FAVORITES="favorites";
    public  static  final String FULL_NAME="full_name";
    public  static  final String COUNT_PUBLISHING="count_publishing";
    public  static  final String LINC_TO_BACKGROUND="linc_to_background";

    public  static  final  String POSTER="poster";
    public  static  final  String SCREEN1="screen1";
    public  static  final  String SCREEN2="screen2";
    public  static  final  String SCREEN3="screen3";
    public  static  final  String JANRE="janre";
    public  static  final  String YEAR="year";
    public  static  final  String CORE="core";
    public  static  final  String PROCESSOR="processor";
    public  static  final  String COREFREQUENCY="corefrequency";
    public  static  final  String RAM="ram";
    public  static  final  String VIDEO_RAM="video_ram";
    public  static  final  String DEVELOPER="developer";
    public  static  final  String EDITION="edition";
    public  static  final  String VIDEO_CARD="video_card";
    public  static  final  String FOR_PLATFORM="for_platform";
    public  static  final  String HHD_RAM="hhd_ram";
    public  static  final  String PRICE="price";
    public  static  final  String LINK_ON_STEAM="link_on_steam";
    public  static  final  String LINK_ON_TEASET="link_on_teaser";
    public  static  final  String LINK_ON_GAMEPLAY="link_on_gameplay";

    public  static  final  String TEXT="text";
    public  static  final  String THEME="theme";
    public  static  final  String USER_FROM="user_from";
    public  static  final  String DATE="date";
    public  static  final  String SENDID="sendid";
    public  static  final  String IS_READ="is_read";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_GAMEALL + "(" + ID + " int(11) primary key, " + TITLE + " varchar(70), "
                + POSTER + " varchar(225), " + SCREEN1 + " varchar(225), " + SCREEN2 + " varchar(225), "
                + SCREEN3 + " varchar(225), " + JANRE + " varchar(60), " + YEAR + " int(4), " + CORE + " int(2), "
                + PROCESSOR + " varchar(60), " + COREFREQUENCY + " varchar(20), " + RAM + " varchar(10), "
                + VIDEO_RAM + " varchar(10), " + DEVELOPER + " varchar(50), " + EDITION + " varchar(50), "
                + VIDEO_CARD + " varchar(60), " + FOR_PLATFORM + " varchar(15), " + HHD_RAM + " varchar(20), "
                + PRICE + " double(5), " + LINK_ON_STEAM + " varchar(200), " + LINK_ON_TEASET + " varchar(25), "
                + LINK_ON_GAMEPLAY + " varchar(25)" + ")");
        db.execSQL("create table "+TABLE_PROFILE+"("+ID+" int(11) primary key, "+USER_GRUP+" int(4), "+EMAIL+" varchar(225), "+NAME
                +" varchar(225), "+COUNT_COMENTS+" int(5), "+PASSWORD+" varchar(225), "+PHOTO+" varchar(225), "+REG_DATE+" varchar(50), "
                +LAST_DATE+" varchar(50), "+INFO+" varchar(225), "+IP+" varchar(15), "+FAVORITES+" varchar(200), "+FULL_NAME+" varchar(225), "
                +COUNT_PUBLISHING+" int(5), "+LINC_TO_BACKGROUND+" varchar(225) "+")");
        db.execSQL("create table "+TABLE_MESSAGES+"("+ID+" int(11) primary key, "+THEME+" varchar(70), "+TEXT+" varchar(225), "+USER_FROM
                +" varchar(70), "+DATE+" varchar(16), "+SENDID+" int(1), "+IS_READ+" int(1), "+USER_GRUP+" int(5), "+PHOTO+" varchar(225)"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_GAMEALL);
        db.execSQL("create table " + TABLE_GAMEALL + "(" + ID + " int(11) primary key, " + TITLE + " varchar(70), "
                + POSTER + " varchar(225), " + SCREEN1 + " varchar(225), " + SCREEN2 + " varchar(225), "
                + SCREEN3 + " varchar(225), " + JANRE + " varchar(60), " + YEAR + " int(4), " + CORE + " int(2), "
                + PROCESSOR + " varchar(60), " + COREFREQUENCY + " varchar(20), " + RAM + " varchar(10), "
                + VIDEO_RAM + " varchar(10), " + DEVELOPER + " varchar(50), " + EDITION + " varchar(50), "
                + VIDEO_CARD + " varchar(60), " + FOR_PLATFORM + " varchar(15), " + HHD_RAM + " varchar(20),"
                + PRICE + " double(5), " + LINK_ON_STEAM + " varchar(200), " + LINK_ON_TEASET + " varchar(25), "
                + LINK_ON_GAMEPLAY + " varchar(25)" + ")");
    }
    public void DesLogin(SQLiteDatabase db, Context context){
        db.execSQL("drop table if exists " + TABLE_PROFILE);
        db.execSQL("create table "+TABLE_PROFILE+"("+ID+" int(11) primary key, "+USER_GRUP+" int(4), "+EMAIL+" varchar(225), "+NAME
                +" varchar(225), "+COUNT_COMENTS+" int(5), "+PASSWORD+" varchar(225), "+PHOTO+" varchar(225), "+REG_DATE+" varchar(50), "
                +LAST_DATE+" varchar(50), "+INFO+" varchar(225), "+IP+" varchar(15), "+FAVORITES+" varchar(200), "+FULL_NAME+" varchar(225), "
                +COUNT_PUBLISHING+" int(5), "+LINC_TO_BACKGROUND+" varchar(225) "+")");
        RefreshMessages(db);
        context.stopService(new Intent(context,MessageService.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    public void RefreshMessages(SQLiteDatabase db){
        db.execSQL("drop table if exists " + TABLE_MESSAGES);
        db.execSQL("create table "+TABLE_MESSAGES+"("+ID+" int(11) primary key, "+THEME+" varchar(70), "+TEXT+" varchar(225), "+USER_FROM
                +" varchar(70), "+DATE+" varchar(16), "+SENDID+" int(1), "+IS_READ+" int(1), "+USER_GRUP+" int(5), "+PHOTO+" varchar(225)"+")");
    }
}
