package cn.sille.scexam.service.impl;

import cn.sille.scexam.model.AnswerPaperStat;
import cn.sille.scexam.model.AnswerQuestionTypeStat;
import cn.sille.scexam.model.ExamClass;
import cn.sille.scexam.model.Student;
import cn.sille.scexam.repository.AnswerPaperStatRepository;
import cn.sille.scexam.repository.ClassGroupRepository;
import cn.sille.scexam.repository.ExamClassRepository;
import cn.sille.scexam.repository.StudentRepository;
import cn.sille.scexam.service.StatisticService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.scripts.JS;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static cn.sille.scexam.common.ScExamConstants.SATTISTIC_ECHART_GET_SCORE;
import static cn.sille.scexam.common.ScExamConstants.SATTISTIC_ECHART_LOSS_SCORE;

@Service
public class StatisticServiceImpl implements StatisticService {
	@Autowired
	private StudentRepository studentRepository;
    @Autowired
    private ClassGroupRepository classGroupRepository;
    @Autowired
    private AnswerPaperStatRepository answerPaperStatRepository;
    @Autowired
    private ExamClassRepository examClassRepository;

    @Override
    public Map<String, Object> getStudentClassStatistic(Student student){
        Map<String, Object> resultMap = new HashMap<>();
        List<ExamClass> examClassList = examClassRepository.findAllByClassGroup(student.getClassGroup());
        String[] title = new String[examClassList.size()];
        Integer[] classRanking = new Integer[examClassList.size()];
        Integer[] gradeRanking = new Integer[examClassList.size()];
        for(int i = 0; i < examClassList.size(); i++){
            ExamClass examClass = examClassList.get(i);
            title[i] = examClass.getExamPaper().getTitle();
            //班级排名
            List<AnswerPaperStat> answerPaperStatListInClass = answerPaperStatRepository
                    .findAllByExamPaperAndClassGroup(examClass.getExamPaper(), examClass.getClassGroup());
            sortByTotalActualScore(answerPaperStatListInClass);
            classRanking[i] = getExamRanking(student.getUserCode(), answerPaperStatListInClass);
            //年级排名
            List<AnswerPaperStat> answerPaperStatListInGrade = answerPaperStatRepository
                    .findAllByExamPaper(examClass.getExamPaper());
            sortByTotalActualScore(answerPaperStatListInGrade);
            gradeRanking[i] = getExamRanking(student.getUserCode(), answerPaperStatListInGrade);
        }
        resultMap.put("title", title);
        resultMap.put("classRanking", classRanking);
        resultMap.put("gradeRanking", gradeRanking);
        return resultMap;
    }

    @Override
    public Map<String, Object> getStudentGetScoreStatistic(Student student){
        Map<String, Object> resultMap = new HashMap<>();
        List<ExamClass> examClassList = examClassRepository.findAllByClassGroup(student.getClassGroup());
        String[] title = new String[examClassList.size()];
        double[] selfScore = new double[examClassList.size()];
        double[] classAverageScore = new double[examClassList.size()];
        double[] gradeAverageScore = new double[examClassList.size()];
        for(int i = 0; i < examClassList.size(); i++){
            ExamClass examClass = examClassList.get(i);
            title[i] = examClass.getExamPaper().getTitle();

            List<AnswerPaperStat> answerPaperStatListInClass = answerPaperStatRepository
                    .findAllByExamPaperAndClassGroup(examClass.getExamPaper(), examClass.getClassGroup());
            //获取自己分数
            selfScore[i] = (getSelfExamScore(student.getUserCode(), answerPaperStatListInClass)
                    /examClass.getExamPaper().getTotalScore().doubleValue())*100;
            //班级平均分
            classAverageScore[i] = (getExamAverageScore(answerPaperStatListInClass)/examClass.getExamPaper().getTotalScore().doubleValue())*100;
            //年级平均分
            List<AnswerPaperStat> answerPaperStatListInGrade = answerPaperStatRepository
                    .findAllByExamPaper(examClass.getExamPaper());
            gradeAverageScore[i] = (getExamAverageScore(answerPaperStatListInGrade)/examClass.getExamPaper().getTotalScore().doubleValue())*100;
        }
        resultMap.put("title", title);
        resultMap.put("selfScore", selfScore);
        resultMap.put("classAverageScore", classAverageScore);
        resultMap.put("gradeAverageScore", gradeAverageScore);
        return resultMap;
    }

    @Override
    public Map<String, Object> getStudentLossScoreStatistic(Student student){
        Map<String, Object> resultMap = new HashMap<>();
        List<ExamClass> examClassList = examClassRepository.findAllByClassGroup(student.getClassGroup());
        double[] classAverageScore = new double[examClassList.size()];
        double[] gradeAverageScore = new double[examClassList.size()];
        for(int i = 0; i < examClassList.size(); i++){
            ExamClass examClass = examClassList.get(i);
            AnswerPaperStat answerPaperStat = answerPaperStatRepository.findByStudentAndExamPaper(student, examClass.getExamPaper());
            if(answerPaperStat != null){
                JSONArray examResult = new JSONArray();
                JSONObject scoreDetail = null;
                String[] bottomType = null;
                if(!answerPaperStat.getAnswerQuestionTypeStats().isEmpty()){
                    List<AnswerQuestionTypeStat> answerQuestionTypeStatList = new ArrayList<>(answerPaperStat.getAnswerQuestionTypeStats());
                    bottomType = new String[answerQuestionTypeStatList.size() + 1];
                    //获取自己分数详情
                    for(int qt = 0;qt < answerQuestionTypeStatList.size(); qt++){
                        scoreDetail = new JSONObject();
                        AnswerQuestionTypeStat answerQuestionTypeStat = answerQuestionTypeStatList.get(qt);
                        scoreDetail.put("name", answerQuestionTypeStat.getExamQuestionType().getTypeName());
                        scoreDetail.put("value", answerQuestionTypeStat.getActualScore().doubleValue());
                        examResult.add(scoreDetail);
                        bottomType[qt] = answerQuestionTypeStat.getExamQuestionType().getTypeName();
                    }
                    bottomType[answerQuestionTypeStatList.size()] = SATTISTIC_ECHART_LOSS_SCORE;
                }else{
                    bottomType = new String[2];
                    scoreDetail = new JSONObject();
                    scoreDetail.put("name", SATTISTIC_ECHART_GET_SCORE);
                    scoreDetail.put("value", answerPaperStat.getTotalActualScore().doubleValue());
                    examResult.add(scoreDetail);
                    bottomType[0] = SATTISTIC_ECHART_GET_SCORE;
                    bottomType[1] = SATTISTIC_ECHART_LOSS_SCORE;
                }
                scoreDetail = new JSONObject();
                scoreDetail.put("name", SATTISTIC_ECHART_LOSS_SCORE);
                scoreDetail.put("value", examClass.getExamPaper().getTotalScore().subtract(answerPaperStat.getTotalActualScore()).doubleValue());
                examResult.add(scoreDetail);

                Map<String, Object> preExamMap = new HashMap<>();
                preExamMap.put("title", examClass.getExamPaper().getTitle()+"为(" + examClass.getExamPaper().getTotalScore() + "分制)");
                preExamMap.put("subTitle", examClass.getExamPaper().getTitle());
                preExamMap.put("detail", examResult);
                preExamMap.put("bottomType", bottomType);
                resultMap.put(String.valueOf(i), preExamMap);
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getStudentStatisticData(Long id, String echartType){
        Student student = new Student();
        student.setId(id);

        

        return null;
    }

    /**
     * 按成绩由高到低排序
     * @param answerPaperStatList
     */
    private void sortByTotalActualScore(List<AnswerPaperStat> answerPaperStatList){
        Collections.sort(answerPaperStatList, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                AnswerPaperStat answerPaperStat1=(AnswerPaperStat)o1;
                AnswerPaperStat answerPaperStat2=(AnswerPaperStat)o2;
                return answerPaperStat2.getTotalActualScore().compareTo(answerPaperStat1.getTotalActualScore());
            }
        });
        int ranking = 1;
        for(AnswerPaperStat answerPaperStat : answerPaperStatList){
            answerPaperStat.setRanking(ranking);
            ranking++;
        }
    }

    /**
     * 根据学号获取当前成绩的排名
     * @param userCode
     * @param answerPaperStatList
     * @return
     */
    private int getExamRanking(String userCode, List<AnswerPaperStat> answerPaperStatList){
        int ranking = 0;
        for(AnswerPaperStat answerPaperStat : answerPaperStatList){
            if(StringUtils.equals(userCode, answerPaperStat.getStudent().getUserCode())){
                ranking = answerPaperStat.getRanking();
                break;
            }
        }
        return ranking;
    }

    /**
     * 计算平均分
     * @param answerPaperStatList
     */
    private double getExamAverageScore(List<AnswerPaperStat> answerPaperStatList){
        double averageScore = 0;
        if(!answerPaperStatList.isEmpty()){
            BigDecimal sumScore = new BigDecimal(0);
            for(int i = 0; i < answerPaperStatList.size(); i++){
                sumScore = sumScore.add(answerPaperStatList.get(i).getTotalActualScore());
            }
            averageScore = sumScore.divide(new BigDecimal(answerPaperStatList.size())).doubleValue();
        }
        return averageScore;
    }

    /**
     * 根据学号获取分数
     * @param answerPaperStatList
     */
    private double getSelfExamScore(String userCode, List<AnswerPaperStat> answerPaperStatList){
        double score = 0;
        for(AnswerPaperStat answerPaperStat : answerPaperStatList){
            if(StringUtils.equals(userCode, answerPaperStat.getStudent().getUserCode())){
                score = answerPaperStat.getTotalActualScore().doubleValue();
                break;
            }
        }
        return score;
    }
}