package com.uuc.auth.api.test;

import com.uuc.auth.api.enums.GrantTypeEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author deng
 * @date 2022/7/14 0014
 * @description
 */
public class TestDemo {
    public static void main(String[] args) {
        GrantTypeEnum[] values = GrantTypeEnum.values();
        Optional<GrantTypeEnum> first = Arrays.stream(values).findFirst();
        System.out.println(values.toString());
    }
}
