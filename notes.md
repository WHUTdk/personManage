spring bean生命周期

    1.扫描类得到BeanDefinition(BeanClass、scope、dependOn、initMethodName、propertyValues...)
    2.BeanFactoryPostProcessor(处理BeanDefinition，可以获取相关属性，并且可以设置修改BeanClass)
    3.实例化Bean对象 new Object()
    4.设置对象属性
    5.检查是否实现Aware接口，实现Aware接口的Bean能感知自身相关属性（spring会给Bean设置相关参数比如BeanName）
    6.执行BeanPostProcessor前置处理
    7.检查是否实现InitializingBean接口，以决定是否调用afterPropertiesSet方法
    8.检查是否配置自定义的init-method方法
    9.执行BeanPostProcessor后置处理
    10.注册必要的Destrucuion相关回调接口
    11.使用中...
    12.检查是否实现DisposableBean接口，执行destroy方法
    13.检查是否配置自定义销毁方法，有的话执行

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
    FactoryBean:生产Bean的工厂，实现该接口可自定义实例化并注册Bean到spring容器中

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
    违背了AOP的设计原则，AOP一般发生在Bean的后置处理中，不直接参与Bean的初始化流程。
    spring只有在发生循环依赖时，才会提前进行AOP，在属性设置期间，将代理对象放入第二级缓存中，让其他Bean完成初始化。
   

spring事务原理

spring事务的传播机制

    （1）死活不要事务的
        PROPAGATION_NEVER：没有就非事务执行，有就抛出异常
        PROPAGATION_NOT_SUPPORTED：没有就非事务执行，有就将事务挂起
    （2）事务可有可无
        PROPAGATION_SUPPORTS：没有就非事务执行，有就使用当前事务执行
    （3）必须要有事务
        PROPAGATION_REQUIRED：默认配置。没有就新建事务，有就使用当前事务
        PROPAGATION_REQUIRED_NEW：不管有没有，都新建事务，原来有就将原事务挂起。内部事务抛异常回滚，外部事务也会异常回滚。外部事务异常回滚，内部事务不会回滚
        PROPAGATION_NESTED；没有就嵌套事务，有就使用当前事务。如果嵌套，内部事务异常回滚会影响外部事务
        PROPAGATION_MANDATORY：没有就抛出异常，有就使用当前事务

spring事务失效场景
    
    spring事务的实现原理是AOP，事务失效的根本原因是AOP不起作用。
    1、发生自调用，类里面使用this调用方法，此时的this不是代理对象而是类对象本身，事务不会生效
    2、方法不是public的
    3、数据库存储引擎不支持事务
    4、事务未被spring管理
    5、事务指定异常了，抛出的异常非指定的异常
    6、异常被catch捕捉


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

    1.寄存器：较小的内存空间，通过计数器选取下一条要执行的字节码指令
    2.虚拟机栈：线程运行需要的内存空间，每个方法运行时会创建一个栈帧，保存方法参数、局部变量、返回值等信息
    3.本地方法栈:非Java代码方法运行时需要的内存空间，比如Object类的clone()、notify()、hashCode()
    4.堆：年轻代（eden区、survivor1、survivor2，内存占比8:1:1）和老年代old（老年代约占堆2/3）。
            进入老年代条件：
                对象经过15次Minor GC仍然存活（XX:MaxTenuringThreshold=15）
                survivor区小于等于某个年龄的对象的总和占用空间超过survivor区的50%
                大对象可以直接进入老年代
    5.方法区：线程共享。包括类信息（方法、字段、构造器）、运行时常量池、静态变量
            方法区是一种规范，具体实现有永久代（1.8之前，PermGen在jvm内存）和元空间（Metaspace在操作系统内存）
    
jvm内存分代模型

    新生代、老年代、永久区（jdk1.8之前）/元空间
    永久代和元空间区别：
        1、永久代必须指定大小  元空间可以不用设置大小，无上限（受限于物理内存）
        2、字符串常量 1.7在永久代  1.8在堆中

jvm垃圾回收器

    新生代回收器：serial、Parallel Acavenge(复制算法)、parNew
    老年代回收器：cms(基于标记-清除算法)、serial old(复制算法)、parallel old(基于标记-整理算法)
    整堆回收器： G1、ZGC

垃圾回收算法

    1.标记-清除：标记活跃对象，清除不活跃的对象。缺点：会造成内存碎片
    2.复制：开辟一个内存空间，将活跃的对象复制到这个空间中。缺点：消耗额外内存
    3.标记-整理（标记-压缩）：垃圾对象进行移动，压缩在一起进行回收，不会产生内存碎片，但效率偏低

判断是否垃圾对象

    1.引用计数法：对象有新的引用加一，引用失效减一。此方法无法解决两个对象循环引用问题
    2.可达性分析法：以一系列的GC Roots对象作为起始点，从起点往下搜索，整个路径成为引用链，
        当一个对象没有和任何引用链相连时，就作为垃圾对象
        可以作为GC Roots的对象：
            1.虚拟机栈引用对象
            2.本地方法栈JNI引用对象
            3.静态属性引用对象
            4.常量引用对象

什么时候执行垃圾回收

    Minor GC：Eden空间不足就进行Minor GC
    Full GC：full GC会伴随一次minor GC
        1.老年代不足
        2.永久代不足（jdk1.8之后变为元空间，元空间在本地内存不在堆内存中）
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


springboot自动配置原理

    @SpringBootApplication注解：
        @SpringBootConfiguration
        @ComponentScan
        @EnableAutoConfiguration
            @AutoConfigurationPackage：获取启动类所在包路径，作为自动配置扫描包
            @Import(AutoConfigurationImportSelector.class):
                1.加载spring-autoconfigure-metadata.properties下所有配置信息,主要是配置类对应过滤条件的相关信息
                    （ 根据官网说法，使用这种配置方式可以有效的降低SpringBoot的启动时间，因为通过这种过滤方式能减少@Configuration类的数量，从而降低初始化Bean时的耗时）
                2.加载spring.factories下所有自动配置类
                3.过滤，去除不合格的配置类
                4.各个组件配置类，通过properties文件，加载配置信息

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
    
    启动jar时，会执行JarLauncher的main方法，接着调用自身的launch方法，该方法内部会先获取JarFileArchive集合，
    然后根据JarFileArchive集合创建类加载器LaunchedURLClassLoader
    之后会另起一个线程执行Start-Class指向的自定义的main方法，该main方法执行后，会构造spring容器和启动内置servlet容器等过程

redis数据结构

    1、String
        SDS动态字符串，SDS属性包括已使用的长度、空闲未使用的长度、字符数组
        操作字符串，redis会执行以下操作：1、计算出大小是否足够；2、开辟空间至满足所需大小
        优点：1、快速获取字符串长度；2、避免缓冲区溢出；3、降低空间分配次数提升内存使用效率
    2、List
        压缩列表zipList、双端列表linkedList
    3、set
        整数值集合intSet、hashTable
    4、zset
        压缩列表zipList、跳跃表和字典skipList
    5、Hash
        压缩列表zipList、hashTable

redis内存淘汰策略

    redis内存不足时，会触发内存淘汰策略，redis5.0之前只有6种淘汰策略：
        1.volatile-lru：在已设置过期时间的key中，删除最近很少使用的
        2.volatile-ttl：在已设置过期时间的key中，删除将要过期的
        3.volatile-random：在已设置过期时间的key中，随即删除
        4.allkeys-lru：删除最近很少使用的key
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
    2.AOF
        追加方式，将每一个写命令追加到文件中，文件名为appendonly.aof
        AOF持久化触发方式：
            1.appendfsync always 同步操作，每次发生数据变更，都会追加到磁盘中
            2.appendfsync everysec 异步操作，每秒一次
            3.appendfsync no 
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

    运行原理：
        通过lua脚本加锁，同时启动看门狗异步任务，维护锁的超时时间。锁的value会加上线程id
        通过发布订阅模式，通知其他线程当前锁的状态，其他线程循环尝试是否能加锁
        同一个线程，支持可重入锁，每次加锁，统计客户端的加锁次数+1，当加锁次数为0时，代表该锁可以被释放
        通过lua脚本释放锁，保证原子性
        看门狗（watch dog）:定时任务，每隔（锁超时时间的1/3）秒，就会检查锁是否还存在，存在的话，就重新设置生存时间，默认设置超时时间30s
        
    缺点：
        集群模式下，master挂掉后，锁未及时同步，新的master可能又会获取到锁。可以用红锁解决redLock
                
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
        3.ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
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
    提交线程任务时，线程会先提交到worker集合，提交成功后，会执行线程启动方法
    worker大于等于核心线程数后，线程会提交到阻塞队列
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
        底层实现主要通过汇编lock前缀指令，它会锁定这块内存区域的缓存，，并写道主内存中
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
        
偏向锁、轻量级锁、重量级锁

    synchronized同步锁自身的优化，当没有锁竞争时，会先获取偏向锁。如果有少量竞争，偏向锁
    会升级为轻量级锁，竞争线程会启动自旋，如果自旋到达阈值仍然没有拿到锁，就会升级为重量级锁，
    重量级锁会让竞争线程进入阻塞状态，直到持有锁的线程执行完唤醒它们。
    
cas乐观锁

    三个基本操作数：共享变量内存值、预期值、要修改的值
    先获取共享变量的内存值，赋值给临时变量作为预期值，更新数据时，比对内存值和预期值
    只有当共享变量内存值=预期值时，共享变量内存值才会更新为要修改的值
    
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
        hash索引使用类似hash表的结构，数组+链表，不支持范围查询
    为什么不使用二叉树：
        B+树每个节点包含更多的索引值，一次性能够加载更多的数据，减少磁盘IO，而且能够降低树的高度，减少查询次数和复杂度；
    回表：
        普通索引（非主键索引）：先扫描普通索引树定位主键值，再扫描主键索引树定位行记录。
    覆盖索引：
        查询的字段在索引树上，即触发覆盖索引。常见实现覆盖索引的方法是：将被查询的字段，建立到联合索引里去。
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
        读已提交：可能发送不可重复读
        可重复读：可能发生幻读
        串行化：
    ACID：
        A：atomivity 原子性，事务要么成功，要么失败，失败的话数据会回滚。undo log可以保证原子性
        C： consistency一致性，其他三个特性保证了最终一致性
        I： isolation 隔离性。锁（写-写操作）和mvcc（写-读操作）实现隔离性
        D： durability 持久性，即使数据库宕机，也不会丢失已提交的事务。redo log可以保证持久性
        
    redo log：
        数据加载到Buffer Pool后，执行写操作，生成redo log，存在log buffer区域，然后顺序持久化到redo log文件中（mysql重启后会加载redo log文件，保证事务持久性）
        redo log采用WAL预写式日志，分为prepare和commit两个阶段，更新数据写入到redo log后处于prepare状态，再告知执行器执行完成，随时可以提交事务
        默认有两个文件 logfile0  logfile1  每个默认48M。持久化策略可配置，默认是事务提交后，脏页数据同步持久化到redo log file中
        write position
        check point
    double write buffer:
        触发check point后，会将buffer pool中的脏数据刷入磁盘，该过程根据双写机制，保证脏数据能够可靠的刷盘。
    undo log：
        作用：1、回滚；2、mvcc
        
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
        
    主从复制延迟原因，解决方案：
        原因：读写分离，主库写，从库读。主库的DDL/DML操作顺序产生binlog效率很高，从库的sql线程是单线程，效率低，还可能与其他查询操作产生锁竞争，当业务繁忙时，产生的DDL超过SQL进程所承受的范                围，执行会产生延时，从而同步数据就会出现延迟
        解决方案：
            1、提升硬件配置
            2、增加从库数量，分散压力
            3、写数据时，确保主从同步成功才返回成功，此方案影响性能一般不考虑
            4、引入缓存中间件，写数据时，将数据存入缓存，读数据时如果读不到的话就从缓存读取，当数据同步成功后再删除缓存数据
            4、如果是服务器流量太大，造成业务繁忙影响同步，可以对上层流量进行限流
            
    mvcc 多版本并发控制:
        mvcc主要为了提高并发读写性能，不用加锁就能实现多个事务并发读写，通过undo log和read-view实现
        mysql开启一个事务，会分配一个事务id
        mvcc会维护一个版本列表，针对每个写操作，会顺序添加到版本列表中，列表每行记录包括修改的数据、事务id、回滚指针
        查询时，会生成一致性视图read-view,由当前未提交的事务id数组和以创建的最大事务id组成（[100,200],300），查询的结果需要跟read-view做对比从而得到最终结果。
            mysql默认隔离级别是可重复读，同一个事务中，多次查询，会沿用第一次查询所用到的read-view（即使前后查询的表不一样）。
            
    行锁：
    表锁：
    间隙锁：锁住查询区间。间隙锁可以帮助解决幻读。查询没有匹配到记录会用到间隙锁
    临键锁（next-key-lock）：行锁和间隙锁的结合，会把查询区间锁住，相邻的下一个区间也会锁住（左开右闭），查询匹配到数据会用临键锁。next-ley-lock可以帮助解决幻读
    读锁（共享锁）：加上读锁后，其他事务只能对该数据加读锁，不能加写锁。
    写锁（互斥锁）：加上写锁后，其他事务不能加任何锁，不能修改也不能读取，直到写锁释放。写锁可以避免脏读
    意向共享锁：
    意向排他锁：

    
dubbo
    
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
                2、遍历invokers，比较maxCurrent，获取集合中最大权重的invoker
                3、将本次使用的invoker权重置为最低，以便下次不会再使用
        3、LeastActive，最少活跃数，选择上一次请求耗时最短的服务器
            实现原理：
                没接收到一个请求，活跃数+1，请求结束活跃数-1。leastActive：最小活跃数  leastCount：是最小活跃数的invoker个数
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
        1、Failover Cluster   默认配置。失败自动切换，自动重试其他服务器
        2、Failfast Cluster   快速失败，立即报错，只发起一次调用
        3、Failsafe Cluster   失败安全，出现异常时，直接忽略
        4、Failback Cluster   失败自动恢复，记录失败请求，定时重发
        5、Forking Cluster    并行调用多个服务器，只要一个成功立即返回
        6、Broadcast Cluster  广播逐个调用所有提供者，任意一个报错则报错 
    
    dubbo的spi机制：
    
    dubbo和springCoud比较
        
        
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
                
    mq如何保证高可用？
    
    mq如何防止重复消费？
        1、rocketmq每条消息有messageId，可以做唯一校验
        2、业务上实现幂等        
    
    mq如果保证顺序消费？
        生产者：
            1、业务上保证顺序发送
            2、rocketmq自带了发送方法，将唯一业务值作为hashKey，可以实现相同的业务数据发送到相同的队列上
        消费者：
            rocketmq提供了顺序消费模式
    
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