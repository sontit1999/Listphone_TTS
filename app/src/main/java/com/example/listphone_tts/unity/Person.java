package com.example.listphone_tts.unity;

public class Person {
    private int id;
    private String linkanh;
    private String name;
    private String phonenumber;
    private Boolean ischeck;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(Boolean ischeck) {
        this.ischeck = ischeck;
    }

    public Person(int id,String linkanh, String name, String phonenumber) {
        this.id = id;
        this.linkanh = linkanh;
        this.name = name;
        this.phonenumber = phonenumber;
        this.ischeck = false;
    }

    public String getLinkanh() {
        return linkanh;
    }

    public void setLinkanh(String linkanh) {
        this.linkanh = linkanh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
