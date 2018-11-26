package cn.sille.scexam.controller;

import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.Student;
import cn.sille.scexam.service.ClassGroupService;
import cn.sille.scexam.service.StudentService;
import cn.sille.scexam.util.Result;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/classGroup")
public class ClassGroupController {
	@Autowired
	private ClassGroupService classGroupService;

	@RequestMapping("/add")
	public String add() {
		return "/admin/classGroup/edit";
	}

	@ResponseBody
	@RequestMapping("/save")
	public Result save(ClassGroup classGroup){
		try{
			return Result.success(classGroupService.add(classGroup));
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@PostMapping("/delete/{id}")
	public Result delete(@PathVariable Long id){
		try{
			classGroupService.delete(id);
			return Result.success();
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@PostMapping("/update")
	public Result update(@RequestBody ClassGroup classGroup){
		try{
			return Result.success(classGroupService.update(classGroup));
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@GetMapping("/get/{id}")
	public Result get(@PathVariable Long id){
		try{
			ClassGroup classGroup = classGroupService.get(id);
			if(classGroup ==null){throw new RuntimeException();}
			return Result.success(classGroup);
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@GetMapping("/getAll")
	public Result getAll(){
		try{
			return Result.success(classGroupService.getAll());
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@RequestMapping("/getPage")
	public String getPage(ModelMap model, Pageable pageable) {
        pageable.getSortOr(new Sort(Sort.Direction.DESC, "userCode"));
		Page<ClassGroup> pageBean = classGroupService.getPage(pageable);
		model.addAttribute("modelList", pageBean.getContent());
		model.addAttribute("curPage", pageBean.getNumber());
		model.addAttribute("totalPages", pageBean.getTotalPages());
		return "/admin/classGroup/list";
	}
}