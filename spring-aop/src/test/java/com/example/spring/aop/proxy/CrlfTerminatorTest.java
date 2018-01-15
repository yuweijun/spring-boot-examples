package com.example.spring.aop.proxy;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yuweijun on 2018-01-12.
 */
public class CrlfTerminatorTest {

    @Test
    public void newLine() {
        char u0031 = '\u0031';
        char u000b = '\u000B';
        char u000c = '\u000C';
        // \n和\r这2个字符是java语法中的终结符，不能直接用单引号和双引号表示为直接量
        // 这2个字符外面加单引号和双引号都不能正常运行
        // char u000a = \u000A; System.out.println("前面换行符，这里的内容会被输出");
        // char u000d = \u000D; System.out.println("前面有回车符，这里的内容会被输出");

        // 这2个字符只能使用以下形式来表示
        char u000a = '\n';
        char u000d = '\r';
        System.out.println("这会被打印出来" + u000a + "这里的内容会被输出");
        System.out.println("---------------");
        System.out.println("---------------");
        System.out.println("不会被打印出来" + u000d + "这里的内容会被输出");
    }

}
