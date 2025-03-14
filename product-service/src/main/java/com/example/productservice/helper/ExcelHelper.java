package com.example.productservice.helper;

import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {
    private final static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final static String[] HEADERS = { "Id", "Title", "Description", "Published" };
    private final static String SHEET = "Tutorials";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType()) ? true : false;
    }

//    public static List<Tutorial> excelToTutorials(InputStream is) {
//        try {
//            Workbook workbook = new XSSFWorkbook(is);
//
//            Sheet sheet = workbook.getSheet(SHEET);
//            Iterator<Row> rows = sheet.iterator();
//
//            List<Tutorial> tutorials = new ArrayList<Tutorial>();
//
//            int rowNumber = 0;
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//
//                // skip header
//                if (rowNumber == 0) {
//                    rowNumber++;
//                    continue;
//                }
//
//                Iterator<Cell> cellsInRow = currentRow.iterator();
//
//                Tutorial tutorial = new Tutorial();
//
//                int cellIdx = 0;
//                while (cellsInRow.hasNext()) {
//                    Cell currentCell = cellsInRow.next();
//
//                    switch (cellIdx) {
//                        case 0:
//                            tutorial.setId((long) currentCell.getNumericCellValue());
//                            break;
//
//                        case 1:
//                            tutorial.setTitle(currentCell.getStringCellValue());
//                            break;
//
//                        case 2:
//                            tutorial.setDescription(currentCell.getStringCellValue());
//                            break;
//
//                        case 3:
//                            tutorial.setPublished(currentCell.getBooleanCellValue());
//                            break;
//
//                        default:
//                            break;
//                    }
//
//                    cellIdx++;
//                }
//
//                tutorials.add(tutorial);
//            }
//
//            workbook.close();
//
//            return tutorials;
//        } catch (IOException e) {
//            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
//        }
//    }
}
