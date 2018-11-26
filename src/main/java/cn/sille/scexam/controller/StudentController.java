package cn.sille.scexam.controller;

import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.Student;
import cn.sille.scexam.service.StudentService;
import cn.sille.scexam.util.Result;

import org.apache.commons.lang3.StringUtils;
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

@Controller
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private StudentService studentService;

	@PostMapping("/add")
	public Result add(@RequestBody Student student){
		try{
			return Result.success(studentService.add(student));
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@PostMapping("/delete/{id}")
	public Result delete(@PathVariable Long id){
		try{
			studentService.delete(id);
			return Result.success();
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@PostMapping("/update")
	public Result update(@RequestBody Student student){
		try{
			return Result.success(studentService.update(student));
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@GetMapping("/get/{id}")
	public Result get(@PathVariable Long id){
		try{
			Student student = studentService.get(id);
			if(student ==null){throw new RuntimeException();}
			return Result.success(student);
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@GetMapping("/getAll")
	public Result getAll(){
		try{
			return Result.success(studentService.getAll());
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure(e.toString());
		}
	}

	@RequestMapping("/getPage")
	public String getPage(ModelMap model, Pageable pageable) {
        pageable.getSortOr(new Sort(Sort.Direction.DESC, "userCode"));
		Page<Student> pageBean = studentService.getPage(pageable);
		model.addAttribute("modelList", pageBean.getContent());
		model.addAttribute("curPage", pageBean.getNumber());
		model.addAttribute("totalPages", pageBean.getTotalPages());
		return "/admin/student/list";
	}

	@RequestMapping("/uploadPage")
	public String uploadPage(HttpServletRequest request) {
		return "/admin/student/upload";
	}


    @RequestMapping("/downloadExcel")
    public void createExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "学生名单导入模板.xlsx";
		response.setHeader("Content-disposition", "attachment;filename="
				+ new String(fileName.getBytes("gb2312"), "ISO8859-1"));//设置文件头编码格式
		response.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");//设置类型
		response.setHeader("Cache-Control", "no-cache");//设置头
		response.setDateHeader("Expires", 0);//设置日期头

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("学生名单");

		Row row = sheet.createRow(0);
		//创建单元格并设置单元格内容
		row.createCell(0).setCellValue("姓名");

		workbook.write(response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
    }

    @ResponseBody
    @RequestMapping("/upload")
    public Result singleFileUpload(MultipartFile uploadFile, ClassGroup classGroup) {
        try {
            if (uploadFile.isEmpty()) {
                throw new RuntimeException("Please select a file to upload");
            }
            studentService.uploadAndSaveList(uploadFile, classGroup);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(e.toString());
        }
    }
}