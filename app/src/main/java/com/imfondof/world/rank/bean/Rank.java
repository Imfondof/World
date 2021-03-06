package com.imfondof.world.rank.bean;

public class Rank {

	private int id;
	private String type;	//类型
	private String name;	//备注
	private float score;	//分数
	private int vcount;		//成功的次数
	private int dcount;		//失败的次数
	private int allcount;	//总次数

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getVcount() {
		return vcount;
	}

	public void setVcount(int vcount) {
		this.vcount = vcount;
	}

	public int getDcount() {
		return dcount;
	}

	public void setDcount(int dcount) {
		this.dcount = dcount;
	}

	public int getAllcount() {
		return allcount;
	}

	public void setAllcount(int allcount) {
		this.allcount = allcount;
	}

	public Rank(int id, String type, String name, float score, int vcount,
			int dcount, int allcount) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.score = score;
		this.vcount = vcount;
		this.dcount = dcount;
		this.allcount = allcount;
	}

	@Override
	public String toString() {
		return "Rank [id=" + id + ", type=" + type + ", name=" + name
				+ ", score=" + score + ", vcount=" + vcount + ", dcount="
				+ dcount + ", allcount=" + allcount + "]";
	}
}
