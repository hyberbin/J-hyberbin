package org.jplus.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;

/**
 * FileUtil说明.
 * @author Hyberbin
 * @date 2013-6-14 14:09:54
 */
public class FileUtil {

    private static final Logger log = LoggerManager.getLogger(FileUtil.class);
    /** 文件路径 */
    private final String path;
    private PrintWriter printWriter;
    /** 文件阅读器 */
    private BufferedReader reader;
    private FileWriter fileWriter;
    /** 文件 */
    private final File file;

    public FileUtil(String path) {
        this.path = path;
        file = new File(path);
    }

    /**
     * 将指定内容的字符串以指定格式写入指定路径的文件
     * @param message 文件内容
     * @param coding 编码格式
     * @return
     */
    public boolean write(String message, String coding) {
        boolean b = true;
        try {
            printWriter = new PrintWriter(file, coding);
            if (message != null) {
                printWriter.println(message);
            }
        } catch (FileNotFoundException e) {
            b = false;
            log.error("写入文件出现异常!", e);
        } catch (UnsupportedEncodingException e) {
            b = false;
            log.error("文件编码格式不对!", e);
        } finally {
            close();
        }
        return b;
    }

    /**
     * 可以按行读取文件
     * @return
     * @throws FileNotFoundException
     */
    public BufferedReader getFileReader() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(file));
        return reader;
    }

    /**
     * 将文件末尾加入内容
     * @param content
     * @throws IOException
     */
    public void append(String content) throws IOException {
        if (fileWriter == null) {
            fileWriter = new FileWriter(path, true);
        }
        fileWriter.write(content);
    }

    public void close() {
        if (printWriter != null) {
            printWriter.close();//  关闭 
            printWriter = null;
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                reader = null;
                log.error("关闭文件出现异常!", ex);
            }
        }
        if (fileWriter != null) {
            try {
                fileWriter.close();
                fileWriter = null;
            } catch (IOException ex) {
                log.error("关闭文件出现异常!", ex);
            }
        }
    }

    /**
     * 以字符串形式读取文件内容
     * @return
     */
    public String getContent() {
        try {
            BufferedReader fileReader = getFileReader();
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = fileReader.readLine()) != null) {
                builder.append(line.trim()).append("\n");
            }
            return builder.toString();
        } catch (IOException ex) {
            log.error("读取文件出现异常!", ex);
        }
        return null;
    }

    /**
     * 获得文件对象
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean copyFile(String oldPath, String newPath) {
        boolean result = true;
        try {
            int byteread;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {  //文件存在时
                InputStream inStream = new FileInputStream(oldPath);  //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (IOException e) {
            result = false;
            log.error("复制单个文件操作出错!", e);
        }
        return result;

    }

    /**
     * 删除文件
     * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
     *
     * @return boolean
     */
    public static boolean delFile(String filePathAndName) {
        boolean result;
        try {
            java.io.File myDelFile = new java.io.File(filePathAndName);
            myDelFile.delete();
            result = true;
        } catch (Exception e) {
            log.error("删除文件操作出错!", e);
            result = false;
        }
        return result;
    }

    /**
     * 删除文件夹
     * @param folderPath String 文件夹路径及名称 如c:/fqf
     * @return
     */
    public static boolean delFolder(String folderPath) {
        boolean result;
        try {
            delAllFile(folderPath);  //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete();  //删除空文件夹
            result = true;
        } catch (Exception e) {
            result = false;
            log.error("删除文件夹操作出错!", e);
        }
        return result;
    }

    /**
     * 显示文件列表
     * @param path
     * @return
     */
    public static String[] showFileList(String path) {
        if (path == null) {//无参数时为项目文件夹下的所有文件
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        if (!file.isDirectory()) {
            return null;
        }
        return file.list();
    }

    public static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 显示文件列表
     * @param path 路径
     * @param type 扩展名
     * @return
     */
    public static String[] showFileList(String path, final String type) {
        if (path == null) {//无参数时为项目文件夹下的所有文件
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        if (!file.isDirectory()) {
            return null;

        }
        class Filter implements FilenameFilter {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("." + type);
            }
        }
        FilenameFilter filter = new Filter();
        return file.list(filter);
    }

    /** */
    /**
     * 删除文件夹里面的所有文件
     * @param path String 文件夹路径 如 c:/fqf
     * @return
     */
    public static boolean delAllFile(String path) {
        File file = new File(path);
        boolean result = true;
        if (!file.exists()) {
            return result;
        }
        if (!file.isDirectory()) {
            return result;
        }
        String[] tempList = file.list();
        File temp;
        for (String tempList1 : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList1);
            } else {
                temp = new File(path + File.separator + tempList1);
            }
            if (result && temp.isFile()) {
                result = temp.delete();
            } else if (result && temp.isDirectory()) {
                delAllFile(path + "/" + tempList1);
                result = delFolder(path + "/" + tempList1);
            }
            if (!result) {
                return result;
            }
        }
        return result;
    }

    /**
     *
     * @param folderPath
     * @return
     */
    public static boolean newFolder(String folderPath) {
        File myFilePath = new File(folderPath);
        boolean result = true;
        try {
            if (!myFilePath.isDirectory()) {
                myFilePath.mkdirs();
            }
        } catch (Exception e) {
            result = false;
            log.error("新建目录操作出错!", e);
        }
        return result;
    }
}
