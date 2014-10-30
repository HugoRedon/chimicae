package refinery;

import com.google.gson.annotations.Expose;

public class Equipment {
	@Expose
	protected long id;
	
	public Equipment(long id){
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
}
