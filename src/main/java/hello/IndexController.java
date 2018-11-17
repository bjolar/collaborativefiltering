package hello;

import org.springframework.web.bind.annotation.RequestParam;

import java.awt.PageAttributes.MediaType;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	  public String index(String name, Model model) {
		ImportData id = new ImportData();
		id.readUserData("C:\\Users\\User\\lol\\users.csv");
		model.addAttribute("users", id.users);
		UserForm userForm = new UserForm();
		userForm.setDistance("pearson");
		model.addAttribute("userForm", userForm);
	    return "index";
	  }
	
	@PostMapping("/")
	public String indexSubmit(@ModelAttribute ("userForm") UserForm userForm, Model model ) {
		//System.out.println(userForm.getId());
		ImportData id = new ImportData();
		id.readUserData("C:\\Users\\User\\lol\\users.csv");
		id.readCSV("C:\\Users\\User\\lol\\ratings.csv");
		model.addAttribute("users", id.users);
		User u = id.users.get(userForm.getId());
		System.out.println(userForm.getDistance());
		Map<String, Double> recommendations = id.getRecommendation(u, userForm.getDistance());
		System.out.println(recommendations.toString());
		model.addAttribute("recommendations", recommendations);
		return "index";
	}
	
	@GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
        return "greeting.html";
    }
}