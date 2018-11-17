package hello;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.*;

public class ImportData {

	public Map<Integer , User> users = new HashMap<Integer, User>();
	public Set<String> allMovies = new HashSet<String>();
	
	/**
	 * 
	 */
	public ImportData() {
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ImportData id = new ImportData();
		id.readUserData("C:\\Users\\User\\lol\\users.csv");
		id.readCSV("C:\\Users\\User\\lol\\ratings.csv");
		id.test();
	}
	
	/**
	 * 
	 * @param user
	 * @param method
	 * @return
	 */
	public Map<String, Double> getRecommendation(User user, String method) {
		 
		HashSet<String> us = unseen(user);
		//System.out.println(us);
		Map<String , Double> ratings = new HashMap<String, Double>();
		Map<String , Double> simSums = new HashMap<String, Double>();
		Map<String , Double> total = new HashMap<String, Double>();

		for (User otherUser: users.values() ) {
			if (otherUser.id != user.id /**&& otherUser.id != 3**/) {
				double sim;
				if (method.equals("pearson")) {
					sim = pearson(user, otherUser);
				} else {
					sim = euclidean(user, otherUser);
				}						
				//System.out.println(user.name + " " + otherUser.name + " " + sim);
				for(Movie m : otherUser.movieList) {
					if (us.contains(m.name)) {
						//System.out.println(m.name);
						//double weighted = round((sim * m.rating), 4);
						double weighted = sim * m.rating;
						//System.out.println(weighted);
						if (!ratings.containsKey(m.name)) {
							ratings.put(m.name, weighted);
							simSums.put(m.name, sim);
						} else {
							double r = ratings.get(m.name);
							//ratings.replace(m.name, round(r+weighted, 3));
							ratings.replace(m.name, (r+weighted));
							double s = simSums.get(m.name);
							//simSums.replace(m.name, round(s+sim, 3));
							simSums.replace(m.name, (s+sim));

						}					
					}
				}
			}
		}
		//System.out.println(ratings.toString());
		//System.out.println(simSums.toString());
		
		for (String name : ratings.keySet()) {
			double a = ratings.get(name);
			double b = simSums.get(name);
			//total.put(name, round(a/b, 3));
			total.put(name, (a/b));
		}
		Map<String, Double> sorted = total
		        .entrySet()
		        .stream()
		        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		        .collect(
		            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
		                LinkedHashMap::new));
		//System.out.println(sorted.toString());
		return sorted;
	}
	
	/**
	 * Test method
	 */
	public void test() {
		//System.out.println(users.get(1).name);
		//System.out.println(users.get(1).movieList.toString());
		double num = euclidean(users.get(7), users.get(1));
		System.out.println(num);
		System.out.println(allMovies.size());
		getRecommendation(users.get(7), "pearson");
		//HashSet<String> us = unseen(users.get(7));
		
		double pNum = pearson(users.get(7), users.get(4));
		System.out.println(pNum);
		
	}
	
	/**
	 *  Read in user data
	 * @param path
	 */
	public void readUserData(String path) {
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] splitted = line.split(";");
		        if (isInteger(splitted[1])) {
			        User newUser = new User();
			        newUser.name = splitted[0];
			        newUser.id = Integer.parseInt(splitted[1]);
			        users.put(newUser.id, newUser);
		        }     
		    }
		} catch (Exception e) {
			System.out.println(e.toString());
	    }
		//System.out.println(users.get(1).name);
	}

	/**
	 * Read CSV
	 * @param path
	 */
	public void readCSV(String path) {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] splitted = line.split(";(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		        if (isInteger(splitted[0])) {
		        	Integer i = Integer.parseInt(splitted[0]);
		        	Movie m = new Movie();
		        	m.name = splitted[1];
		        	allMovies.add(m.name);
		        	m.rating = Double.parseDouble(splitted[2]);
		        	users.get(i).movieList.add(m);
		        }      
		    }
		} catch (Exception e) {
			System.out.println(e.toString());
	    }
	}
	
	/**
	 * Calculate euclidean distance
	 * @param first
	 * @param second
	 * @return
	 */
	public double euclidean( User first, User second) {
		double sim = 0.0;
		int n = 0;
		//System.out.println(first.name);
		//System.out.println(second.name);
		for (Movie firstMovie : first.movieList) {
			for (Movie secondMovie : second.movieList) {
				//System.out.println(firstMovie.name);
				//System.out.println(secondMovie.name);
				if (firstMovie.name.equals(secondMovie.name) ) {
					//System.out.println("inne");
					sim += Math.pow((firstMovie.rating - secondMovie.rating), 2);
					n += 1;
				}
			}
		}
		if (n == 0) {
			return 0.0;
		}
		return round((1 / (1 + sim)), 3);	
	}
	
	/**
	 * Calculate pearson correlation score
	 * @param first
	 * @param second
	 * @return
	 */
	public double pearson(User first, User second) {
		double sum1 = 0.0;
		double sum2 = 0.0;
		double sum1sq = 0.0;
		double sum2sq = 0.0;
		double pSum = 0.0;
		int n = 0;
		for (Movie firstMovie : first.movieList) {
			for (Movie secondMovie : second.movieList) {
				//System.out.println(firstMovie.name);
				//System.out.println(secondMovie.name);
				if (firstMovie.name.equals(secondMovie.name) ) {
					sum1 += firstMovie.rating;
					sum2 += secondMovie.rating;
					sum1sq += Math.pow(firstMovie.rating, 2);
					sum2sq += Math.pow(secondMovie.rating, 2);
					pSum += firstMovie.rating * secondMovie.rating;
					n += 1;
				}
			}
		}
		if (n == 0) {
			return 0.0;
		}
		double num = pSum - ((sum1 * sum2) / n);
		double den = Math.sqrt((sum1sq - (Math.pow(sum1, 2) / n)) * (sum2sq - (Math.pow(sum2, 2) / n))); 	
		return num/den;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public HashSet<String> unseen(User user) {
		HashSet<String> unseenMovies = new HashSet<String>();
		
		for (String name: allMovies) {
			boolean include = false;
			for(Movie m: user.movieList) {
				if (name.equals(m.name)) {
					include = true;
				}
			}
			if(!include) {
				unseenMovies.add(name);
			}
		}
		return unseenMovies;
	}
	
	/**
	 * 
	 * @param val
	 * @param places
	 * @return
	 */
	public double round(double val, int places){
        if(places < 0) throw new IllegalArgumentException();
        
        BigDecimal bigDecimal = new BigDecimal(val);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public boolean isInteger( String input ) {
	    try {
	        Integer.parseInt( input );
	        return true;
	    }
	    catch( NumberFormatException e ) {
	        return false;
	    }
	}
}
