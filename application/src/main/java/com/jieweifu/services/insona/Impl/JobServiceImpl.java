package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Project;
import com.jieweifu.services.insona.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author Jin
 * @date 2018/9/28下午2:14
 */
@Service
public class JobServiceImpl implements JobService {
    private DB db;

    @Autowired
    public JobServiceImpl(DB db) { this.db = db; }

    public int exist(String mac) {
        Project project = db.select()
                .from(Project.class)
                .where("mac = ?", mac)
                .queryForEntity(Project.class);
        int id = project == null ? -1 : project.getId();
        return id;
    }

    @Override
    public void pushProject(Project project) {
        int counts = exist(project.getMac());
        if (counts > 0) {
            db.update()
                    .table(Project.class)
                    .set("createTime", String.valueOf(Instant.now().toEpochMilli()))
                    .set("project", project.getProject())
                    .where("id = ?", counts).execute();
        } else {
            db.insert().save(project).execute();
        }
    }

    @Override
    public Project getProject(String did, String mac) {
        return db.select()
                .from(Project.class)
                .where("did = ? or mac = ?", did, mac)
                .queryForEntity(Project.class);
    }

}
