package cn.sille.scexam.service.impl;

import cn.sille.scexam.model.*;
import cn.sille.scexam.repository.*;
import cn.sille.scexam.service.AnswerPaperStatService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnswerPaperStatServiceImpl implements AnswerPaperStatService {
	@Autowired
	private AnswerPaperStatRepository answerPaperStatRepository;
	@Autowired
	private ExamPaperRepository examPaperRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassGroupRepository classGroupRepository;
    @Autowired
    private ExamClassRepository examClassRepository;

	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public AnswerPaperStat add(AnswerPaperStat answerPaperStat){
		return answerPaperStatRepository.save(answerPaperStat);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public void delete(Long id){
		AnswerPaperStat answerPaperStat = new AnswerPaperStat();
		answerPaperStat.setId(id);
		answerPaperStatRepository.delete(answerPaperStat);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public AnswerPaperStat update(AnswerPaperStat answerPaperStat){
		return answerPaperStatRepository.saveAndFlush(answerPaperStat);
	}

	@Override
	public AnswerPaperStat get(Long id){
		return answerPaperStatRepository.getOne(id);
	}

	@Override
	public List<AnswerPaperStat> getAll(){
		return answerPaperStatRepository.findAll();
	}

	@Override
	public Page<AnswerPaperStat> getPage(Integer page, Integer rows){
		Pageable pageable = PageRequest.of(page-1, rows);
		return answerPaperStatRepository.findAll(pageable);
	}

    @Override
	public void uploadAndSaveList(MultipartFile uploadFile, Long examPaperId, Long classGroupId){
        try {
            ExamPaper examPaper = examPaperRepository.getOne(examPaperId);
            ClassGroup classGroup = classGroupRepository.getOne(classGroupId);
            List<AnswerPaperStat> answerPaperStatList =  getDataFromExcel(uploadFile, examPaper, classGroup);
            answerPaperStatRepository.deleteAllByExamPaperAndClassGroup(examPaper, classGroup);
            answerPaperStatRepository.saveAll(answerPaperStatList);
            ExamClass examClass = examClassRepository.findByExamPaperAndClassGroup(examPaper, classGroup);
            examClass.setStatus(ExamClassStatus.UPLOADED);
            examClassRepository.save(examClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取出filePath中的所有数据信息
     */
    private List<AnswerPaperStat> getDataFromExcel(MultipartFile uploadFile, ExamPaper examPaper, ClassGroup classGroup) throws IOException {
        String fileName = uploadFile.getOriginalFilename();

        //判断是否为excel类型文件
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            System.out.println("文件不是excel类型");
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
        //获得所有数据
        Row titleRow = sheet.getRow(1);

        AnswerPaperStat answerPaperStat = null;
        AnswerQuestionTypeStat answerQuestionTypeStat = null;
        Set<AnswerQuestionTypeStat> answerQuestionTypeStats = null;
        List<ExamQuestionType> examQuestionTypeList = null;
        ExamQuestionType examQuestionType = null;
        List<AnswerPaperStat> answerPaperStatList = new ArrayList<>();
        for (int i = 2; i <= totalRowNum; i++) {
            //获得第i行对象
            Row row = sheet.getRow(i);
            int totalCellNum = row.getLastCellNum();

            Student student = studentRepository.findByUserCodeAndAndClassGroup(row.getCell(0).getStringCellValue().trim(),
                    classGroup);
            answerPaperStat = new AnswerPaperStat();
            answerPaperStat.setStudent(student);
            answerPaperStat.setExamPaper(examPaper);
            answerPaperStat.setClassGroup(classGroup);

            double totalActualScore = row.getCell(totalCellNum - 1).getNumericCellValue();
            answerPaperStat.setTotalActualScore(new BigDecimal(totalActualScore));

            if(!examPaper.getExamQuestionTypes().isEmpty()){
                answerQuestionTypeStats = new HashSet<>();
                examQuestionTypeList = new ArrayList<>(examPaper.getExamQuestionTypes());
                for (int cellNum = 3; cellNum < totalCellNum - 1; cellNum++){
                    examQuestionType = getExamQuestionType(examQuestionTypeList, titleRow.getCell(cellNum).getStringCellValue().trim());
                    answerQuestionTypeStat = new AnswerQuestionTypeStat();
                    double actualScore = row.getCell(cellNum).getNumericCellValue();
                    answerQuestionTypeStat.setActualScore(new BigDecimal(actualScore));
                    answerQuestionTypeStat.setExamQuestionType(examQuestionType);
                    answerQuestionTypeStat.setAnswerPaperStat(answerPaperStat);
                    answerQuestionTypeStats.add(answerQuestionTypeStat);
                }
            }
            answerPaperStat.setAnswerQuestionTypeStats(answerQuestionTypeStats);
            answerPaperStatList.add(answerPaperStat);
        }
        return answerPaperStatList;
    }

    private ExamQuestionType getExamQuestionType(List<ExamQuestionType> examQuestionTypeList, String type){
        ExamQuestionType retExamQuestionType = null;
        for(ExamQuestionType examQuestionType : examQuestionTypeList){
            if(StringUtils.equals(type, examQuestionType.getTypeName())){
                retExamQuestionType = examQuestionType;
                break;
            }
        }
        return retExamQuestionType;
    }


}