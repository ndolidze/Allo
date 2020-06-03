package ge.tsu.android.allo.data;


import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;

public class StorageImp implements Storage {
    private  final  SharedPreferences sharedPreferences;

    public StorageImp(Context context) {

        this.sharedPreferences =context.getSharedPreferences("This_is_storage", Context.MODE_PRIVATE);
    }

    @Override
    public void addMessage(String key, Object object) {
        SharedPreferences sharedPreferences=this.sharedPreferences;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key, new Gson().toJson(object));
        editor.commit();
    }
    @Override
    public Object getMessage( String key, Class klass) {
        SharedPreferences sharedPreferences=this.sharedPreferences;
        String data=sharedPreferences.getString(key, null);
        return data==null ? null : new Gson().fromJson(data, klass);
    }

}
