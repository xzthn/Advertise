package com.hs.advertise.utils;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.utils
 * ProjectName: trunk
 * Date: 2020/4/22 10:52
 */

import java.lang.reflect.Array;

/**

 * 专门针对原型数组及对象数组的操作类。

 */

public class ArrayUtils {



    /**

     * 不能修改的空对象数组。

     */

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**

     * 不能修改的空字符数组。

     */

    public static final char[] EMPTY_CHAR_ARRAY = new char[0];

    /**

     * 不能修改的空字符对象数组。

     */

    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];



    /**

     * 当在一个列表或数组里找不到元素时返回的索引值。

     */

    public static final int INDEX_NOT_FOUND = -1;



    /**

     * 创建数组。

     */

    public static <T> T[] toArray(final T... items) {

        // 这是JDK6.0的新特性。

        return items;

    }



    // 数组克隆。

    public static <T> T[] clone(final T[] array) {

        // 添加了对NULL的处理。

        if (array == null) {

            return null;

        }

        return array.clone();

    }



    /**

     * null转换为空数组。

     * 这是一个防止程序出现空指针错误的方法，建议在使用数组前都调用一次该方法。

     */

    public static Object[] nullToEmpty(final Object[] array) {

        // 添加了对NULL的处理。

        if (array == null || array.length == 0) {

            return EMPTY_OBJECT_ARRAY;

        }

        return array;

    }



    /**

     * 根据给定的起始索引和结束索引生成一个新的数组。

     * 用法如下：

     * Date[] someDates = (Date[])ArrayUtils.subarray(allDates, 2, 5);

     */

    public static <T> T[] subarray(final T[] array, int startIndexInclusive, int endIndexExclusive) {

        if (array == null) {

            return null;

        }

        if (startIndexInclusive < 0) {

            startIndexInclusive = 0;

        }

        if (endIndexExclusive > array.length) {

            endIndexExclusive = array.length;

        }

        final int newSize = endIndexExclusive - startIndexInclusive;

        final Class<?> type = array.getClass().getComponentType();

        if (newSize <= 0) {

            @SuppressWarnings("unchecked") // OK, because array is of type T

            final T[] emptyArray = (T[]) Array.newInstance(type, 0);

            return emptyArray;

        }

        @SuppressWarnings("unchecked") // OK, because array is of type T

        final

        T[] subarray = (T[]) Array.newInstance(type, newSize);

        // 使用了JDK的System.arraycopy()方法。

        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);

        return subarray;

    }





    /**

     * 获取指标数组的长度。null数组的长度为0.

     */

    public static int getLength(final Object array) {

        // 添加了对NULL的判断。

        if (array == null) {

            return 0;

        }

        return Array.getLength(array);

    }



    /**

     * 判断两个数组（不能为NULL）的类型是否一样。

     */

    public static boolean isSameType(final Object array1, final Object array2) {

        if (array1 == null || array2 == null) {

            throw new IllegalArgumentException("The Array must not be null");

        }

        return array1.getClass().getName().equals(array2.getClass().getName());

    }




    /**

     * 反转给定数组中给定范围的元素顺序。

     */

    public static void reverse(final boolean[] array, final int startIndexInclusive, final int endIndexExclusive) {

        if (array == null) {

            return;

        }

        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;

        int j = Math.min(array.length, endIndexExclusive) - 1;

        boolean tmp;

        while (j > i) {

            tmp = array[j];

            array[j] = array[i];

            array[i] = tmp;

            j--;

            i++;

        }

    }



    /**

     * 根据给定的对象来查找它在特定数组的索引位置。

     */

    public static int indexOf(final Object[] array, final Object objectToFind) {

        return indexOf(array, objectToFind, 0);

    }



    /**

     * 从给定的索引位置开始查找特定对象在特定数组中的索引位置。

     */

    public static int indexOf(final Object[] array, final Object objectToFind, int startIndex) {

        if (array == null) {

            return INDEX_NOT_FOUND;

        }

        if (startIndex < 0) {

            startIndex = 0;

        }

        if (objectToFind == null) {

            for (int i = startIndex; i < array.length; i++) {

                if (array[i] == null) {

                    return i;

                }

            }

        } else if (array.getClass().getComponentType().isInstance(objectToFind)) {

            for (int i = startIndex; i < array.length; i++) {

                if (objectToFind.equals(array[i])) {

                    return i;

                }

            }

        }

        return INDEX_NOT_FOUND;

    }



    /**

     * 根据给定的对象来查找它在特定数组中最后出现的索引位置。

     */

    public static int lastIndexOf(final Object[] array, final Object objectToFind) {

        return lastIndexOf(array, objectToFind, Integer.MAX_VALUE);

    }



    /**

     * 从给定的索引位置开始查找特定对象在特定数组中最后出现的索引位置。

     */

    public static int lastIndexOf(final Object[] array, final Object objectToFind, int startIndex) {

        if (array == null) {

            return INDEX_NOT_FOUND;

        }

        if (startIndex < 0) {

            return INDEX_NOT_FOUND;

        } else if (startIndex >= array.length) {

            startIndex = array.length - 1;

        }

        if (objectToFind == null) {

            for (int i = startIndex; i >= 0; i--) {

                if (array[i] == null) {

                    return i;

                }

            }

        } else if (array.getClass().getComponentType().isInstance(objectToFind)) {

            for (int i = startIndex; i >= 0; i--) {

                if (objectToFind.equals(array[i])) {

                    return i;

                }

            }

        }

        return INDEX_NOT_FOUND;

    }



    /**

     * 判断某个对象是否存在于特定的数组里。

     */

    public static boolean contains(final Object[] array, final Object objectToFind) {

        return indexOf(array, objectToFind) != INDEX_NOT_FOUND;

    }





    public static char[] toPrimitive(final Character[] array) {

        if (array == null) {

            return null;

        } else if (array.length == 0) {

            return EMPTY_CHAR_ARRAY;

        }

        final char[] result = new char[array.length];

        for (int i = 0; i < array.length; i++) {

            result[i] = array[i].charValue();

        }

        return result;

    }



    /**

     * 将对象数组转换成原型数组。对于NULL元素可以给定替换字符。

     */

    public static char[] toPrimitive(final Character[] array, final char valueForNull) {

        if (array == null) {

            return null;

        } else if (array.length == 0) {

            return EMPTY_CHAR_ARRAY;

        }

        final char[] result = new char[array.length];

        for (int i = 0; i < array.length; i++) {

            final Character b = array[i];

            result[i] = (b == null ? valueForNull : b.charValue());

        }

        return result;

    }



    /**

     * 将原型数组转换成对象数组。

     */

    public static Character[] toObject(final char[] array) {

        if (array == null) {

            return null;

        } else if (array.length == 0) {

            return EMPTY_CHARACTER_OBJECT_ARRAY;

        }

        final Character[] result = new Character[array.length];

        for (int i = 0; i < array.length; i++) {

            result[i] = Character.valueOf(array[i]);

        }

        return result;

    }



    /**

     * 判断一个对象数组是否为NULL或为空（不存在元素）。

     */

    public static boolean isEmpty(final Object[] array) {

        return array == null || array.length == 0;

    }



    /**

     * 判断一个数组是否不为NULL或不为空（存在元素）。

     */

    public static <T> boolean isNotEmpty(final T[] array) {

        return (array != null && array.length != 0);

    }



    /**

     * 合并给定数组的所有元素到一个新的数组。

     * 主要是使用了System.arraycopy()方法。

     */

    public static <T> T[] addAll(final T[] array1, final T... array2) {

        if (array1 == null) {

            return clone(array2);

        } else if (array2 == null) {

            return clone(array1);

        }

        final Class<?> type1 = array1.getClass().getComponentType();

        @SuppressWarnings("unchecked") // OK, because array is of type T

        final

        T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);

        System.arraycopy(array1, 0, joinedArray, 0, array1.length);

        try {

            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);

        } catch (final ArrayStoreException ase) {

            // Check if problem was due to incompatible types

            /*

             * We do this here, rather than before the copy because:

             * - it would be a wasted check most of the time

             * - safer, in case check turns out to be too strict

             */

            final Class<?> type2 = array2.getClass().getComponentType();

            if (!type1.isAssignableFrom(type2)){

                throw new IllegalArgumentException("Cannot store "+type2.getName()+" in an array of "

                        +type1.getName(), ase);

            }

            throw ase; // No, so rethrow original

        }

        return joinedArray;

    }



    /**

     * 拷贝给定的数组并添加给定元素到新数组的后面。

     */

    public static <T> T[] add(final T[] array, final T element) {

        Class<?> type;

        if (array != null){

            type = array.getClass();

        } else if (element != null) {

            type = element.getClass();

        } else {

            throw new IllegalArgumentException("Arguments cannot both be null");

        }

        @SuppressWarnings("unchecked") // type must be T

        final

        T[] newArray = (T[]) copyArrayGrow1(array, type);

        newArray[newArray.length - 1] = element;

        return newArray;

    }



    /**

     * 拷贝一个数组并将其长度加1。

     */

    private static Object copyArrayGrow1(final Object array, final Class<?> newArrayComponentType) {

        if (array != null) {

            final int arrayLength = Array.getLength(array);

            final Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);

            System.arraycopy(array, 0, newArray, 0, arrayLength);

            return newArray;

        }

        return Array.newInstance(newArrayComponentType, 1);

    }




    /**

     * 从给定的数组里删除给定索引位置的元素。

     */

    @SuppressWarnings("unchecked") // remove() always creates an array of the same type as its input

    public static <T> T[] remove(final T[] array, final int index) {

        return (T[]) remove((Object) array, index);

    }



    /**

     * 从给定的数组里删除第一次出现的给定的元素。

     */

    public static <T> T[] removeElement(final T[] array, final Object element) {

        final int index = indexOf(array, element);

        if (index == INDEX_NOT_FOUND) {

            return clone(array);

        }

        return remove(array, index);

    }



    /**

     * 从给定的数组里删除给定索引位置的元素。

     */

    private static Object remove(final Object array, final int index) {

        final int length = getLength(array);

        if (index < 0 || index >= length) {

            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);

        }



        final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);

        System.arraycopy(array, 0, result, 0, index);

        if (index < length - 1) {

            System.arraycopy(array, index + 1, result, index, length - index - 1);

        }

        return result;

    }








}

