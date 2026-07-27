package com.example.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;

import com.example.dto.ExcelImportResponse;
import com.example.dto.ExcelValidationResponse;
import com.example.entities.Batch;
import com.example.entities.PlacedStudent;
import com.example.entities.Recruiter;
import com.example.exceptions.ExcelImportException;
//import com.example.repositories.BatchRepository;
import com.example.repositories.PlacedStudentRepository;
import com.example.repositories.RecruiterRepository;

@Service
public class ExcelUploadServiceImpl implements ExcelUploadService {

    @Autowired
    private PlacedStudentRepository placedStudentRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private BatchRepository batchRepository;

    private final DataFormatter formatter = new DataFormatter();

    @Override
    public ExcelValidationResponse validateExcel(MultipartFile file) {

        ExcelValidationResponse response =
                new ExcelValidationResponse();

        if (file == null || file.isEmpty()) {

            response.setSuccess(false);
            response.getErrors().add("Excel file is empty.");

            return response;
        }

        if (!file.getOriginalFilename().endsWith(".xlsx")) {

            response.setSuccess(false);
            response.getErrors().add("Only .xlsx file is allowed.");

            return response;
        }

        int total = 0;
        int valid = 0;
        int invalid = 0;

        try (Workbook workbook =
                     new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {

                Row row = rows.next();

                total++;

                Cell studentCell = row.getCell(0);
                Cell packageCell = row.getCell(1);
                Cell recruiterCell = row.getCell(2);
                Cell batchCell = row.getCell(3);

                if (studentCell == null ||
                        recruiterCell == null ||
                        batchCell == null) {

                    invalid++;

                    response.getErrors().add(
                            "Row " + (row.getRowNum() + 1)
                                    + " contains empty mandatory fields.");

                    continue;
                }

                valid++;
            }

            response.setSuccess(invalid == 0);
            response.setTotalRecords(total);
            response.setValidRecords(valid);
            response.setInvalidRecords(invalid);

        } catch (IOException e) {

            throw new ExcelImportException(
                    "Unable to validate excel file.");
        }

        return response;
    }

    @Override
    public ExcelImportResponse uploadExcel(MultipartFile file) {

        ExcelValidationResponse validation =
                validateExcel(file);

        if (!validation.isSuccess()) {

            ExcelImportResponse response =
                    new ExcelImportResponse();

            response.setSuccess(false);
            response.setMessage("Excel validation failed.");
            response.setTotalRecords(
                    validation.getTotalRecords());

            return response;
        }

        int total = 0;
        int imported = 0;
        int failed = 0;

        try (Workbook workbook =
                     new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {

                Row row = rows.next();

                total++;

                String studentName =
                        formatter.formatCellValue(
                                row.getCell(0));

                String packageValue =
                        formatter.formatCellValue(
                                row.getCell(1));

                String recruiterId =
                        formatter.formatCellValue(
                                row.getCell(2));

                String batchId =
                        formatter.formatCellValue(
                                row.getCell(3));

                try {

                    Recruiter recruiter =
                            recruiterRepository.findById(
                                    Integer.parseInt(recruiterId))
                                    .orElseThrow(() ->
                                            new RuntimeException(
                                                    "Recruiter not found"));

                    Batch batch =
                            batchRepository.findById(
                                    Integer.parseInt(batchId))
                                    .orElseThrow(() ->
                                            new RuntimeException(
                                                    "Batch not found"));

                    PlacedStudent placedStudent =
                            new PlacedStudent();

                    placedStudent.setPlacedStudentName(
                            studentName);

                    placedStudent.setPlacementPackage(
                            new BigDecimal(packageValue));

                    placedStudent.setRecruiter(recruiter);

                    placedStudent.setBatch(batch);

                    placedStudentRepository.save(placedStudent);

                    imported++;

                } catch (Exception e) {

                    failed++;
                }

            }

            ExcelImportResponse response =
                    new ExcelImportResponse();

            response.setSuccess(failed == 0);

            if (failed == 0) {

                response.setMessage(
                        "Excel imported successfully.");

            } else {

                response.setMessage(
                        "Excel imported with some failed records.");

            }

            response.setTotalRecords(total);
            response.setImportedRecords(imported);
            response.setFailedRecords(failed);

            return response;

        } catch (IOException e) {

            throw new ExcelImportException(
                    "Unable to import Excel file.");
        }
    }

}
