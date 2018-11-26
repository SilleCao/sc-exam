package cn.sille.scexam.service;

import java.util.Map;

public interface StatisticService {

    Map<String, Object> getStudentStatisticData(Long id, String echartType);
}