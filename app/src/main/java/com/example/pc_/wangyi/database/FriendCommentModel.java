package com.example.pc_.wangyi.database;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by pc- on 2017/7/4.
 */
@Table("FriendCommentModel")
public class FriendCommentModel {



    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public int id;
    //用户名
    @Column("UserName")
    public String userName;
    //评论文本
    @Column("CommentText")
    public String  commentText;
    //点赞的用户名
    @Column("ZanUserName")
    public  String zanUserName;
    //创建的时间，作为标志
    @Column("CreateTime")
    public String createTime;
    @Column("MessageContent")
    public String messageContent;



    public String getMessageContent() {
        return messageContent;
    }
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getZanUserName() {
        return zanUserName;
    }

    public void setZanUserName(String zanUserName) {
        this.zanUserName = zanUserName;
    }
    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void addCommentText(String commentText1){
        this.commentText=this.commentText+commentText1;
    }
    public void addZanUserName(String zanUserName1){
        this.zanUserName=this.zanUserName+zanUserName1;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
