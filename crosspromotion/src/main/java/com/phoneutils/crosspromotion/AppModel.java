package com.phoneutils.crosspromotion;

public class AppModel {
	private String title;
	private String description;
	private String packageName;
	private String logo;
	private String category;
	private long rank;

	public AppModel() {
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public long getRank() {
		return rank;
	}
	public void setRank(long rank) {
		this.rank = rank;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppModel [title=");
		builder.append(title);
		builder.append(", description=");
		builder.append(description);
		builder.append(", packageName=");
		builder.append(packageName);
		builder.append(", logo=");
		builder.append(logo);
		builder.append(", category=");
		builder.append(category);
		builder.append(", rank=");
		builder.append(rank);
		builder.append("]");
		return builder.toString();
	}
}
