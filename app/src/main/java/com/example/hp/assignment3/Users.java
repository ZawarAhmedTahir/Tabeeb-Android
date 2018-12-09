package com.example.hp.assignment3;

/**
 * Created by Hp on 08-12-2018.
 */

public class Users {

    public String name;
    public String email;
    public String dob;
    public String id;
    public String imgUrl;

    public Users() {
        // ...
    }

    public Users(String id,String name, String email,String dob,String imgUrl) {
        this.id=id;
        this.name=name;
        this.email=email;
        this.dob=dob;
        System.out.print("Image--------------"+imgUrl);
        this.imgUrl=imgUrl;

    }

}