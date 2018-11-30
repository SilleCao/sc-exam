package cn.sille.scexam.service;

import cn.sille.scexam.model.Student;

import java.util.Map;

public interface StatisticService {

    Map<String, Object> getStudentClassStatistic(Student student);

    Map<String, Object> getStudentGetScoreStatistic(Student student);

    Map<String, Object> getStudentLossScoreStatistic(Student student);

    Map<String, Object> getStudentStatisticData(Long id, String echartType);
}