package com.mark.myspring.framework.context;

import com.mark.myspring.demo.controller.TestController;
import com.mark.myspring.demo.service.IQueryService;
import com.mark.myspring.framework.annotation.Autowired;
import com.mark.myspring.framework.annotation.Controller;
import com.mark.myspring.framework.annotation.Service;
import com.mark.myspring.framework.beans.BeanDefinition;
import com.mark.myspring.framework.beans.BeanPostProcessor;
import com.mark.myspring.framework.beans.BeanWrapper;
import com.mark.myspring.framework.context.support.BeanDefinitionReader;
import com.mark.myspring.framework.core.BeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: 帅气的Mark
 * @Description: Mark行行好，给点注释吧！
 * @Date: Create in 2018/4/23 14:55
 * @QQ: 85104982
 */
public class ApplicationContext implements BeanFactory {
    private String[] configLocations;
    private BeanDefinitionReader reader;
    //保存配置信息
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
    //注册式单例，保存缓存的bean
    private Map<String, Object> beanCacheMap = new ConcurrentHashMap<String, Object>();
    //存储被代理的对象
    private Map<String, BeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, BeanWrapper>();

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    private void refresh() {
        this.reader = new BeanDefinitionReader(configLocations);
        List<String> beanDefinitions = reader.loadBeanDefinitions();
        doRegisty(beanDefinitions);
        doAutowired();

        TestController controller= (TestController) this.getbean("testController");
        controller.query("Mark");
    }

    private void doAutowired() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanName =beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()){
                getbean(beanName);
            }
        }
        for (Map.Entry<String, BeanWrapper> beanWrapperEntry : beanWrapperMap.entrySet()) {
            populateBean(beanWrapperEntry.getKey(),beanWrapperEntry.getValue().getWrappedInstance());
        }
    }

    private void populateBean(String beanName, Object instance) {
        Class<?> clazz=instance.getClass();

        if(!(clazz.isAnnotationPresent(Controller.class) ||
                clazz.isAnnotationPresent(Service.class))){
            return;
        }
        Field[] fields=clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)){continue;}
            Autowired autowired=field.getAnnotation(Autowired.class);
            String autowiredBeanName=autowired.value().trim();
            if ("".equals(autowiredBeanName)){
                autowiredBeanName=field.getType().getName();
            }
            field.setAccessible(true);
            try {
                field.set(instance,this.beanWrapperMap.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void doRegisty(List<String> beanDefinitions) {
        try {
            for (String className : beanDefinitions) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()){continue;}
                BeanDefinition beanDefinition=reader.registerBean(className);
                if (null != beanDefinition){
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
                }
                Class<?>[] interfaces=beanClass.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    this.beanDefinitionMap.put(anInterface.getName(),beanDefinition);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Object getbean(String beanName) {
        BeanDefinition beanDefinition=this.beanDefinitionMap.get(beanName);
        String className=beanDefinition.getBeanClassName();
        BeanPostProcessor beanPostProcessor=new BeanPostProcessor();
        Object instance = instantionBean(beanDefinition);
        if(null == instance){ return  null;}
        beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
        BeanWrapper beanWrapper=new BeanWrapper(instance);
        beanWrapper.setPostProcessor(beanPostProcessor);
        this.beanWrapperMap.put(beanName,beanWrapper);
        beanPostProcessor.postProcessAfterInitialization(instance,beanName);
        return this.beanWrapperMap.get(beanName).getWrappedInstance();
    }

    private Object instantionBean(BeanDefinition beanDefinition) {
        Object instance=null;
        String className=beanDefinition.getBeanClassName();
        if (this.beanCacheMap.containsKey(className)){
            instance=this.beanCacheMap.get(className);
        }else {
            try {
                Class<?> clazz=Class.forName(className);
                instance=clazz.newInstance();
                this.beanCacheMap.put(className,instance);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
