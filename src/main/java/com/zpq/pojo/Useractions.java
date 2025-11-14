package com.zpq.pojo;

import lombok.Data;

@Data
public class Useractions {
	private long actionId;
	private String name;
	private long articleId;
	private String topic;
	private boolean like;
	private boolean favorite;
}
