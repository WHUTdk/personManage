hashMap的数组大小为什么是2的幂次方？

    hashMap数组下标的计算规则：(tab.length - 1) & hash
    为了减少哈希碰撞使数据分布更均匀，一般会使用取模运算hash%tab.length，但是位运算比取模运算效率高，只有当长度为2的幂次方时，
    才有(tab.length - 1) & hash = hash%tab.length的情况，即数组长度是2的幂次方时，与运算可以替代取模运算。
    同时使用2的幂次方数，与运算的结果更加均匀（2的n次方-1，转为二进制后，有n个1，1和0/1进行与运算结果更平均），减少hash碰撞。
          
JDK1.7的hashMap链表头插法死循环问题

    假设map数组中有个A->B的链表（A是头节点指向B），线程1插入元素后数组进行扩容，创建一个新数组，旧数据还未迁移，
    线程2开始执行，进行数据迁移操作，先插入A元素，然后插入B元素，因为是头插法，此时新数据中，B为头节点指向A。
    线程2执行完后，线程1开始执行，此时存在A指向B，B又指向A的情况，造成环形链表
    如果后续对该位置进行get操作，遍历链表会进入死循环

HashMap链表转红黑树的阈值为什么是8？
    
    源码注释中介绍，根据泊松分布，链表长度超过8的机率很小
    链表长度达到8时，其实先会去判断当前数组长度是否小于64，小于的话会扩容数组，不小于64才会转为红黑树
    
hashMap红黑树转链表的阈值为什么是6？

    防止临界时频繁增删元素，造成链表和红黑树的频繁转换
    
hashMap加载因子为什么是0.75？

    大量统计计算，空间与时间的结合
    
HashMap并发问题

    1.7并发扩容时，造成链表死循环
    1.7数据丢失
    1.8会产生数据丢失
        数据丢失：比如多线程同时执行p.next = newNode(hash, key, value, null);两个线程都获取到p.next，执行完后，有一个数据会被丢失

hashMap的put()方法流程

    1、(tab.length - 1) & hash计算数据下标位置
    2、如果该位置为空，就将key-value封装为Entry/Node对象（1.7Entry对象，1.8Node对象）,添加到空位置中
    3、如果该位置不为空
        1.7 先判断数组是否需要扩容，需要就扩容，不需要，就将Entry对象通过头插法插入链表中
        1.8 先判断当前位置Node的类型，是红黑树还是链表
            如果是红黑树，将k-v封装为Node对象添加到红黑树中，如果key已存在，则更新value
            如果是链表，将k-v封装为Node对象遍历链表，通过尾插法插入链表尾部，遍历中如果key已存在，则更新value
                链表尾部插入元素后，判断链表长度，如果超过8，就转为红黑树
            插入元素到红黑树或链表后，再判断是否需要扩容    

HashMap扩容机制

    扩容时机：
        1、插入元素时，先判断数组长度是否大于64，不大于的话就扩容
        2、map元素个数达到阈值（数组长度*加载因子）
    
concurrentHashMap

    1.7
    1.8
        初始化使用cas
        put时数组元素为空，使用cas自旋插入，数组元素不为空，使用synchronied锁住头节点

concurrentHashMap扩容机制：

    1.7
        1、基于segment分段机制实现，每个segment相当于一个小的hashMap
        2、每个segment内部会进行扩容，和hashMap的扩容机制类似
        3、先生成新的数组，然后转移数据到新数组中
        4、扩容的判断也是每个segment单独判断，判断是否超过阈值
    1.8    
        1、某个线程put时，发现正在扩容，那么该线程会参与一起进行扩容
        2、put时，没有在扩容，先将数据添加到map中，再判断是否超过阈值，超过就进行扩容
        3、扩容之前也是生成一个新数组
        4、转移元素时，先将原数组分组，将每组分给不同的线程进行元素转移，每个线程负责一组或多组元素的转移工作
        5、元素转移插入时，1.8进行了优化，新下标位置只分为两种，原数组下标或原数组下标+原数组长度
    
谈谈对spring的理解：

    spring是一个框架，帮我们起到了一个IOC容器的作用，用来承载我们整体的Bean对象，他帮我们进行了整个对象从创建到销毁的整个生命周期的管理。
    我们在使用spring的时候，可以使用配置文件，也可以使用注解的方式来进行相关实现。启动spring应用时，spring会将我们定义的bean对象转换为
    beanDefinetion，然后完成beandefinition的解析和加载过程，之后再对bean进行实例化和一系列初始化操作，包括aware、beanPostProcessor等接口实现，
    还有AOP等操作。整个对象完成初始化后，就会放入单例池，容器能直接使用对象了。

spring bean生命周期

    1、创建BeanFactory容器
    2.扫描加载配置文件或配置类，得到BeanDefinition(BeanClass、scope、dependOn、initMethodName、propertyValues...)
    3.BeanFactoryPostProcessor(处理BeanDefinition，可以获取相关属性，并且可以设置修改BeanClass)
    4.实例化Bean对象 new Object()
    5.设置对象属性
    6.检查是否实现Aware接口，实现Aware接口的Bean能获取自身相关属性或获取容器相关属性（BeanName、BeanFactory等）
    7.执行BeanPostProcessor的before方法：postProcessBeforeInitialization
    8.检查是否实现InitializingBean接口，以决定是否调用afterPropertiesSet方法
    9.检查是否配置自定义的init-method方法
    10.执行BeanPostProcessor的after方法：postProcessAfterInitialization
    11.注册必要的destructionCallback相关回调接口
    12.使用中...
    13.检查是否实现DisposableBean接口，执行destroy方法
    14.检查是否配置自定义销毁方法，有的话执行

@Import、@Component、@Bean区别
    
    @Component:只能用在类上，自动扫描配置Bean，spring管理生命周期，无法自定义配置第三方类
    @Bean:显示声明和配置Bean，能够自定义创建和配置bean（自定义处理第三方类），一般和@Configuration搭配使用
    @Import:只能用在类上，一般有下列三种用法：
        1.生成指定类的BeanDefinition
        2.实现ImportSelector接口，在实现方法中，生成指定Bean的BeanDefinition
        3.实现ImportBeanDefinitionRegistrar接口，生成指定Bean的BeanDefinition

BeanFactory、ApplicationContext、FactoryBean区别
    
    三个都是接口
    BeanFactory:spring底层的简单接口，提供基础方法，比如getBean()
    ApplicationContext:继承了BeanFactory和一些其他接口，spring的高级容器，对应实现的接口提供的功能更多
    FactoryBean:实现该接口可定制Bean到spring容器中。内部getObeject()方法返回的是自定义对象，如果要返回FactoryBean对象，使用&BeanName

spring循环依赖和三级缓存

循环依赖

        A类的属性是B类，B类的属性是A类。spring容器进行Bean扫描注册，
    给A类设置属性时，发现B类未创建，就去创建B类，然后B类设置属性时，
    发现A类又没有创建，由此造成了循环。
    spring为了解决Bean的循环依赖，引入了三级缓存。
    
三级缓存
    
    第一级缓存：singletonObjects = new ConcurrentHashMap() spring的单例池，存放初始化完成的对象
    第二级缓存：earlySingletonObjects = new HashMap() 发生循环依赖时，会放入代理对象
    第三级缓存：singletonFactories = new HashMap() 单例工厂，存放构造器生成的原始对象
        单个Bean进行初始化时，A类先创建原始对象（new A()），会将原始对象
    放在第三级缓存中，供其他类注入使用。B类将A类的原始对象注入后，顺利
    完成创建，然后A类也能完成创建，最后A类的完整对象会放在单例池中。
        按照上面流程，其实二级缓存就已经能够解决循环依赖了。但spring中的有些Bean
    需要AOP生成代理对象，那就不能使用原始对象来注入。spring引入第二级缓存earlySinletonObjects,
    会提前AOP将代理对象放入第二级缓存中，然后删除第三级缓存。
        当然也可以这样设计，在原始对象生成后立即生成代理对象，缓存中不放原始对象，
    直接将代理对象放入缓存中，这样也只使用了二级缓存就解决了。这种设计不管是否有循环依赖，都会提前提前进行了AOP，
    违背了AOP的设计原则，AOP一般发生在Bean的后置处理中，不提前参与Bean的初始化流程。
    spring只有在发生循环依赖时，才会提前进行AOP，在属性设置期间，将代理对象放入第二级缓存中，让其他Bean完成初始化。
   

spring事务原理

    spring事务首先要基于数据库引擎的事务实现。分为编程式事务和声明式事务。
    编程式事务：通过TransactionTemplate或TransactionManager手动管理事务，实际很少使用
    声明式事务：通过注解+AOP实现
        1、对使用了@Transactional注解的Bean，spring会创建代理对象
        2、当调用代理方法时，判断方法上是否加了@Transactional注解
        3、加了事务注解的话，则会利用事务管理器创建数据库连接
        4、修改数据库连接的autocommit为false，禁止自动提交
        5、执行sql
        6、如果有异常，判断异常是否需要回滚，需要的话就回滚，否则仍然提交事务
    spring事务主要包含五个属性来管理事务：
        1、隔离级别（对应数据库的隔离级别）
        2、传播行为
        3、回滚规则（可以指定回滚异常）
        4、是否只读
        5、事务超时

spring事务的传播机制

    （1）死活不要事务的
        PROPAGATION_NEVER：没有就非事务执行，有就抛出异常
        PROPAGATION_NOT_SUPPORTED：没有就非事务执行，有就将事务挂起
    （2）事务可有可无
        PROPAGATION_SUPPORTS：没有就非事务执行，有就使用当前事务执行
    （3）必须要有事务
        PROPAGATION_REQUIRED：默认配置。没有就新建事务，有就使用当前事务
        PROPAGATION_REQUIRED_NEW：不管有没有，都新建事务，原来有就将原事务挂起。内部事务抛异常回滚，并向上抛出异常，外部事务也会异常回滚。外部事务异常回滚，内部事务不会回滚
        PROPAGATION_NESTED；没有就新建事务，有就嵌套事务。如果嵌套，内部事务异常回滚，并且向上抛出，外部事物也会回滚
        PROPAGATION_MANDATORY：没有就抛出异常，有就使用当前事务

spring事务失效场景
    
    spring事务的实现原理是AOP，大部分事务失效的根本原因是AOP不起作用。
        1、发生自调用，类里面使用this调用方法，此时的this不是代理对象而是类对象本身，事务不会生效
        2、方法不是public的（cglib代理基于父子类，private私有方法无法被子类覆盖）
        3、数据库存储引擎不支持事务
        4、事务未被spring管理
        5、事务指定异常了，抛出的异常非指定的异常
        6、异常被catch捕捉

AOP实现原理

    核心类AbstractAutoProxyCreator，实现了BeanPostProcessor接口，postProcessAfterInitialization方法中执行了代理逻辑

    BeanPostProcessor接口实现的postProcessAfterInitialization方法中，判断是否需要AOP，
    需要的话，执行代理逻辑，使用JDK（基于接口）或cglib（基于父类）实现动态代理。
    进入代理拦截逻辑invoke方法中,先获取切面的通知调用链集合，如果没有通知，则直接用原始对象执行方法，
    如果有通知，创建一个调用链对象（责任链模式），执行proceed方法，调用链遍历执行对应通知代理拦截的invoke方法，invoke方法又递归执行proceed方法，
    递归到最后，执行目标实际方法。目标方法执行完后，递归返回。
    因为around前置和before通知的逻辑在实际方法之前，after通知的逻辑在实际方法之后，所以经过递归执行后，
    实际的逻辑执行顺序：around前置逻辑->before逻辑->实际方法->around后置逻辑->after逻辑->afterreturning逻辑
    
    通知的实际逻辑执行顺序：
        around-before-method-around-after-afterthrowing（有异常的话）/afterreturning

类加载器、加载机制、加载过程
        
    类加载器
        1.启动类加载器 Bootstrap ClassLoader 加载JAVA_HOME\lib目录
        2.拓展类加载器 Extension ClassLoader 加载JAVA_HOME\lib\ext目录 
        3.应用类加载器 Application ClassLoader 加载classpath目录
        4.自定义类加载器 必须继承ClassLoader 
    加载机制：
        1.全盘负责
        2.父类委托
        3.缓存机制
    加载过程：
        1.加载：将.class文件加载到内存，产生与加载类对应的Class对象
        2.验证：确保class文件的字节流包含的信息符合虚拟机规范，并且不会对虚拟机造成安全危害
        3.准备：给类变量分配内存并设置初始值（方法区中），基本类型为0，引用类型为null
        4.解析：将常量池中的符号引用替换为直接引用
        5.初始化：真正执行类中的Java代码程序（静态变量和静态代码块）

双亲委派机制

    概念：先委托父类加载器寻找目标类，在找不到的情况下，在自己的路径中寻找并加载类
    优点：
        1.安全机制，避免核心类比如String.class被随意篡改
        2.避免类的重复加载，有些类父加载器已经加载，子类就不需要再次加载
        
jvm内存结构

    1.寄存器：较小的内存空间，通过计数器选取下一条要执行的字节码指令。寄存器不会产生内存溢出。
    2.虚拟机栈：线程运行需要的内存空间，每个方法运行时会创建一个栈帧，保存方法参数、局部变量、返回值等信息
    3.本地方法栈:非Java代码方法运行时需要的内存空间，比如Object类的clone()、notify()、hashCode()
    4.堆：年轻代（eden区、survivor1、survivor2，内存占比8:1:1）和老年代old（老年代约占堆2/3）。
            进入老年代条件：
                对象经过15次Minor GC仍然存活（XX:MaxTenuringThreshold=15）
                survivor区小于等于某个年龄的对象的总和占用空间超过survivor区的50%
                大对象可以直接进入老年代
    5.方法区：线程共享。包括类信息（类名、方法、字段、访问修饰符、构造器）、常量池、静态变量
            方法区是一种规范，具体实现有永久代（1.8之前，PermGen在jvm内存）和元空间（Metaspace在操作系统内存）
    
jvm内存分代模型

    新生代、老年代、永久区（jdk1.8之前）/元空间
    永久代和元空间区别：
        1、永久代必须指定大小  元空间可以不用设置大小，无上限（受限于物理内存）
        2、字符串常量 jdk1.6在永久代  jdk1.7、1.8在堆中

判断是否垃圾对象（垃圾对象判定算法）

    1.引用计数法：对象有新的引用加一，引用失效减一。此方法无法解决两个对象循环引用问题
    2.可达性分析法：以一系列的GC Roots对象作为起始点，从起点往下搜索，整个路径成为引用链，
        当一个对象没有和任何引用链相连时，就作为垃圾对象
        可以作为GC Roots的对象：
            1.虚拟机栈引用对象
            2.本地方法栈JNI引用对象
            3.方法区中静态属性引用对象
            4.方法区中常量引用对象
            5、Java虚拟机内部的引用对象，比如常驻的异常对象、系统类加载器、基本数据类型对应的Class对象
            6、被同步锁持有的对象

垃圾回收算法

    1.标记-清除：标记活跃对象，清除不活跃的对象。缺点：1、执行效率不稳定，对象越多效率越慢；2、会造成大量不连续的内存碎片
    2.标记-复制：开辟一个内存空间，将活跃的对象复制到这个空间中。缺点：1、消耗额外内存；2、存活对象越多，效率越慢
    3.标记-整理（标记-压缩）：垃圾对象进行移动，压缩在一起进行回收，不会产生内存碎片。缺点：1、移动对象需对应更新对象的引用；2、移动对象过程会造成STW
    

jvm垃圾回收器

    新生代回收器：serial(标记-整理算法)、parNew(标记-整理算法)、Parallel Scavenge(标记-复制算法，jdk1.7/1.8新生代默认算法)
    老年代回收器：cms(标记-清除算法)、serial old(标记-复制算法)、parallel old(标记-整理算法，jdk1.7/1.8老年代默认算法)
    整堆回收器： G1、ZGC
    
Minor GC：新生代的垃圾收集  
Major GC：老年代的垃圾收集  
Mixed GC：整个新生代和部分老年代的垃圾收集，目前只有G1收集器有这种行为  
Full GC：整个Java堆和方法区的垃圾收集

什么时候执行垃圾回收

    Minor GC：Eden空间不足就进行Minor GC
    Full GC：full GC会伴随一次minor GC
        1.老年代不足
        2.永久代不足（jdk1.8之后变为元空间，元空间在系统内存不在堆内存中）
        3.Minor GC后晋升到老年代的对象大小超过老年代剩余大小
        
survivor为什么有两个

    执行复制算法GC时，需要两个survivor区的交替移动活跃对象。
    执行GC，eden和survivor1活跃对象复制到survivor2，然后清空survivor1和Eden，
    下次gc时eden和survivor2活跃对象复制到survivor1，然后清空survivor2和Eden，如此反复。
    
    
内存溢出/泄漏问题定位

    1.jps查看java进程，找到程序进程pid
    2.jmap -heap pid 查看堆内存情况 / jinfo pid
    3.jmap ‐histo:live pid | more 查看活跃对象，注意：用这个命令会导致堆停止
      jmap -dump:format=b,live,file=dump_xx.dat pid  将内存使用情况dump到文件中
    4.jvm参数中设置内存溢出转储文件，-XX:+HeapDumpOnOutOfMemoryError

cpu占用过高问题定位

    1.top命令，找到占用cpu高的线程PID
    2.使用ps H -eo pid,tid,%cpu,%mem|grep pid  找到进程下的所有线程，及线程对应占用cpu、内存情况
    3.将占用cpu高的线程tid转为十六进制
    4.使用jstack pid 展示进程下所有线程详细情况，对比nid与上一步tid十六进制的值，找到占用cpu高的线程详细情况


springboot启动过程、自动配置原理
         
    SpringApplication.run(StartApplication.class, args)
    1、new SpringApplication(primarySources)
        执行启动类构造方法，构造方法内部会获取当前应用的主类mainApplicationClass、设置web应用类型（servlet）、从spring.factories文件加载初始化类和监听器类（只是全限定类名）
    2、执行SpringApplication对象的run方法，run方法内部会设置相关属性值，相当于准备工作，包括启动监听器、设置启动计时、设置上下文对象、设置异常报告器、设置环境对象，打印banner等
    3、prepareContext(context, environment, listeners, applicationArguments, printedBanner);
        给应用程序上下文对象设置具体的值，包括环境对象，触发监听器、初始化参数、创建对象工厂等
    4、Set<Object> sources = getAllSources(); 
        获取资源文件，包含应用的主类
    5、load(context, sources.toArray(new Object[0]));         
    6、loader.load();
        加载资源文件，判断主类是否有component注解，有的话执行下一步
    7、annotatedReader.register(source);
        注册主类到容器中
    8、refreshContext(context)，内部执行spring的refresh()，bean的初始化和自动装配就是在此方法内完成
        其中包括执行invokeBeanFactoryPostProcessor,内部会完成bean的实例化
        内部会使用ConfigurationClassPostProcessor类来解析包含@Configuration注解的类，
        解析springboot应用的主类，依次判断解析主类上的所有注解，比如@componentScan、@PropertiesSource、@Import等
        启动类上@Import注解引入的类有：AutoConfigurationImportSelector.class、AutoConfigurationPackages.Registrar.class
    9、执行@Import注解引入类的处理逻辑（自动配置过程）
        AutoConfigurationImportSelector，内部会执行importSelector类的getAutoConfigurationEntry()方法
        
    10、refresh()方法执行完后，记录启动结束时间和启动日志
    11、监听器发布事件
    12、callRunners(context, applicationArguments);真正启动springboot应用
    
    @SpringBootApplication注解：
           @SpringBootConfiguration
           @ComponentScan
           @EnableAutoConfiguration
               @AutoConfigurationPackage：获取启动类所在包路径，作为自动配置扫描包
               @Import(AutoConfigurationImportSelector.class):               
                   AutoConfigurationImportSelector内部主要逻辑：
                   1.加载META-INF/spring.factories下所有自动配置类（文件中EnableAutoConfiguration= 后的所有类）
                   2.去重、排除、过滤，去除不合格的配置类
                   3.通过监听事件，通知各个组件配置类，通过项目的properties/yml文件进行自动配置
           

springboot原生注解

    @SpringBootApplication
    @SpringbootConfiguration
    @EnableAutoConfiguration------核心注解
    @AutoConfigurationPackage
    @ConfigurationProperties
    @ServletComponentScan
    
springboot安全关闭

springboot启动、关闭、重启命令

springboot加载配置文件信息方式
    
    1.@Value注解
    2.Environment类
    3.@ConfigurationProperties注解，指定前缀
    4.@PropertiesSource注解读取指定配置文件
    
springboot jar为什么能直接运行？

    jar包目录结构
        META-INF：
            包含jar的基础信息，包括入口程序
            Main-Class：JarLauncher
            Start-Class：自己定义的Main函数
        BOOT-INF
            classes应用程序
            lib 第三方依赖jar包
        springboot loader相关类
    
    启动jar时，会执行JarLauncher的main方法，接着调用自身的launch方法，该方法内部会先获取JarFileArchive（jar包文件档案）集合，
    然后根据JarFileArchive集合创建类加载器LaunchedURLClassLoader
    之后会另起一个线程执行Start-Class指向的自定义的main方法，该main方法执行后，会构造spring容器和启动内置servlet容器等过程

自定义springboot-starter

    自定义jar包命名规则：xxx-spring-boot-starter
    配置类加上@Configuration注解
    具体自定义实现方法，有多种方式：
        1、META-INF/spring.factories文件下放入配置类的全限定类名
        2、启动类使用@Import注解，引入自定义配置类
        3、自定义注解，@EnableXxx ,注解上加入@Import注解，引入配置类，启动类加上@EnableXxx注解

redis数据结构

    1、String
        SDS动态字符串，SDS属性包括已使用的长度、空闲未使用的长度、字符数组
        操作字符串，redis会执行以下操作：1、计算出大小是否足够；2、开辟空间至满足所需大小
        优点：1、快速获取字符串长度；2、避免缓冲区溢出；3、降低空间分配次数提升内存使用效率
    2、List
        redis3.2之前：压缩列表zipList、双端列表linkedList
        redis3.2之后 快速列表quickList
        zipList：连续内存组成的顺序型数据结构，每个节点保存一个字节数组或整数值，空间利用率高。
                linkedList转为zipList的转换条件：list-max-ziplist-value  列表所有元素长度小于64字节
                                              list-max-ziplist-entries  列表元素个数小于512
        quicklist：zipList和LinkedList的结合
    3、set
        整数值集合intSet、hashTable
    4、zset
        压缩列表zipList、跳跃表和字典skipList
            数据量小时使用压缩列表，达到阈值时转为跳跃表，
                阈值设置：zset-max-ziplist-entires 128  元素超过128个
                        zset-max-ziplist-value 64     单个元素值大于64byte
            跳表：基于有序链表，分层冗余数据，改造成可以支持“折半查找”数据结构，可以快速的进行增删查数据
    5、Hash
        压缩列表zipList、hashTable

redis内存淘汰策略

    redis内存不足时，会触发内存淘汰策略，redis5.0之前只有6种淘汰策略：
        1.volatile-lru：在已设置过期时间的key中，删除最近很少使用的（lru）
        2.volatile-ttl：在已设置过期时间的key中，删除将要过期的
        3.volatile-random：在已设置过期时间的key中，随机删除
        4.allkeys-lru：删除最近很少使用的key(lru)
        5.allkeys-random：随机删除
        6.no-enviction：禁止删除。内存满时，新增数据会报错，默认策略
    redis5.0后，新增两种淘汰策略：
        1.volatile-lfu：在已设置过期的key中，删除使用频率最低的
        2.allkeys-lfu：删除使用频率最低的
        
redis删除机制

    1.惰性删除：使用key时，检查是否过期，过期的话就删除
    2.定期删除：周期性检查已设置过期时间的key，过期的话就删除，默认100ms检查一次
    
redis持久化
    
    1.RDB快照
        默认持久化策略。将某时刻数据全量备份在二进制文件中，文件名为dump.rdb。
        可以手动修改配置文件设置持久化触发频率（m秒内有n个数据被修改）。
        RDB持久化触发方式：
            1.save：手动执行save命令，同步操作持久化，会阻塞客户端请求
            2.bgsave：手动执行bgsave命令，异步操作，执行fork创建子进程执行持久化，不会阻塞客户端请求
            3.自动化：根据配置文件配置的数据修改频率自动触发 save m n
        数据量大的话，RDB操作严重占用磁盘IO操作
        fork子进程创建临时文件进行持久化，同时父进程也会创建一个临时缓冲区，存放该期间内的写命令，持久化结束后会合并临时缓冲区和临时文件，形成新的快照dump文件替换旧的
    2.AOF
        追加方式，将每一个写命令追加到文件中，文件名为appendonly.aof
        AOF持久化触发方式：
            1.appendfsync always 同步操作，每次发生数据变更，都会追加到磁盘中
            2.appendfsync everysec 异步操作，每秒一次
            3.appendfsync no 
        aof文件满足重写条件后，会进行重写压缩文件大小
        AOF重写过程
            执行fork创建子进程，子进程将内存数据根据命令合并规则重写到新aof文件中，同时父进程会创建一个重写缓冲区，
            重写中客户端有新的写命令会同步到缓冲区中和旧的aof文件中，防止重写中这部分数据丢失，新的aof文件重写完成后，
            子进程会通知父进程，父进程会将重写缓冲区的数据写入到新的aof文件中，最后新的aof文件替换旧的aof文件。

redis缓存雪崩、缓存穿透、缓存击穿
    
    缓存雪崩
        热点key在同一时间大面积失效，导致请求全部落在数据库上
        解决方案：热点key设置随机过期时间
                热点key不设置过期时间
    缓存穿透
        大量请求一个不存在的key，导致请求全部落在数据库上
        解决方案：缓存不存在的key，过期时间设置很短
                布隆过滤器
                请求参数校验，过滤部分请求
    缓存击穿
        一个热点key失效，导致大量请求落在数据库上
        解决方案：热点数据不设置过期
                互斥锁

redis集群模式

    1、主从模式
        分为master和slave。master写，slave读
        工作机制：
            当slave初始化启动后，主动向master发送sync命令，master接收到同步命令后，在后台保存快照以及使用临时缓冲区保存执行快照这段时间内的命令，
            快照完成后，会将快照文件和临时缓冲区的命令发送给slave，slave加载快照文件和缓冲区命令，实现主从的数据一致性。
            初始化完成之后，master每次接收到写命令，都会主动发送给slave。
        主从模式master挂掉后，无法写入数据。
    2、Sentinel模式（哨兵）
        sentinel建立在主从模式的基础上。当master挂掉后，sentinel会从slave中选择一台作为master，并修改所有节点的配置文件，让其他slave都指向新的master
        master重启后，不再是master，而是变为slave
        sentinel一般也会集群部署，sentinel集群之间会互相监控。sentinel具有高可用性
        工作机制：
            每个sentinei以每秒一次的频率向master、slave以及其他sentinel发送ping命令，
            如果一个实例的最后一次有效PING回复时间超过down-after-milliseconds 选项所指定的值， 则这个实例会被sentinel标记为主观下线
            如果master被标记为主观下线，则监视这个master的所有sentinel都要以每秒一次的频率确认master是否进入主观下线
            如果有足够数量的sentinel（配置文件设置个数）确认master进入主观下线状态，那这台master会被标记为客观下线
            一般情况下，每个sentinel会以每10秒的频率向master发送info 命令（返回服务器信息和统计数据）
            如果master被标记为客观下线，那么频率会变为每秒一次发送info 命令
        当使用sentinel模式的时候，客户端就不要直接连接Redis，而是连接sentinel的ip和port，由sentinel来提供具体的可提供服务的Redis实现，
        这样当master节点挂掉以后，sentinel就会感知并将新的master节点提供给使用者。
        
        脑裂：集群中不同节点，对集群状态有不同的理解
            情景理解：
        
        
    3、Cluster模式
        sentinel模式基本可以满足一般生产的需求，具备高可用性。但是当数据量过大到一台服务器存放不下的情况时，主从模式或sentinel模式就不能满足需求了，
        这个时候需要对存储的数据进行分片，将数据存储到多个Redis实例中。cluster模式的出现就是为了解决单机Redis容量有限的问题，将Redis的数据根据一定的规则分配到多台机器。
        cluster投票：
            集群所有master参与投票，超过半数master节点与某master节点通讯超时，就认为该master挂掉
            如果集群超过半数master挂掉，无论是否有slave，整个集群都会进入fail不可用状态
        cluster哈希槽：
            Redis集群中内置了16384个哈希槽，当需要在Redis集群中放置一个key-valu 时，redis先对key使用crc16算法算出一个结果，
            然后把结果对16384求余数，这样每个key都会对应一个编号在0-16383之间的哈希槽，redis会根据节点数量大致均等的将哈希槽映射到不同的节点
        cluster什么情况会进入完全不可以状态
            1、master挂掉，且当前master没有slave，导致hash槽映射不完整（某个范围内的哈希槽不可用），集群会进入fail不可用状态
            2、如果集群超过半数master挂掉，无论是否有slave，整个集群都会进入fail不可用状态

redis主从复制

redis数据库缓存一致性

    1、更新数据库后，删除缓存
        删除操作失败，采用补偿机制
        使用数据库的binlog，数据变更后操作redis
    2、删除缓存。更新数据库后，再延迟删除缓存
    
redis分布式锁要注意什么问题？

    1、锁要设置超时时间
    2、锁要手动释放
    3、业务超时，超过锁超时时间，锁被其他线程获取
        定时线程，给锁续命
    4、锁被其他线程释放
        每个线程给锁的value值加上唯一标识
    5、集群模式，加完锁的master突然挂掉，锁还未同步到新master中，锁丢失，其他线程又能够获取锁
        redlock
    
redisson
    
    RedissonLock.class
    运行原理：
        通过lua脚本加锁，同时启动看门狗异步任务，维护锁的超时时间。锁的value会加上线程id
        同一个线程，支持可重入锁，每次加锁，统计客户端的加锁次数+1，当加锁次数为0时，代表该锁可以被释放
        通过lua脚本释放锁，保证原子性
        看门狗（watch dog）:定时任务，renewExpiration方法自身调用，每隔（锁超时时间的1/3）秒，就会检查锁是否还存在，存在的话，就重新设置生存时间，默认设置超时时间30s
        
    缺点：
        集群模式下，master挂掉后，锁未及时同步到新的master，其他线程可能又会获取到锁。可以用红锁解决redLock
        redlock：超过半数redis加锁成功才算成功
        
redis的线程模型

    redis4.0之前使用单线程模型，基于epoll实现IO多路复用并发处理客户端请求
    redis4.0之后，引入多线程，进行异步删除操作
    IO多路复用：
    传统的套接字读写操作时阻塞的，不支持并发，性能低
    NIO设计思路：使用轮询策略，轮询是否有新连接和已有的连接是否有数据读写，当一个连接没有数据时，会轮询下一个连接，不会阻塞
        redis的IO多路复用，将socket的轮询操作直接调用操作系统的函数（windows使用select()，linux使用epoll()方法），
        操作系统进行读写操作，然后通知线程处理业务
       
IO、NIO、BIO
       
         
线程的状态

    1.新建：新创建线程
    2.就绪：获取同步锁，等待cpu执行权
    3.运行：获取cpu执行权，正在执行程序
    4.等待：wait()放啊，失去cpu执行权，同时释放同步锁，需要被手动唤醒，否则无限期等待
    5.阻塞：有IO阻塞（等待IO流执行完成）和同步阻塞（线程竞争锁对象）
    6.睡眠：sleep()方法，失去cpu执行权，但不释放同步锁，睡眠时间结束后自动进入就绪状态
    7.死亡：线程执行完或异常退出

创建线程的方式

    1.继承Thread类
    2.实现Runnable接口
    3.实现Callable接口，并使用FutureTask包装实现类
    
线程池的创建方式

    自动创建：
        1.Executors.newCachedThreadPool()
        2.Executors.newFixedThreadPool()
        3.Executors.newSingleThreadExecutor()
        4.Executors.newScheduledThreadPool()
        5.Executors.newWorkStealingPool()
    手动创建：
        new ThreadPoolExecutor(int corePoolSize,
                                      int maximumPoolSize,
                                      long keepAliveTime,
                                      TimeUnit unit,
                                      BlockingQueue<Runnable> workQueue,
                                      ThreadFactory threadFactory,
                                      RejectedExecutionHandler handler)
        corePoolSize：核心线程数
        maximumPoolSize：最大线程数，队列已满，且当前线程小于此参数，就会创建新的线程
        keepAliveTime：非核心线程空闲回收时间，maximumPoolSize大于corePoolSize时有效
        workQueue：阻塞队列，核心线程数满时，新任务会先入队列
        threadFactory：线程工厂
        handler：拒绝策略，队列满且当前线程等于maximumPoolSize，新任务会触发拒绝策略
    线程池拒绝策略：
        1.ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
        2.ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
        3.ThreadPoolExecutor.DiscardOldestPolicy：丢弃队首的任务，然后重新尝试执行任务（重复此过程）
        4.ThreadPoolExecutor.CallerRunsPolicy：重试添加当前的任务，自动重复调用 execute() 方法，直到成功

常见线程阻塞队列

    1.SyncchronousQueue 无缓冲同步队列，不存储任务，上一个任务被取出后才能接收下一个任务，一般配合最大线程数为无界使用
    2.LinkedBlockingQueue 无界阻塞队列，可以无限制添加
    3.ArrayBlockingQueue 有界阻塞队列，初始化必须指定容量，最多只能添加指定数量的任务
    4.PriorityBlockingQueue

线程池状态
        
    1.Running：能够接收新任务，以及能够处理已添加的任务
    2.Shutdown：不接收新任务，但会处理已添加的任务，可调用shoutdown()方法切换到此状态
    3.Stop：不接收新任务，也不会处理已添加的任务，并且会中断正在执行的任务，可调用shutdownNow()方法
    4.Tidying：所有的任务终止，任务数为0，处于Tidying状态时,线程池会执行钩子函数terminated()
    5.Terminated：线程池彻底终止
    
执行shutdownNow()方法后，正在运行的线程怎么实现中断的？

    遍历worker集合中线程，如果正在执行的话，就执行线程的打断方法interrupt()，
    interrupt()实际并不会立即中断正在运行的线程，而是给线程设置一个中断标识，
    如果一个线程执行了interrupt()方法，然后再执行阻塞方法（sleep/wait/join），就会抛出异常并真正停止线程

线程池底层运行原理

    实现核心Worker类 Worker类实现了Runnable接口，新建Worker时会新建线程
    提交线程任务时，线程会先提交到worker集合，提交成功后，开启新线程执行方法（不管是否有空闲线程，都会新建线程）
    当前线程数大于等于核心线程数后，线程会提交到阻塞队列
    线程池内空闲线程会自旋从队列中获取任务
    队列满了后，会创建非核心线程执行任务，有非核心线程并且空闲时间达到超时时间后会被回收（随机回收超时的空闲线程，并非是核心线程满了之后创建的线程）
    队列满了并且活跃线程数达到最大线程数，会执行拒绝策略
    核心代码：
        int c = ctl.get();
        if (workerCountOf(c) < corePoolSize) {
            //当前线程小于核心线程数，添加新的核心线程worker
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        //核心线程数满了后，向队列添加worker
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                //新增非核心线程worker
                addWorker(null, false);
        }
        //添加非核心线程失败，执行拒绝策略
        else if (!addWorker(command, false))
            reject(command);
    
volatile
    
    1、缓存可见性（cpu高速缓存修改数据后会立即写回主内存，其他线程会感知主内存数据的修改）
    2、禁止cpu指令重排序
    volatile缓存可见性底层实现原理：
        底层实现主要通过汇编lock前缀指令，它会锁定这块内存区域的缓存，并写到主内存中
        lock指令的解释：
            1、会将当前cpu缓存中的数据立即写入到系统内存
            2、写回主内存的操作会使其他CPU缓存了该内存地址的数据无效
            3、提供内存屏障功能，使lock前后指令不能重排序
    volatile能保证可见性、有序性，但不能保证原子性
    
死锁

     死锁排查思路：1.找到程序运行的进程号pid；2.jstack pid查看是否有死锁
    
     private static final Object a = new Object();
     private static final Object b = new Object();
    
     public static void main(String[] args) throws InterruptedException {
            //死锁模拟
            new Thread(() -> {
                synchronized (a) {
                    try {
                        System.out.println("线程1获取锁a");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (b) {
                        System.out.println("线程1获取锁a和b");
                    }
                }
            }).start();
    
            Thread.sleep(1000);
            new Thread(() -> {
                synchronized (b) {
                    System.out.println("线程2获取锁b");
                    synchronized (a) {
                        System.out.println("线程2获取锁a和b");
                    }
                }
            }).start();
        }  
        
synchronized底层实现原理

    synchronized锁的是对象，底层锁信息存储在锁对象的对象头的mark word中，不同的锁状态对应不同的存储信息和锁标志位。
    偏向锁：mark word存储的线程id
    轻量级锁：mark word存储的指向栈中轻量级锁的指针
    重量级锁：mark word存储指向堆中重量级锁的指针
    
    偏向锁本质上不是锁，轻量级锁本质上是线程的自旋
    重量级锁才是传统意义上的锁，重量级锁指向堆中的monitor对象，monitor对象包含等待队列、持有锁的线程、waitSet，
    新线程发现锁被占用时，会先进入等待队列，当锁被释放后，队列的元素会移动并唤醒队首线程。
    如果持有锁的线程执行了wait()方法，该线程会进入waitSet集合中，如果被notify()方法唤醒，会进入等待队列中。
    
    synchronized修饰的代码块，编译成字节码文件，会生成对应的monitorenter和monitorexit指令，其中monitorexit指令会有两个，隐式在finally会执行，这样线程异常也能释放锁
    synchronized修饰的方法，生成了ACCSYNCCHRONIED关键字
    
    monitor对象可以进行锁计数，实现可重入，持有锁的线程重入时，monitor计数+1，当计数为0时，其他线程可以竞争锁

synchronized的偏向锁、轻量级锁、重量级锁

    synchronized同步锁自身的优化，当没有锁竞争时，会先获取偏向锁（本质上不是锁，底层锁对象存储了线程id）。如果有少量竞争，偏向锁
    会升级为轻量级锁，竞争线程会启动自旋，如果自旋到达阈值仍然没有拿到锁（自旋10次）或者竞争线程比较多（竞争线程数达到CPU核数的一半），
    就会升级为重量级锁，重量级锁会让竞争线程进入等待阻塞状态，直到持有锁的线程执行完唤醒它们。
    
cas乐观锁
    
    compareAndAwap    自旋，属于轻量级锁
    三个基本操作数：共享变量内存值、预期值、要修改的值
    先获取共享变量的内存值，赋值给临时变量作为预期值，更新数据时，比对内存值和预期值
    只有当共享变量内存值=预期值时，共享变量内存值才会更新为要修改的值
    
    cas必须具备原子性，否则依然会有并发问题（比如比较相等通过后，内存值又被修改）。
        jdk的cas方法，底层使用native方法，使用c++方法，最底层使用汇编语言的lock cmpxchg指令来实现原子性
    
cas的ABA问题如何解决？

    ABA问题：线程1读到内存值0，将0作为预期值，在比较之前，其他线程对内存值进行修改，比如0修改为1，然后将1又修改为0，
    线程1此时再执行比较，发现内存值和预期值一样，于是执行后续操作。其实此时内存值已经被其他线程修改，但线程1无法判断，认为内存值没有被修改。
    解决方案：
        1、版本号机制，数据被修改一次，版本号+1，每次比对时加上版本号一起比对

synchronized和reentrantLock区别
    
    1、synchronized是关键字，reentrantLock是一个类
    2、synchronized会自动加锁和释放锁，reentrantLock需要手动加锁和释放锁
    3、synchronized是jvm层面实现的锁，reentrantLock是api层面实现的锁
    4、synchronized是非公平锁，reentrantLock可以选择公平锁和非公平锁
    5、synchronized锁的是对象，reentrantLock锁的是线程，底层基于AQS实现锁竞争
    6、synchronized底层有锁升级的过程

谈谈AQS，AQS底层为什么用cas+volatile
    
    AQS：AbstractQueuedSynchronizer
        state：默认0，cas方式获取锁，有线程获取锁后+1，锁重入一次也+1，释放锁一次-1
        exclusiveOwnerThread：当前锁线程
        阻塞队列：Node节点组成，volatile修饰节点的属性，比如prev、next、waitStatus、thread
        head：阻塞队列的头，初始化时为空节点（无线程），队列添加线程后，会移除头空节点，添加该线程的节点变为头
        tail：阻塞队列的尾，尾部为空时说明队列需要初始化     
        
        原理：线程使用cas判断修改state是否为0，加锁成功后，state变为1，exclusiveOwnerThread指向获取锁的线程，
            线程2尝试获取锁，加锁失败，加入队列的队首中。其余竞争线程依次加入队列中。
            尝试获取锁时，分为公平锁和非公平锁、
               非公平锁：直接cas判断获取
                公平锁：判断队列是否有线程且不是当前线程+cas     
        
        ReentrantLock和CountDownLatch底层都是基于AQS实现的       
            
threadLocal
    
    线程维护一个ThreadLocalMap,map的key为threadLocal对象（实例对象的引用地址），value为存储的值。
    ThreadLocalMap的Enrty数组继承了弱引用 extends WeakReference<ThreadLocal<?>>，threadLocal对象实例通过弱引用指向map的key。
    threadLocal对象实例指向了两个引用：
        1、ThreadLocal threadLocal = new ThreadLoca<>()    强引用
        2、线程中ThreadLocalMap的key     弱引用
    
    为什么Enrty要使用弱引用，而不是强引用？
        如果使用强引用，即使将ThreadLocal对象的引用置为空（threadLocal=null）,线程中的ThreadLocalMap的key仍然指向threadLocal实例对象，不会被回收。会存在内存泄露。
        即使是使用弱引用，仍然会存在内存泄露风险。
    
    为什么threadLocal使用完后要remove？
        线程ThreadLocalMap中的key是弱引用指向threadLocal实例对象，如果threadLocal实例对象被回收后（threadLocal=null），线程ThreadLocalMap中的key会变为null，
        此时map中的value将无法被访问到，value无法被回收，依然存在内存泄漏问题。
        其实threadLocal内部，每次get、set时会自己清理key为null的value，但是如果一直没有执行get、set，那么value会一直存在
        所以每次使用完threadLocal后，要主动清理value。
        
    threadLocal和线程池结合使用时，注意线程执行完后，要主动清理ThreadLocalMap，否则可能会出现数据混乱问题。
    
微服务优缺点

    优点：
        解耦
        业务拆分、功能拆分、易于开发
        分布式
    缺点：
        通信耗时、有风险
        部署复杂
        分布式事务
      
CAP理论

    C：Consistency 强一致性
    A：Availability 可用性
    P：Partition tolerance 分区容错性
    CAP理论核心：一个分布式系统不可能同时很好的满足一致性、可用性和分区容错性这三个需求
                P一定要满足
        AP：Eureka
        CP：Zookeeper/Consul

BASE理论

    Basically Available 基本可用
    Soft State 柔性状态
    Eventual Consistency 最终一致性
     
Ribbon负载规则
    
    1.随机
    2.轮询：顺序选择
    3.重试：重试选择可用的素服
    4.最低并发：选择并发最低的服务
    5.可用过滤：过滤一直失败或有高并发的服务
    6.响应时间加权重：响应时间越长，权重越低；响应时间越短权重越高，被选中的概率越高
    
Hystrix

    分布式系统服务间提供容错保护能力
        线程池coreSize默认10个
        信号量默认最大并发度10
    1.服务降级 fallback
        程序运行异常
        运行超时
        服务熔断触发降级
        线程池/信号量打满触发降级
    2.服务熔断 circuit breaker
        熔断触发的条件：默认10秒内至少20次请求，并且有50%以上失败，就会启动熔断机制
        熔断开启后，默认5秒后会进入半开状态，会让一个请求进行转发，如果成功，熔断器关闭，如果失败，继续开启
    3.服务限流 flowlimit：
    4.服务隔离
        1.线程池隔离：使用独立的线程池对应每一个服务
        2.信号量隔离：使用原子计数器来记录当前有多少个线程在运行。
                    超过阈值拒绝请求；不超过的话，请求通过计数加一，请求返回计数减一

hystrix常用参数

consul
    
    和eureka区别
        eureka：牺牲一致性，保证高可用和最终一致性。注册速度很快
        consul：强一致性，leader挂掉后，重新选举期间整个consul不可用，牺牲了可用性。注册速度慢
    和其他注册中心优缺点

gateway网关
    
    三个核心概念：
        路由Route
        断言Predicate
        过滤Filter
        
分布式事务
    
    2PC：二阶段提交协议：准备阶段(prepare)和提交阶段(commit)
    TCC：两阶段补偿方案 try-confirm-cancel
    最大努力通知：
    基于mq可靠消息最终一致性：
    saga：

nginx正向代理和反向代理
    
    1.正向代理
        为客户端进行代理，服务端不知道真正的客户端是谁
    2.反向代理
        为服务端代理，客户端不知道真正的服务端是谁
    
linux实用命令

    du -sh *|sort -h   查看当前目录磁盘占用情况
    

Mysql

    搜索引擎：
        1、innodb：支持事务；提供行级锁和外键约束；使用聚集索引；适合大数据量
        2、Myisam：不支持事务；不提供行级锁和外键约束；使用非聚集索引；适合小数据量，查询多修改少的情况
        
    索引：
        1、聚集索引：使用B+树索引结构，非叶子节点为索引值，叶子节点包含索引值和完整的数据记录；
        2、非聚集索引：使用B+树索引结构，非叶子节点为索引值，叶子节点包含索引值和索引指向的数据地址；
    B树、B+树区别：
        B树每个非叶子节点包含索引值和数据记录，B+树非叶子节点只包含索引值，使用B+树，保证每个非叶子节点能够存储的索引值更多（默认16kb）,能够降低索引树的高度；
        B树上下层级节点没有冗余索引，查询数据需要遍历上下层级。B+树每个节点首位索引值冗余了上一级节点的索引，B+树的叶子节点包含了所有索引值，查询直接遍历叶子节点就行；
        B树叶子节点的之间没有双向指针，B+树叶子节点之间有双向指针，B+树叶子节点所有索引值天然排序；
        
    为什么很少用Hash索引：
        hash索引使用类似hash表的结构，数组+链表，不支持范围查询，也不支持排序
        数据量大的话，hash冲突多，链表遍历耗时
    为什么不使用二叉树：
        B+树每个节点包含更多的索引值，一次性能够加载更多的数据，减少磁盘IO，而且能够降低树的高度，减少查询次数和复杂度；
        
    回表：
        普通索引（非主键索引）：先扫描普通索引树定位主键值，再扫描主键索引树定位行记录。
    覆盖索引：
        查询的字段在索引树上，即触发覆盖索引。常见实现覆盖索引的方法是：将被查询的字段，建立到联合索引里去。
    最左匹配：
        针对组合索引。
        例如：a,b,c 为组合索引
            where a= ? and b=?  走索引
            where a = ？and c= ? 走索引,只使用a
            where a = ？and b>? and c= ? 走索引,只使用a,b
            where a = ？and b like '%xxx%' and c= ? 走索引,只使用a
            where b =? and c=? 不走索引
            where b = ? and c =? and a=? 也会走索引，mysql会帮助优化，调整顺序
            select a,b,c from t where c = ? 走索引
    索引下推：   
        
    explain关键字
        id：标识符 执行顺序的标识，id越大优先级越高，id相同，从上往下顺序执行
        select_type: 查询类型  SIMPLE（简单查询）、PRIMARY（复杂查询的最外层查询）、UNION等
        table:输出结果集的表   表名或表的简称
        partition:匹配的分区 
        type：表的连接类型  ALL、index、range、 ref、eq_ref、const、system、NULL（从左到右，性能从差到好）
        possible_keys：查询时可能用到的索引
        key：实际使用到的索引
        key_len：索引字段的长度
        ref：列与索引的比较  表查找值所用到的列或常量，常见的有： const(常量)，字段名等
        rows：扫描的行数
        filtered:按表条件过滤的行百分比
        Extra:执行情况的描述和说明  
            Using index：使用覆盖索引
            Using index condition：查询的列未完全覆盖索引
            Using where：未使用索引
            Using temporary：需要使用临时表
            
    隔离级别：
        读未提交：可能发生脏读
        读已提交：可能发生不可重复读
        可重复读：
        串行化：
    ACID：
        A：atomivity 原子性，事务要么成功，要么失败，失败的话数据会回滚。undo log可以保证原子性
        C：consistency一致性，其他三个特性保证了最终一致性
        I：isolation 隔离性。锁（写-写操作）和mvcc（写-读操作）实现隔离性
        D：durability 持久性，即使数据库宕机，也不会丢失已提交的事务。redo log可以保证持久性
        
    redo log：
        数据加载到Buffer Pool后，执行写操作，生成redo log，存在log buffer区域，然后顺序持久化到redo log文件中（mysql重启后会加载redo log文件，保证事务持久性）
        redo log采用WAL预写式日志，分为prepare和commit两个阶段，更新数据写入到redo log内存后处于prepare状态，再告知执行器执行完成，随时可以提交事务
        默认有两个文件 logfile0  logfile1  每个默认48M，循环写入。持久化策略可配置，默认是事务提交后，脏页数据同步从内存持久化到redo log file中
        redo log 以块的形式保存，重做日志块redo log block，每块大小512字节，磁盘扇区是磁盘写入的最小单位，也是512字节，所以redo log刷盘具有原子性，没有双写机制。
    
    redo log刷盘策略：参数innodb_flush_log_at_trx_commit
                    0：事务提交时不写入，仅在master thread中进行，master thread每1秒调用一次fsync操作；
                    1：事务提交时，调用一次fsync操作；
                    2：事务提交时，仅写入文件系统缓存中不写入磁盘，不同文件系统刷盘时机各不一样。
    
    LSN：Log Sequence Number 日志序列号
        1、重做日志写入的总量
        2、checkpoint的位置
        3、页的版本
        
    double write buffer:
        。
        
    undo log：
        作用：1、回滚；2、mvcc
        undo log是逻辑日志，可以理解为：将对应的写操作记录一条相反操作的日志
        
    bin log
        二进制日志
        写入时机：事务执行中->bin log cache->事务commit->bin log file
        binlog日志刷盘策略：根据sync_binlog参数设置
            sync_binlog=0  提交事务只写，不刷盘
            sync_binlog=1  每次提交事务，就刷盘
            sync_binlog=N  每次提交事务都写，但积累N个事务后才刷盘
    
    读写分离实现原理：
    
    mysql主从复制过程：
        1、主节点 bin log dump线程
            当从节点连接主节点时，主节点会创建一个log dump线程，用于发送bin log内容
        2、从节点I/O线程
            当从节点执行start slave命令后，从节点会创建一个I/O线程用来连接主节点，请求主库中更新的bin log，I/O线程接收主节点bin log dump进程发来的更新内容，保存在本地的relaylog中
        3、从节点sql进程
            SQL线程负责读取relaylog中的内容，解析成具体的操作并执行，最终保证主从数据库的一致性
    同步方式：
        1、全同步复制：所有从节点同步完成才返回主节点成功，性能低
        2、半同步复制：只要有一台从节点同步成功，就返回主节点成功
        
    主从同步方式：
        1、SBR 基于SQL语句的复制
        2、RBR 基于行的复制
        3、MBR 混合模式复制
        
    主从复制延迟原因，解决方案：
        原因：读写分离，主库写，从库读。
            主库的DDL/DML操作顺序写binlog效率很高，从库IO读取主库的binlog也是顺序读，效率也很高，
            从库写relay log也是顺序写，效率高，从库SQL线程解析relay log也是顺序读，效率也高，
            但是从库的sql线程执行sql时，要先从硬盘文件寻找加载数据然后再修改，比较耗时。而且执行sql线程是单线程，效率低，还可能与其他查询操作产生锁竞争，
            当业务繁忙时，产生的写操作比较多，前面的流程比较快，最后执行sql的效率很慢，写操作产生堆积，数据同步就会产生延时
        解决方案：
            1、提升硬件配置
            2、增加从库数量，分散压力
            3、如果是服务器流量太大，造成业务繁忙影响同步，可以对上层流量进行限流
            4、引入缓存中间件，写数据时，将数据存入缓存，读数据时如果读不到的话就从缓存读取，当数据同步成功后再删除缓存数据，一般用不到
            5、写数据时，确保主从同步成功才返回成功，此方案影响性能一般不考虑
            6、MTS（multi-thread-slave），多线程执行sql（但会引入很多其他问题）
            
    mvcc 多版本并发控制:
        mvcc主要为了提高并发读写性能，不用加锁就能实现多个事务并发读写，通过undo log和read-view实现
        mysql开启一个事务，会分配一个事务id
        mvcc会维护一个版本列表，针对每个写操作，会顺序添加到版本列表中，列表每行记录包括修改的数据、事务id、回滚指针
        查询时，会生成一致性视图read-view,由当前未提交的事务id数组和以创建的最大事务id组成（[100,200],300），查询的结果需要跟read-view做对比从而得到最终结果。
            mysql默认隔离级别是可重复读，同一个事务中，多次查询，会沿用第一次查询所用到的read-view（即使前后查询的表不一样）。
            
    表锁：
    行锁（record lock）：单个行记录上锁
    间隙锁（gap lock）：锁住查询区间。间隙锁可以帮助解决幻读。查询没有匹配到记录会用到间隙锁
    临键锁（next-key lock）：行锁和间隙锁的结合，会把查询区间锁住，相邻的下一个区间也会锁住（左开右闭），查询匹配到数据会用临键锁。next-ley-lock可以帮助解决幻读
    innodb加锁原则：1、加锁的基本单位是 next-key lock。next-key lock 是前开后闭（左开右闭）区间。
                   2、只有访问到的对象才会加锁。
        优化 1：索引上的等值查询，命中唯一索，退化为行锁；命中普通索引，左右两边为GAP Lock + Record Lock。
        优化 2：索引上的等值查询，未命中，所在的Net-Key Lock，退化为GAP Lock 。
    
    读锁（共享锁 S锁）：加上读锁后，其他事务只能对该数据加读锁，不能加写锁。
    写锁（排他锁 X锁）：加上写锁后，其他事务不能加任何锁，不能修改也不能读取，直到写锁释放。写锁可以避免脏读
    意向共享锁（IS lock）：事务想要获取一张表中某几行的共享锁
    意向排他锁（IX lock）：事务想要获取一张表中某几行的排他锁

    
dubbo

    dubbo各服务总体调用关系
        1、服务容器负责启动、加载，运行服务提供者
        2、服务提供者在启动时，向注册中心注册自己提供的服务
        3、服务消费者在启动时，向注册中心订阅自己所需的服务
        4、注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者
        5、消费服务者，从提供者列表中，基于负载均衡算法，选择一台提供者进行调用
        6、服务消费者和提供者。在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心
           
    dubbo服务暴露的过程：
        Dubbo 会在 Spring 实例化完 bean 之后，在刷新容器最后一步发布 ContextRefreshEvent 事件的时候，
        通知实现了 ApplicationListener 的 ServiceBean 类进行回调 onApplicationEvent 事件方法，
        Dubbo 会在这个方法中调用 ServiceBean 父类 ServiceConfig 的 export 方法，而该方法真正实现了服务的（异步或者非异步）发布。
        export()内部逻辑：
            doExportUrls()，执行url组装，组装完成后，根据作用域，判断是导出到local还是remote。默认都会进行本地暴露
            本地暴露：
                1、通过proxyFactory获取invoker，invoker代表一个可执行体，可以将需要代理的方法，用invoker进行处理
                2、获取dubbo协议对象（参考dubbo支持的协议）
                3、通过协议调用export()方法，获取本地inJvmExporter对象，并将对象信息存放在exportMap中
                    inJvmExporter对外暴露invoker。通过Exporter对象可以实现对服务的调用。
            远程暴露：
                1、根据URL对象，遍历注册中心，获取监控中心
                2、proxyFactory获取invoker
                3、registryProtocol.export()->doLocalExport()->dubboProtocol.export()->openServer()->createServer()->doOpen(),最终启动服务，绑定端口
                4、开始注册服务，获取Registry对象（通过SPI机制获取真正的Registry对象，比如ZookeeperRegistry），执行服务注册,zkClient.create()
                5、registry订阅服务，监听服务变化
                6、返回Export对象，每次返回新对象
                
    proxyFactory：
         SPI ExtensionLoader  拓展点加载器
         通过配置文件和SPI机制获取，配置文件包括以下几种代理：
         javassist默认、jdk、stub
     
     dubbo服务注册原理：
             远程暴露过程中实现服务注册，参考远程服务暴露
               
    dubbo服务引用原理：
        入口：new ReferenceConfig<>().get(),获取客户端dubbo的代理类，get()方法内部逻辑：
        1、加载配置信息
        2、createProxy()
        3、从配置信息获取注册中心信息
        4、registryProtocol.refer()方法，客户端连接zk，创建消费者节点
        5、构建动态目录RegistryDirectory，该目录订阅、维护服务提供者，并缓存提供者信息
        6、通过集群方案，返回一个invoker
        7、proxyFactory.getProxy(invoker),对invoker进行代理
        8、new InvokeInvocationHandler(invoker)，传入生成的invoker，构造handler
        9、handler内部执行invoker.invoke()方法，传入调用的方法对象和参数
        10.invoke()方法内部通过负载均衡选择一个invoker，执行实际调用
        
        
    客户端方法调用的流程：
        dubbo客户端生成代理类，代理类传入InvokeInvocationHandler对象，InvokeInvocationHandler构造方法传入invoker对象
        service.method()(客户端调用原始方法)->handler.invoke()(进入代理类执行invoke方法)->invoker.invoke()->doInvoke()->负载均衡选择invoker->invoker.invoke()真正调用
        
    
    超时时间：默认1000ms
    重试次数：默认3次（不包含第一次，失败后重试三次）

    配置覆盖规则
        1、方法级优之，接口级次之，全局配置再次之
        2、同级别消费者优先，提供者次之
        
    负载均衡策略：
        服务端服务/方法级别、客户端服务/方法级别都可以配置负载均衡。但是，具体实现在客户端，客户端根据策略选择服务
        1、Random，基于权重的随机负载均衡，默认配置
            实现原理：
                1、累加权重之和，顺便判断所有权重是否一样
                2、如果权重不一样，对总权重执行nextInt方法，int offset = ThreadLocalRandom.current().nextInt(totalWeight);
                3、根据生成的随机数，判断落在哪个区间  遍历执行offset -= weights[i];   offset<0   return invokers.get(i);
                4、如果权重都一样，直接取随机
        2、RoundRobin，基于权重的轮询
            实现原理：
                1、设置maxCurrent，记录当前最大权重值。concurrentHashMap记录每个URL的权重对象，invokers数量变动时，map会清理重置
                2、遍历invokers，比较设置maxCurrent，获取集合中最大权重的invoker
                3、将本次使用的invoker权重置为最低，以便下次不会再使用
        3、LeastActive，最少活跃数，选择上一次请求耗时最短的服务器
            实现原理：
                每接收到一个请求，活跃数+1，请求结束活跃数-1。leastActive：最小活跃数  leastCount：是最小活跃数的invoker个数
                1、获取invoker的活跃数
                2、如果有多个invoker都是最小活跃数，就判断权重是否相同
                3、如果只有一个invoker是最小活跃数，直接返回
                4、多个invoker都是最小活跃数，权重不同，进行权重随机逻辑
                5、权重相同，直接随机选择invoker
        4、ConsistentHash，一致性hash
            实现原理：
                对参数进行Md5和hash运算，得到hash值，再去treeMap中查找invoker
                
    dubbo支持的协议：
        1、dubbo
            默认协议，单一长连接、NIO异步通讯、tcp协议、hessian二进制序列化
            适用小数据量、高并发的场景，以及消费者数量远大于提供者数量的场景；
            不适用大数据量服务，比如传文件、视频等
        2、RMI
            阻塞式短连接、同步传输、tcp协议、JDK序列化
        3、hessian
            短连接、同步传输、http协议、hessian二进制序列化
            适用传输数据包大、提供者数量比消费者数据量多
        4、http
            短连接、同步传输、json序列化
        5、redis
        6、webservice
            短连接、同步传输、http协议、soap序列化
            
    dubbo支持的通讯框架：
        1、netty
        
    短连接、长连接：
        短连接：建立socket连接后，发送接受完数据后马上断开连接
        长连接：基于tcp通讯，一直保持连接，不管是否发送接收数据
        
    dubbo心跳机制：
        dubbo的心跳是双向心跳，客户端会给服务端发送心跳，反之，服务端也会向客户端发送心跳。
        心跳定时器：HeartbeatTimerTask,默认心跳间隔时间60s
        接收的一方更新 lastRead 字段，发送的一方更新 lastWrite 字段，最后读或写的时间超过心跳间隙的时间，便发送心跳请求给对端
        重连、断连定时器：ReconnectTimerTask/CloseTimerTask
        最后一次读或写的时间大于心跳超时时间（默认是心跳间隔时间的3倍，跟重试次数有关），客户端会重连，服务端会断开
        
    dubbo内置服务容器
        1、Spring Container
        2、Jetty Container
        3、Log4j Container
        
    dubbo集群容错方案
        1、Failover   默认配置。失败自动切换，自动重试其他服务器
        2、Failfast   快速失败，立即报错，只发起一次调用
        3、Failsafe   失败安全，出现异常时，直接忽略
        4、Failback   失败自动恢复，记录失败请求，定时重发
        5、Forking    并行调用多个服务器，只要一个成功立即返回
        6、Broadcast  广播逐个调用所有提供者，任意一个报错则报错 
        
    dubbo服务降级：
        配置mock属性，可以实现服务降级
        
    dubbo节点角色：
        1、provider：服务提供方
        2、consumer：服务消费方
        3、registry：服务发现和注册的注册中心
        4、monitor：监控中心，可以统计服务调用次数和调用时间
        5、container：服务运行容器
    
    有多个同名服务，怎么连接指定服务？
        可以配置环境点对点直连，绕过注册中心，将以服务接口为单位，忽略注册中心的提供者列表。
    
    dubbo服务上线怎么支持旧版本？
        可以用版本号（version）过渡，多个不同版本的服务注册到注册中心，版本号不同的服务相互间不引用。这个和服务分组的概念有一点类似。
    
    dubbo一个服务接口有多种实现，怎么区分？
        可以使用group属性来分组，服务提供防和消费放都指定同一个服务
        
    dubbo可以对结果进行缓存吗？
        可以，dubbo提供了声明式缓存，用于加速热门数据的访问数据
    
    dubbo支持哪几种结果缓存类型？
        1、lru
        2、threadLocal
        3、jchace
    
    dubbo服务之间的调用是阻塞的吗？
        默认是同步等待结果阻塞的，支持异步调用
        dubbo是基于NIO的非阻塞实现并行调用，客户端不需要启动多线程即可并行调用多个远程服务，相对多线程开销较小，异步调用会返回一个future对象
        
    dubbo如何优雅的停机？
        通过JDK的ShutdownHook完成优雅停机
    
    dubbo服务调用链过长怎么解决？
        可以使用Pinpoint、Zipkin和apache Skywalking等实现分布式服务追踪
    
    dubbo服务读写容错策略怎么做？
        读操作使用failover，失败进行切换重试
        写操作使用failfast快速失败    
    
    dubbo的spi机制：
        SPI的本质是将接口的实现类的全限定类名配置在文件中，并由服务加载器读取配置文件，加载实现类。
        这样可以在运行中，动态地为接口替换实现类。不过Java SPI不支持AOP和IOC，也不能单独获取某个指定的实现类
        Dubbo就是通过SPI机制加载所有的组件，不过dubbo并未使用Java原生的SPI机制，而是对其进行了增强（支持AOP和IOC），使其能够更好的满足需求。
        dubbo SPI加载文件的路径和顺序：
            1、META-INF/dubbo/internal
            2、META-INF/dubbo/
            3、META-INF/services/
        dubbo的proxyFactory和protocol就是通过SPI机制获取的
        例如：
            指定名称方式：
                PrxyFactory jdkproxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension("jdk")
            自适应方式，根据运行时参数：
                PrxyFactory jdkproxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension()
                自适应实现需要通过@Adaptive注解，该注解作用在方法上时，dubbo会为此方法生成代理逻辑
                
zookeeper

    启动时选举流程
    
    重新选举流程
    
    怎么保证强一致性
    
    ZAB协议
    
    服务提供者能实现失效踢出是根据什么原理?
        
rocketMq
        
    rocketmq的路由中心：nameServer。nameServer保存broker的全部信息
    
    rocketmq事务机制：
        事务消息只跟生产者有关，跟消费者无关
        生产者发送事务消息：
            1、producer发送half消息（half消息不会被消费者消费）
            2、broker回复half消息
            3、producer执行本地事务
            4、producer返回本地事务状态，包括回滚、提交、未知。broker会将回滚状态的消息直接丢弃
            5、如果事务状态未知，broker回查未知状态的事务
            6、producer检查本地事务状态
            7、producer回复本地事务状态
            ...重复5、6、7步骤。最大回查次数默认15次
            
    rocketmq底层存储优化：
        1、顺序写
        2、异步刷盘
            数据先存储到os cache中，再异步刷到磁盘。吞吐量高，但可能丢数据
        3、零拷贝
            mmp+write
                
    负载均衡：
        发送消息负载均衡：
            同一个topic，多个队列，发送消息轮询队列
        消费消息负载均衡：
            5个队列，2个消费者，则consumer1消费3个队列，consumer2消费2个队列
            如果consumer数量大于队列数，那么多余的consumer将空闲不能消费消息
        
    mq如何保证高可用？
    
    mq如何防止重复消费？
        1、生产端查找原因为什么会重复发送
        2、rocketmq每条消息有messageId，可以做唯一校验
        3、业务上实现幂等        
    
    mq如果保证顺序消费？
        生产者：
            1、业务上保证顺序发送
            2、rocketmq自带了发送方法，将唯一业务值作为hashKey，可以实现相同的业务数据发送到相同的队列上
            局部有序：相同业务，发到相同队列
            全局有序：只使用一个队列
        消费者：
            rocketmq提供了顺序消费模式。同一个队列只会被同一个消费者消费。（一个消费者可以消费多个queue和topic，但一个queue只会被同一个consumer消费）
            消费线程请求到broker时，会申请独占锁，获得锁后，才会消费。消费成功后，手动提交偏移量。
            
    mq顺序消费的原理：
        
    
    mq发送端消息推送失败怎么办？
    
    mq如何防止消息不丢失？
        生产者发送丢失：
            1、同步发送+失败多次重试
            2、使用rocketmq提供的事务机制
        broker持久化丢失（消息先写入os cache，再写入磁盘）：
            1、os cache异步刷盘改为同步刷盘
            2、使用mq集群，主从消息备份。防止磁盘损坏
                普通集群：异步复制改为同步复制
                dlegger：会进行leader选举。设置follower全部同步完后，leader才会commit
        消费者：
            1、手动提交消息，消费者本地消费成功后，才返回成功
        

canal 工作原理

    1、canal 模拟 MySQL slave 的交互协议，伪装自己为 MySQL slave ，向 MySQL master 发送dump 协议
    2、MySQL master 收到 dump 请求，开始推送 binary log 给 slave (即 canal )
    3、canal 解析 binary log 对象(原始为 byte 流)

es

    倒排索引：
        通过分词策略，形成词典和文章的映射关系，映射表存储单词存在的位置和次数，词典+映射表就是倒排索引。
    
    数据写入文档流程：
        1、客户端选择一个节点发送请求，这个节点就是协调节点
        2、协调节点根据文档id对文档进行路由得到对应存储的shard分片，并将请求转发到分片所在节点
        3、所在分片接收到请求后，将数据先存储到memory buffer中，同时为了保存持久化数据不丢失，也会将数据存储到translog buffer中，translog会根据策略持久化到磁盘中
        4、执行refresh操作，每秒（或memory buffer满了）将memory buffer中的数据刷到segment fileSystem buffer中（此时数据会建立索引，可以被搜索到），
            同时生成一个comimit point记录数据执行到哪个segment中
        5、每30分钟或trans log满了后（默认512M），会执行flush操作，此时先执行refresh，再将segment中的数据commit到磁盘中，完成持久化后会清除旧的translog文件
        6、refresh操作每秒一次，会产生很多segment文件，es后台会自行对segment文件进行合并merge操作，并丢弃被标记删除的数据，形成一个新的segment文件以供快速搜索
        7、如果节点有副本，数据会同步到副本中，当协调节点发现存储节点和副本节点都完成后，就返回相应给客户端
        
    写操作一致性consistency：
        one：主分片状态OK，就允许写
        all：所有主分片和副本状态都是OK，才允许写
        quorum：默认值。所有的分片中，大部分分片状态OK，就允许写  shardNum = int( (primary + number_of_replicas) / 2 ) + 1
        
    可靠性（持久性）保证：
        trans log事务日志
        
    删除/更新数据的原理：
        执行删除请求，文档并没有被真正的删除，而是在.del文件中被标记为删除。该文档依旧能被匹配查询到，不过会在结果中被过滤掉。并且在执行segment合并时，在.del文件中的数据不会被合并到新segment文件中
        执行更新请求，会创建新文档，并指定一个新版本号，旧版本的文档会在.del文件中被标记为删除，新版本文档会索引到新segment中，查询返回时会对结果进行过滤
        
    数据搜索流程：
        分为query和fetch
        query：
            1、请求发送到协调节点
            2、协调节点请求转发到所有分片，每个分片本地会构建一个priority queue，大小为from+size，存放文档id和排序值（不用返回文档，因为后续还要继续排序分页）
            3、协调节点合并构建一个全局排序的priorityqueue，并对from+size之间的文档id执行下一步请求
            比如，查询100,10的分页数据，每个分片都会查询加载前100页+101页前10 的所有数据到内存中，然后协调节点将所有返回数据合并，排序后再根据100，10进行截取获取实际要的文档id  
        fetch：
            1、协调节点根据文档id，路由计算，请求数据对应的主分片或副本（轮询方式请求）
            2、各分片返回文档给协调节点
            3、协调节点按照之前的排序值，对文档进行合并、排序
            4、协调节点将结果返回客户端
        es数据先写入memory buffer，而查询能查询到segment buffer中的数据，因为refresh是每秒一次，所以es查询是近实时的
        
    es副本
        1、高可用，因此，副本分片不能与原、主分片在同一个节点（交叉备份）
            如果只有一个节点，那么副本将不会被分配到节点中，副本无效
        2、拓展搜索量、吞吐量，搜索时可以利用副本分片
    
    es分片
        分片数目，在索引创建时就缺点，后续不能改，但是可以动态修改副本数
        1、水平分割拓展数据量
        2、查询可以并行查询，提高查询效率和吞吐量
        分片数量设置规则：
            1、每个分片占用硬盘容量不超过ES最大JVM的堆空间大小
            2、分片数不要大大超过节点数，否则一个节点有太多分片，如果该节点故障，那么丢失的数据就会越多。分片数一般不超过节点数的3倍
        
    写数据路由计算：
        对文档id(_id字段值)进行hash运算，然后对主分片数取模   hash(_id)%主分片数
    
    读数据分片控制：
        客户端可以访问任何一个节点的数据（一般是轮询访问节点），访问的节点称为协调节点
        
    es并发情况，如何保证读写一致？
        1、数据增加版本号，乐观锁机制，应用层处理并发冲突
        2、一致性配置：one/one/quonum
        3、设置replication参数为sync，如果设置为async,也可以设置请求参数_prefercence为primary来查询主分片，保证数据是最新的
        
    es分页查询
        实现方案：
            1、from+size浅分页
                原理：每个分片返回from+size的数据，协调节点合并排序数据再进行from+size筛选，数据都是加载到内存中，
                    优点：支持跳页查询，支持实时查询
                    缺点：如果分页from参数太大的话，内存加载的数据就会越多，深度分页会造成内存溢出
            2、scroll深分页
                原理：每次获取一页的数据，返回一个scroll_id。然后根据scroll_id进行下一页的查询。
                     scroll分为初始化和遍历两部分，遍历时是从初始化完的快照中读取数据，初始化后的写数据无法被读取，所以不支持实时查询
                    优点：深度分页不会造成内存溢出
                    缺点：不支持跳页查询，也不支持实时查询
                    
网络七层协议

    1、物理层
    2、数据链路层
    3、网络层  ip
    4、传输层  tcp、udp
    5、会话层  rpc、sql
    6、表示层  加密、ASCII
    7、应用层  http、telnet、ftp
    
https安全传输

    1、客户端向服务端发送数据之前，需要先建立tcp连接，建立完tcp连接后，服务端会先给客户端发送公钥，客户端拿到公钥后就可以用来加密数据，
        服务端接收到数据后可以用私钥解密数据，这种就是通过非对称加密来传输数据
    2、
