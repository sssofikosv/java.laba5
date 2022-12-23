package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;


public class Injector {
    private static Properties prop= new Properties (  );

    /**
     * This method is working with an object
     * We create an object of class Field to get all the declared fields of class
     * Then using function getAnnotation() we finding out which fields contain annotation (AutoInjectable)
     * With function setAccessible we make possible working with these fields
     * Then by function getType we get "class className", transform it to string and split by two parts
     * Then we pick the class name
     * then by function getProperty we extract a property with needed class name
     * then we put the needed value into the needed field
     *
     */
    public <T> T inject(T object) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, NullPointerException {
        loading ();
        Field[] field = object.getClass ().getDeclaredFields ();
        for (Field f:field
        ) {
            if(f.getAnnotation ( AutoInjectable.class )!=null){
                f.setAccessible ( true );
                String fieldClassname=f.getType ().toString ().split ( " " )[1];
                String injectedClassName = prop.getProperty ( fieldClassname );
                try {
                    Object value = Class.forName(injectedClassName).newInstance();
                    f.set (object, value);
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Exception");
                };

            }
        }
        return object;
    }

    /**
     * We load from needed file all the properties
     */
    private void loading() throws IOException {
        FileInputStream inputStream= new FileInputStream ( "src/main/resources/data.properties" );
        prop.load ( inputStream );

    }
}
