package org.example.springordermanagement.customer;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName();

        Optional<Customer> customer = customerService.getCustomerById(id);

        if (customer.isPresent()) {
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return ResponseEntity.ok(customer.get());
            }

            if (customer.get().getEmail().equals(currentEmail)) {
                return ResponseEntity.ok(customer.get());
            }
            else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable long id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);

        if (updatedCustomer != null) {
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/excel")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportAllCustomersToExcel(HttpServletResponse response) throws IOException {
        List<Customer> customers = customerService.getAllCustomers();

        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Customers_report_(" + LocalDate.now() + ")_" +
                UUID.randomUUID() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Клиенты");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        DataFormat dataFormat = workbook.createDataFormat();
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(dataFormat.getFormat("dd.MM.yyyy"));
        dateCellStyle.setBorderBottom(BorderStyle.THIN);
        dateCellStyle.setBorderTop(BorderStyle.THIN);
        dateCellStyle.setBorderLeft(BorderStyle.THIN);
        dateCellStyle.setBorderRight(BorderStyle.THIN);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Список клиентов");
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        CellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setFont(titleFont);
        titleRow.setHeightInPoints(30);
        titleCell.setCellStyle(titleCellStyle);

        Row headerRow = sheet.createRow(1);
        String[] headers = {"ID", "ФИО", "Email", "Телефон", "Дата регистрации"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 2;

        if (customers != null) {
            for (Customer customer : customers) {
                Row row = sheet.createRow(rowNum++);

                Cell idCell = row.createCell(0);
                idCell.setCellValue(customer.getId());
                idCell.setCellStyle(cellStyle);

                Cell ns = row.createCell(1);
                ns.setCellValue(customer.getName() + " " + customer.getSurname());
                ns.setCellStyle(cellStyle);

                Cell emailCell = row.createCell(2);
                emailCell.setCellValue(customer.getEmail());
                emailCell.setCellStyle(cellStyle);

                Cell phoneCell = row.createCell(3);
                phoneCell.setCellValue(customer.getPhone());
                phoneCell.setCellStyle(cellStyle);

                Cell registerDateCell = row.createCell(4);
                registerDateCell.setCellValue(customer.getRegistrationDate());
                registerDateCell.setCellStyle(dateCellStyle);
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
