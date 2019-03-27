package com.dev.pipi.commlib.base.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */


@GlideModule
public final class MyAppGlideModule extends AppGlideModule {

    private int diskSize = 1024 * 1024 * 100;
    private int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 8;  // 取1/8最大内存作为最大缓存

    /**
     *  关闭manifest的解析,避免添加相同的module
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    /**
     * 通过GlideBuilder设置默认的结构(Engine,BitmapPool ,ArrayPool,MemoryCache等等).
     */
    public void applyOptions(Context context, GlideBuilder builder) {
        // 定义缓存大小和位置
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskSize));  // 内存中
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "glideCache", diskSize)); // sd卡中

        builder.setMemoryCache(new LruResourceCache(memorySize)); // 自定义内存大小
        builder.setBitmapPool(new LruBitmapPool(memorySize)); // 自定义图片池大小

        RequestOptions options = new RequestOptions().format(DecodeFormat.PREFER_RGB_565);
        builder.setDefaultRequestOptions(options);
    }
}