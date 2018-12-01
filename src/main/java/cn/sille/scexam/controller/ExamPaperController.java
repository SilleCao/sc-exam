package cn.sille.scexam.controller;

import cn.sille.scexam.model.*;
import cn.sille.scexam.repository.ClassGroupRepository;
import cn.sille.scexam.repository.ExamClassRepository;
import cn.sille.scexam.service.AnswerPaperStatService;
import cn.sille.scexam.service.ExamPaperService;
import cn.sille.scexam.util.Result;

import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/examPaper")
public class ExamPaperController {
    @Autowired
    private ExamPaperService examPaperService;
    @Autowired
    private AnswerPaperStatService answerPaperStatService;
    @Autowired
    private ClassGroupRepository classGroupRepository;
    @Autowired
    private ExamClassRepository examClassRepository;

    @RequestMapping("/add")
    public String add(ModelMap model) {
        List<ClassGroup> classGroupList = classGroupRepository.findAll();
        model.addAttribute("classGroupList", classGroupList);
        return "/admin/examPaper/edit";
    }

    @ResponseBody
    @RequestMapping("/save")
    public Result save(HttpServletRequest request, ExamPaper examPaper) {
        try {
            String[] typeNames = request.getParameterValues("typeName");
            String[] targetScores = request.getParameterValues("targetScore");
            if (typeNames != null && typeNames.length > 0) {
                Set<ExamQuestionType> examQuestionTypeSet = new HashSet<>();
                ExamQuestionType examQuestionType = null;
                for (int i = 0; i < typeNames.length; i++) {
                    examQuestionType = new ExamQuestionType();
                    examQuestionType.setTypeName(typeNames[i]);
                    examQuestionType.setTargetScore(Integer.parseInt(targetScores[i]));
                    examQuestionType.setExamPaper(examPaper);
                    examQuestionTypeSet.add(examQuestionType);
                }
                examPaper.setExamQuestionTypes(examQuestionTypeSet);
            }
            String[] classIds = request.getParameterValues("classId");
            if(classIds != null && classIds.length > 0){
                ClassGroup classGroup = null;
                ExamClass examClass = null;
                Set<ExamClass> examClasses = new HashSet<>();
                for(String classId : classIds){
                    classGroup = new ClassGroup();
                    examClass = new ExamClass();
                    classGroup.setId(Long.parseLong(classId));
                    examClass.setClassGroup(classGroup);
                    examClass.setExamPaper(examPaper);
                    examClass.setStatus(ExamClassStatus.UNUPLOAD);
                    examClasses.add(examClass);
                }
                examPaper.setExamClasses(examClasses);
            }
            examPaperService.add(examPaper);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(e.toString());
        }
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        try {
            examPaperService.delete(id);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(e.toString());
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody ExamPaper examPaper) {
        try {
            return Result.success(examPaperService.update(examPaper));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(e.toString());
        }
    }

    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        try {
            ExamPaper examPaper = examPaperService.get(id);
            if (examPaper == null) {
                throw new RuntimeException();
            }
            return Result.success(examPaper);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(e.toString());
        }
    }

    @GetMapping("/getAll")
    public Result getAll() {
        try {
            return Result.success(examPaperService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(e.toString());
        }
    }

    @RequestMapping("/getPage")
    public String getPage(ModelMap model, Pageable pageable) {
        Page<ExamPaper> pageBean = examPaperService.getPage(pageable);
        model.addAttribute("modelList", pageBean.getContent());
        model.addAttribute("curPage", pageBean.getNumber());
        model.addAttribute("totalPages", pageBean.getTotalPages());
        return "/admin/examPaper/list";
    }

    @RequestMapping("/uploadPage")
    public String uploadPage(HttpServletRequest request) {
        String examPaperId = request.getParameter("examPaperId");
        ExamPaper examPaper = new ExamPaper();
        examPaper.setId(Long.parseLong(examPaperId));
        List<ExamClass> examClasses = examClassRepository.findAllByExamPaper(examPaper);
        request.setAttribute("examClasses", examClasses);
        request.setAttribute("examPaperId", examPaperId);
        return "/admin/examPaper/upload";
    }

    @RequestMapping("/downloadPage")
    public String downloadExcelPage(HttpServletRequest request) {
        String examPaperId = request.getParameter("id");
        ExamPaper examPaper = new ExamPaper();
        examPaper.setId(Long.parseLong(examPaperId));
        List<ExamClass> examClasses = examClassRepository.findAllByExamPaper(examPaper);
        request.setAttribute("examClasses", examClasses);
        request.setAttribute("examPaperId", examPaperId);
        return "/admin/examPaper/download";
    }

    @RequestMapping("/downloadExcel")
    public void createExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String classGroupId = request.getParameter("classGroupId");
        if (StringUtils.isNotBlank(id)) {
            ExamPaper examPaper = examPaperService.get(Long.parseLong(id));
            String fileName = examPaper.getTitle() + "-导入模板.xlsx";
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(fileName.getBytes("gb2312"), "ISO8859-1"));//设置文件头编码格式
            response.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");//设置类型
            response.setHeader("Cache-Control", "no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(examPaper.getTitle());
            //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            Row row1 = sheet.createRow(0);
            //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
            Cell cell = row1.createCell(0);
            //设置单元格内容
            cell.setCellValue(examPaper.getTitle() + "成绩一览表");
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            cell.setCellStyle(cellStyle);

            Row row2 = sheet.createRow(1);
            //创建单元格并设置单元格内容
            row2.createCell(0).setCellValue("学号");
            row2.createCell(1).setCellValue("姓名");
            row2.createCell(2).setCellValue("班级");
            int mergeCount = 3;
            if (examPaper.getExamQuestionTypes() != null) {
                for (ExamQuestionType examQuestionType : examPaper.getExamQuestionTypes()) {
                    row2.createCell(mergeCount).setCellValue(examQuestionType.getTypeName());
                    mergeCount++;
                }
            }
            row2.createCell(mergeCount).setCellValue("总得分");
            mergeCount++;
            //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, mergeCount - 1));

            int startRow = 2;
            ClassGroup classGroup = classGroupRepository.getOne(Long.parseLong(classGroupId));
            if (classGroup != null & classGroup.getStudents() != null){
                List<Student> studentList = new ArrayList<>(classGroup.getStudents());
                Collections.sort(studentList, new Comparator(){
                    @Override
                    public int compare(Object o1, Object o2) {
                        Student stu1=(Student)o1;
                        Student stu2=(Student)o2;
                        return stu1.getUserCode().compareTo(stu2.getUserCode());
                    }
                });
                for(Student student : studentList){
                    Row row = sheet.createRow(startRow);
                    //创建单元格并设置单元格内容
                    row.createCell(0).setCellValue(student.getUserCode());
                    row.createCell(1).setCellValue(student.getUserName());
                    row.createCell(2).setCellValue(classGroup.getClassName());
                    startRow++;
                }
            }
            sheet.autoSizeColumn((short)2);
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

    @ResponseBody
    @RequestMapping("/upload")
    public Result singleFileUpload(MultipartFile uploadFile, Long examPaperId, Long classGroupId) {
        try {
            if (uploadFile.isEmpty()) {
                throw new RuntimeException("Please select a file to upload");
            }
            answerPaperStatService.uploadAndSaveList(uploadFile, examPaperId, classGroupId);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(e.toString());
        }
    }

    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

}