package net.codejava.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.codejava.Repository.InstanceRepository;
import net.codejava.Service.ProductService;
import net.codejava.model.Instance;
import net.codejava.model.Product;

@Controller
public class AppController {
	@Autowired
	private ProductService service;
	
	@Autowired
	InstanceRepository instrepo;
	
	static int autoinc = 100;
	
	/*
	 * 1. The Home page displays the Name of the Engineer and his role with corresponding instance he is using.
	 * 2. From the home page new instance creation and querying the instance has been added as a feature. 
	 */
	@RequestMapping("/")
	public String viewHomePage(Model model) 
	{
		List<Product> listProducts = service.listAll();
		
		model.addAttribute("listProducts", listProducts);
		return "index";
	}
	
	/*
	 * To add New user along with the Instance ID in the database.
	 */
	@RequestMapping("/new")
	public String showNewProductForm(Model model) 
	{
		Product product = new Product();
		model.addAttribute("product", product);
		
		return "new_product";
		
	}
	
	/*
	 * The saveProduct method is used to save if any changes is made or new user is created in the database.
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Product product) 
	{
		service.save(product);
		
		return "redirect:/";
	}
	
	/*
	 * The update is made for specific engineer using his id as a key.
	 * The get() method is called from the ProductRepository interface.
	 */
	@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductForm(@PathVariable(name = "id") int id) 
	{
		ModelAndView mav = new ModelAndView("edit_product");
		
		Product product =  service.get(id);
		mav.addObject("product", product);
		
		return mav;
	}
	
	/*
	 * 1. The delete operation is made using the Engineer id.
	 * 2. The delete() method is called from the ProductRepository interface.
	 */
	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable (name = "id")int id) 
	{
		service.delete(id);
		return "redirect:/";
	}
	
	/*
	 * The Query method gets instance ID and engineer ID as input and checks whether the instance is available to use.
	 */
	@RequestMapping("/Query")
	public String Query()
	{
		return "Query";
	}
	
	/*
	 * The state of the instance is checked in this function, if the state is true then the instance is
	 * ready to use or else the engineer has to wait until the other engineer releases the instance.
	 */
	@RequestMapping(path = "put")
	public ModelAndView releaseInstance(@RequestParam String id, @RequestParam String employee_id, ModelMap model) 
	{
		autoinc++;
		Instance instance = instrepo.findById(Integer.parseInt(id)).orElse(null);
        Boolean state = instance.isState();
        String viewInstanceName = instance.getName();
        String purpose;
        if (state.equals(true)) {
            instance.setState(false);
            purpose="Out";
        }
        else {
            instance.setState(true);
            purpose = "In";
        }
        instrepo.save(instance);
        
        if(purpose == "Out")
        {
        	String renamed_person = viewInstanceName + " is used by Engineer ID: " + employee_id;
        	System.out.println("Autoinc Purpose out : " + autoinc);
        	Product inst = new Product(autoinc,id,renamed_person,"FreshChat", "SE", purpose);
            service.save(inst);
            ModelAndView mv = new ModelAndView("redirect:/", model);
            return mv;
        }
        else
        {
        	String renamed_person = viewInstanceName + " is release by Engineer ID: " + employee_id;
        	System.out.println("Autoinc Purpose in : " + autoinc);
        	Product inst=new Product(autoinc,id,renamed_person,"FreshChat", "SE", purpose);
            service.save(inst);
            ModelAndView mv = new ModelAndView("redirect:/", model);
            return mv;
        }
        
	}
	
	/*
	 * The ViewInstance method is used to while the details of corresponding engineer in the database.
	 */
	@RequestMapping("/ViewInstance")
	public String ViewInstance()
	{
		return "ViewInstance";
	}
	
	/*
	 * The getInstance method print the engineer information with the ID as an input.
	 */
	@GetMapping("getInstance")
	public ModelAndView getInstance(@RequestParam int id)
	{
		ModelAndView mv = new ModelAndView("redirect:/");
		Instance instance = instrepo.findById(id).orElse(new Instance());
		System.out.println("instance is   " + instance.getName());
		mv.addObject(instance);
		return mv;
	}
	
}
