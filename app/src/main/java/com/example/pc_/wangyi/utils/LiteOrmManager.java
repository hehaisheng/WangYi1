package com.example.pc_.wangyi.utils;

import android.content.Context;

import com.example.pc_.wangyi.database.FriendCommentModel;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

import java.util.List;

/**
 * Created by pc- on 2017/7/9.
 */
public class LiteOrmManager {


    public  Context context;
    public  LiteOrm liteOrm;

    public static  LiteOrmManager liteOrmManager;
    public static  LiteOrmManager newInstance(Context context){
        if(liteOrmManager==null){
            synchronized (LiteOrmManager.class){
                if(liteOrmManager==null){
                    liteOrmManager=new LiteOrmManager(context);
                }

            }
        }
        return liteOrmManager;
    }
    public LiteOrmManager (Context context){
        DataBaseConfig config = new DataBaseConfig(context.getApplicationContext(), "liteOrm.db");
        config.dbVersion = 1;
        config.onUpdateListener = null;
        liteOrm = LiteOrm.newSingleInstance(config);
    }

//    - 保存（插入or更新）
//    School school = new School("hello");
//    liteOrm.save(school);
//    - 插入
//    Book book = new Book("good");
//    liteOrm.insert(book, ConflictAlgorithm.Abort);
//    - 更新
//    book.setIndex(1988);
//    book.setAuthor("hehe");
//    liteOrm.update(book);
    public void save(FriendCommentModel friendModel){
        liteOrm.save(friendModel);
    }
    public void update(FriendCommentModel friendModel){
       liteOrm.update(friendModel);
   }
    public <T> List<T> getQueryAll(Class<T> cla) {
        return liteOrm.query(cla);
    }
}
