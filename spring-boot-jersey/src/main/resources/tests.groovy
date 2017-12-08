/**
 * 
 * $ cd src/main/resources
 * $ spring test app.groovy tests.groovy
 * 
 * 或者进入spring shell之后执行test命令：
 * 
 * $ spring shell
 * $ test app.groovy tests.groovy
 */
class ApplicationTests {

    @Test
    void homeSaysHello() {
        assertEquals("Hello World!", new WebApplication().home())
    }

}