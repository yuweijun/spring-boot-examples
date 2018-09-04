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

}
