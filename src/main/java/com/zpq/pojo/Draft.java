package com.zpq.pojo;

import lombok.Data;

@Data
public class Draft {
	private int draftId;
	private String topic;
	private String userId;
	private String name;
	private String model;
	private int typeId;
	private String data_html;
	private String data_text;
}
