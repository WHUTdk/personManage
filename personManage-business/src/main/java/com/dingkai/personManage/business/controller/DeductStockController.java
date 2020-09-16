package com.dingkai.personManage.business.controller;

import com.dingkai.personManage.common.response.BaseResult;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

/**
 * @Author dingkai
 * @Date 2020/9/10 21:16
 */
@RestController
@RequestMapping("stock")
@Validated
public class DeductStockController {

    private static final Logger logger = LoggerFactory.getLogger(DeductStockController.class);

    @Autowired
    private Redisson redisson;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("setStock")
    public BaseResult setStock(@RequestParam(value = "stock", defaultValue = "10")
                               @Min(value = 1) Integer stock) {
        try {
            stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
            return BaseResult.success();
        } catch (Exception e) {
            logger.error("设置库存出错", e);
            return BaseResult.error("-1", "设置库存出错");
        }
    }

    /**
     * redisson实现分布式锁，库存扣减问题
     */
    @GetMapping("deductStock")
    public void deductStock() {
        String lockKey = "lockKey";
        RLock redissonLock = redisson.getLock(lockKey);
        try {
            //分布式锁
            redissonLock.lock();
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                //有库存,库存减一
                int realStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", String.valueOf(realStock));
                logger.info("库存扣减成功，剩余库存：{}", realStock);
            } else {
                logger.warn("库存扣减失败，库存不够");
            }
        } finally {
            redissonLock.unlock();
        }
    }


}
