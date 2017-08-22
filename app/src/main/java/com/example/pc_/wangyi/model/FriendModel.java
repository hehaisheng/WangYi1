package com.example.pc_.wangyi.model;

/**
 * Created by pc- on 2017/7/4.
 */
public class FriendModel {



    public String userName;
    public String commentText;
    public String zanUserName;
    public String createTime;
    public String userMessage;

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
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


}
