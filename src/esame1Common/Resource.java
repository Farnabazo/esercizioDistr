package esame1Common;

import java.io.Serializable;

public class Resource implements Serializable {
	static int count=0;
	private ResourceType type;
	private int resourceNum;
	public Resource (ResourceType t) {
		this.type = t;
		resourceNum=count++;
	}
	public ResourceType getType() {
		return type;
	}
	public int getNum() {
		return resourceNum;
	}
	
}