package com.zpq.pojo;

import lombok.Data;

@Data
public class Usercomment {
	private long commentId;
	private String name;
	private long articleId;
	private String topic;
	private String comment;
	private String uploadTime;
}
