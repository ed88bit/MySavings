package my.mys.service;

import lombok.AllArgsConstructor;
import my.mys.domain.Movements;
import my.mys.repository.MovementsRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;

@Service
@AllArgsConstructor
public class DatabaseLoaderService {

    static Logger logger = Logger.getLogger(DatabaseLoaderService.class);
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
            logger.info("loading data from excel, you will be able to follow what cell I am reading (row/column).");
            logger.info("ATTENTION! I will skip 1st row because I suppose there's the header of the excel.");
            //Iterate through each rows one by one
            //and store to DB
            fromExceltoDB(movementsFromExcel, sheet);
        } catch (Exception e) {
            logger.info("I found a problem, please see the error below:");
            e.printStackTrace();
            return "ko";
        }
        logger.info("Everything went fine. Closing.");
        return "ok";
    }

    private void fromExceltoDB(Movements movementsFromExcel, HSSFSheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();
        int rowIteratorCount = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (rowIteratorCount > 0) {
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellIteratorCount = 0;
                while (cellIterator.hasNext()) {
                    logger.info("Cell (row) Number: " + (rowIteratorCount + 1) + " ");
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
                            logger.info("Cell (column) Number: " + (cellIteratorCount + 1) + " ");
                            cellIteratorCount++;
                            break;
                        case STRING:
                            switch (cellIteratorCount) {
                                case 3:
                                    movementsFromExcel.setDetail(cell.getStringCellValue());
                                    break;
                            }
                            logger.info("Cell (column) Number: " + (cellIteratorCount + 1) + " ");
                            cellIteratorCount++;
                            break;
                        default:
                            logger.info("Cell (column) Number: " + (cellIteratorCount + 1) + " ");
                            cellIteratorCount++;
                            break;
                    }
                }
                saveToRepository(movementsFromExcel);
            }
            rowIteratorCount++;
        }
    }

    private void saveToRepository(Movements movementsFromExcel) {
        Movements movements = Movements.builder()
                .accounting_date(movementsFromExcel.getAccounting_date())
                .competence_month(isAfterTheTwentySeventh(movementsFromExcel) ? movementsFromExcel.getAccounting_date().getMonthValue() + 1 : movementsFromExcel.getAccounting_date().getMonthValue())
                .competence_year(movementsFromExcel.getAccounting_date().getYear())
                .negative_amounts(movementsFromExcel.getNegative_amounts())
                .positive_amounts(movementsFromExcel.getPositive_amounts())
                .detail(movementsFromExcel.getDetail())
                .build();
        movementsRepository.save(movements);
    }

    private boolean isAfterTheTwentySeventh(Movements movementsFromExcel) {
        return ((movementsFromExcel.getAccounting_date().getDayOfMonth() >= 25 && null != movementsFromExcel.getPositive_amounts())
                || (movementsFromExcel.getAccounting_date().getDayOfMonth() >= 27 && null != movementsFromExcel.getNegative_amounts()));
    }
}