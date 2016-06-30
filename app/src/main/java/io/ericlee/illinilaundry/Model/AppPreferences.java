package io.ericlee.illinilaundry.Model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class AppPreferences {

    public static final String BOOKMARKED_DORMS = "bookmarks";
    public static final String HAS_BOOKMARKS = "hasbookmarks";
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName();
    private SharedPreferences _sharedPrefs;
    private SharedPreferences.Editor _prefsEditor;
    private static AppPreferences instance;

    private AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public static AppPreferences getInstance(Context context) {
        if(instance == null) {
            instance = new AppPreferences(context);
        }

        return instance;
    }

    public boolean hasBookmarks() {
        // Get our boolean from prefs or return false
        return _sharedPrefs.getBoolean(HAS_BOOKMARKS, false);
    }

    public Set<String> getBookmarkedDorms() {
        // Get our string from prefs or return an empty string
        return _sharedPrefs.getStringSet(BOOKMARKED_DORMS, new HashSet<String>(1));
    }

    public void saveBookmarks(Set<String> text) {
        // Have HAS_BOOKMARKS be set by saveBookmarks instead of a seperate method as a way to code
        // safely.
        if (text.isEmpty()) {
            _prefsEditor.putBoolean(HAS_BOOKMARKS, false);
        } else {
            _prefsEditor.putBoolean(HAS_BOOKMARKS, true);
        }

        _prefsEditor.putStringSet(BOOKMARKED_DORMS, text);
        _prefsEditor.commit();
    }
}