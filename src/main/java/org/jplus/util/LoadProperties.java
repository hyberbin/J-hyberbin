package org.jplus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import org.jplus.hyb.log.Logger;
import org.jplus.hyb.log.LoggerManager;

/**
 * 读取配置文件
 *
 * @version 1.0
 * @author hyberbin
 */
public class LoadProperties {

    private static final Logger log = LoggerManager.getLogger(LoadProperties.class);
    /** Properties对象 */
    private final Properties props = new SafeProperties();
    private String filePath;

    /**
     * 构造方法
     * @param filePath
     */
    public LoadProperties(String filePath) {
        InputStream in = null;
        try {
            if (!filePath.replace("\\", "/").contains("/")) {
                filePath = filePath.replace(".", "/");
                String typeString = filePath.substring(filePath.lastIndexOf("/")).replace("/", ".");
                filePath = filePath.substring(0, filePath.lastIndexOf("/")) + typeString;
                this.filePath = filePath;
                log.debug(filePath);
                in = this.getClass().getClassLoader().getResource(filePath).openStream();
            } else {
                this.filePath = java.net.URLDecoder.decode(filePath, "utf-8");
                log.debug(this.filePath);
                File f = new File(filePath);
                this.filePath = f.getAbsolutePath();
                in = new FileInputStream(this.filePath);
            }
            props.load(in);
        } catch (IOException ex) {
            log.error("LoadProperties找不到数据库配置文件，请检查缺省包下面的" + filePath + "文件!", ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    log.error("LoadProperties找不到数据库配置文件，请检查缺省包下面的" + filePath + "文件!", ex);
                }
            }
        }
    }

    public LoadProperties(InputStream in) {
        try {
            props.load(in);
        } catch (IOException ex) {
            log.error("LoadProperties找不到配置文件", ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    log.error("LoadProperties找不到数据库配置文件", ex);
                }
            }
        }
    }

    /**
     * 自动读取整个配置文件并存入一个对象
     *
     * @param object
     * @return
     */
    public Object loadProperties(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                String name = field.getName();
                String pValue = props.getProperty(field.getName());
                if (pValue == null) {
                    continue;
                }
                Object value = ConverString.asType(field.getType(), pValue);
                FieldUtil.setFieldValue(object, name, value);
            }
        } catch (Exception ex) {
            log.error("LoadProperties读取配置文件错误\t", ex);
        }
        return object;
    }

    public Properties getProps() {
        return props;
    }

    public void store() {
        try {
            OutputStream fos = new FileOutputStream(filePath);
            props.store(fos, "");
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            log.error("LoadProperties写入" + filePath + "文件失败\t", ex);
        }

    }

    public String getProperty(String name) {
        Object get = props.get(name);
        return get == null ? null : get.toString();
    }

    public int getIntProperties(String name) {
        return Integer.parseInt(getProperty(name));
    }

    public boolean getBoolProperties(String name) {
        return Boolean.TRUE.equals(props.get(name)) || "true".equals(getProperty(name));
    }
}
