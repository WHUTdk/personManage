# personManage
spring bean生命周期

    1.实例化Bean对象
    2.设置对象属性
    3.检查是否实现Aware接口，实现Aware接口的Bean能感知自身相关属性（spring会给Bean设置相关参数比如BeanName）
    4.执行BeanPostProcessor前置处理
    5.检查是否实现InitializingBean接口，以决定是否调用afterPropertiesSet方法
    6.检查是否配置自定义的init-method方法
    7.执行BeanPostProcessor后置处理
    8.注册必要的Destrucuion相关回调接口
    9.使用中...
    10.检查是否实现DisposableBean接口，执行destroy方法
    11.检查是否配置自定义销毁方法，有的话执行

spring循环依赖和三级缓存

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
    并完成最终设置，然后放入一级缓存singletonObjects中并移除二级缓存中自己的代理对象。

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
    4.堆
    5.方法区：线程共享。包括类信息（方法、字段、构造器）、运行时常量池、静态变量
            方法区是一种规范，具体实现有永久代（1.8之前，PermGen在jvm内存）和元空间（Metaspace在本地内存）
    
内存溢出/泄漏问题定位

    1.jps查看java进程，找到程序进程pid
    2.jmap -heap pid 查看堆内存情况 
    3.jmap ‐histo:live pid | more 查看活跃对象
      jmap -dump:format=b,live,file=dump_xx.dat pid  将内存使用情况dump到文件中

cpu占用过高问题定位

    1.top命令，找到占用cpu高的线程PID
    2.使用ps H -eo pid,tid,%cpu,%mem|grep pid     找到进程下的所有线程，及线程对应占用cpu情况
    3.将占用cpu高的线程tid转为十六进制
    4.使用jstack pid 展示进程下所有线程详细情况，对比nid与上一步tid十六进制的值，找到占用cpu高的线程详细情况
     
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

springboot启动原理（自动配置原理）

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
        默认持久化策略。将数据全量备份在二进制文件中，文件名为dump.rdb。
        可以手动修改配置文件设置持久化触发频率（m秒内有n个数据被修改）。
        RDB持久化触发方式：
            1.save：手动执行save命令，同步操作持久化，会阻塞客户端请求
            2.bgsave：手动执行bgsave命令，异步操作，执行fork创建子进程执行持久化，不会阻塞客户端请求
            3.自动化：根据配置文件配置的数据修改频率自动触发 save m n
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
        
linux实用命令

    du -sh *|sort -h   查看当前目录磁盘占用情况