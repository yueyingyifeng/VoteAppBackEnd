package com.fy.voteappbackend.Tools;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVTools {

    private static final String VoteItem_UPLOAD_PATH = "CSVUpload\\";    //图片存储路径
    private static final String CSVFILE_EXTName = "csv"; //CSV文件后缀

    /**
     * 创建CSV文件并返回CSV文件存储的绝对路径
     * @param
     * @return
     */
    public static String createVoteItemIntoCSV() throws IOException {

        //给文件定义一个唯一标识
        String id = IdUtil.fastSimpleUUID();
        String voteItemID = id + StrUtil.DOT + CSVFILE_EXTName;

        //创建文件对象
        File uploadFile = new File(VoteItem_UPLOAD_PATH + voteItemID);

        //如果CSVUpload目录不存在则创建一个
        if (!uploadFile.getParentFile().exists()) {
            if (uploadFile.getParentFile().mkdirs()){
                return null;
            }
        }

        //创建csv文件
        uploadFile.createNewFile();

        if (!uploadFile.exists()){
            System.out.println("创建失败");
            return null;
        }
            return uploadFile.getAbsolutePath();

    }

    /**
     * 存储投票选项到CSV文件中，并保存到指定位置
     * @param voteItem
     * @return
     */
    public static boolean SaveVoteItemIntoCSV(String[] voteItem,String dataPath) throws IOException {

        //如果没有传入数据返回null
        if (voteItem.length == 0) {
            return false;
        }

        //将内容输入到文件
        try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(Paths.get(dataPath), StandardCharsets.UTF_8),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.NO_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            //写数据到文件
            // 调用方法切分数组
            List<String[]> result = splitArray(voteItem, 3);

            // 打印结果
            for (String[] subArray : result) {
                writer.writeNext(subArray);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }

    /**
     * 读取存储在csv中的投票项数据
     * @param dataPath
     * @return
     * @throws IOException
     */
    public static String[][] readVoteItemFromCSV(String dataPath) throws IOException {

        //先将读取的数据存在List中然后再转化为Strings二维数组
        List<String[]> voteItemList = new ArrayList<>();

        //如果CSV文件不存在则返回null
        File file = new File(dataPath);
        if (!file.exists()) {
            return null;
        }

        try {
            //使用CSVReader读取数据
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            CSVReader csvReader = new CSVReader(new InputStreamReader(in, "UTF-8"));

            //循环遍历逐行读取
            String[] strs;
            int index = 0;
            while ((strs = csvReader.readNext()) != null) {
                voteItemList.add(strs);
            }

            //关闭读取流
            csvReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return voteItemList.toArray(new String[0][]);

    }
    /**
     * 将数组按每 chunkSize 个元素切分成多个子数组
     *
     * @param originalArray 原始数组
     * @param chunkSize     每个子数组的大小
     * @return 包含子数组的列表
     */
    public static List<String[]> splitArray(String[] originalArray, int chunkSize) {
        List<String[]> result = new ArrayList<>();
        int length = originalArray.length;

        // 遍历数组，按 chunkSize 切分
        for (int i = 0; i < length; i += chunkSize) {
            // 计算当前子数组的结束位置
            int end = Math.min(length, i + chunkSize);
            // 创建子数组
            String[] subArray = new String[end - i];
            // 复制元素到子数组
            System.arraycopy(originalArray, i, subArray, 0, subArray.length);
            // 将子数组添加到结果列表
            result.add(subArray);
        }

        return result;
    }

    /**
     * 将String[][]转化为String[]
     * @param twoDArray
     * @return
     */
    public static String[] convert2DTo1D(String[][] twoDArray) {
        // 计算一维数组的总长度
        int totalLength = 0;
        for (String[] array : twoDArray) {
            totalLength += array.length;
        }

        // 创建一维数组
        String[] oneDArray = new String[totalLength];

        // 将二维数组的元素复制到一维数组中
        int index = 0;
        for (String[] array : twoDArray) {
            for (String str : array) {
                oneDArray[index++] = str;
            }
        }

        return oneDArray;
    }
}
