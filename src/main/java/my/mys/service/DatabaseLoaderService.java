package my.mys.service;

import lombok.AllArgsConstructor;
import my.mys.repository.MovementsRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.jni.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.Iterator;

@Service
@AllArgsConstructor
public class DatabaseLoaderService {

    private final MovementsRepository movementsRepository;

    public String loadDataFromExcel(MultipartFile inputFile) {
        try {
            String newLine = System.getProperty("line.separator");
            InputStream inputStream = inputFile.getInputStream();
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
            HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);

            //Getting the first sheet of the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int rowIteratorCount = 0;
            while (rowIterator.hasNext()) {
                System.out.print(newLine);
                System.out.print("******************");
                System.out.print("Line Number: "+rowIteratorCount);
                System.out.print("******************");
                rowIteratorCount++;
                System.out.print(newLine);
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellIteratorCount = 0;
                while (cellIterator.hasNext()) {
                    System.out.print("Cell (column) Number: "+rowIteratorCount+" ");
                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly, other cell types exist, you may want to check them out
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            //if()
                            System.out.print(cell.getNumericCellValue()+"||");
                            break;
                        case STRING:
                            System.out.print(cell.getStringCellValue()+"||");
                            break;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}