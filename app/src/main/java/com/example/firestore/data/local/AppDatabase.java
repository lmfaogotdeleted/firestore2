package com.example.firestore.data.local;
import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@androidx.room.Database(entities = {MediaItem.class, TrackingEntity.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao MovieDao();
    public abstract TrackingDao TrackingDao();
    private static AppDatabase instance;
    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "Television"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}

