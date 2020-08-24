# personManage
一、spring循环依赖和三级缓存

循环依赖

        A类的属性是B类，B类的属性是A类。spring容器进行Bean扫描注册，
    给A类设置属性时，发现B类未创建，就去创建B类，然后B类设置属性时，
    发现A类又没有创建，由此造成了循环。
    spring为了解决Bean的循环依赖，引入了三级缓存。
    
三级缓存
    
        单个Bean进行初始化时，A类先创建原始对象（new A()），会将原始对象
    放在三级缓存中，供其他类注入使用。B类将A类的原始对象注入后，顺利
    完成创建，然后A类也能完成创建，最后A类的完整对象会放在单例池中，
    同时移除之前一级缓存中的A类初始对象。
        如果A类有AOP等其他操作时，会产生一个问题：A类后续会创建一个代理对象
    放在单例池中，但是B类之前注入的是A的原始对象，这就造成了A类对象在单例池中
    有多个的问题。为了解决这个问题，引入了第二层缓存earlySingletonObjects
    具体体现在：B类在注入A类原始对象时，会创建A类的代理对象放入二级缓存中
    同时删除一级缓存中的A类原始对象。
        最后回到A类在BeanPostProcessor步骤里，会获取二缓存自己的代理对象，
    并完成最终设置，然后放入一级缓存singletonObjects中并移除二级缓存种自己的代理对象。