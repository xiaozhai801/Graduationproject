package com.zpq.pojo;

import lombok.Data;

@Data
public class UserAction {
	private long id;
	private String userId;
	private int titleId;
	private boolean like;
	private boolean favorite;
}
