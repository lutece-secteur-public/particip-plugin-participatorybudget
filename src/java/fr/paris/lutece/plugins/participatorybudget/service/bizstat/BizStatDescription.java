package fr.paris.lutece.plugins.participatorybudget.service.bizstat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)

@Target   (ElementType.METHOD)

public @interface BizStatDescription {
    public String value();
}
