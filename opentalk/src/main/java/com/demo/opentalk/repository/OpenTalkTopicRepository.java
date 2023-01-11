package com.demo.opentalk.repository;

import com.demo.opentalk.entity.OpenTalkTopic;
import com.demo.opentalk.model.projection.OpenTalkTopicProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface OpenTalkTopicRepository extends JpaRepository<OpenTalkTopic, Integer> {

    @Modifying
    @Query("DELETE FROM OpenTalkTopic " +
            "WHERE employee.employeeNo = :employeeNo")
    public void deleteOpenTalkTopicsByEmployeeNo(@Param("employeeNo") Integer employeeNo);
    @Query("SELECT opt FROM OpenTalkTopic opt " +
            "WHERE (:active IS NULL OR opt.employee.active = :active) " +
                "AND (:branchNo IS NULL OR opt.companyBranch.branchNo = :branchNo) " +
                "AND (:firstName IS NULL OR opt.employee.firstName = :firstName) " +
                "AND ((:startDate IS NULL AND :endDate IS NULL) OR (opt.date BETWEEN :startDate AND :endDate)) " +
                "AND (opt.date < CURRENT_DATE)")
    public Page<OpenTalkTopic> getOpenTalkTopicByCriteria(Pageable pageable,
                                                          @Param("active") Boolean active,
                                                          @Param("branchNo") Integer branchNo,
                                                          @Param("firstName") String firstName,
                                                          @Param("startDate") Date startDate,
                                                          @Param("endDate") Date endDate);

    @Query("SELECT opt FROM OpenTalkTopic opt " +
            "WHERE (:branchNo IS NULL OR opt.companyBranch.branchNo = :branchNo) " +
            "AND (:firstName IS NULL OR opt.employee.firstName = :firstName) " +
            "AND ((:startDate IS NULL OR :endDate IS NULL) OR (opt.date BETWEEN :startDate AND :endDate)) " +
            "AND (opt.date > CURRENT_DATE )")
    public Page<OpenTalkTopic> getOpenTalkTopicUpComing(Pageable pageable,
                                                        @Param("branchNo") Integer branchNo,
                                                        @Param("firstName") String firstName,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate);

    @Query("SELECT opt FROM OpenTalkTopic  opt " +
            "WHERE opt.employee.employeeNo = :employeeNo " +
            "AND ((:startDate IS NULL OR :endDate IS NULL) " +
            "OR (opt.date BETWEEN :startDate AND :endDate))")
    public Page<OpenTalkTopic> getOpenTalkTopicForEmployeeNo(@Param("employeeNo") Integer employeeNo,
                                                             Pageable pageable,
                                                             @Param("startDate") Date startDate,
                                                             @Param("endDate") Date endDate);

    @Query("SELECT opt FROM OpenTalkTopic opt " +
            "WHERE opt.employee.employeeNo = :employeeNo " +
            "ORDER BY opt.date desc")
    public Page<OpenTalkTopic> getOpenTalkTopicIsRegisterForEmployee(Pageable pageable,
                                                                     @Param("employeeNo") Integer employeeNo);

    @Query(nativeQuery = true,
            value = "SELECT opt.topic_no as topicNo, " +
                    "opt.topic_name as topicName, " +
                    "opt.branch_no as branchNo," +
                    "opt.link_meeting as linkMeeting, " +
                    "cb.branch_name as branchName, " +
                    "opt.date as date, " +
                    "concat(em.last_name, ' ', em.first_name) as fullName " +
                    "FROM OPEN_TALK_TOPIC opt " +
                    "JOIN COMPANY_BRANCH cb ON opt.branch_no = cb.branch_no " +
                    "JOIN EMPLOYEE em ON opt.employee_no = em.employee_no " +
                    "WHERE opt.date > CURRENT_DATE " +
                    "ORDER BY opt.date " +
                    "LIMIT 1")
    public OpenTalkTopicProjection getOpenTalkTopicIsComingSoon();

}
