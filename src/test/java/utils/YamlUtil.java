package utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlUtil {
    private static Map<String, Object> config;

    static {
        Yaml yaml = new Yaml();
        try (InputStream in = YamlUtil.class.getClassLoader().getResourceAsStream("mail-config.yml")) {
            if (in == null) {
                throw new RuntimeException("未能找到 mail-config.yml 配置文件");
            }
            config = yaml.load(in);
            if (config == null) {
                throw new RuntimeException("配置文件内容为空");
            }
        } catch (Exception e) {
            throw new RuntimeException("加载 mail-config.yml 配置失败", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMailConfig() {
        return (Map<String, Object>) config.get("mail");
    }
}