package com.mark.myspring.framework.context.support;


import com.mark.myspring.framework.beans.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author: 帅气的Mark
 * @Description: Mark行行好，给点注释吧！
 * @Date: Create in 2018/4/23 14:59
 * @QQ: 85104982
 */
public class BeanDefinitionReader {
    private Properties config=new Properties();
    private List<String> registyBeanClasses=new ArrayList<String>();
    private final String SCAN_PACKAGE="scanPackage";

    public BeanDefinitionReader(String... configLocations) {
        InputStream is=this.getClass().getClassLoader().getResourceAsStream(configLocations[0].replaceAll("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String packageName) {
        URL url=this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.","/"));
        File classDir=new File(url.getFile());
        for (File file:classDir.listFiles()) {
            if (file.isDirectory()){
                doScanner(packageName+"."+file.getName());
            }else {
                registyBeanClasses.add(packageName+"."+file.getName().replace(".class",""));
            }
        }
    }

    public BeanDefinition registerBean(String className){
        if (this.registyBeanClasses.contains(className)){
            BeanDefinition beanDefinition=new BeanDefinition();
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.lastIndexOf(".")+1)));
            return beanDefinition;
        }
        return null;
    }

    private String lowerFirstCase(String str){
        char [] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public Properties getConfig(){return this.config;}

    public List<String> loadBeanDefinitions(){ return this.registyBeanClasses;}
}
