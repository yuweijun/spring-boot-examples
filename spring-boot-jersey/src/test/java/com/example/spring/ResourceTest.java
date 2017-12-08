package com.example.spring;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.util.stream.Stream;

/**
 * http://www.importnew.com/17644.html
 * ClassPathResource提供了三个构造器：
 * <p>
 * public ClassPathResource(String path)：使用默认的ClassLoader加载“path”类路径资源；
 * public ClassPathResource(String path, ClassLoader classLoader)：使用指定的ClassLoader加载“path”类路径资源；
 * <p>
 * 比如当前类路径是“cn.javass.spring.chapter4.ResourceTest”，
 * 而需要加载的资源路径是“cn/javass/spring/chapter4/test1.properties”，
 * 则将加载的资源在“cn/javass/spring/chapter4/test1.properties”；
 * <p>
 * public ClassPathResource(String path, Class<?> clazz)：
 * 使用指定的类加载“path”类路径资源，将加载相对于当前类的路径的资源；
 * <p>
 * 比如当前类路径是“cn.javass.spring.chapter4.ResourceTest”，
 * 而需要加载的资源路径是“cn/javass/spring/chapter4/test1.properties”，
 * 则将加载的资源在“cn/javass/spring/chapter4/cn/javass/spring/chapter4/test1.properties”；
 * <p>
 * 而如果需要 加载的资源路径为“test1.properties”，将加载的资源为“cn/javass/spring/chapter4/test1.properties”。
 *
 * @author yuweijun 2017-02-08
 */
public class ResourceTest {

    private void dumpStream(Resource resource) {
        InputStream is = null;
        try {
            // 1.获取文件资源
            is = resource.getInputStream();
            // 2.读取资源
            byte[] descBytes = new byte[is.available()];
            is.read(descBytes);
            System.out.println(new String(descBytes));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 3.关闭资源
                is.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * ByteArrayResource可多次读取数组资源，即isOpen ()永远返回false。
     */
    @Test
    public void testByteArrayResource() {
        Resource resource = new ByteArrayResource("Hello World!".getBytes());
        if (resource.exists()) {
            dumpStream(resource);
        }
        Assert.assertEquals(false, resource.isOpen());
    }

    /**
     * InputStreamResource代表java.io.InputStream字节流，
     * 对于“getInputStream”操作将直接返回该字节流，
     * 因此只能读取一次该字节流，即“isOpen”永远返回true。
     */
    @Test
    public void testInputStreamResource() {
        ByteArrayInputStream bis = new ByteArrayInputStream("Hello World!".getBytes());
        Resource resource = new InputStreamResource(bis);
        if (resource.exists()) {
            dumpStream(resource);
        }
        Assert.assertEquals(true, resource.isOpen());
    }

    /**
     * FileSystemResource代表java.io.File资源，
     * 对于“getInputStream”操作将返回底层文件的字节流，
     * “isOpen”将永远返回false，从而表示可多次读取底层文件的字节流。
     */
    @Test
    public void testFileResource() throws IOException {
        File file = new File("pom.xml");
        Resource resource = new FileSystemResource(file);
        if (resource.exists()) {
            dumpStream(resource);
        }
        System.out.println("path:" + resource.getFile().getAbsolutePath());
        Assert.assertEquals(false, resource.isOpen());
    }

    @Test
    public void testClasspathResourceByDefaultClassLoader() throws IOException {
        // 使用默认的加载器加载资源，将加载当前ClassLoader类路径上相对于根路径的资源：
        Resource resource = new ClassPathResource("logback-test.xml");
        if (resource.exists()) {
            dumpStream(resource);
        }
        System.out.println("path:" + resource.getFile().getAbsolutePath());
        Assert.assertEquals(false, resource.isOpen());
    }

    @Test
    public void testClasspathResourceByClassLoader() throws IOException {
        // 使用指定的ClassLoader进行加载资源，将加载指定的ClassLoader类路径上相对于根路径的资源：
        ClassLoader cl = this.getClass().getClassLoader();
        Resource resource = new ClassPathResource("logback-test.xml", cl);
        if (resource.exists()) {
            dumpStream(resource);
        }

        System.out.println("path:" + resource.getFile().getAbsolutePath());
        Assert.assertEquals(false, resource.isOpen());
    }

    @Test(expected = FileNotFoundException.class)
    public void testClasspathResourceByClass() throws IOException {
        Class clazz = this.getClass();
        Resource resource1 = new ClassPathResource("logback-test.xml" , clazz);
        if(resource1.exists()) {
            dumpStream(resource1);
        }
        System.out.println("path:" + resource1.getFile().getAbsolutePath());
        Assert.assertEquals(false, resource1.isOpen());
    }

    @Test
    public void testResourceLoad() throws IOException {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("classpath:logback-test.xml");
        //验证返回的是ClassPathResource
        Assert.assertEquals(ClassPathResource.class, resource.getClass());
        Resource resource2 = loader.getResource("file:pom.xml");
        String absolutePath = resource2.getFile().getAbsolutePath();
        System.out.println(absolutePath);
        //验证返回的是ClassPathResource
        Assert.assertEquals(UrlResource.class, resource2.getClass());
        Resource resource3 = loader.getResource("logback-test.xml");
        //验证返默认可以加载ClasspathResource
        Assert.assertTrue(resource3 instanceof ClassPathResource);
    }

    @Test
    public void testClasspathPrefix() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //只加载一个绝对匹配Resource，且通过ResourceLoader.getResource进行加载
        Resource[] resources=resolver.getResources("classpath:logback-test.xml");
        Assert.assertEquals(1, resources.length);
        //只加载一个匹配的Resource，且通过ResourceLoader.getResource进行加载
        resources = resolver.getResources("classpath:META-INF/*.xml");
        Assert.assertTrue(resources.length == 0);
    }

    @Test
    public void testClasspathAsteriskPrefixLimit() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 将首先通过ClassLoader.getResources("")加载目录，
        // 将只返回文件系统的类路径不返回jar的跟路径
        // 然后进行遍历模式匹配
        Resource[] resources = resolver.getResources("classpath*:asm-*.txt");
        System.out.println(resources.length);
        Assert.assertTrue(resources.length > 0);

        // 将通过ClassLoader.getResources("asm-license.txt")加载
        // asm-license.txt存在于com.springsource.net.sf.cglib-2.2.0.jar
        resources = resolver.getResources("classpath*:asm-license.txt");
        System.out.println(resources.length);
        Stream.of(resources).forEach(System.out::println);
        Assert.assertTrue(resources.length == 1);

        // 将只加载文件系统类路径匹配的Resource
        resources = resolver.getResources("classpath*:LICENS*");
        System.out.println(resources.length);
        Stream.of(resources).forEach(System.out::println);
        Assert.assertTrue(resources.length > 1);
    }

}
