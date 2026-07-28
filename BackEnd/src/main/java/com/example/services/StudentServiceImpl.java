package com.example.services;

import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.StudentRequest;
import com.example.dto.StudentResponse;
import com.example.entities.Batch;
import com.example.entities.Course;
import com.example.entities.Enquiry;
import com.example.entities.Student;
import com.example.exceptions.DuplicateAdmissionException;
import com.example.exceptions.StudentNotFoundException;
import com.example.repositories.BatchRepository;
import com.example.repositories.CourseRepository;
import com.example.repositories.EnquiryRepository;
import com.example.repositories.StudentRepository;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final EnquiryRepository enquiryRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            EnquiryRepository enquiryRepository,
            CourseRepository courseRepository,
            BatchRepository batchRepository,
            PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.enquiryRepository = enquiryRepository;
        this.courseRepository = courseRepository;
        this.batchRepository = batchRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StudentResponse registerStudent(
            StudentRequest request) {

        if (studentRepository.existsByEnquiryEnquiryId(
                request.getEnquiryId())) {

            throw new DuplicateAdmissionException(
                    "Student is already registered for enquiry ID: "
                            + request.getEnquiryId());
        }

        if (request.getStudentUsername() != null
                && !request.getStudentUsername().isBlank()
                && studentRepository.existsByStudentUsername(
                        request.getStudentUsername())) {

            throw new DuplicateAdmissionException(
                    "Student username already exists: "
                            + request.getStudentUsername());
        }

        Enquiry enquiry = enquiryRepository
                .findById(request.getEnquiryId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Enquiry not found with ID: "
                                        + request.getEnquiryId()));

        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Course not found with ID: "
                                        + request.getCourseId()));

        Batch batch = batchRepository
                .findById(request.getBatchId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Batch not found with ID: "
                                        + request.getBatchId()));

        validateBatchAndCourse(batch, course);

        Student student = new Student();

        student.setEnquiry(enquiry);
        student.setStudentName(request.getStudentName());
        student.setStudentAddress(request.getStudentAddress());
        student.setStudentGender(request.getStudentGender());
        student.setPhotoUrl(request.getPhotoUrl());
        student.setStudentDob(request.getStudentDob());
        student.setStudentQualification(
                request.getStudentQualification());
        student.setStudentMobile(request.getStudentMobile());
        student.setCourseFee(request.getCourseFee());
        student.setCourse(course);
        student.setBatch(batch);
        student.setStudentUsername(
                request.getStudentUsername());

        if (request.getStudentPassword() != null
                && !request.getStudentPassword().isBlank()) {

            student.setStudentPassword(
                    passwordEncoder.encode(
                            request.getStudentPassword()));
        }

        Student savedStudent =
                studentRepository.save(student);

        /*
         * Your Enquiry entity uses Boolean processed flag.
         */
        enquiry.setEnquiryProcessedFlag(true);
        enquiryRepository.save(enquiry);

        return convertToResponse(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {

        return studentRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentById(
            Integer studentId) {

        Student student =
                findStudentById(studentId);

        return convertToResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentByEnquiryId(
            Integer enquiryId) {

        Student student = studentRepository
                .findByEnquiryEnquiryId(enquiryId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found for enquiry ID: "
                                        + enquiryId));

        return convertToResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> searchStudents(
            String name,
            Long mobile) {

        boolean hasName =
                name != null && !name.isBlank();

        boolean hasMobile =
                mobile != null;

        List<Student> students;

        if (hasName && hasMobile) {

            String searchName =
                    name.trim().toLowerCase(Locale.ROOT);

            students = studentRepository
                    .findByStudentMobile(mobile)
                    .stream()
                    .filter(student ->
                            student.getStudentName() != null
                                    && student.getStudentName()
                                            .toLowerCase(Locale.ROOT)
                                            .contains(searchName))
                    .toList();

        } else if (hasName) {

            students = studentRepository
                    .findByStudentNameContainingIgnoreCase(
                            name.trim());

        } else if (hasMobile) {

            students = studentRepository
                    .findByStudentMobile(mobile);

        } else {

            students = studentRepository.findAll();
        }

        return students
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public StudentResponse updateStudent(
            Integer studentId,
            StudentRequest request) {

        Student student =
                findStudentById(studentId);

        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Course not found with ID: "
                                        + request.getCourseId()));

        Batch batch = batchRepository
                .findById(request.getBatchId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Batch not found with ID: "
                                        + request.getBatchId()));

        validateBatchAndCourse(batch, course);

        String requestedUsername =
                request.getStudentUsername();

        String existingUsername =
                student.getStudentUsername();

        boolean usernameChanged =
                requestedUsername != null
                        && !requestedUsername.isBlank()
                        && (existingUsername == null
                        || !requestedUsername.equalsIgnoreCase(
                                existingUsername));

        if (usernameChanged
                && studentRepository.existsByStudentUsername(
                        requestedUsername)) {

            throw new DuplicateAdmissionException(
                    "Student username already exists: "
                            + requestedUsername);
        }

        student.setStudentName(request.getStudentName());
        student.setStudentAddress(request.getStudentAddress());
        student.setStudentGender(request.getStudentGender());
        student.setPhotoUrl(request.getPhotoUrl());
        student.setStudentDob(request.getStudentDob());
        student.setStudentQualification(
                request.getStudentQualification());
        student.setStudentMobile(request.getStudentMobile());
        student.setCourseFee(request.getCourseFee());
        student.setCourse(course);
        student.setBatch(batch);
        student.setStudentUsername(requestedUsername);

        if (request.getStudentPassword() != null
                && !request.getStudentPassword().isBlank()) {

            student.setStudentPassword(
                    passwordEncoder.encode(
                            request.getStudentPassword()));
        }

        Student updatedStudent =
                studentRepository.save(student);

        return convertToResponse(updatedStudent);
    }

    private Student findStudentById(
            Integer studentId) {

        return studentRepository
                .findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: "
                                        + studentId));
    }

    private void validateBatchAndCourse(
            Batch batch,
            Course course) {

        if (batch.getCourse() == null
                || batch.getCourse().getCourseId() == null
                || !batch.getCourse()
                        .getCourseId()
                        .equals(course.getCourseId())) {

            throw new IllegalArgumentException(
                    "Selected batch does not belong to "
                            + "the selected course");
        }
    }

    private StudentResponse convertToResponse(
            Student student) {

        StudentResponse response =
                new StudentResponse();

        response.setStudentId(
                student.getStudentId());

        response.setStudentName(
                student.getStudentName());

        response.setStudentAddress(
                student.getStudentAddress());

        response.setStudentGender(
                student.getStudentGender());

        response.setPhotoUrl(
                student.getPhotoUrl());

        response.setStudentDob(
                student.getStudentDob());

        response.setStudentQualification(
                student.getStudentQualification());

        response.setStudentMobile(
                student.getStudentMobile());

        response.setCourseFee(
                student.getCourseFee());

        response.setStudentUsername(
                student.getStudentUsername());

        if (student.getEnquiry() != null) {
            response.setEnquiryId(
                    student.getEnquiry()
                            .getEnquiryId());
        }

        if (student.getCourse() != null) {

            response.setCourseId(
                    student.getCourse()
                            .getCourseId());

            response.setCourseName(
                    student.getCourse()
                            .getCourseName());
        }

        if (student.getBatch() != null) {

            response.setBatchId(
                    student.getBatch()
                            .getBatchId());

            response.setBatchName(
                    student.getBatch()
                            .getBatchName());
        }

        return response;
    }
}