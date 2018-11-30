package cn.sille.scexam.controller;

import cn.sille.scexam.model.AnswerPaperStat;
import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.ExamPaper;
import cn.sille.scexam.model.Student;
import cn.sille.scexam.repository.AnswerPaperStatRepository;
import cn.sille.scexam.service.AnswerPaperStatService;
import cn.sille.scexam.service.ExamPaperService;
import cn.sille.scexam.service.StatisticService;
import cn.sille.scexam.service.StudentService;
import cn.sille.scexam.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.sille.scexam.common.ScExamConstants.*;

/**
 * @author Sille_Cao
 * @date 2018/11/19 16:58
 */
@Controller
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private ExamPaperService examPaperService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private AnswerPaperStatRepository answerPaperStatRepository;

    @RequestMapping("/getExamPaperPage")
    public String getExamPaperPage(ModelMap model, Pageable pageable) {
        Page<ExamPaper> pageBean = examPaperService.getPage(pageable);
        model.addAttribute("modelList", pageBean.getContent());
        model.addAttribute("curPage", pageBean.getNumber());
        model.addAttribute("totalPages", pageBean.getTotalPages());
        return "/admin/statistic/paper_list";
    }

    @RequestMapping("/getClassPage")
    public String getClassPage(ModelMap model, Pageable pageable) {
        Page<ClassGroup> pageBean = studentService.getClassGroupPage(pageable);
        model.addAttribute("modelList", pageBean.getContent());
        model.addAttribute("curPage", pageBean.getNumber());
        model.addAttribute("totalPages", pageBean.getTotalPages());
        return "/admin/statistic/class_list";
    }

    @RequestMapping("/getStudentPage")
    public String getStudentPage(ModelMap model, Pageable pageable) {
        Page<Student> pageBean = studentService.getPage(pageable);
        model.addAttribute("modelList", pageBean.getContent());
        model.addAttribute("curPage", pageBean.getNumber());
        model.addAttribute("totalPages", pageBean.getTotalPages());
        return "/admin/statistic/student_list";
    }

    @RequestMapping("/studentStatistic")
    public String studentStatistic(HttpServletRequest request) {
        String id = request.getParameter("id");
        if(StringUtils.isNotBlank(id)){
            Student student = studentService.get(Long.parseLong(id));
            request.setAttribute("student", student);
        }
        return "/admin/statistic/student_statistic";
    }

    @ResponseBody
    @RequestMapping("/getStudentStatisticData")
    public Result getStudentStatisticData(HttpServletRequest request) {
        try{
            String id = request.getParameter("id");
            if(StringUtils.isNotBlank(id)){
                Student student = studentService.get(Long.parseLong(id));
                String echartType = request.getParameter("echartType");
                Map<String, Object> resultMap = new HashMap<>();
                if(StringUtils.equals(SATTISTIC_ECHART_TYPE_RANKING, echartType)){
                    resultMap = statisticService.getStudentClassStatistic(student);
                }else if(StringUtils.equals(SATTISTIC_ECHART_TYPE_ACHIEVEMENT, echartType)){
                    resultMap = statisticService.getStudentGetScoreStatistic(student);
                }else if(StringUtils.equals(SATTISTIC_ECHART_TYPE_LOSSSCORE, echartType)){
                    resultMap = statisticService.getStudentLossScoreStatistic(student);
                }else if(StringUtils.equals(SATTISTIC_ECHART_TYPE_COLLECT, echartType)){

                }
                return Result.success(resultMap);
            }
            return Result.success();
        }catch(Exception e){
            e.printStackTrace();
            return Result.failure(e.toString());
        }
    }


}
