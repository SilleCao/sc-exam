package cn.sille.scexam.controller;

import cn.sille.scexam.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class IndexController {
	@RequestMapping("index")
	public String index(){
		return "index";
	}

	@RequestMapping("welcome")
	public String welcome(){
		return "welcome";
	}
}