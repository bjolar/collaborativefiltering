package hello;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationController {

    private static final String template = "Recommendation for, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/recommendation")
    public Recommendation greeting(@RequestParam(value="name", defaultValue="you") String name) {
    	
    	ImportData id = new ImportData();
		id.readUserData("C:\\Users\\User\\lol\\users.csv");
		id.readCSV("C:\\Users\\User\\lol\\ratings.csv");
		Map<String, Double> recommendations = id.getRecommendation(id.users.get(7), "pearson");
    	
        return new Recommendation(counter.incrementAndGet(),
                            String.format(template, recommendations.toString()));
    }
}