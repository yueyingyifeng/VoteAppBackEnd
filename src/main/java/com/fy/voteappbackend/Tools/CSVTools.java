package com.fy.voteappbackend.Tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.opencsv.CSVWriter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVTools {

    private static final String VoteItem_UPLOAD_PATH = "CSVUpload\\";    //图片存储路径
    private static final String CSVFILE_EXTName = "csv"; //CSV文件后缀

    /**
     * 存储投票选项到CSV文件中，并保存到指定位置
     * @param voteItem
     * @return
     */
    public static String saveVoteItem(@RequestBody String[] voteItem) throws IOException {

        for (String item : voteItem) {
            System.out.println(item);
        }

        //如果没有传入数据返回null
        if (voteItem.length == 0) {
            return null;
        }

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


        //将内容输入到文件
            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(Paths.get(uploadFile.getAbsolutePath()), StandardCharsets.UTF_8),
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


            return uploadFile.getAbsolutePath();

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
}
