package cn.sille.scexam.service;

import cn.sille.scexam.model.AnswerPaperStat;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface AnswerPaperStatService {

	AnswerPaperStat add(AnswerPaperStat answerPaperStat);


	void delete(Long id);


	AnswerPaperStat update(AnswerPaperStat answerPaperStat);


	AnswerPaperStat get(Long id);


	List<AnswerPaperStat> getAll();


	Page<AnswerPaperStat> getPage(Integer page, Integer rows);

	void uploadAndSaveList(MultipartFile uploadFile, Long examPaperId, Long classGroupId);

}