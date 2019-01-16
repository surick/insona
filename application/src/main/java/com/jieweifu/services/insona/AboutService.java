package com.jieweifu.services.insona;

import com.jieweifu.models.insona.About;

/**
 * @author Jin
 * @date 2018/12/10
 */
public interface AboutService {

    /**
     * 更新关于
     * @param about
     */
    void updateAbout(About about);

    /**
     * 获取关于
     * @param id
     * @return
     */
    About getAbout(Integer id);
}
