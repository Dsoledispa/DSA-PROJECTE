package edu.upc.dsa.util.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JoinColumn {
    String name();
    String referencedColumnName();
}