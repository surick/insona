package com.jieweifu.common.gizwits;

import java.io.FileInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by 陶Lyn
 * on 2018/4/27.
 */
public class Setting {
    private static final Logger logger = LogManager.getLogger();
    private static volatile Properties properties = null;
    private static final String defaultFileName = "/application.properties";
    private static final String settingPropertyName = "application.propertiesFile";

    private static Properties getProperties() {
        if (properties == null) {
            synchronized (Setting.class) {
                if (properties == null) {
                    try {
                        Reader reader = null;
                        boolean found = false;

                        String propertiesFileName = System.getProperty(settingPropertyName);
                        if (propertiesFileName != null) {
                            File file = new File(propertiesFileName);
                            if (file.exists()) {
                                reader = new InputStreamReader(new FileInputStream(propertiesFileName), "UTF-8");
                                found = true;
                            }
                        }

                        if (!found) {
                            reader = new InputStreamReader(Setting.class.getResourceAsStream(defaultFileName), "UTF-8");
                        }

                        properties = new Properties();
                        properties.load(reader);
                    } catch (Exception e) {
                        logger.fatal("Load setting exception: {}", e.toString());
                    }
                }
            }
        }

        return properties;
    }

    public static String getValue(String key) {
        String result = null;

        result = System.getenv(key);

        if (result == null && getProperties() != null) {
            result = properties.getProperty(key);
        }

        return result;
    }


}
