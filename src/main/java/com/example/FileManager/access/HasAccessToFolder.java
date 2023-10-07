package com.example.FileManager.access;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasAccessToFolder {
    String id();
    String role();
}
