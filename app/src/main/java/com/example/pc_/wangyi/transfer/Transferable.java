package com.example.pc_.wangyi.transfer;

/**
 * Created by pc- on 2017/5/23.
 */
public interface Transferable {


    /**
     *
     * @throws Exception
     */
    void init() throws Exception;


    /**
     *
     * @throws Exception
     */
    void parseHeader() throws Exception;


    /**
     *
     * @throws Exception
     */
    void parseBody() throws Exception;


    /**
     *
     * @throws Exception
     */
    void finish() throws Exception;
}
