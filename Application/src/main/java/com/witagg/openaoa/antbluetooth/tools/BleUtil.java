//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: MIT
//

package com.witagg.openaoa.antbluetooth.tools;

import android.bluetooth.le.AdvertiseData;
import android.os.ParcelUuid;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
import java.util.Random;

/**
 * ble util
 */
public class BleUtil {

      //mac to byte[]
    public static byte[] macStrToBytes(String macStr) {
        String[] macStrs = macStr.split(":");
        byte[] macBytes = new byte[6];
        for(int i = 0;i < macStrs.length;i++){
            macBytes[i] = (byte)(Integer.parseInt(macStrs[i],16));
        }
        return macBytes;
    }

    /**
     * bytes str to Bytes
     * @param String src Byte，
     * @return byte[]
     */
    public static byte hexStr2Bytes(String src)
    {
        int m=0,n=0;
        int l=src.length()/2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++)
        {
            m=i*2+1;
            n=m+1;
            ret[i] = (byte)(Byte.decode("0x" + src.substring(i*2, m) + src.substring(m,n)));
        }
        return ret[0];
    }

    public static byte hexString2byte(String str){
        str = str.toLowerCase();
        char[] chars = str.toCharArray();

        byte b =  (byte)(char2byte((char)(chars[0]<<4))|char2byte(chars[1]));
        return b;
    }
    public static byte char2byte(char c){
        return (byte)"0123456789ABCDEF".indexOf(c);
    }

    public static String getRandomMacStr() {
        byte[] macBytes = new byte[3];
        int random1 = getRandomNum(-127,127);
        int random2 = getRandomNum(-127,127);
        int random3 = getRandomNum(-127,127);
        return byteToHex(random1) + ":" + byteToHex(random2)  + ":" + byteToHex(random3);
    }
    public static int getRandomNum(int startNum,int endNum){
        if(endNum > startNum){
            Random random = new Random();
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }

    private final static String[] hexArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String byteToHex(int n) {
        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexArray[d1] + hexArray[d2];
    }

}
