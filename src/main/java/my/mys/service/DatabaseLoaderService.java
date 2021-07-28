package my.mys.service;

import lombok.AllArgsConstructor;
import my.mys.domain.Movements;
import my.mys.repository.MovementsRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.jni.File;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
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
            Movements movementsFromExcel = new Movements();

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
                System.out.print(newLine);
                Row row = rowIterator.next();
                if(rowIteratorCount > 0) {
                    //For each row, iterate through all the columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int cellIteratorCount = 0;
                    while (cellIterator.hasNext()) {
                        System.out.print("Cell (column) Number: "+cellIteratorCount+" ");
                            Cell cell = cellIterator.next();
                            //Check the cell type and format accordingly, other cell types exist, you may want to check them out
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    switch (cellIteratorCount) {
                                        case 0:
                                            movementsFromExcel.setAccounting_date(cell.getLocalDateTimeCellValue().toLocalDate());
                                            break;
                                        case 1:
                                            movementsFromExcel.setNegative_amounts(cell.getNumericCellValue());
                                            movementsFromExcel.setPositive_amounts(null);
                                            break;
                                        case 2:
                                            movementsFromExcel.setPositive_amounts(cell.getNumericCellValue());
                                            movementsFromExcel.setNegative_amounts(null);
                                            break;
                                    }
                                    System.out.print(cell.getNumericCellValue() + " << numeric||");
                                    cellIteratorCount++;
                                    break;
                                case STRING:
                                    switch (cellIteratorCount) {
                                    case 3:
                                        movementsFromExcel.setDetail(cell.getStringCellValue());
                                        break;
                                }
                                    System.out.print(cell.getStringCellValue() + " << string||");
                                    cellIteratorCount++;
                                    break;
                                default:
                                    System.out.print(cell.getStringCellValue() + " << something else||");
                                    cellIteratorCount++;
                                    break;
                            }
                        }
                    Movements movements = Movements.builder()
                            .accounting_date(movementsFromExcel.getAccounting_date())
                            .competence_month(isAfterTheTwentySeventh(movementsFromExcel) ? movementsFromExcel.getAccounting_date().getMonthValue()+1:movementsFromExcel.getAccounting_date().getMonthValue())
                            .competence_year(movementsFromExcel.getAccounting_date().getYear())
                            .negative_amounts(movementsFromExcel.getNegative_amounts())
                            .positive_amounts(movementsFromExcel.getPositive_amounts())
                            .detail(movementsFromExcel.getDetail())
                            .build();
                    movementsRepository.save(movements);
                    }
                rowIteratorCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    private boolean isAfterTheTwentySeventh(Movements movementsFromExcel) {
        return ((movementsFromExcel.getAccounting_date().getDayOfMonth() >= 25 && null!=movementsFromExcel.getPositive_amounts())
                ||(movementsFromExcel.getAccounting_date().getDayOfMonth() >= 27 && null!=movementsFromExcel.getNegative_amounts()));
    }
}