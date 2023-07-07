package com.ldy.ex3.POJO;

public class City {
    private String name;
    private String code;
    private String id;
    private int level;

    public City() {
    }

    public City(String name, String code, String id, int level) {
        this.name = name;
        this.code = code;
        this.id = id;
        this.level = level;

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
