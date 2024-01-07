package com.notice.test.helper;

import java.lang.reflect.Field;

/**
 * <pre>
 *     리플레션 편의 클래스
 * </pre>
 */
public class ReflectionHelper {

    /**
     * <pre>
     *     객체 중 private, private final로 선언된 변수의 값을 임의로 설정하기 위한 메서드
     * </pre>
     *
     * @param object
     * @param var
     * @param value
     */
    public static void reflection(Object object, String var, Object value) {

        try {
            Field field = object.getClass().getDeclaredField(var);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
