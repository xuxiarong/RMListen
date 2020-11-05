package com.rm.module_main.activity.splash;

import java.util.ArrayList;

/**
 * desc   :
 * date   : 2020/11/04
 * version: 1.0
 */
class MatchUtils {

    public static String[] getNumber() {
        String input = "123,asd,8866,fdfd222,11";
        StringBuffer sb = new StringBuffer();
        char[] chars = input.toCharArray();
        //遍历整个字符串，选出数字和英文逗号
        for (char aChar : chars) {
            if (aChar == ',') {
                sb.append(aChar);
            }
            //0x30代表数字0对应的char，0x39代表数字9对应的char
            else if (aChar <= 0x39 && aChar >= 0x30) {
                sb.append(aChar);
            }
        }
        //根据逗号切割，例如输入 = 123,asd,8866,fdfd222,11   切割后 =  [123, , 8866, 222, 11]
        String[] tempArray = sb.toString().split(",");
        //先定义一个List去接收结果
        ArrayList<String> numList = new ArrayList<String>();
        for (String element : tempArray) {
            //应该有可能在字符串切割后，会有空的元素就是例如像123, ,（两个逗号中间就是有个为""的元素）
            if(!element.equals("")){
                numList.add(element);
            }
        }
        //再定义一个数组去接收List的元素
        String[] numArray =new String[numList.size()];
        for (int i = 0; i < numList.size(); i++) {
            numArray[i] = numList.get(i);
        }
        return numArray;
    }
}
