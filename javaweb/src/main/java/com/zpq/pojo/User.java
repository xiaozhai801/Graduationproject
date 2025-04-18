package com.zpq.pojo;

import lombok.Data;

@Data
public class User {
	private String userId;
	private String name;
	private String password;
	private String sex;
	private int age;
	private String email;
	private String avatar;
}
