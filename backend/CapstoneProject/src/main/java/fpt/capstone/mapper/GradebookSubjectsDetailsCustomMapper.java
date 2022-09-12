package fpt.capstone.mapper;

import fpt.capstone.entities.GradebookSubjectsDetails;
import fpt.capstone.vo.GradebookSubjectsDetailsVO;
import org.apache.commons.lang3.StringUtils;

public class GradebookSubjectsDetailsCustomMapper {
    private GradebookSubjectsDetailsCustomMapper() {
    }

    public static GradebookSubjectsDetails toEntity(GradebookSubjectsDetailsVO vo) {
        if (vo == null) {
            return null;
        }

        GradebookSubjectsDetails gradebookSubjectsDetails = new GradebookSubjectsDetails();

        gradebookSubjectsDetails.setId(vo.getId());
        gradebookSubjectsDetails.setCode(vo.getCode());
        gradebookSubjectsDetails.setStudentCode(vo.getStudentCode());
        gradebookSubjectsDetails.setParentCode(vo.getParentCode());
        if (StringUtils.isNotEmpty(vo.getAvgScore())) {
            gradebookSubjectsDetails.setAvgScore(Double.parseDouble(vo.getAvgScore()));
        }
        gradebookSubjectsDetails.setSubjectCode(vo.getSubjectCode());
        gradebookSubjectsDetails.setEvaluate(vo.getEvaluate());
        gradebookSubjectsDetails.setEvaluateStatus(vo.getStatus());
        gradebookSubjectsDetails.setUpdater(vo.getUpdater());

        return gradebookSubjectsDetails;
    }

    public static GradebookSubjectsDetailsVO toDto(GradebookSubjectsDetails entity) {
        if (entity == null) {
            return null;
        }

        GradebookSubjectsDetailsVO gradebookSubjectsDetailsVO = new GradebookSubjectsDetailsVO();

        gradebookSubjectsDetailsVO.setId(entity.getId());
        gradebookSubjectsDetailsVO.setCode(entity.getCode());
        gradebookSubjectsDetailsVO.setStudentCode(entity.getStudentCode());
        gradebookSubjectsDetailsVO.setParentCode(entity.getParentCode());
        if (entity.getAvgScore() != null) {
            gradebookSubjectsDetailsVO.setAvgScore(String.valueOf(entity.getAvgScore()));
        }
        gradebookSubjectsDetailsVO.setSubjectCode(entity.getSubjectCode());
        gradebookSubjectsDetailsVO.setEvaluate(entity.getEvaluate());
        gradebookSubjectsDetailsVO.setUpdater(entity.getUpdater());
        gradebookSubjectsDetailsVO.setStatus(entity.getEvaluateStatus());

        return gradebookSubjectsDetailsVO;
    }
}
