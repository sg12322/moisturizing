package com.sdydj.moisturizing.product.web;

import com.sdydj.moisturizing.product.entity.CategoryEntity;
import com.sdydj.moisturizing.product.service.CategoryService;
import com.sdydj.moisturizing.product.vo.Catelog2Vo;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping({"/","index.html",})
    public String indexPage(Model model){
        //查询出所有一级分类
        List<CategoryEntity> categoryEntities= categoryService.getLeavel1Categotys();
        //视图解析器进行拼串
        //"classpath:/templates/";   ".html";
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }
    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson(){

        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return  catalogJson;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        //获取一把锁 只要锁的名字一样 就是同一把锁
        RLock lock = redisson.getLock("my-lock");
        //加锁
        //lock.lock();//阻塞式等待     默认加的锁是30s
        //锁的自动续期 如果业务超长 运行期间自动给锁上新的30s   不用担心业务时间长 锁自定过期
        //加锁的业务运行完成 就不会给当前锁续期  即使不手动解锁  锁默认在30s后自动删除

        lock.lock(10, TimeUnit.SECONDS);//10秒后自动解锁   自动解锁时间一定要大于业务的执行时间
        //lock.lock(10, TimeUnit.SECONDS);  不会自动续期
        //如果我们传递了锁的超时时间  就发送给redis执行脚本 进行占锁 默认超时就是我们指定的时间
        //如果我们未指定锁的超时时间 lockWatchdogTimeout = 30 * 1000;
        //只要占锁成功 就会启动一个定时任务 重新设置看门狗时间  新的时间就是看门狗的默认时间  每隔10s 续满30s
        //internalLockLeaseTime{看门狗时间} / 3
        //最佳 lock.lock(10, TimeUnit.SECONDS); 省掉续期时间 手动解锁
        try{
            System.out.println("业务  代码"+Thread.currentThread().getId());
            Thread.sleep(30000);
        }catch (Exception e){

        }finally {
            //解锁  解锁代码没有运行 redisson也不会出现死锁
            System.out.println("释放锁"+Thread.currentThread().getId());
            lock.unlock();
        }


        return  "hello";
    }


    //保证一定能读到最新数据  修改期间  写锁是一个排他锁(互斥锁 独享锁) 读锁是一个共享锁
    //读+读 相当于无锁  只会在redis中记录好，所有当前读锁 他们都会同时加锁成功
    //写锁没释放读锁必须等待
    //写+读 等待写锁释放
    //写+写 阻塞方式
    //读+写  有读锁 写需要等待
    //只要有写的存在 都必须等待
    @ResponseBody
    @GetMapping("/write")
    public  String writeValue(){
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("rw-lock");

        String s="";
        RLock rLock = readWriteLock.writeLock();

        try {
            //改数据加写锁 读数据加读锁
            rLock.lock();
            System.out.println("写锁加锁成功" + Thread.currentThread().getId());
            s= UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("writeValue",s);
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            rLock.unlock();
            System.out.println("写锁释放" + Thread.currentThread().getId());
        }
        return  s;

    }


    @ResponseBody
    @GetMapping("/read")
    public  String readValue(){
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("rw-lock");
       // ReentrantReadWriteLock lock1 = new ReentrantReadWriteLock();
        RLock lock = readWriteLock.readLock();
        String  s="";
        try{
            lock.lock();
            System.out.println("读锁加锁成功" + Thread.currentThread().getId());
            Thread.sleep(30000);
            s=redisTemplate.opsForValue().get("writeValue");
        }catch (Exception e){
            System.out.println(e);
        }finally {
            lock.unlock();
            System.out.println("读锁释放" + Thread.currentThread().getId());
        }

        return  s;

    }

    /**
     * 车库停车
     * 信号量也可以用作分布式限流
     * @return
     * @throws InterruptedException
     */

    @ResponseBody
    @GetMapping("/park")
    public  String park() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        //park.acquire();//获取一个信号 获取一个值 占一个车位
        boolean b = park.tryAcquire();
        if (b){

        }else{
            return "error";
        }

        return "ok=>"+b;
    }
    @ResponseBody
    @GetMapping("/go")
    public  String go() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        park.release();// 释放一个车位
//        Semaphore semaphore=new Semaphore(5);
//        semaphore.acquire();
//        semaphore.release();
        return "ok";
    }


    /**
     * 放假锁门
     * 5个班的人都走了 可以锁
     * @return
     * @throws InterruptedException
     */
    @ResponseBody
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch lockDoor = redisson.getCountDownLatch("lockDoor");
        lockDoor.trySetCount(5);
        lockDoor.await();//等待闭锁完成
        return "放假了";
    }

    @ResponseBody
    @GetMapping("/gogogo/{id}")
    public String gogogo(@PathVariable("id") Long id) throws InterruptedException {
        RCountDownLatch lockDoor = redisson.getCountDownLatch("lockDoor");
        lockDoor.countDown();//计数减一
        //CountDownLatch
        return "gogogo"+id;
    }

}
