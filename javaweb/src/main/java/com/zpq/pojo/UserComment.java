package com.zpq.pojo;

import lombok.Data;

@Data
public class UserComment {
	private long id;
	private String userId;
	private String name;
	private int titleId;
	private String comment;
	private String uploadTime;
	
}
