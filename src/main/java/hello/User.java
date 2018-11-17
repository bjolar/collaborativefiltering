package hello;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String name;
	public ArrayList<Movie> movieList = new ArrayList<Movie>();
	
	public User() {
	}
	
	public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
