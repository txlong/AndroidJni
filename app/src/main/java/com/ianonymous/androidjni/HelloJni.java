/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ianonymous.androidjni;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ianonymous.androidjni.aes.AESCryptor;
import com.ianonymous.androidjni.aes.JavaAESCryptor;

import java.io.UnsupportedEncodingException;

public class HelloJni extends Activity {
    public static final String TESTDATA = "kLNyk5O9jj0kG/lqskHCLs7HQttQjqMwNToSXGVs7WraXf0bVpBA1vaE+30Mz2wu/6dmmU6mHOVAye+w9zrgZswPAjqEtU8nAa7+z5RDeil/5kBEEnV/IVO+Xry6YO4AL6xuHm/6k32zn6C8R2ZCvNL/vvUbk49YH5MEyCU/9See8Y8hqM9jPTmGc9+izcIjZtkMnC1PfShwvgdtE5gkkBqVJx20bnjyzEEPIb3dxt/DlhmbnpBeC6GWzCHjzvdLcC3mfYHoP6+A1r+oXjDxGFfKIDgtwaUZfzAKhlpsx9gOn7e2CaC85Nyu2Xy1vjTBlJiwN1EPvI87nQrWWqOBDyRRzhlbc+f2pEfZ6yIQKXnR7QKLKptxnD3jcKuH5r2l82b1Q3OSFTCYRCzYtA/CYbdJq4gRxx8bFwSeqmxtYy0=kLNyk5O9jj0kG/lqskHCLs7HQttQjqMwNToSXGVs7WraXf0bVpBA1vaE+30Mz2wu/6dmmU6mHOVAye+w9zrgZswPAjqEtU8nAa7+z5RDeil/5kBEEnV/IVO+Xry6YO4AL6xuHm/6k32zn6C8R2ZCvNL/vvUbk49YH5MEyCU/9See8Y8hqM9jPTmGc9+izcIjZtkMnC1PfShwvgdtE5gkkBqVJx20bnjyzEEPIb3dxt/DlhmbnpBeC6GWzCHjzvdLcC3mfYHoP6+A1r+oXjDxGFfKIDgtwaUZfzAKhlpsx9gOn7e2CaC85Nyu2Xy1vjTBlJiwN1EPvI87nQrWWqOBDyRRzhlbc+f2pEfZ6yIQKXnR7QKLKptxnD3jcKuH5r2l82b1Q3OSFTCYRCzYtA/CYbdJq4gRxx8bFwSeqmxtYy0=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Create a TextView and set its content.
         * the text is retrieved by calling a native
         * function.
         */
        TextView tv = new TextView(this);
        tv.setText(stringFromJNI());
        setContentView(tv);

        try {
            System.out.println("======jni-crypt-test======");
            byte[] data = TESTDATA.getBytes("UTF-8");
            data = AESCryptor.crypt(data, System.currentTimeMillis(), 0);
            String hexStr = AESCryptor.bytes2HexStr(data);
            System.out.println("encrypt:" + hexStr);

            data = AESCryptor.hexStr2Bytes(hexStr);
            data = AESCryptor.crypt(data, System.currentTimeMillis(), 1);
            System.out.println("decrypt:" + new String(data, "UTF-8"));

            System.out.println("======java-crypt-test======");
            data = TESTDATA.getBytes("UTF-8");
            data = JavaAESCryptor.encrypt(data);
            hexStr = AESCryptor.bytes2HexStr(data);
            System.out.println("encrypt:" + hexStr);

            data = AESCryptor.hexStr2Bytes(hexStr);
            data = JavaAESCryptor.decrypt(data);
            System.out.println("decrypt:" + new String(data, "UTF-8"));

            System.out.println("======jni-file-test======");
            data = AESCryptor.read("/mnt/sdcard/test.txt", System.currentTimeMillis());
            if (data != null) {
                System.out.println("read:" + new String(data, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */
    public native String stringFromJNI();

    /* This is another native method declaration that is *not*
     * implemented by 'hello-jni'. This is simply to show that
     * you can declare as many native methods in your Java code
     * as you want, their implementation is searched in the
     * currently loaded native libraries only the first time
     * you call them.
     *
     * Trying to call this function will result in a
     * java.lang.UnsatisfiedLinkError exception !
     */
    public native String unimplementedStringFromJNI();

    /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.hellojni/lib/libhello-jni.so at
     * installation time by the package manager.
     */
    static {
        System.loadLibrary("aes_jni");
    }
}
