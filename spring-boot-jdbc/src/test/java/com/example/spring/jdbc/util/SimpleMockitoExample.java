package com.example.spring.jdbc.util;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * http://waylau.com/mockito-quick-start/
 * http://www.cnblogs.com/qwop/p/3432166.html
 *
 * @author yuweijun 2016-09-07
 */
public class SimpleMockitoExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMockitoExample.class);

    @Test
    public void test1() {
        // mock creation
        List mockedList = mock(List.class);

        // using mock object
        mockedList.add("one");
        mockedList.clear();

        // verification 验证行为
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

    @Test
    public void test2() {
        // You can mock concrete classes, not just interfaces
        LinkedList mockedList = mock(LinkedList.class);

        // stubbing
        // 在执行前 stub，而后在交互中验证
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        // following prints "first"
        LOGGER.info("mockedList.get(0) is : {}", mockedList.get(0));

        try {
            // following throws runtime exception
            System.out.println(mockedList.get(1));
        } catch (Exception e) {
            LOGGER.error("mockedList throw runtime exception.", e);
        }

        // following prints "null" because get(999) was not stubbed
        LOGGER.info("mockedList.get(999) is : {}", mockedList.get(999));

        // Although it is possible to verify a stubbed invocation, usually it's just redundant
        // If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        // If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
        verify(mockedList).get(0);
    }

    @Test
    public void test3() {
        LinkedList mockedList = mock(LinkedList.class);

        // stubbing using built-in anyInt() argument matcher
        when(mockedList.get(anyInt())).thenReturn("element");

        // following prints "element"
        LOGGER.info("999 {}", mockedList.get(999));

        // you can also verify using an argument matcher
        verify(mockedList).get(anyInt());
    }

    @Test
    public void test4() {
        // 调用次数验证
        LinkedList mockedList = mock(LinkedList.class);
        // using mock
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        // following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        // exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        // verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        // verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(5)).add("three times");
    }

    @Test
    public void test5() {
        // 方法处理异常
        LinkedList mockedList = mock(LinkedList.class);
        doThrow(new RuntimeException()).when(mockedList).clear();

        try {
            // 这次调用算一次，上面的不算
            // following throws RuntimeException:
            mockedList.clear();
        } catch (Exception e) {
            LOGGER.error("mockedList runtime exception", e);
        }
        verify(mockedList, times(1)).clear();
    }

    @Test
    public void test6() {
        // A. Single mock whose methods must be invoked in a particular order
        List singleMock = mock(List.class);

        // using a single mock
        singleMock.add("was added first");
        singleMock.add("was added second");

        // create an inOrder verifier for a single mock
        InOrder inOrder = inOrder(singleMock);

        // following will make sure that add is first called with "was added first, then with "was added second"
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        // B. Multiple mocks that must be used in a particular order
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        // using mocks
        firstMock.add("was called first");
        secondMock.add("was called second");

        // create inOrder object passing any mocks that need to be verified in order
        inOrder = inOrder(firstMock, secondMock);

        // following will make sure that firstMock was called before secondMock
        inOrder.verify(firstMock).add("was called first");
        inOrder.verify(secondMock).add("was called second");

        // Oh, and A + B can be mixed together at will
    }

    public void testMap() {
        Map mock = Mockito.mock(Map.class);
        Mockito.when(mock.get("city")).thenReturn("深圳");
        // test code
        assertEquals("城市测试", "深圳", mock.get("city"));
        Mockito.verify(mock).get(Matchers.eq("city"));
        Mockito.verify(mock, Mockito.times(2));
    }

    @Test
    public void testSpy() {
        // Lets mock a LinkedList
        List list = new LinkedList();
        list.add("yes");
        List spy = Mockito.spy(list);
        // You have to use doReturn() for stubbing
        assertEquals("yes", spy.get(0));
        Mockito.doReturn("foo").when(spy).get(0);
        assertEquals("foo", spy.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSpy2() {
        // Lets mock a LinkedList
        List list = new LinkedList();
        List spy = Mockito.spy(list);

        // this would not work
        // real method is called so spy.get(0)
        // throws IndexOutOfBoundsException (list is still empty)
        Mockito.when(spy.get(0)).thenReturn("foo");
        assertEquals("foo", spy.get(0));
    }


}
