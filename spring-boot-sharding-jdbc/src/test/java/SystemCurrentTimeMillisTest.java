import org.junit.Test;

/**
 * @author yuweijun
 * @date 2018-09-04
 */
public class SystemCurrentTimeMillisTest {

    @Test
    public void test1() {
        long rand = System.currentTimeMillis() % 1000;
        System.out.println(rand);
        System.out.println((int) rand);
    }

    @Test
    public void test2() {
        // long rand = ((int) System.currentTimeMillis()) % 1000;
        // 下面这个写法其实是先强转再取模的，所以long转int可能变成负数，取模之后结果为负值
        // 强转优化级高于"*/%"这3个操作，但是却低于"."方法调用的点运算符
        long rand = (int) System.currentTimeMillis() % 1000;
        System.out.println(rand);
        System.out.println((int) rand);
    }

}
