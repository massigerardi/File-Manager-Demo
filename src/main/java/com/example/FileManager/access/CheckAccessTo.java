package com.example.FileManager.access;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

@Aspect
@Component
public class CheckAccessTo {

    @Before("@annotation(HasAccessToFile)")
    public void checkFile(JoinPoint point) throws NoSuchMethodException {
        Method method = Arrays.stream(point.getTarget().getClass().getMethods()).filter(m -> m.getName().equals(point.getSignature().getName())).findFirst().get();
        HasAccessToFile annotation = method.getAnnotation(HasAccessToFile.class);
        List<String> parameters = Arrays.stream(method.getParameters()).map(p -> p.getName()).collect(Collectors.toList());
        int index = parameters.indexOf(annotation.id());
        UUID resourceId = (UUID) point.getArgs()[index];
        //get user from security context
        //get resource permissions from repository
        //check permissions: can user access resource with id = {resourceId}
    }

    @Before("@annotation(HasAccessToFolder)")
    public void checkFolder(JoinPoint point) throws NoSuchMethodException {
        Method method = Arrays.stream(point.getTarget().getClass().getMethods()).filter(m -> m.getName().equals(point.getSignature().getName())).findFirst().get();
        HasAccessToFolder annotation = method.getAnnotation(HasAccessToFolder.class);
        List<String> parameters = Arrays.stream(method.getParameters()).map(p -> p.getName()).collect(Collectors.toList());
        int index = parameters.indexOf(annotation.id());
        UUID resourceId = (UUID) point.getArgs()[index];
        //get user from security context
        //get resource permissions from repository
        //check permissions: can user access resource with id = {resourceId}
    }



}
