package genius.com.callremainder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by RAMSARAN on 31-03-2018.
 */
public class DataStorage {
    private final String DATA = "call_remainder_app_data";
    private SharedPreferences sharedPreferences;

    public DataStorage(Context contect){
        this.sharedPreferences = contect.getSharedPreferences(DATA, 0);
    }

    public void saveData(String key, String value){
        this.sharedPreferences.edit().putString(key, value).apply();
    }

    public String getData(String Key){
        return this.sharedPreferences.getString(Key, "");
    }

    public boolean hasData(String key){
        return this.sharedPreferences.contains(key);
    }

    public void clearAll(){
        this.sharedPreferences.edit().clear().apply();
    }
}
