package com.example.config;

import com.example.entities.Batch;
import com.example.entities.Course;
import com.example.repositories.BatchRepository;
import com.example.repositories.CourseRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CourseRepository courseRepository, BatchRepository batchRepository) {
        return args -> {
            // Option A: If course table is empty, seed default courses with cover photos
            if (courseRepository.count() == 0) {

                // ==================== PG DIPLOMAS ====================
                Course dac = new Course();
                dac.setCourseName("PG-DAC");
                dac.setCourseCategory("PG DIPLOMA");
                dac.setCourseDescription("Post Graduate Diploma in Advanced Computing");
                dac.setCourseDuration(180);
                dac.setCourseFees(new BigDecimal("90000.00"));
                dac.setCourseFeesFrom(LocalDate.now());
                dac.setCourseFeesTo(LocalDate.now().plusYears(1));
                dac.setCoverPhoto("/images/Dac.jpg");
                dac.setAgeGrpType("Graduate");
                dac.setCourseIsActive(true);
                dac.setIsFeatured(true);

                Course dbda = new Course();
                dbda.setCourseName("PG-DBDA");
                dbda.setCourseCategory("PG DIPLOMA");
                dbda.setCourseDescription("Post Graduate Diploma in Big Data Analytics");
                dbda.setCourseDuration(180);
                dbda.setCourseFees(new BigDecimal("90000.00"));
                dbda.setCourseFeesFrom(LocalDate.now());
                dbda.setCourseFeesTo(LocalDate.now().plusYears(1));
                dbda.setCoverPhoto("/images/Dbda.jpg");
                dbda.setAgeGrpType("Graduate");
                dbda.setCourseIsActive(true);
                dbda.setIsFeatured(true);

                Course ditiss = new Course();
                ditiss.setCourseName("PG-DITISS");
                ditiss.setCourseCategory("PG DIPLOMA");
                ditiss.setCourseDescription("Post Graduate Diploma in IT Infrastructure, Systems and Security");
                ditiss.setCourseDuration(180);
                ditiss.setCourseFees(new BigDecimal("90000.00"));
                ditiss.setCourseFeesFrom(LocalDate.now());
                ditiss.setCourseFeesTo(LocalDate.now().plusYears(1));
                ditiss.setCoverPhoto("/images/Ditss.jpg");
                ditiss.setAgeGrpType("Graduate");
                ditiss.setCourseIsActive(true);
                ditiss.setIsFeatured(false);

                // ==================== CERTIFICATIONS ====================
                Course java = new Course();
                java.setCourseName("Java Programming");
                java.setCourseCategory("CERTIFICATION");
                java.setCourseDescription("Core and Advanced Java Certification with Spring Boot");
                java.setCourseDuration(90);
                java.setCourseFees(new BigDecimal("25000.00"));
                java.setCourseFeesFrom(LocalDate.now());
                java.setCourseFeesTo(LocalDate.now().plusYears(1));
                java.setCoverPhoto("/images/Java.jpg");
                java.setAgeGrpType("Any");
                java.setCourseIsActive(true);
                java.setIsFeatured(true);

                Course python = new Course();
                python.setCourseName("Python Programming");
                python.setCourseCategory("CERTIFICATION");
                python.setCourseDescription("Python Programming Certification for Data & Automation");
                python.setCourseDuration(60);
                python.setCourseFees(new BigDecimal("20000.00"));
                python.setCourseFeesFrom(LocalDate.now());
                python.setCourseFeesTo(LocalDate.now().plusYears(1));
                python.setCoverPhoto("/images/Python.jpg");
                python.setAgeGrpType("Any");
                python.setCourseIsActive(true);
                python.setIsFeatured(false);

                Course cpp = new Course();
                cpp.setCourseName("C++ Programming");
                cpp.setCourseCategory("CERTIFICATION");
                cpp.setCourseDescription("Low-Latency Object Oriented Programming in Modern C++");
                cpp.setCourseDuration(60);
                cpp.setCourseFees(new BigDecimal("18000.00"));
                cpp.setCourseFeesFrom(LocalDate.now());
                cpp.setCourseFeesTo(LocalDate.now().plusYears(1));
                cpp.setCoverPhoto("/images/C++.jpg");
                cpp.setAgeGrpType("Any");
                cpp.setCourseIsActive(true);
                cpp.setIsFeatured(false);

                Course webDev = new Course();
                webDev.setCourseName("Web Development");
                webDev.setCourseCategory("CERTIFICATION");
                webDev.setCourseDescription("Full Stack Web Development with React and Node");
                webDev.setCourseDuration(90);
                webDev.setCourseFees(new BigDecimal("22000.00"));
                webDev.setCourseFeesFrom(LocalDate.now());
                webDev.setCourseFeesTo(LocalDate.now().plusYears(1));
                webDev.setCoverPhoto("/images/Webdevelopment.jpg");
                webDev.setAgeGrpType("Any");
                webDev.setCourseIsActive(true);
                webDev.setIsFeatured(false);

                // ==================== SHORT-TERM ====================
                Course mscit = new Course();
                mscit.setCourseName("MS-CIT");
                mscit.setCourseCategory("SHORT-TERM");
                mscit.setCourseDescription("Basic Computer Literacy and Information Technology");
                mscit.setCourseDuration(60);
                mscit.setCourseFees(new BigDecimal("5000.00"));
                mscit.setCourseFeesFrom(LocalDate.now());
                mscit.setCourseFeesTo(LocalDate.now().plusYears(1));
                mscit.setCoverPhoto("/images/Mscit.jpg");
                mscit.setAgeGrpType("Any");
                mscit.setCourseIsActive(true);
                mscit.setIsFeatured(false);

                Course tally = new Course();
                tally.setCourseName("Tally Prime");
                tally.setCourseCategory("SHORT-TERM");
                tally.setCourseDescription("Financial Accounting and GST Compliance in Tally Prime");
                tally.setCourseDuration(45);
                tally.setCourseFees(new BigDecimal("8000.00"));
                tally.setCourseFeesFrom(LocalDate.now());
                tally.setCourseFeesTo(LocalDate.now().plusYears(1));
                tally.setCoverPhoto("/images/Tally.jpg");
                tally.setAgeGrpType("Any");
                tally.setCourseIsActive(true);
                tally.setIsFeatured(false);

                // Save all courses (MySQL auto-generates course_id and JPA assigns them back to these instances)
                List<Course> savedCourses = courseRepository.saveAll(List.of(dac, dbda, ditiss, java, python, cpp, webDev, mscit, tally));
                System.out.println("✅ [Code-First Initializer] All courses seeded with cover photos!");

                // ==================== BATCHES INITIALIZATION ====================
                if (batchRepository.count() == 0) {
                    List<Batch> batchesToSave = new ArrayList<>();

                    for (Course course : savedCourses) {
                        // 1. Morning Batch
                        Batch morningBatch = new Batch();
                        morningBatch.setBatchName(course.getCourseName() + " - Morning Batch");
                        morningBatch.setBatchStartTime(LocalTime.of(9, 30));
                        morningBatch.setBatchEndTime(LocalTime.of(13, 30));
                        morningBatch.setBatchIsActive(true);
                        morningBatch.setCourse(course); // Link foreign key to auto-incremented course ID
                        batchesToSave.add(morningBatch);

                        // 2. Evening Batch (for PG Diplomas and Certifications)
                        if ("PG DIPLOMA".equals(course.getCourseCategory()) || "CERTIFICATION".equals(course.getCourseCategory())) {
                            Batch eveningBatch = new Batch();
                            eveningBatch.setBatchName(course.getCourseName() + " - Evening Batch");
                            eveningBatch.setBatchStartTime(LocalTime.of(14, 30));
                            eveningBatch.setBatchEndTime(LocalTime.of(18, 30));
                            eveningBatch.setBatchIsActive(true);
                            eveningBatch.setCourse(course); // Link foreign key to auto-incremented course ID
                            batchesToSave.add(eveningBatch);
                        }
                    }

                    batchRepository.saveAll(batchesToSave);
                    System.out.println("✅ [Code-First Initializer] All active batches seeded and linked to course IDs!");
                }
            } 
            // Option B: If courses already exist in DB without cover photos, update them automatically
            else {
                List<Course> courses = courseRepository.findAll();
                for (Course course : courses) {
                    if (course.getCoverPhoto() == null || course.getCoverPhoto().isEmpty()) {
                        String name = course.getCourseName().toLowerCase();
                        if (name.contains("dac")) course.setCoverPhoto("/images/Dac.jpg");
                        else if (name.contains("dbda")) course.setCoverPhoto("/images/Dbda.jpg");
                        else if (name.contains("ditiss") || name.contains("ditss")) course.setCoverPhoto("/images/Ditss.jpg");
                        else if (name.contains("java")) course.setCoverPhoto("/images/Java.jpg");
                        else if (name.contains("python")) course.setCoverPhoto("/images/Python.jpg");
                        else if (name.contains("c++")) course.setCoverPhoto("/images/C++.jpg");
                        else if (name.contains("mscit") || name.contains("ms-cit")) course.setCoverPhoto("/images/Mscit.jpg");
                        else if (name.contains("tally")) course.setCoverPhoto("/images/Tally.jpg");
                        else course.setCoverPhoto("/images/Webdevelopment.jpg"); // Default fallback
                        
                        courseRepository.save(course);
                    }
                }
                System.out.println("✅ [Code-First Initializer] Existing courses updated with cover photo paths!");
            }
        };
    }
}