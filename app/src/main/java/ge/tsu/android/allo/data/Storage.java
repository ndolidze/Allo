package ge.tsu.android.allo.data;

import android.content.Context;

public interface Storage {
    void addMessage( String key, Object object);
    Object getMessage(String key, Class klass);
}

