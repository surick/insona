package com.jieweifu.controllers;

import com.froala.editor.File;
import com.froala.editor.file.FileOptions;
import com.froala.editor.file.FileValidation;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.Document;
import com.jieweifu.models.insona.Example;
import com.jieweifu.models.insona.Product;
import com.jieweifu.services.insona.DocumentService;
import com.jieweifu.services.insona.ExampleService;
import com.jieweifu.services.insona.ProductService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller("File")
@RequestMapping("file")
public class FileController {

    private static Logger logger = LoggerFactory.getLogger(FileController.class);
    private DocumentService documentService;
    private ProductService productService;
    private ExampleService exampleService;

    private RedisUtil redisUtil;

    @Autowired
    public FileController(ExampleService exampleService,DocumentService documentService, RedisUtil redisUtil,ProductService productService) {
        this.documentService = documentService;
        this.redisUtil = redisUtil;
        this.productService = productService;
        this.exampleService = exampleService;
    }

    @Value("${custom.upload.file}")
    private String uploadPath;

    @Value("${custom.upload.home}")
    private String DocumentUpload;

    @PostMapping("upload")
    @ResponseBody
    public Map<Object, Object> upload(HttpServletRequest request) {
        FileOptions options = new FileOptions();
        Map<Object, Object> responseData = new HashMap<>();
        options.setValidation(new FileValidation());
        try {
            File.upload(request, uploadPath, options).forEach((key, value) -> responseData.put(key, "/uploads/file/" + value));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("error", e.toString());
        }
        return responseData;
    }

    @PostMapping("delete")
    @ResponseBody
    public String delete(HttpServletRequest request, @RequestParam String src) {
        try {
            File.delete(request, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @PostMapping("DocumentUpload")
    @ResponseBody
    public Map<Object, Object> DocumentUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        FileOptions options = new FileOptions();
        Map<Object, Object> responseData = new HashMap<>();
        options.setValidation(new FileValidation());
        try {
            File.upload(request, DocumentUpload, options).forEach((key, value) -> responseData.put(key, "/uploads/home/" + value));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("error", e.toString());
        }
        Document document = new Document();
        document.setName(file.getOriginalFilename());
        document.setFileType("全部");
        document.setFileUrl(String.valueOf(responseData.get("link")));
        int total = documentService.getDocumentTotal();
        document.setSortNo(total + 1);
        document.setIsDelete(0);
        String label = (String) redisUtil.get("label");
        document.setLabel(label);
        documentService.saveDocument(document);
        return responseData;
    }

    @PostMapping("products")
    @ResponseBody
    public Result readExcel(MultipartFile file) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum+1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list.add(cells);
                }
            }
        }
        return save(list);
    }
    public void checkFile(MultipartFile file) throws IOException{
        //判断文件是否存在
        if(null == file){
            logger.error("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if(!fileName.endsWith("xls")&&!fileName.endsWith("xlsx")){
            logger.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }
    public Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith("xls")){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith("xlsx")){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }
    public String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public Result save(List<String[]> list) {
        try{
            for(String[] cells : list){
                Product product = new Product();
                product.setDid(cells[0]);
                product.setName(cells[1]);
                product.setSerial_code(cells[2]);
                product.setVersion(cells[3]);
                product.setSub_inter(cells[4]);
                product.setSub_maker(cells[5]);
                product.setType(cells[6]);
                productService.saveProduct(product);
            }
        }catch (Exception e){
            return new Result().setError("批量新增失败");
        }

        return new Result().setMessage("批量新增成功");
    }
    @PostMapping("example")
    @ResponseBody
    public Map<Object, Object> example(HttpServletRequest request) {
        FileOptions options = new FileOptions();
        Map<Object, Object> responseData = new HashMap<>();
        options.setValidation(new FileValidation());
        try {
            File.upload(request, uploadPath, options).forEach((key, value) -> responseData.put(key, "/uploads/file/" + value));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("error", e.toString());
        }
        Example example = new Example();
        example.setFileUrl((String) responseData.get("link"));
        example.setId(1);
        exampleService.save(example);
        return responseData;
    }

}
