package edu.northeastern.csye6200.entity;

public class Recipe extends Base {
	private static int serial_id = 1;
	private String title;
	private String image_url;
	private String process;

	public Recipe(String title, String image_url, String process) {
		this.id = serial_id++;
		this.title = title;
		this.image_url = image_url;
		this.process = process;
	}
	
	public Recipe(int id, String title, String image_url, String process) {
		serial_id = Math.max(id + 1, serial_id);
		this.id = id;
		this.title = title;
		this.image_url = image_url;
		this.process = process;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}
	
}
