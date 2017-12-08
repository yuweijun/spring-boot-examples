package com.example.spring.jdbc.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * http://www.it610.com/article/5121861.htm
 * Gson转换同父类不同子类列表
 *
 * @auther yuweijun on 2017-06-12.
 */
public class GsonParentAndDifferentChild {

    public static void main(String[] args) {
        String resultStr = "{'code':'00','personList':[{\"id\":\"123456\",\"productCategory\":\"101\",\"name\":\"name\",\"age\":\"25\",\"ranking\":\"0\"},{\"id\":\"123456\",\"productCategory\":\"201\",\"name\":\"name\",\"age\":\"25\",\"ranking\":\"1\"}]}";

        Gson gson = new GsonBuilder().registerTypeAdapter(RANKING.class, new JsonDeserializer<RANKING>() {
            @Override
            public RANKING deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                int ranking = json.getAsJsonPrimitive().getAsInt();
                return RANKING.values()[ranking];
            }
        }).registerTypeAdapter(new TypeToken<List<BasePerson>>() {
        }.getType(), new JsonDeserializer<List<BasePerson>>() {
            @Override
            public List<BasePerson> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List list = new ArrayList<BasePerson>();
                JsonArray ja = json.getAsJsonArray();
                for (JsonElement je : ja) {
                    String type = je.getAsJsonObject().get("productCategory").getAsString();
                    list.add(context.deserialize(je, PersonType.getByProductCategory(type).getType()));
                }
                return list;
            }
        }).create();
        ResultEntity result = gson.fromJson(resultStr, ResultEntity.class);
        System.out.println(result);
    }
}

class BasePerson {
    private Long id;
    private String productCategory;
    private RANKING ranking;

    public RANKING getRanking() {
        return ranking;
    }

    public void setRanking(RANKING ranking) {
        this.ranking = ranking;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BasePerson{" +
                "id=" + id +
                ", productCategory='" + productCategory + '\'' +
                ", ranking=" + ranking +
                '}';
    }
}

class PersonWithName extends BasePerson {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class PersonWithAge extends BasePerson {
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

enum PersonType {
    WITHNAME("名称", PersonWithName.class, "101"),
    WITHAGE("年龄", PersonWithAge.class, "201");

    private String displayName;
    private Class<? extends BasePerson> type;
    private String productCategory;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Class<? extends BasePerson> getType() {
        return type;
    }

    public void setType(Class<? extends BasePerson> type) {
        this.type = type;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    private PersonType(String displayName, Class<? extends BasePerson> type, String productCategory) {
        this.displayName = displayName;
        this.type = type;
        this.productCategory = productCategory;
    }

    public static PersonType getByProductCategory(String category) {
        for (PersonType t : values()) {
            if (t.productCategory.equals(category)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "PersonType{" +
                "displayName='" + displayName + '\'' +
                ", type=" + type +
                ", productCategory='" + productCategory + '\'' +
                '}';
    }
}

enum RANKING {
    TOP, BOTTOM;
}

class ResultEntity {
    String code;
    List<BasePerson> personList;

    public List<BasePerson> getPersonList() {
        return personList;
    }

    public void setPersonList(List<BasePerson> personList) {
        this.personList = personList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "code='" + code + '\'' +
                ", personList=" + personList +
                '}';
    }
}
