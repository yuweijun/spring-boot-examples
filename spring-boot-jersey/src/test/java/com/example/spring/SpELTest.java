package com.example.spring;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;

/**
 * http://www.importnew.com/17692.html
 * http://www.importnew.com/17724.html
 *
 * @author yuweijun 2017-02-15
 */
public class SpELTest {

    @Test
    public void add() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("1 + 2");
        int value = expression.getValue(int.class);
        Assert.assertEquals(3, value);
    }

    @Test
    public void helloWorld() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("('Hello' + ' World').concat(#end)");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("end", "!");
        String value = expression.getValue(context, String.class);
        Assert.assertEquals("Hello World!", value);
    }

    /**
     * 模板表达式就是由字面量与一个或多个表达式块组成。
     * 每个表达式块由“前缀+表达式+后缀”形式组成，如“${1+2}”即表达式块。
     * 在前边我们已经介绍了使用ParserContext接口实现来定义表达式是否是模板及前缀和后缀定义。
     * 在此就不多介绍了，
     * 如“Error ${#v0} ${#v1}”表达式表示由字面量“Error ”、模板表达式“#v0”、模板表达式“#v1”组成，
     * 其中v0和v1表示自定义变量，需要在上下文定义。
     */
    @Test
    public void testParserContext() {
        ExpressionParser parser = new SpelExpressionParser();
        ParserContext parserContext = new ParserContext() {
            @Override
            public boolean isTemplate() {
                return true;
            }

            @Override
            public String getExpressionPrefix() {
                return "#{";
            }

            @Override
            public String getExpressionSuffix() {
                return "}";
            }
        };
        String template = "#{'Hello '}#{'World!'}";
        Expression expression = parser.parseExpression(template, parserContext);
        Assert.assertEquals("Hello World!", expression.getValue());
    }

    @Test
    public void types() {
        ExpressionParser parser = new SpelExpressionParser();
        String str1 = parser.parseExpression("'Hello World!'").getValue(String.class);
        String str2 = parser.parseExpression("\"Hello World!\"").getValue(String.class);

        int int1 = parser.parseExpression("1").getValue(Integer.class);
        long long1 = parser.parseExpression("-1L").getValue(long.class);
        float float1 = parser.parseExpression("1.1").getValue(Float.class);
        double double1 = parser.parseExpression("1.1E+2").getValue(double.class);
        int hex1 = parser.parseExpression("0xa").getValue(Integer.class);
        long hex2 = parser.parseExpression("0xaL").getValue(long.class);

        boolean true1 = parser.parseExpression("true").getValue(boolean.class);
        boolean false1 = parser.parseExpression("false").getValue(boolean.class);

        Object aNull = parser.parseExpression("null").getValue(Object.class);
        assertNull(aNull);
    }

    @Test
    public void calculator() {
        ExpressionParser parser = new SpelExpressionParser();
        // 幂运算
        int result3 = parser.parseExpression("2 ^ 3").getValue(Integer.class); // 8
        System.out.println(result3);
        int result2 = parser.parseExpression("4 MOD 3").getValue(Integer.class); // 8
        System.out.println(result2);

        String expression1 = "2>1 and (!true or !false)";
        boolean result1 = parser.parseExpression(expression1).getValue(boolean.class);
        Assert.assertEquals(true, result1);

        String expression2 = "2>1 and (NOT true or NOT false)";
        boolean result4 = parser.parseExpression(expression2).getValue(boolean.class);
        Assert.assertEquals(true, result4);

        String expression3 = "2>1 && (NOT true || NOT false)";
        boolean result5 = parser.parseExpression(expression3).getValue(boolean.class);
        Assert.assertEquals(true, result5);
    }

    @Test
    public void stringTruncate() {
        ExpressionParser parser = new SpelExpressionParser();
        String expressionString = "'Hello World!'[0]";
        Expression expression = parser.parseExpression(expressionString);
        String value = expression.getValue(String.class);
        System.out.println(value);
        assertEquals("H", value);
    }

    public String key;

    @Test
    public void elvis() {
        ExpressionParser parser = new SpelExpressionParser();
        String expressionString = "true?:false";
        Expression expression = parser.parseExpression(expressionString);
        boolean value = expression.getValue(boolean.class);
        assertTrue(value);
        expressionString = "null?:1";
        expression = parser.parseExpression(expressionString);
        int value2 = expression.getValue(int.class);
        System.out.println(value2);
        expressionString = "key?:'HelloWorld'";
        expression = parser.parseExpression(expressionString);
        String value3 = expression.getValue(this, String.class);
        System.out.println(value3);
        assertEquals("HelloWorld", value3);
    }

    @Test
    public void lengthOfArray() {
        ExpressionParser parser = new SpelExpressionParser();
        String[] context = {"a", "b"};
        String expressionString = "length?:0";
        Expression expression = parser.parseExpression(expressionString);
        int length = expression.getValue(context, int.class);
        System.out.println(length);
        assertEquals(2, length);
    }

    @Test
    public void regexp() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("'123' matches '\\d+'");
        boolean value = expression.getValue(boolean.class);
        System.out.println(value);
        assertTrue(value);

        expression = parser.parseExpression("'123' matches '\\d{3}'");
        value = expression.getValue(boolean.class);
        System.out.println(value);
        assertTrue(value);
    }

    @Test
    public void testClassTypeExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        //java.lang包类访问
        Class<String> result1 = parser.parseExpression("T(String)").getValue(Class.class);
        Assert.assertEquals(String.class, result1);
        //其他包类访问
        String expression2 = "T(com.example.spring.SpELTest)";
        Class<String> result2 = parser.parseExpression(expression2).getValue(Class.class);
        Assert.assertEquals(SpELTest.class, result2);
        //类静态字段访问
        int result3 = parser.parseExpression("T(Integer).MAX_VALUE").getValue(int.class);
        Assert.assertEquals(Integer.MAX_VALUE, result3);
        //类静态方法调用
        int result4 = parser.parseExpression("T(Integer).parseInt('1')").getValue(int.class);
        Assert.assertEquals(1, result4);
    }

    @Test
    public void testConstructorExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        String result1 = parser.parseExpression("new String('haha')").getValue(String.class);
        Assert.assertEquals("haha", result1);
        Date result2 = parser.parseExpression("new java.util.Date()").getValue(Date.class);
        Assert.assertNotNull(result2);
    }


    @Test
    public void testInstanceof() {
        ExpressionParser parser = new SpelExpressionParser();
        boolean result1 = parser.parseExpression("'hello' instanceof T(java.lang.String)").getValue(boolean.class);
        Assert.assertTrue(result1);
    }

    /**
     * 使用“#variable”来引用在EvaluationContext定义的变量；
     * 除了可以引用自定义变量，
     * 还可以使用“#root”引用根对象，
     * “#this”引用当前上下文对象。
     */
    @Test
    public void testVariableExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("variable", "haha");
        context.setVariable("variable", "haha");
        String result1 = parser.parseExpression("#variable").getValue(context, String.class);
        Assert.assertEquals("haha", result1);

        context = new StandardEvaluationContext("haha");
        String result2 = parser.parseExpression("#root").getValue(context, String.class);
        Assert.assertEquals("haha", result2);
        String result3 = parser.parseExpression("#this").getValue(context, String.class);
        Assert.assertEquals("haha", result3);
    }

    @Test
    public void testFunctionExpression() throws SecurityException, NoSuchMethodException {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        Method parseInt = Integer.class.getDeclaredMethod("parseInt", String.class);
        context.registerFunction("parseInt", parseInt);
        context.setVariable("parseInt2", parseInt);
        String expression1 = "#parseInt('3') == #parseInt2('3')";
        boolean result1 = parser.parseExpression(expression1).getValue(context, boolean.class);
        Assert.assertEquals(true, result1);
    }

    @Test
    public void testAssignExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        // 1.给root对象赋值
        EvaluationContext context = new StandardEvaluationContext("root");
        String result1 = parser.parseExpression("#root='new'").getValue(context, String.class);
        Assert.assertEquals("new", result1);
        System.out.println(result1);
        String result2 = parser.parseExpression("#this='old'").getValue(context, String.class);
        Assert.assertEquals("old", result2);
        System.out.println(result2);

        // 2.给自定义变量赋值
        context.setVariable("#variable", "variable");
        String result3 = parser.parseExpression("#variable=#root").getValue(context, String.class);
        Assert.assertEquals("root", result3);
        System.out.println(result3);
    }

    /**
     * 对象属性获取非常简单，即使用如“a.property.property”这种点缀式获取，
     * SpEL对于属性名首字母是不区分大小写的；
     * SpEL还引入了Groovy语言中的安全导航运算符“(对象|属性)?.属性”，
     * 用来避免但“?.”前边的表达式为null时抛出空指针异常，而是返回null；
     * 修改对象属性值则可以通过赋值表达式或Expression接口的setValue方法修改。
     */
    @Test
    public void testProperties() {
        ExpressionParser parser = new SpelExpressionParser();
        // 1.访问root对象属性
        Date date = new Date();
        StandardEvaluationContext context = new StandardEvaluationContext(date);
        // 对于当前上下文对象属性及方法访问，可以直接使用属性或方法名访问，
        // 比如此处根对象date属性“year”，注意此处属性名首字母不区分大小写。
        int result1 = parser.parseExpression("Year").getValue(context, int.class);
        System.out.println(result1);
        Assert.assertEquals(date.getYear(), result1);
        int result2 = parser.parseExpression("year").getValue(context, int.class);
        Assert.assertEquals(date.getYear(), result2);
        System.out.println(result2);

        // 2.安全访问
        context.setRootObject(null);
        // SpEL引入了Groovy的安全导航运算符，比如此处根对象为null，所以如果访问其属性时肯定抛出空指针异常，
        // 而采用“?.”安全访问导航运算符将不抛空指针异常，而是简单的返回null。
        Object result3 = parser.parseExpression("#root?.year").getValue(context, Object.class);
        Assert.assertEquals(null, result3);
        System.out.println(result3);

        // 3.给root对象属性赋值
        context.setRootObject(date);
        int result4 = parser.parseExpression("Year = 4").getValue(context, int.class);
        Assert.assertEquals(4, result4);
        System.out.println(result4);

        // 给对象属性赋值可以采用赋值表达式或Expression接口的setValue方法赋值，而且也可以采用点缀方式赋值。
        parser.parseExpression("Year").setValue(context, 5);
        int result5 = parser.parseExpression("Year").getValue(context, int.class);
        System.out.println(result5);
        Assert.assertEquals(5, result5);
    }

    @Test
    public void testMethodInvoke() {
        // 对象方法调用更简单，跟Java语法一样；如“’haha’.substring(2,4)”将返回“ha”；而对于根对象可以直接调用方法；
        ExpressionParser parser = new SpelExpressionParser();
        Date date = new Date();
        StandardEvaluationContext context = new StandardEvaluationContext(date);
        int result2 = parser.parseExpression("getYear()").getValue(context, int.class);
        Assert.assertEquals(date.getYear(), result2);
        System.out.println(result2);
    }

    /**
     * SpEL支持使用“@”符号来引用Bean，在引用Bean时需要使用BeanResolver接口实现来查找Bean，
     * Spring提供BeanFactoryResolver实现；
     */
    @Test
    public void testBeanExpression() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
        ctx.refresh();
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(ctx));
        Properties result1 = parser.parseExpression("@systemProperties").getValue(context, Properties.class);
        Assert.assertEquals(System.getProperties(), result1);
    }

    /**
     * 从Spring3.0.4开始支持内联List，使用{表达式，……}定义内联List，
     * 如“{1,2,3}”将返回一个整型的ArrayList，而“{}”将返回空的List，对于字面量表达式列表，
     * SpEL会使用java.util.Collections.unmodifiableList方法将列表设置为不可修改。
     */
    @Test
    public void testCollections() {
        ExpressionParser parser = new SpelExpressionParser();
        //将返回不可修改的空List
        List<Integer> result1 = parser.parseExpression("{}").getValue(List.class);
        //对于字面量列表也将返回不可修改的List
        List<Integer> result2 = parser.parseExpression("{1,2,3}").getValue(List.class);
        Assert.assertEquals(new Integer(1), result2.get(0));
        try {
            result2.set(0, 2);
            //不可能执行到这，对于字面量列表不可修改
            Assert.fail();
        } catch (Exception e) {
        }

        //对于列表中只要有一个不是字面量表达式，将只返回原始List，
        //不会进行不可修改处理
        String expression3 = "{{1+2,2+4},{3,4+4}}";
        List<List<Integer>> result3 = parser.parseExpression(expression3).getValue(List.class);
        result3.get(0).set(0, 1);
        Assert.assertEquals(2, result3.size());

        //声明一个大小为2的一维数组并初始化
        int[] result5 = parser.parseExpression("new int[2]{1,2}").getValue(int[].class);

        //定义一维数组但不初始化
        int[] result6 = parser.parseExpression("new int[1]").getValue(int[].class);

        //定义多维数组但不初始化
        int[][][] result7 = parser.parseExpression("new int[1][2][3]").getValue(int[][][].class);
        //错误的定义多维数组，多维数组不能初始化
        String expression4 = "new int[1][2][3]{{1}{2}{3}}";
        try {
            int[][][] result4 = parser.parseExpression(expression4).getValue(int[][][].class);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testCollectionElementAccess() {
        ExpressionParser parser = new SpelExpressionParser();

        //SpEL内联List访问
        int result1 = parser.parseExpression("{1,2,3}[0]").getValue(int.class);
        //即list.get(0)
        Assert.assertEquals(1, result1);

        //SpEL目前支持所有集合类型的访问
        Collection<Integer> collection = new HashSet<Integer>();
        collection.add(1);
        collection.add(2);
        EvaluationContext context2 = new StandardEvaluationContext();
        context2.setVariable("collection", collection);
        int result2 = parser.parseExpression("#collection[1]").getValue(context2, int.class);
        //对于任何集合类型通过Iterator来定位元素
        Assert.assertEquals(2, result2);

        //SpEL对Map字典元素访问的支持
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("a", 1);
        EvaluationContext context3 = new StandardEvaluationContext();
        context3.setVariable("map", map);
        int result3 = parser.parseExpression("#map['a']").getValue(context3, int.class);
        Assert.assertEquals(1, result3);
    }

    @Test
    public void elementEdit() {
        ExpressionParser parser = new SpelExpressionParser();
        // 1.修改数组元素值
        int[] array = new int[]{1, 2};
        EvaluationContext context1 = new StandardEvaluationContext();
        context1.setVariable("array", array);
        int result1 = parser.parseExpression("#array[1] = 3").getValue(context1, int.class);
        Assert.assertEquals(3, result1);

        // 2.修改集合值
        Collection<Integer> collection = new ArrayList<Integer>();
        collection.add(1);
        collection.add(2);
        EvaluationContext context2 = new StandardEvaluationContext();
        context2.setVariable("collection", collection);
        int result2 = parser.parseExpression("#collection[1] = 3").getValue(context2, int.class);
        Assert.assertEquals(3, result2);
        parser.parseExpression("#collection[1]").setValue(context2, 4);
        result2 = parser.parseExpression("#collection[1]").getValue(context2, int.class);
        Assert.assertEquals(4, result2);

        // 3.修改map元素值
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("a", 1);
        EvaluationContext context3 = new StandardEvaluationContext();
        context3.setVariable("map", map);
        int result3 = parser.parseExpression("#map['a'] = 2").getValue(context3, int.class);
        Assert.assertEquals(2, result3);
    }

    @Test
    public void testProjection() {
        // 在SQL中投影指从表中选择出列，而在SpEL指根据集合中的元素中通过选择来构造另一个集合，
        // 该集合和原集合具有相同数量的元素；SpEL使用“（list|map）.![投影表达式]”来进行投影运算
        ExpressionParser parser = new SpelExpressionParser();

        // 1.首先准备测试数据
        Collection<Integer> collection = new ArrayList<Integer>();
        collection.add(4);
        collection.add(5);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("a", 1);
        map.put("b", 2);
        // 2.测试集合或数组
        EvaluationContext context1 = new StandardEvaluationContext();
        context1.setVariable("collection", collection);
        Collection<Integer> result1 = parser.parseExpression("#collection.![#this+1]")
                .getValue(context1, Collection.class);
        Assert.assertEquals(2, result1.size());
        Assert.assertEquals(new Integer(5), result1.iterator().next());

        // 对于集合或数组使用如上表达式进行投影运算，
        // 其中投影表达式中“#this”代表每个集合或数组元素，
        // 可以使用比如“#this.property”来获取集合元素的属性，其中“#this”可以省略。
        //3.测试字典
        EvaluationContext context2 = new StandardEvaluationContext();
        context2.setVariable("map", map);
        List<Integer> result2 = parser.parseExpression("#map.![value+1]")
                .getValue(context2, List.class);
        Assert.assertEquals(2, result2.size());
        // SpEL投影运算还支持Map投影，但Map投影最终只能得到List结果，
        // 如上所示，对于投影表达式中的“#this”将是Map.Entry，所以可以使用“value”来获取值，使用“key”来获取键。
    }

    @Test
    public void elementSelection() {
        // 集合选择：
        // 在SQL中指使用select进行选择行数据，
        // 而在SpEL指根据原集合通过条件表达式选择出满足条件的元素并构造为新的集合，
        // SpEL使用“(list|map).?[选择表达式]”，
        // 其中选择表达式结果必须是boolean类型，
        // 如果true则选择的元素将添加到新集合中，false将不添加到新集合中。
        ExpressionParser parser = new SpelExpressionParser();
        //1.首先准备测试数据
        Collection<Integer> collection = new ArrayList<Integer>();
        collection.add(4);
        collection.add(5);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("a", 1);
        map.put("b", 2);
        //2.集合或数组测试
        EvaluationContext context1 = new StandardEvaluationContext();
        context1.setVariable("collection", collection);
        // 对于集合或数组选择，如“#collection.?[#this>4]”将选择出集合元素值大于4的所有元素。
        // 选择表达式必须返回布尔类型，使用“#this”表示当前元素。
        Collection<Integer> result1 = parser.parseExpression("#collection.?[#this>4]")
                .getValue(context1, Collection.class);
        Assert.assertEquals(1, result1.size());
        Assert.assertEquals(new Integer(5), result1.iterator().next());
        //3.字典测试
        EvaluationContext context2 = new StandardEvaluationContext();
        context2.setVariable("map", map);
        // 对于字典选择，如“#map.?[#this.key != 'a']”将选择键值不等于”a”的，其中选择表达式中“#this”是Map.Entry类型，
        // 而最终结果还是Map，这点和投影不同；
        // 集合选择和投影可以一起使用，如“#map.?[key != 'a'].![value+1]”将首先选择键值不等于”a”的，
        // 然后在选出的Map中再进行“value+1”的投影。
        Map<String, Integer> result2 = parser.parseExpression("#map.?[#this.key != 'a']")
                .getValue(context2, Map.class);
        Assert.assertEquals(1, result2.size());
        List<Integer> result3 = parser.parseExpression("#map.?[key != 'a'].![value+1]")
                .getValue(context2, List.class);
        Assert.assertEquals(new Integer(3), result3.iterator().next());
    }

}
