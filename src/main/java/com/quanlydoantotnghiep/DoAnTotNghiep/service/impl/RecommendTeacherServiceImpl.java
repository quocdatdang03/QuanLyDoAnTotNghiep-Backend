package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.instructor.RecommendTeacherRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.*;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.SemesterRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.StudentRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.StudentTeacherProposalRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.TeacherRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.RecommendTeacherService;
import com.quanlydoantotnghiep.DoAnTotNghiep.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendTeacherServiceImpl implements RecommendTeacherService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final SemesterRepository semesterRepository;
    private final StudentTeacherProposalRepository studentTeacherProposalRepository;

    @Override
    public ObjectResponse getAllTeachersByFacultyOfStudent(AccountDto accountDto, String keyword, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Pageable pageable = AppUtils.createPageable(pageNumber, pageSize, sortBy, sortDir);

        Student student = studentRepository.findByAccount_AccountId(accountDto.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given account id: "+accountDto.getAccountId()));

        // get faculty of student
        Faculty faculty = student.getClazz().getFaculty();

        Page<Teacher> teacherPage = teacherRepository.findAllTeachersByFaculty(keyword, faculty.getFacultyId(), pageable);

        // convert to TeacherAccountResponse
        List<TeacherAccountResponse> teacherAccountResponses = teacherPage.getContent()
                .stream().map((item) -> convertToTeacherAccountResponse(item))
                    .collect(Collectors.toList());

        return AppUtils.createObjectResponse(teacherPage, teacherAccountResponses);
    }

    @Override
    public List<TeacherAccountResponse> getAllRecommendedTeacherOfStudent(String studentCode) {

        Student student = studentRepository.findByAccount_Code(studentCode)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given code: "+studentCode));

        // get current semester
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();
        if (currentSemester == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Current semester is null");
        }

        // get all recommended teacher of student in current semester
        List<StudentTeacherProposal> studentTeacherProposals = studentTeacherProposalRepository.findByStudentStudentIdAndSemesterSemesterId(student.getStudentId(), currentSemester.getSemesterId());

        return studentTeacherProposals.stream()
                .map((item) -> convertToTeacherAccountResponse(item.getTeacher()))
                .collect(Collectors.toList());
    }

    @Override
    public TeacherAccountResponse recommendTeacher(RecommendTeacherRequest recommendTeacherRequest) {

        // get current semester
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();
        if (currentSemester == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Current semester is null");
        }

        // get student
        Student student = studentRepository.findById(recommendTeacherRequest.getStudentId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given id: "+recommendTeacherRequest.getStudentId()));

        // get teacher
        Teacher teacher = teacherRepository.findById(recommendTeacherRequest.getTeacherId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given id: "+recommendTeacherRequest.getTeacherId()));

        // check if student is already recommended this teacher in current semester -> throw error
        if(studentTeacherProposalRepository
                .existsByStudentStudentIdAndTeacherTeacherIdAndSemesterSemesterId(
                        student.getStudentId(),
                        teacher.getTeacherId(),
                        currentSemester.getSemesterId())
        ) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "student is already recommended this teacher in current semester");
        }

        StudentTeacherProposalId studentTeacherProposalId = StudentTeacherProposalId.builder()
                .studentId(student.getStudentId())
                .teacherId(teacher.getTeacherId())
                .semesterId(currentSemester.getSemesterId())
                .build();

        // save studentTeacherProposal
        StudentTeacherProposal studentTeacherProposal = StudentTeacherProposal.builder()
                .id(studentTeacherProposalId)
                .student(student)
                .teacher(teacher)
                .semester(currentSemester)
                .flagDelete(false)
                .build();

        StudentTeacherProposal savedStudentTeacherProposal = studentTeacherProposalRepository.save(studentTeacherProposal);

        return convertToTeacherAccountResponse(savedStudentTeacherProposal.getTeacher());
    }

    @Override
    public TeacherAccountResponse removeRecommendTeacher(RecommendTeacherRequest recommendTeacherRequest) {

        // get student
        Student student = studentRepository.findById(recommendTeacherRequest.getStudentId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student is not exists with given id: "+recommendTeacherRequest.getStudentId()));

        // get teacher
        Teacher teacher = teacherRepository.findById(recommendTeacherRequest.getTeacherId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Teacher is not exists with given id: "+recommendTeacherRequest.getTeacherId()));

        // get current semester
        Semester currentSemester = semesterRepository.findByIsCurrentIsTrue();
        if (currentSemester == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Current semester is null");
        }

        StudentTeacherProposal studentTeacherProposal = studentTeacherProposalRepository
                .findByStudentStudentIdAndTeacherTeacherIdAndSemesterSemesterId(student.getStudentId(), teacher.getTeacherId(), currentSemester.getSemesterId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Student does not recommend this teacher in current semester"));

        // delete studentTeacherProposal
        studentTeacherProposalRepository.delete(studentTeacherProposal);

        return convertToTeacherAccountResponse(teacher);
    }

    private TeacherAccountResponse convertToTeacherAccountResponse(Teacher item) {

        TeacherAccountResponse teacherAccountResponse = modelMapper.map(item.getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherId(item.getTeacherId());
        teacherAccountResponse.setTeacherCode(item.getAccount().getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(item.getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(item.getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(item.isLeader());

        return teacherAccountResponse;
    }
}
