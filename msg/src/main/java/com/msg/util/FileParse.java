package com.msg.util;

import com.mktsms.common.CodeConts;
import com.mktsms.ecpection.MktsmsSendException;
import com.mktsms.entity.MktsmsSendRecord;
import com.mktsms.vo.MktsmsImportMemberVO;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wd on 2017/11/21.
 */
public class FileParse {
    private Logger logger = Logger.getLogger(FileParse.class);
    /**
     * 解析传过来的文件，存成发送记录
     *
     * @param
     * @return
     */
    public static List<MktsmsSendRecord> smsFileParse(InputStream inputStream, String excelFileName) throws IOException {
        List<MktsmsSendRecord> list = new ArrayList<>();
        MktsmsImportMemberVO vo = new MktsmsImportMemberVO();
        //FileInputStream inputStream = new FileInputStream(file);
        try {
            //创建工作簿
            Workbook workbook = createWorkbook(inputStream, excelFileName);
            //创建工作表sheet
            Sheet sheet = getSheet(workbook, 0);
            //获取sheet中的行数
            int begin = sheet.getFirstRowNum();
            int end = sheet.getLastRowNum();
            //获取表头单元格个数
            int cells = sheet.getRow(0).getPhysicalNumberOfCells();
            //获取表头
            Row sheetRow = sheet.getRow(0);
            for (int i=0;i<5;i++){
                Cell cell = sheetRow.getCell(i);
                if (cell == null) {
                    cell = sheetRow.createCell(i);
                }
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String value = null == cell.getStringCellValue() ? "" : cell.getStringCellValue();
                if (i==0&&!("客户编号".equals(value))){
                    throw new MktsmsSendException(CodeConts.PARAM_LEGAL,"模板不正确，请使用默认模板");
                }
                if (i==1&&!("客户姓名".equals(value))){
                    throw new MktsmsSendException(CodeConts.PARAM_LEGAL,"模板不正确，请使用默认模板");
                }
                if (i==2&&!("客户类型".equals(value))){
                    throw new MktsmsSendException(CodeConts.PARAM_LEGAL,"模板不正确，请使用默认模板");
                }
                if (i==3&&!("手机号".equals(value))){
                    throw new MktsmsSendException(CodeConts.PARAM_LEGAL,"模板不正确，请使用默认模板");
                }
                if (i==4&&!("性别".equals(value))){
                    throw new MktsmsSendException(CodeConts.PARAM_LEGAL,"模板不正确，请使用默认模板");
                }

            }

            //int rowEnd = sheet.getPhysicalNumberOfRows();
            for(int i = 1; i <= end; i++){
                Row row = sheet.getRow(i);
                if(row == null || isBlankRow(row)){
                    continue;
                }else{
                    //利用反射，给JavaBean的属性进行赋值
                    Field[] fields = vo.getClass().getDeclaredFields();
                    //Row row = sheet.getRow(i);
                    Cell cell2 = row.getCell(3);
                    if (cell2 == null) {
                        cell2 = row.createCell(3);
                    }
                    cell2.setCellType(Cell.CELL_TYPE_STRING);
                    if (cell2.getStringCellValue()==null|| Objects.equals(cell2.getStringCellValue(), "")){
                        throw new MktsmsSendException(CodeConts.PARAM_LEGAL,"手机号码为必填字段，请输入后重试");
                    }

                    for (int index=0;index<fields.length;index++){
                        Cell cell = row.getCell(index);
                        if (cell == null) {
                            cell = row.createCell(index);
                        }
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String value = null == cell.getStringCellValue() ? "" : cell.getStringCellValue();
                        Field field = fields[index];
                        String fieldName = field.getName();
                        if (fieldName.equals("serialVersionUID")){
                            continue;
                        }
                        if (fields[index].isSynthetic()) {
                            continue;
                        }
                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Method setMethod = vo.getClass().getMethod(methodName, new Class[]{String.class});
                        setMethod.invoke(vo, new Object[]{value});
                    }
                    if (isHasValues(vo)) {//判断对象属性是否有值
                        MktsmsSendRecord mktsmsSendRecord =voConventToRecord(vo);
                        list.add(mktsmsSendRecord);
                        vo.getClass().getConstructor(new Class[]{}).newInstance(new Object[]{});//重新创建一个vo对象
                    }
                }
            }
        }catch (Exception e){
           throw new MktsmsSendException(CodeConts.FAILURE,e.getMessage());
        }
        finally {
            inputStream.close();
        }
        return list;
    }

    /**
     * 判断是否为空行
     * @param row
     * @return
     */
    public static boolean isBlankRow(Row row){
        if(row == null) return true;
        boolean result = true;
        for(int i = 0; i < 5; i++){
            Cell cell = row.getCell(i, Row.RETURN_BLANK_AS_NULL);
            String value = "";
            if(cell != null){
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:// 字符串
                        value = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        value = String.valueOf((int) cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:// Boolean
                        value = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:// 公式
                        value = String.valueOf(cell.getCellFormula());
                        break;
                    /*case Cell.CELL_TYPE_BLANK:// 空值
                        break;*/
                    default:
                        break;
                }

                if(!value.trim().equals("")){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }


    /**
     * @param @param  is
     * @param @param  excelFileName
     * @param @return
     * @param @throws IOException
     * @return Workbook
     * @throws
     * @Title: createWorkbook
     * @Description: 判断excel文件后缀名，生成不同的workbook
     */
    private static Workbook createWorkbook(InputStream is, String excelFileName) throws IOException {
        if (excelFileName.endsWith(".xls")) {
            return new HSSFWorkbook(is);
        } else if (excelFileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(is);
        }
        return null;
    }

    /**
     * @param @param  workbook
     * @param @param  sheetIndex
     * @param @return
     * @return Sheet
     * @throws
     * @Title: getSheet
     * @Description: 根据sheet索引号获取对应的sheet
     */
    private static Sheet getSheet(Workbook workbook, int sheetIndex) {
        return workbook.getSheetAt(sheetIndex);
    }

    /**
     * @param @param  object
     * @param @return
     * @return boolean
     * @throws
     * @Title: isHasValues
     * @Description: 判断一个对象所有属性是否有值，如果一个属性有值(非空)，则返回true
     */
    public static boolean isHasValues(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        boolean flag = false;
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            if (fieldName.equals("serialVersionUID")){
                continue;
            }
            if (fields[i].isSynthetic()) {
                continue;
            }
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getMethod;
            try {
                getMethod = object.getClass().getMethod(methodName);
                Object obj = getMethod.invoke(object);
                if (null != obj && !"".equals(obj)) {
                    flag = true;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
               throw new MktsmsSendException(CodeConts.PARAM_LEGAL,"对象属性非空判断出现问题");
            }

        }
        return flag;

    }

    /**
     * 将导入文件之后上传成员的vo对象转化为发送记录对象
     *
     * @param vo
     * @return
     */
    private static MktsmsSendRecord voConventToRecord(MktsmsImportMemberVO vo) {
        MktsmsSendRecord mktsmsSendRecord = new MktsmsSendRecord();
        try {
            if(vo.getCustCode()!=null&&vo.getCustCode()!=""){
                boolean numeric = isNumeric(vo.getCustCode());
                if (!numeric){
                    throw new MktsmsSendException(CodeConts.PARAM_LEGAL,"文件内容或格式不符");
                }
                mktsmsSendRecord.setCustCode(Long.valueOf(vo.getCustCode()));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new MktsmsSendException(CodeConts.PARAM_LEGAL,"文件内容或格式不符");
        }

        if (vo.getCustName()!=null){
            mktsmsSendRecord.setCustName(vo.getCustName());
        }
        if (vo.getPhone()!=null){
            mktsmsSendRecord.setPhone(vo.getPhone());
        }
        switch (vo.getCustType()) {
            case "机构":
                mktsmsSendRecord.setCustType((byte) 0);
                break;
            case "个人":
                mktsmsSendRecord.setCustType((byte) 1);
                break;
            case "产品":
                mktsmsSendRecord.setCustType((byte) 2);
                break;
            case "":
                break;
            default:
                throw new MktsmsSendException(CodeConts.PARAM_LEGAL, "文件内容或格式不符");
        }

        switch (vo.getSex()) {
            case "男":
                mktsmsSendRecord.setCustType((byte) 1);
                mktsmsSendRecord.setSex((byte) 1);
                break;
            case "女":
                mktsmsSendRecord.setCustType((byte) 1);
                mktsmsSendRecord.setSex((byte) 0);
                break;
            case "":
                break;
            default:
                throw new MktsmsSendException(CodeConts.PARAM_LEGAL, "文件内容或格式不符");
        }
        return mktsmsSendRecord;
    }

    public static boolean isNumeric(String str)
    {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}
