package com.example.spring.jdbc.util;

import com.example.spring.jdbc.model.People;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http://wiki.fasterxml.com/JacksonInFiveMinutes
 *
 * @author yuweijun 2016-06-08
 */
public class JacksonExample {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Streaming API Example
     *
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        JsonFactory f = new JsonFactory();
        JsonGenerator g = f.createGenerator(System.out, JsonEncoding.UTF8);

        g.writeStartObject();
        g.writeObjectFieldStart("name");
        g.writeStringField("first", "Joe");
        g.writeStringField("last", "Sixpack");
        g.writeEndObject(); // for field 'name'

        g.writeStringField("gender", "m");
        g.writeBooleanField("verified", false);

        g.writeFieldName("userImage"); // no 'writeBinaryField' (yet?)
        byte[] binaryData = "binaryData".getBytes();
        g.writeBinary(binaryData);
        g.writeEndObject();
        g.close();
        // important: will force flushing of output, close underlying output stream
    }

    /**
     * Full Data Binding (POJO) Example
     *
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        People user = mapper.readValue("{\"id\":1,\"fullName\":\"test.yu\",\"jobTitle\":\"programmer\"}", People.class);
        logger.info("user full name is {}", user.getFullName());
    }

    /**
     * "Raw" Data Binding Example
     * <p>
     * Concrete Java types that Jackson will use for simple data binding are:
     * <p>
     * JSON Type                  Java Type
     * object                 LinkedHashMap<String,Object>
     * array                  ArrayList<Object>
     * string                 String
     * number (no fraction)   Integer, Long or BigInteger (smallest applicable)
     * number (fraction)      Double (configurable to use BigDecimal)
     * true|false             Boolean
     * null                   null
     *
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
        Map<String, Object> userData = new HashMap();
        Map<String, String> nameStruct = new HashMap();
        nameStruct.put("first", "Joe");
        nameStruct.put("last", "Sixpack");
        userData.put("name", nameStruct);
        userData.put("gender", "MALE");
        userData.put("verified", Boolean.FALSE);
        userData.put("userImage", "Rm9vYmFyIQ==");

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        System.out.println("1. ==========================");
        mapper.writeValue(System.out, userData);
        // 上面已经将System.out.close()了
        // below lines will not output
        System.out.println("2. ==========================");
        System.out.close();
        System.out.println("3. ==========================");
    }

    /**
     * Data Binding with Generics
     * The main complication is handling of Generic types: if they are used, one has to use TypeReference object, to work around Java Type Erasure:
     * the same as gson:
     * new Gson().fromJson("{}", new TypeToken<Map<String, String>>(){}.getType());
     *
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, People> result = mapper.readValue("{\"user\":{\"id\":1,\"fullName\":\"test.yu\",\"jobTitle\":\"programmer\"}}", new TypeReference<Map<String, People>>() {
        });
        logger.info("generic binding: {}", result);
    }

    @Test
    public void test5() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        People user = new People() {

            public String getAttr1() {
                return "jackson will get this value as property : attr1";
            }

        };
        user.setFullName("test.yu");
        user.setJobTitle("programmer");
        Map<String, People> map = new HashMap();
        map.put("user", user);

        // mapper.writeValue(System.out, map);

        String s = new Gson().toJson(user, People.class);
        System.out.println(s);
    }

    /**
     * Streaming API Example 2: arrays
     */
    @Test
    public void test6() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"foo\": \"bar\"},{\"foo\": \"biz\"}]";
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser(json);
        // advance stream to START_ARRAY first:
        jp.nextToken();
        // and then each time, advance to opening START_OBJECT
        while (jp.nextToken() == JsonToken.START_OBJECT) {
            Map<String, String> foobar = mapper.readValue(jp, Map.class);
            // process
            // after binding, stream points to closing END_OBJECT
            logger.info("map {}", foobar);
        }
    }

    /**
     * test Double with jackson
     * <p>
     * 注意当Double的数字超过10,000,000时,Double会引入科学计数法,此时如果将Double对象转换成字符串时也是科学计数法表示的,千万不能再用字符串的正则表达式进行验证。
     */
    @Test
    public void test7() throws JsonProcessingException {
        Double amount1 = new Double("80099690.89"); // 8.009969089E7
        Double amount2 = new Double("8009969.89"); // 8009969.89
        logger.info("amount1 value toString() {}", amount1);
        logger.info("amount2 value toString() {}", amount2);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Double> map1 = new HashMap();
        map1.put("amount1", amount1);
        map1.put("amount2", amount2);
        String json1 = mapper.writeValueAsString(map1);
        logger.info("json1 is {}", json1);
        String s1 = new Gson().toJson(map1);
        logger.info("gson s1 is {}", s1);

        Map<String, String> map2 = new HashMap();
        map2.put("amount1", "" + amount1);
        map2.put("amount2", "" + amount2);
        String json2 = mapper.writeValueAsString(map2);
        logger.info("json2 is {}", json2);
        String s2 = new Gson().toJson(map2);
        logger.info("gson s1 is {}", s2);
    }

    @Test
    public void test8() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        People user = new People() {

            private String fullName = "this is name in subclass of People";

            public String getAttr1() {
                return "jackson will get this value as property : attr1";
            }

            public String getFullName() {
                return fullName;
            }

        };

        user.setFullName("test.yu");
        user.setJobTitle("programmer");
        Map<String, People> map = new HashMap();
        map.put("user", user);

        String json2 = mapper.writeValueAsString(map);
        System.out.println(json2); // 子类的fullName覆盖了父类的同名属性

        String s = new Gson().toJson(user); // 不给具体类型的情况下，Gson会返回null
        System.out.println(s); // null
        String x = new Gson().toJson(user, People.class);
        System.out.println(x);
    }
}
