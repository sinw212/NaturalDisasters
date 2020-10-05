package com.example.ppnd.Other;

import android.util.Log;

public class WaitNotify {
    synchronized public void mWait()
    {
        try
        {
            wait();
        }
        catch(Exception e)
        {
            Log.d("Debug", e.getMessage());
        }
    }

    synchronized public void mNotify()
    {
        try
        {
            notify();
        }
        catch(Exception e)
        {
            Log.d("Debug", e.getMessage());
        }
    }
}
