package cn.sille.scexam.repository;

import cn.sille.scexam.model.AnswerPaperStat;
import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.ExamPaper;
import cn.sille.scexam.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerPaperStatRepository extends JpaRepository<AnswerPaperStat,Long> {
    List<AnswerPaperStat> findAllByStudent(Student student);
    void deleteAllByExamPaperAndClassGroup(ExamPaper examPaper, ClassGroup classGroup);
    List<AnswerPaperStat> findAllByExamPaperAndClassGroup(ExamPaper examPaper, ClassGroup classGroup);
    List<AnswerPaperStat> findAllByClassGroup(ClassGroup classGroup);
    List<AnswerPaperStat> findAllByExamPaper(ExamPaper examPaper);
    AnswerPaperStat findByStudentAndExamPaper(Student student, ExamPaper examPaper);
}