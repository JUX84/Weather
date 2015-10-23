package uavignon.fr.weather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {
    private static SyncAdapter sSyncAdapter = null;
    private static final Object syncAdapterLock = new Object();
    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (syncAdapterLock) {
            if (sSyncAdapter == null)
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
