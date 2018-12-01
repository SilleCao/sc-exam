package cn.sille.scexam.service.impl;

import cn.sille.scexam.model.AnswerPaperStat;
import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.Student;
import cn.sille.scexam.repository.ClassGroupRepository;
import cn.sille.scexam.repository.StudentRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cn.sille.scexam.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentRepository studentRepository;
    @Autowired
    private ClassGroupRepository classGroupRepository;

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public Student add(Student student){
		return studentRepository.save(student);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public void delete(Long id){
		Student student = new Student();
		student.setId(id);
		studentRepository.delete(student);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public Student update(Student student){
		return studentRepository.saveAndFlush(student);
	}

	@Override
    public Student get(Long id){
		return studentRepository.getOne(id);
	}

	@Override
    public List<Student> getAll(){
		return studentRepository.findAll();
	}

	@Override
    public Page<Student> getPage(Pageable pageable){
		return studentRepository.findAll(pageable);
	}

    @Override
    public Page<ClassGroup> getClassGroupPage(Pageable pageable){
        return classGroupRepository.findAll(pageable);
    }

	@Override
    @Transactional
	public void uploadAndSaveList(MultipartFile uploadFile, ClassGroup classGroup){
		try {
            classGroup = classGroupRepository.getOne(classGroup.getId());
			List<Student> studentList =  getDataFromExcel(uploadFile, classGroup);
            studentRepository.saveAll(studentList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 读取出filePath中的所有数据信息
     */
    private List<Student> getDataFromExcel(MultipartFile uploadFile, ClassGroup classGroup) throws IOException {
        String fileName = uploadFile.getOriginalFilename();

        //判断是否为excel类型文件
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            throw new RuntimeException("文件不是excel类型");
        }
        Workbook wookbook = null;
        try {
            //2003版本的excel，用.xls结尾
            wookbook = new HSSFWorkbook(uploadFile.getInputStream());//得到工作簿
        } catch (Exception ex) {
            //2007版本的excel，用.xlsx结尾
            wookbook = new XSSFWorkbook(uploadFile.getInputStream());//得到工作簿
        }
        //得到一个工作表
        Sheet sheet = wookbook.getSheetAt(0);
        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();

        String curMaxUserCode = null;

        Student student = null;
        //获得所有数据
        List<Student> studentList = new ArrayList<>();
        for (int i = 1; i <= totalRowNum; i++) {
            //获得第i行对象
            Row row = sheet.getRow(i);
            //获得获得第i行第0列的 String类型对象
            Cell cell = row.getCell(0);
            String userName = cell.getStringCellValue().trim();
            if(StringUtils.isNotBlank(userName)){
                student = new Student();
                student.setClassGroup(classGroup);
                student.setUserCode(getUserCode(classGroup.getClassCode(), null, i));
                student.setUserName(cell.getStringCellValue().trim());
                studentList.add(student);
            }
        }
        return studentList;
    }

    private String getUserCode(String classCode, String curMaxUserCode, int n){
        String userCode = "";
        if(StringUtils.isBlank(curMaxUserCode)){
            userCode = classCode + String.format("%0" + 3 + "d", n);
        }else{
            userCode = String.valueOf(Long.parseLong(curMaxUserCode) + 1);
        }
        return userCode;
    }

    @Override
    public Map<String, Object> getStudentStatisticData(Long id, String echartType){
        Student student = new Student();
        student.setId(id);


        return null;
    }
}