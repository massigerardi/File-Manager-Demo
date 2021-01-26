package com.example.FileManager.access;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasAccessToFile {
    String id();
    String role();
}
