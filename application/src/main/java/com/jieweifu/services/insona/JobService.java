package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Project;

/**
 * @author Jin
 * @date 2018/9/28下午2:03
 */
public interface JobService {

    /**
     * 上传
     * @param project
     */
    void pushProject(Project project);

    /**
     * 获取
     * @param did
     * @param mac
     * @return
     */
    Project getProject(String did, String mac);
}
