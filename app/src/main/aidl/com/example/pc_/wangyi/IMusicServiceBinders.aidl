// IMusicServiceBinders.aidl
package com.example.pc_.wangyi;
import com.example.pc_.wangyi.IMusicCompleteListener;
// Declare any non-default types here with import statements

interface IMusicServiceBinders {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     //只能使用基本参数，要不就自己导入
    IBinder queryBinder(int code);
    void registerListener(IMusicCompleteListener iMusicCompleteListener);
    void unregisterListener();



}
