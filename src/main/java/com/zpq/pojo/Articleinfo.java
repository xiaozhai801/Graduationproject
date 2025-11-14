package com.zpq.pojo;

import lombok.Data;

@Data
public class Articleinfo {
	private long articleId;
	private int userId;
	private String name;
	private String topic;
	private String typeName;
	private String uploadTime;
	private int release;
	private int views;
	private int likes;
	private int favorites;
	private String dataHtml;
}
