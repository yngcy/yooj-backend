package com.yocy.common.web.constraint;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */
public class CityValidator implements ConstraintValidator<CheckCityValid, String> {

    private CityType type;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public void initialize(CheckCityValid annotation) {
        this.type = annotation.value();
    }

    @SneakyThrows
    @Override
    public boolean isValid(String inputValue, ConstraintValidatorContext context) {
        if (inputValue == null) {
            return false;
        }
        // 从缓存中获取城市常量
        String jsonStr = redisTemplate.opsForValue().get("constant:city");
        // 如果无缓存，则加载文件（json）到缓存中
        if (!StringUtils.hasText(jsonStr)) {
            ClassPathResource resource = new ClassPathResource("city.json");
            InputStream inputStream = resource.getInputStream();
            String line = IoUtil.readUtf8(inputStream);
            redisTemplate.opsForValue().set("constant:city", line);
            jsonStr = line;

        }
        List<CityEntity> cityJson = JSONUtil.toList(jsonStr, CityEntity.class);
        CityEntity entity;
        switch (type) {
            case PROVINCE: {
                entity = cityJson.stream().filter(item -> inputValue.equals(item.getName()) && item.getParent() == null)
                        .findFirst()
                        .orElse(null);
                break;
            }
            default:
            case CITY: {
                entity = cityJson.stream().filter(item -> {
                    // 找出名字相符且有父节点的entity
                    if (inputValue.equals(item.getName()) && item.getParent() != null) {
                        // 找出没有父节点的父节点
                        CityEntity parentEntity = cityJson.stream()
                                .filter(parent -> item.getParent().equals(parent.getValue()) && parent.getParent() == null)
                                .findFirst().orElse(null);
                        return parentEntity != null;
                    }
                    return false;
                }).findFirst().orElse(null);
                break;
            }
            case AREA: {
                entity = cityJson.stream().filter(item -> {
                    // 找出名字相符且有父节点的entity
                    if (inputValue.equals(item.getName()) && item.getParent() != null) {
                        // 找出有父节点的父节点
                        CityEntity parentEntity = cityJson.stream()
                                .filter(parent -> item.getParent().equals(parent.getValue()) && parent.getParent() != null)
                                .findFirst().orElse(null);
                        return parentEntity != null;
                    }
                    return false;
                }).findFirst().orElse(null);
                break;
            }
        }
        return entity != null;
    }
}
