package fpt.capstone.repository;

import fpt.capstone.form_data.*;
import fpt.capstone.utility.DataUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.crypto.Data;
import java.math.BigInteger;
import java.text.Collator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TransferStudentCustomRepositoryImpl implements TransferStudentCustomRepository {
    private final EntityManager entityManager;
    private static final String SCHOOL_YEAR_PARAM = "schoolYear";
    private static final String GRADE_LEVEL_PARAM = "gradeLevel";
    private static final String CLASS_CODE_PARAM = "classCode";
    private static final String TRANSFER_STATUS_PARAM = "transferStatus";
    private static final String STUDENT_SEARCH_PARAM = "studentSearch";

    public TransferStudentCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<TransferStudentsForm> searchTransferStudents(TransferStudentsSearchForm transferStudentsSearchForm, Pageable pageable) {
        System.out.println(transferStudentsSearchForm.toString());
        String sql = buildSearchTransferStudentsQuery();

        // Trim khoảng trống
        if (transferStudentsSearchForm.getStudentSearch() != null && transferStudentsSearchForm.getStudentSearch().length() > 0) {
            transferStudentsSearchForm.setStudentSearch(transferStudentsSearchForm.getStudentSearch().trim().toLowerCase());
        }
        Query query = entityManager.createNativeQuery(sql)
                .setParameter(SCHOOL_YEAR_PARAM, transferStudentsSearchForm.getSchoolYear())
                .setParameter(GRADE_LEVEL_PARAM, transferStudentsSearchForm.getGradeLevel())
                .setParameter(CLASS_CODE_PARAM, transferStudentsSearchForm.getClassCode())
                .setParameter(TRANSFER_STATUS_PARAM, transferStudentsSearchForm.getTransferStatus())
                .setParameter(STUDENT_SEARCH_PARAM, transferStudentsSearchForm.getStudentSearch())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        Query countQuery = null;
        countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM (" + sql + ") a")
                .setParameter(SCHOOL_YEAR_PARAM, transferStudentsSearchForm.getSchoolYear())
                .setParameter(GRADE_LEVEL_PARAM, transferStudentsSearchForm.getGradeLevel())
                .setParameter(CLASS_CODE_PARAM, transferStudentsSearchForm.getClassCode())
                .setParameter(TRANSFER_STATUS_PARAM, transferStudentsSearchForm.getTransferStatus())
                .setParameter(STUDENT_SEARCH_PARAM, transferStudentsSearchForm.getStudentSearch());

        long countResult = ((BigInteger) countQuery.getSingleResult()).longValue();
        List<Object[]> lst = null;
        try {
            lst = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TransferStudentsForm> result = mapResultList(lst);
        return new PageImpl<>(result, pageable, countResult);
    }

    private String buildSearchTransferStudentsQuery() {
        return "SELECT " +
                " sh.student_code, " + // 0
                " s.full_name, " + // 1
                " sh.current_class_code, " + // 2
                " cr.name AS className, " + // 3
                " cr.grade_level, " + // 4
                " gl.code AS gradeCode, " + // 5
                " sh.year AS currentSchoolYear, " + // 6
                " ap.name AS competitionTitle, " + // 7
                " ts.id tsId, " + // 8
                " ts.code AS tsCode, " + // 9
                " ts.class_code AS tsClassCode, " + // 10
                " ts.grade_code AS tsGradeCode, " + // 11
                " ts.school_year AS tsSchoolYear, " + // 12
                " tsd.id AS tsdId, " + // 13
                " tsd.code AS tsdCode, " + // 14
                " tsd.current_school_year AS tsdCurrentSchoolYear, " + // 15
                " tsd.current_class_code AS tsdCurrentClassCode, " + // 16
                " tsd.student_code AS tsdStudentCode, " + // 17
                " tsd.academic_ability AS tsdAcademicAbility, " + // 18
                " tsd.conduct AS tsdConduct, " + // 19
                " tsd.status AS tsdStatus, " + // 20
                " tsd.new_class_code AS tsdNewClassCode, " + // 21
                " tsd.new_shool_year AS tsdNewSchoolYear, " + // 22
                " tsd.parent_code AS tsdParentCode, " + // 23
                " ass.id AS assId, " + // 24
                " ass.class_code AS assClassCode, " + // 25
                " ass.semester AS assSemester, " + // 26
                " ass.school_year AS assSchoolYear, " + // 27
                " ass.create_date AS assCreateDate, " + // 28
                " ass.creator AS assCreator, " + // 29
                " ass.update_date AS assUpdateDate, " + // 30
                " ass.updater AS assUpdater, " + // 31
                " ass.code AS assCode, " + // 32
                " assd.id AS assdId, " + // 33
                " assd.student_code AS assdStudentCode, " + // 34
                " assd.number_off AS assdNumberOff, " + // 35
                " assd.number_off_allowed AS assdNumberOffAllowed, " + // 36
                " assd.academic_ability AS assdAcademicAbility, " + // 37
                " assd.conduct AS assdConduct, " + // 38
                " assd.competition_title AS assdCompetitionTitle, " + // 39
                " assd.parent_code AS assdParentCode, " + // 40
                " ncr.name AS ncrNewClassName, " + // 41
                " ncr.grade_level AS ncrNewGrade " + // 42
                "FROM " +
                " student_history sh " +
                " JOIN students s ON sh.student_code = s.code " +
                " JOIN class_room cr ON sh.current_class_code = cr.code " +
                " JOIN grade_level gl ON gl.id = cr.grade_level " +
                " LEFT JOIN assess_student_conduct ass ON sh.year = ass.school_year " +
                " AND sh.current_class_code = ass.class_code " +
                " AND ass.semester = 0 " +
                " LEFT JOIN assess_student_conduct_details assd ON ass.code = assd.parent_code " +
                " AND sh.student_code = assd.student_code " +
                " LEFT JOIN ap_param ap ON assd.competition_title = ap.code " +
                " AND ap.type = 'COMPETITION' " +
                " LEFT JOIN transfer_students ts ON sh.year = ts.school_year " +
                " AND sh.current_class_code = ts.class_code " +
                " LEFT JOIN transfer_students_details tsd ON ts.code = tsd.parent_code " +
                " AND sh.student_code = tsd.student_code " +
                " LEFT JOIN class_room ncr ON tsd.new_class_code = ncr.code " +
                " AND tsd.new_shool_year = ncr.years " +
                "WHERE " +
                " s.status NOT IN (2, 3) " +
                " AND (:schoolYear IS NULL OR sh.year = :schoolYear)  " +
                " AND (:gradeLevel IS NULL OR cr.grade_level = :gradeLevel)  " +
                " AND (:classCode IS NULL OR sh.current_class_code = :classCode) " +
                "AND (:transferStatus IS NULL OR CASE WHEN :transferStatus = 2 THEN tsd.id IS NULL When :transferStatus =3 then (tsd.id IS NULL or tsd.status=0 or tsd.status=1)  else tsd.status = :transferStatus  END)" +
//                " AND (:transferStatus IS NULL " +
//                " OR CASE WHEN :transferStatus = 2 THEN tsd.id IS NULL ELSE tsd.status = :transferStatus END) " +
//                "AND (:transferStatus IS NULL " +
//                "OR tsd.status = :transferStatus " +
                " AND (:studentSearch IS NULL " +
                " OR s.full_name LIKE concat('%', :studentSearch, '%') " +
                " OR LOWER(s.code) LIKE concat('%', :studentSearch, '%')) " +
                " ORDER BY s.full_name, s.code";
    }

    private List<TransferStudentsForm> mapResultList(List<Object[]> resultList) {

        List<TransferStudentsForm> transferStudentsFormList = resultList.stream()
                .map(obj -> {
                            TransferStudentsForm transferStudentsForm = new TransferStudentsForm();
                            transferStudentsForm.setId((Integer) obj[8]);
                            transferStudentsForm.setCode(DataUtil.safeToString(obj[9]));
                            transferStudentsForm.setClassCode(DataUtil.safeToString(obj[2]));
                            transferStudentsForm.setClassName(DataUtil.safeToString(obj[3]));
                            transferStudentsForm.setGradeLevel((Integer) obj[4]);
                            transferStudentsForm.setGradeCode(DataUtil.safeToString(obj[5]));
                            transferStudentsForm.setSchoolYear(DataUtil.safeToString(obj[6]));
                            transferStudentsForm.setCompetitionTitle(DataUtil.safeToString(obj[7]));

                            TransferStudentsDetailsForm transferStudentsDetailsForm = new TransferStudentsDetailsForm();
                            transferStudentsDetailsForm.setId((Integer) obj[13]);
                            transferStudentsDetailsForm.setCode(DataUtil.safeToString(obj[14]));
                            transferStudentsDetailsForm.setCurrentSchoolYear(DataUtil.safeToString(obj[15]));
                            transferStudentsDetailsForm.setCurrentClassCode(DataUtil.safeToString(obj[16]));
                            transferStudentsDetailsForm.setStudentCode(DataUtil.safeToString(obj[0]));
                            transferStudentsDetailsForm.setStudentName(DataUtil.safeToString(obj[1]));
                            transferStudentsDetailsForm.setAcademicAbility(DataUtil.safeToString(obj[18]));
                            transferStudentsDetailsForm.setConduct(DataUtil.safeToString(obj[19]));
                            transferStudentsDetailsForm.setStatus((Integer) obj[20]);
                            transferStudentsDetailsForm.setNewClassCode(DataUtil.safeToString(obj[21]));
                            transferStudentsDetailsForm.setNewClassName(DataUtil.safeToString(obj[41]));
                            transferStudentsDetailsForm.setNewGradeLevel((Integer) obj[42]);
                            transferStudentsDetailsForm.setNewSchoolYear(DataUtil.safeToString(obj[22]));
                            transferStudentsDetailsForm.setParentCode(DataUtil.safeToString(obj[23]));

                            AssessStudentConductForm assessStudentConductForm = new AssessStudentConductForm();
                            assessStudentConductForm.setId((Integer) obj[24]);
                            assessStudentConductForm.setClass_code(DataUtil.safeToString(obj[25]));
                            assessStudentConductForm.setSemester(DataUtil.safeToString(obj[26]));
                            assessStudentConductForm.setSchool_year(DataUtil.safeToString(obj[27]));
                            assessStudentConductForm.setCreate_date(DataUtil.safeToString(obj[28]));
                            assessStudentConductForm.setCreator(DataUtil.safeToString(obj[29]));
                            assessStudentConductForm.setUpdate_date(DataUtil.safeToString(obj[30]));
                            assessStudentConductForm.setUpdater(DataUtil.safeToString(obj[31]));
                            assessStudentConductForm.setCode(DataUtil.safeToString(obj[32]));

                            AssessStudentConductDetailForm assessStudentConductDetailForm = new AssessStudentConductDetailForm();
                            assessStudentConductDetailForm.setId((Integer) obj[33]);
                            assessStudentConductDetailForm.setStudent_code(DataUtil.safeToString(obj[34]));
                            assessStudentConductDetailForm.setNumber_off((Integer) obj[35]);
                            assessStudentConductDetailForm.setNumber_off_allowed((Integer) obj[36]);
                            assessStudentConductDetailForm.setAcademic_ability(DataUtil.safeToString(obj[37]));
                            assessStudentConductDetailForm.setConduct(DataUtil.safeToString(obj[38]));
                            assessStudentConductDetailForm.setCompetition_title(DataUtil.safeToString(obj[39]));
                            assessStudentConductDetailForm.setParent_code(DataUtil.safeToString(obj[40]));

                            transferStudentsForm.setDetails(transferStudentsDetailsForm);
                            transferStudentsForm.setAssess(assessStudentConductForm);
                            transferStudentsForm.setAssessDetails(assessStudentConductDetailForm);

                            return transferStudentsForm;
                        }

                ).collect(Collectors.toList());
        transferStudentsFormList.sort((o1, o2) -> Collator.getInstance().compare(o1.getDetails().getStudentName().substring(o1.getDetails().getStudentName().lastIndexOf(' ') + 1), o2.getDetails().getStudentName().substring(o2.getDetails().getStudentName().lastIndexOf(' ') + 1)));
        return transferStudentsFormList;

    }

}
