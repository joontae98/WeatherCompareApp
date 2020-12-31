package com.example.weathercompareapp;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String TIME = "time";
        public static final String ICONID = "iconId";
        public static final String TEMP = "temp";
        public static final String DIFFERTEMP = "differTemp";
        public static final String _TABLENAME0 = "usertable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +TIME+" text not null , "
                +ICONID +" text not null , "
                +TEMP+" text not null , "
                +DIFFERTEMP+" integer not null );";
    }
}
