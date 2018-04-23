package com.mark.myspring.framework.beans;

import com.mark.myspring.framework.core.FactoryBean;

/**
 * @Author: 帅气的Mark
 * @Description: Mark行行好，给点注释吧！
 * @Date: Create in 2018/4/23 15:06
 * @QQ: 85104982
 */
public class BeanWrapper extends FactoryBean{
    private BeanPostProcessor postProcessor;
    private Object wrapperInstance;
    private Object originalInstance;

    public BeanWrapper(Object instance) {
        this.wrapperInstance = instance;
        this.originalInstance = instance;
    }

    public BeanPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(BeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public Object getWrappedInstance(){
        return this.wrapperInstance;
    }

    public Class<?> getWrappedClass(){
        return this.wrapperInstance.getClass();
    }
}
