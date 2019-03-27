package com.dev.pipi.commlib.base.glide;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */


public class ImageLoader {
    /**
     * 默认最基础的形式显示图片
     *
     * @param context 上下文，可与对应生命周期绑定
     * @param url   可以是 url,SDCard,assets,raw,drawable,ContentProvider...
     * @param imageView 需要填充的imageview
     */
    public static void display(Context context, Object url, ImageView imageView){
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .into(imageView);
    }

    /**
     * 自定义设置占位图,错误图,远端空图
     *
     * @param context
     * @param url
     * @param imageView
     * @param placeResource 占位图
     * @param errorResource 访问错误显示图片
     * @param fallbackResource 访问远端model为空时显示图片
     */
    public static void display(Context context, Object url, ImageView imageView, int placeResource,
                               int errorResource, int fallbackResource){
        GlideApp.with(context)
                .load(url)
                .placeholder(placeResource)
                .error(errorResource)
                .fallback(fallbackResource)
                .into(imageView);
    }

    /**
     * 不经过内存缓存加载图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void displaySkipMemory(Context context, Object url, ImageView imageView){
        GlideApp.with(context)
                .asBitmap()
                .skipMemoryCache(true)
                .load(url)
                .into(imageView);
    }

    /**
     * 不经过硬盘缓存加载图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void displaySkipDiskCahe(Context context, Object url, ImageView imageView){
        GlideApp.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(url)
                .into(imageView);
    }

    /**
     * 不经过硬盘缓存和内存缓存加载图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void displaySkipMemoryAndDiskCahe(Context context, Object url, ImageView imageView){
        GlideApp.with(context)
                .asBitmap()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(url)
                .into(imageView);
    }

    /**
     * 按比例显示缩略图
     *
     * @param context
     * @param url
     * @param thumbnailSize 缩略图倍数，如0.1f
     * @param imageView
     */
    public static void displayThumbnail(Context context, Object url, ImageView imageView, float thumbnailSize){
        GlideApp.with(context)
                .load(url)
                .thumbnail(thumbnailSize)
                .into(imageView);
    }

    /**
     * 自定义大小去匹配imageView
     *
     * @param context
     * @param url
     * @param width 自定义的宽度
     * @param height 自定义的高度
     * @param imageView
     */
    public static void displayOverrideSize(Context context, Object url, int width, int height, ImageView imageView){
        GlideApp.with(context)
                .load(url)
                .override(width, height)
                .into(imageView);
    }

    /**
     * 显示gif图片
     */
    public void displayGif(Context context, Object url, ImageView imageView){
        GlideApp.with(context)
                .asGif()
                .load(url)
                .into(imageView);
    }

    /**
     * 无动画的加载图片
     */
    public static void displayNoAnimate(Context context, Object url, ImageView imageView){
        GlideApp.with(context)
                .asBitmap()
                .dontAnimate()
                .load(url)
                .into(imageView);
    }

    /**
     * 获取图片的bitmap,然后做后续的处理
     * @param context
     * @param url
     * @param bitmapListener 获取图片bitmap的监听
     */
    public static void displayIntoTarget(Context context, Object url, final BitmapListener bitmapListener){
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        bitmapListener.setBitmapResource(resource);
                    }
                });
    }

    /**
     * 监听加载的结果，然后显示到imageview
     * @param context
     * @param url
     * @param imageView
     * @param onLoadListener 图片加载成功与否的监听
     */
    public static void displayWithListener(Context context, Object url, ImageView imageView, final OnLoadListener onLoadListener){
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        onLoadListener.onLoadFailed(e, isFirstResource);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        onLoadListener.onLoadSuccess(resource, isFirstResource);
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 加载圆形的图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void displayCircle(Context context, Object url, ImageView imageView){
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .transform(new CircleCrop())
                .into(imageView);
    }

    /**
     * 加载圆角图片
     * @param context
     * @param url
     * @param imageView
     * @param roundingRadius
     */
    public static void displayRounded(Context context, Object url, ImageView imageView, int roundingRadius){
        GlideApp.with(context)
                .load(url)
                .transform(new RoundedCorners(roundingRadius))
                .into(imageView);
    }

    /**
     * 加载圆角图片，并重写宽高，如果override了宽高，则圆角显示不出来
     * @param context
     * @param url
     * @param imageView
     * @param roundingRadius
     * @param width
     * @param height
     */
    @Deprecated
    public static void displayOverrideRounded(Context context, Object url, ImageView imageView, int roundingRadius,
                                              int width, int height){
        GlideApp.with(context)
                .load(url)
                .transform(new RoundedCorners(roundingRadius))
                .override(width, height)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 清除内存缓存
     * @param context
     */
    public static void clearMemory(Context context){
        GlideApp.get(context).clearMemory();
    }

    /**
     * 清楚硬盘缓存
     * @param context
     */
    public static void clearDiskCache(Context context){
        GlideApp.get(context).clearDiskCache();
    }

}