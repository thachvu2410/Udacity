module com.udacity.catpoint.security {

    requires miglayout;

    requires java.prefs;
    requires com.udacity.catpoint.image;


    opens security.data to com.google.gson;
    requires com.google.gson;


    requires com.google.common;

    requires java.desktop;
    requires java.sql;
}