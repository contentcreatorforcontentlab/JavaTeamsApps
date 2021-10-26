package com.contentlab.teams.java.conversationaltab;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
	@GetMapping("/")
	public String root()
	{
		return "index";
	}
	
	@GetMapping("/index")
	public String index()
	{
		return "index";
	}
	
	@GetMapping("/configure")
	public String configure()
	{
		return "configure";
	}

	@GetMapping("/Configure")
	public String configure2()
	{
		return "configure";
	}

	@GetMapping("/conversationTab")
    public String conversationTab()
    {
        return "conversationTab";
    }
}