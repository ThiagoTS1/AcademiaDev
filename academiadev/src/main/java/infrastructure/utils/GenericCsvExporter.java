package infrastructure.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class GenericCsvExporter {
    
    public static <T> String exportToCsv(List<T> data, List<String> selectedColumns) {
        if (data == null || data.isEmpty()) {
            return "";
        }

        Class<?> clazz = data.get(0).getClass();

        StringBuilder csv = new StringBuilder();
        
        csv.append(String.join(",", selectedColumns)).append("\n");
        
        for (T item : data) {
            List<String> values = selectedColumns.stream()
                .map(columnName -> getFieldValue(item, columnName))
                .collect(Collectors.toList());
            csv.append(String.join(",", values)).append("\n");
        }
        
        return csv.toString();
    }

    private static <T> String getFieldValue(T item, String columnName) {
        try {
            String getterName = "get" + capitalize(columnName);
            Method getter = findGetterMethod(item.getClass(), getterName, columnName);
            if (getter != null) {
                Object value = getter.invoke(item);
                return value != null ? escapeCsvValue(value.toString()) : "";
            }
            
            Field field = findField(item.getClass(), columnName);
            if (field != null) {
                field.setAccessible(true);
                Object value = field.get(item);
                return value != null ? escapeCsvValue(value.toString()) : "";
            }
        } catch (Exception e) {
        }
        return "";
    }

    private static Method findGetterMethod(Class<?> clazz, String getterName, String fieldName) {
        try {
            return clazz.getMethod(getterName);
        } catch (NoSuchMethodException e) {
            if (fieldName.startsWith("is") || fieldName.startsWith("has")) {
                try {
                    return clazz.getMethod(fieldName);
                } catch (NoSuchMethodException ex) {
                    try {
                        return clazz.getMethod("is" + capitalize(fieldName));
                    } catch (NoSuchMethodException exc) {
                        return null;
                    }
                }
            }
            return null;
        }
    }

    private static List<Field> getFieldsForColumns(Class<?> clazz, List<String> selectedColumns) {
        return selectedColumns.stream()
            .map(columnName -> {
                try {
                    Field field = findField(clazz, columnName);
                    if (field != null) {
                        field.setAccessible(true);
                    }
                    return field;
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(field -> field != null)
            .toList();
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            String getterName = "get" + capitalize(fieldName);
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (Exception ex) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.getName().equalsIgnoreCase(fieldName)) {
                        return field;
                    }
                }
            }
        }
        return null;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static String escapeCsvValue(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

