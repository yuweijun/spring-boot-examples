package com.example.spring.jdbc.mapper;

import com.example.spring.jdbc.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 命名解析：为了减少输入量，MyBatis 对所有的命名配置元素（包括语句，结果映射，缓存等）使用了如下的命名解析规则。
 * <p>
 * 完全限定名（比如“com.mypackage.MyMapper.selectAllThings”）将被直接查找并且找到即用。
 * 短名称（比如“selectAllThings”）如果全局唯一也可以作为一个单独的引用。
 * 如果不唯一，有两个或两个以上的相同名称（比如“com.foo.selectAllThings ”和“com.bar.selectAllThings”），
 * 那么使用时就会收到错误报告说短名称是不唯一的，这种情况下就必须使用完全限定名。
 * <p>
 * 对于简单语句来说，注解使代码显得更加简洁，然而 Java 注解对于稍微复杂的语句就会力不从心并且会显得更加混乱。
 * 因此，如果你需要做很复杂的事情，那么最好使用 XML 来映射语句。
 * <p>
 * 范围（Scope）和生命周期
 * 理解我们目前已经讨论过的不同范围和生命周期类是至关重要的，因为错误的使用会导致非常严重的并发问题。
 * <p>
 * 提示 对象生命周期和依赖注入框架
 * <p>
 * 依赖注入框架可以创建线程安全的、基于事务的 SqlSession 和映射器（mapper）并将它们直接注入到你的 bean 中，因此可以直接忽略它们的生命周期。
 * 如果对如何通过依赖注入框架来使用 MyBatis 感兴趣可以研究一下 MyBatis-Spring 或 MyBatis-Guice 两个子项目。
 * <p>
 * SqlSessionFactoryBuilder
 * <p>
 * 这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。
 * 因此 SqlSessionFactoryBuilder 实例的最佳范围是方法范围（也就是局部方法变量）。
 * 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但是最好还是不要让其一直存在以保证所有的 XML 解析资源开放给更重要的事情。
 * <p>
 * SqlSessionFactory
 * <p>
 * SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由对它进行清除或重建。
 * 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏味道（bad smell）”。
 * 因此 SqlSessionFactory 的最佳范围是应用范围。有很多方法可以做到，最简单的就是使用单例模式或者静态单例模式。
 * <p>
 * SqlSession
 * <p>
 * 每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的范围是请求或方法范围。
 * 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。
 * 也绝不能将 SqlSession 实例的引用放在任何类型的管理范围中，比如 Serlvet 架构中的 HttpSession。
 * 如果你现在正在使用一种 Web 框架，要考虑 SqlSession 放在一个和 HTTP 请求对象相似的范围中。换句话说，每次收到的 HTTP 请求，就可以打开一个 SqlSession，返回一个响应，就关闭它。
 * 这个关闭操作是很重要的，你应该把这个关闭操作放到 finally 块中以确保每次都能执行关闭。下面的示例就是一个确保 SqlSession 关闭的标准模式：
 * <pre>
 * SqlSession session = sqlSessionFactory.openSession();
 * try {
 *      // do work
 * } finally {
 *      session.close();
 * }
 * </pre>
 * 在你的所有的代码中一致性地使用这种模式来保证所有数据库资源都能被正确地关闭。
 * <p>
 * 映射器实例（Mapper Instances）
 * <p>
 * 映射器是创建用来绑定映射语句的接口。映射器接口的实例是从 SqlSession 中获得的。因此从技术层面讲，映射器实例的最大范围是和 SqlSession 相同的，因为它们都是从 SqlSession 里被请求的。尽管如此，映射器实例的最佳范围是方法范围。也就是说，映射器实例应该在调用它们的方法中被请求，用过之后即可废弃。并不需要显式地关闭映射器实例，尽管在整个请求范围（request scope）保持映射器实例也不会有什么问题，但是很快你会发现，像 SqlSession 一样，在这个范围上管理太多的资源的话会难于控制。所以要保持简单，最好把映射器放在方法范围（method scope）内。下面的示例就展示了这个实践：
 * <pre>
 * SqlSession session = sqlSessionFactory.openSession();
 * try {
 *      BlogMapper mapper = session.getMapper(BlogMapper.class);
 *      // do work
 * } finally {
 *      session.close();
 * }
 * </pre>
 *
 * @author yuweijun 2016-06-11.
 */
@Mapper
public interface UserMapper {

    // 可以用#{0}来表示方法参数的位置 Available parameters are [0, param1]
    @Select("SELECT * FROM user WHERE name = #{name}")
    User findByName(String name);

    // 可以不用@Param标识name参数, 如上所示,
    // 如果方法中有多个参数, 则需要用@Param来表示参数名, 或者是用[0, 1, param1, param2]来表示参数${0}, ${param1}
    // User findByName(@Param("name") String name);

    User getById(@Param("id") int id);

    @Insert("INSERT INTO user (name, age) VALUES (#{name}, #{age})")
    void save(@Param("name") String name, @Param("age") int age);

    @Select("SELECT * FROM user WHERE age > #{age}")
    List<User> findByAge(@Param("age") int age);

}
