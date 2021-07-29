package com.sdydj.moisturizing.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sdydj.moisturizing.product.service.CategoryBrandRelationService;
import com.sdydj.moisturizing.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.product.dao.CategoryDao;
import com.sdydj.moisturizing.product.entity.CategoryEntity;
import com.sdydj.moisturizing.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redisson;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /***
     * 得到一级菜单
     * @return
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        List<CategoryEntity> collect = categoryEntities.stream().filter(entity ->
                entity.getParentCid() == 0
        ).map(entity -> {
            entity.setChildren(getChildren(entity, categoryEntities));
            return entity;
        }).sorted((m1, m2) -> {
            return m1.getSort() - m2.getSort();
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前删除的菜单是否被引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联数据
     * @param category
     */
    @Transactional
    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updataCategory(category.getCatId(), category.getName());
        //同时修改缓存中的数据
        //或者删除redis.del(catelogJSON) 等待下一次主动查询更新

    }

    @Override
    public List<CategoryEntity> getLeavel1Categotys() {
        List<CategoryEntity> entityList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return entityList;
    }


    //TODO 产生对外内存溢出 outofDireMeMORYeRROR
    //springboot 2.0 默认使用 lettuce 作为操作redis的客户端  使用netty进行网络通信
    //lettuce的bug导致对外内存溢出 -Xmx100m netty如果没有指定堆外内存，默认使用-Xmx100m
    //-Dio.netty.maxDirectMemory进行设置
    //解决方案  不能使用 -Dio.netty.maxDirectMemory 只去调大堆外内存
    //升级lettuce客户端  切换使用jedis
    //RedisTemplate
    //lettue jedis 操作redis底层客户端    spring再次封装RedisTemplate


    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        //给缓存中放入json字符串， 拿出的json字符串 瑶瑶逆转为能用的对象类型 【序列化与反序列化】
        /**
         * 空结果缓存  解决缓存穿透
         * 设置随机过期时间 解决缓存雪崩
         * 加锁 解决缓存击穿
         */

        //加入缓存逻辑 缓存中存的数据是json字符串
        //json跨语言跨平台兼容
        String catelogJSON = stringRedisTemplate.opsForValue().get("catelogJSON");

        if (StringUtils.isEmpty(catelogJSON)) {
            //缓存中没有 查数据库
            Map<String, List<Catelog2Vo>> jsonForDB = getCatalogJsonForDBWithRedisLock();
            return jsonForDB;
        }

        //转换为我们指定的对象
        Map<String, List<Catelog2Vo>> catelog = JSON.parseObject(catelogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return catelog;
    }

    /**
     * 缓存里面的数据和数据库保持一致
     * 缓存数据一致性
     * 双写模式
     * 失效模式
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonForDBWithRedissonLock() {
        //锁的名字一样就是一把锁  锁的粒度  越细越快
        //锁的粒度，具体某个商品的数据 product-11-lock
        RLock lock = redisson.getLock("CatalogJson-lock");
        lock.lock();
        //加锁成功 执行业务
        Map<String, List<Catelog2Vo>> data = null;
        try {
            data = getDataFromDb();
        } finally {
            lock.unlock();
        }

        return data;


    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonForDBWithRedisLock() {
        //占分布式锁 去redis占坑
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            System.out.println("获取分布式锁成功");
            //加锁成功 执行业务
            //设置过期时间必须和加锁是同步的 原子操作  （防止加锁后，设置过期时间前 服务器宕机 锁永不过期）
            // stringRedisTemplate.expire("key",300,TimeUnit.SECONDS)
            Map<String, List<Catelog2Vo>> data = null;
            try {
                data = getDataFromDb();
            } finally {
                //删除锁
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);

            }

            //获取值对比 对比成功删除  原子操作 （防止 得到对比值后，删除前  当前锁过期 另一线程进入 删除其它锁）
//            String lockValue = stringRedisTemplate.opsForValue().get("lock");
//            if(uuid.equals(lockValue)){
//                //删除自己的锁
//                stringRedisTemplate.delete("lock");
//
//            }
            return data;
        } else {
            //加锁失败  重试 synchronized()
            System.out.println("获取分布式锁失败，等待重试");
            try {
                Thread.sleep(200);
            } catch (Exception E) {

            }
            return getCatalogJsonForDBWithRedisLock();//自旋的方式
        }


    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catelogJSON = stringRedisTemplate.opsForValue().get("catelogJSON");

        if (!StringUtils.isEmpty(catelogJSON)) {
            //缓存不为null直接返回
            Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catelogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });

            return stringListMap;
        }
        System.out.println("查询数据库。。。。。。");
        List<CategoryEntity> list = baseMapper.selectList(null);

        //查出一级菜单
        List<CategoryEntity> leavel1Categotys = getParent_cid(list, 0L);
        //封装数据
        Map<String, List<Catelog2Vo>> parent_cid = leavel1Categotys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //每一个的一级分类 查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(list, v.getCatId());
            //封装上面的结果
            List<Catelog2Vo> collect = null;
            if (categoryEntities != null) {
                collect = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //找当前2级分类的3级分类封装成vo。
                    List<CategoryEntity> leval3Catelog = getParent_cid(list, l2.getCatId());
                    List<Catelog2Vo.Catelog3Vo> Catelog3Vo = null;
                    if (leval3Catelog != null) {
                        Catelog3Vo = leval3Catelog.stream().map(l3 -> {
                            //封装成指定格式
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                    }
                    catelog2Vo.setCatalog3List(Catelog3Vo);
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return collect;
        }));
        //查到的对象 转为json 放入缓存中
        String jsonString = JSON.toJSONString(parent_cid);
        stringRedisTemplate.opsForValue().set("catelogJSON", jsonString, 1, TimeUnit.DAYS);
        return parent_cid;
    }

    //从数据库获取数据
    public Map<String, List<Catelog2Vo>> getCatalogJsonForDBWithlocalLock() {
        //用map做缓存

        /**
         将数据库多次查询变为一次
         */
        //只要是同一把锁，就能锁住需要这个锁的所有线程
        //synchronized(this),springboot所有的组件在容器中都是单例的
        //TODO 本地锁 synchronized JUC(LOCK)  在分布式情况下 要锁住所有 必须使用分布式锁
        synchronized (this) {
            return getDataFromDb();
        }
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> list, Long parent_cid) {
        List<CategoryEntity> collect = list.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
        return collect;
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);

        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }


    /***
     * 得到子菜单
     * @param root
     * @param categoryEntities
     * @return
     */
    public List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> categoryEntityStream = categoryEntities.stream().filter(entity ->
                entity.getParentCid() == root.getCatId()
        ).map((entity) -> {
            entity.setChildren(getChildren(entity, categoryEntities));
            return entity;
        }).sorted((m1, m2) -> {
            return (m1.getSort() == null ? 0 : m1.getSort()) - (m2.getSort() == null ? 0 : m2.getSort());
        }).collect(Collectors.toList());
        ;
        return categoryEntityStream;
    }

}