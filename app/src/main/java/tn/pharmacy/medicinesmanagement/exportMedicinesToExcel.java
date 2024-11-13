package tn.pharmacy.medicinesmanagement;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import tn.pharmacy.medicinesmanagement.entities.Medicine;

public class exportMedicinesToExcel {

    public static void exportMedicinesToExcel(Context context, List<Medicine> medicines) {
        // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Medicines");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Description");
        headerRow.createCell(2).setCellValue("Price");
        headerRow.createCell(3).setCellValue("Quantity");

        // Fill in the data rows
        int rowIndex = 1;
        for (Medicine medicine : medicines) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(medicine.getName());
            row.createCell(1).setCellValue(medicine.getDescription());
            row.createCell(2).setCellValue(medicine.getPrice());
            row.createCell(3).setCellValue(medicine.getQuantity());
        }

        // Save the file to external storage
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Medicines.xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            // Show success message
            Toast.makeText(context, "Medicines exported successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show error message
            Toast.makeText(context, "Error exporting medicines: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
