package com.uuc.alarm.util;

import com.uuc.common.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description 公共工具类
 * @author llb
 * @since 2022/7/29 14:45
 */
public class CommonUtil {
    public static final String SPACE = "\\s+";

    private CommonUtil() {

    }

    public static List<Integer> convertStringToIntegerList(String src) {
        if (src == null || StringUtils.isEmpty(src.replaceAll(SPACE, ""))) {
            return new ArrayList<>();
        }
        return Stream.of(src.replaceAll(SPACE, "").split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    public static Set<Integer> convertStringToIntegerSet(String src) {
        if (src == null || StringUtils.isEmpty(src.replaceAll(SPACE, ""))) {
            return new HashSet<>();
        }
        return Stream.of(src.replaceAll(SPACE, "").split(",")).map(Integer::parseInt).collect(Collectors.toSet());
    }

    public static List<Long> convertStringToLongList(String src) {
        if (src == null || StringUtils.isEmpty(src.replaceAll(SPACE, ""))) {
            return new ArrayList<>();
        }
        return Stream.of(src.replaceAll(SPACE, "").split(",")).map(Long::parseLong).collect(Collectors.toList());
    }

    public static Set<Long> convertStringToLongSet(String src) {
        if (src == null || StringUtils.isEmpty(src.replaceAll(SPACE, ""))) {
            return new HashSet<>();
        }
        return Stream.of(src.replaceAll(SPACE, "").split(",")).map(Long::parseLong).collect(Collectors.toSet());
    }

    public static Integer[] convertStringToIntegerArray(String src) {
        if (src == null || StringUtils.isEmpty(src.replaceAll(SPACE, ""))) {
            return new Integer[]{};
        }
        return (Integer[]) Stream.of(src.replaceAll(SPACE, "").split(",")).map(Integer::parseInt).toArray();
    }
}
