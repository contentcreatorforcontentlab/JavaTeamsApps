package com.contentlab.teams.java.channeltabsso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
	@Autowired
	private Environment env;

	@GetMapping("/")
	public String index()
	{
		return "index";
	}
	
	@GetMapping("/Configure")
	public String configure()
	{
		return "Configure";
	}

	@GetMapping("/Auth/Start")
    public String Start(Model model)
    {
		String clientId = env.getProperty(SSOAuthHelper.ClientIdConfigurationSettingsKey); 

		model.addAttribute("clientId", clientId);
        return "Auth/Start";
    }

	@GetMapping("/Auth/End")
    public String End()
    {
        return "Auth/End";
    }
}